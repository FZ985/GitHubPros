package cn.xml;

import org.w3c.dom.Node;

/**
 * Description:
 * Author: jfz
 * Date: 2021-02-03 16:33
 */
public interface NodeHandler {

    String handleNode(Node node, StringBuilder layoutXml);

    String handleNode(Node node, String namespace, StringBuilder layoutXml);

    abstract class Adapter implements cn.xml.NodeHandler {

        @Override
        public String handleNode(Node node, String namespace, StringBuilder layoutXml) {
            String name = handleNode(node, layoutXml);
            layoutXml.append(namespace);
            return name;
        }
    }


    class NameRouter extends Adapter {

        @Override
        public String handleNode(Node node, StringBuilder layoutXml) {
            String name = node.getNodeName();
            layoutXml.append("<").append(name).append("\n");
            return name;
        }
    }
}