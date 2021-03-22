package cn.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Description: 解析布局
 * Author: jfz
 * Date: 2021-02-03 16:26
 */
public class XmlConverter2 {

    private static final NodeHandler NODE_HANDLER = new NodeHandler.NameRouter();

    public static String convertToAndroidLayout(String xml) throws IOException, SAXException, ParserConfigurationException {
        return convertToAndroidLayout(new InputSource(new StringReader(xml)));
    }

    //    xmlns:app="http://schemas.android.com/apk/res-auto"
    public static String convertToAndroidLayout(InputSource source) throws ParserConfigurationException, IOException, SAXException {
        StringBuilder layoutXml = new StringBuilder();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(source);
        handleNode(document.getFirstChild(), "xmlns:android=\"http://schemas.android.com/apk/res/android\"", layoutXml);
        return layoutXml.toString();
    }

    private static void handleNode(Node node, String nameSpace, StringBuilder layoutXml) {
        String nodeName = node.getNodeName();
        String mappedNodeName = NODE_HANDLER.handleNode(node, nameSpace, layoutXml);
//        handleText(nodeName, node.getTextContent(), layoutXml);
        handleAttributes(nodeName, node.getAttributes(), layoutXml);
        layoutXml.append(">\n");
        handleChildren(node.getChildNodes(), layoutXml);
        layoutXml.append("</").append(mappedNodeName).append(">\n");
    }

    private static void handleAttributes(String nodeName, NamedNodeMap attributes, StringBuilder layoutXml) {
        if (attributes == null)
            return;
        int len = attributes.getLength();
        for (int i = 0; i < len; i++) {
            Node attr = attributes.item(i);
//            handleAttribute(nodeName, attr, layoutXml);
            if (!attr.getNodeName().equals("style")) {
                layoutXml.append("android:");
            }
            layoutXml.append(mapAttrName(nodeName, attr.getNodeName()))
                    .append("=\"").append(mapAttrValue(nodeName, attr.getNodeName(), attr.getNodeValue())).append("\"\n");
        }
    }

    private static void handleChildren(NodeList nodes, StringBuilder layoutXml) {
        if (nodes == null)
            return;
        int len = nodes.getLength();
        for (int i = 0; i < len; i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            handleNode(node, "", layoutXml);
        }
    }

    private static String mapAttrName(String nodeName, String attrName) {
        return attrName;
    }

    private static String mapAttrValue(String nodeName, String attrName, String value) {
//        Map<String, String> valueMap = mAttrValueMap.get(attrName);
//        if (valueMap == null)
//            return value;
//        String mappedValue = valueMap.get(value);
//        return mappedValue == null ? value : mappedValue;
        return value;
    }
}