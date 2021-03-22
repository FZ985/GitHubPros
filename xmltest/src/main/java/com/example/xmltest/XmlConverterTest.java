package com.example.xmltest;


import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import cn.xml.xml.XmlConverter;

/**
 * Created by Stardust on 2017/5/14.
 */
public class XmlConverterTest {


    public static void testNodeNameMap() throws IOException, SAXException, ParserConfigurationException {
        System.out.println(XmlConverter.convertToAndroidLayout("<linear></linear>"));
    }

    public static void testAttrNameMap() throws IOException, SAXException, ParserConfigurationException {
        System.out.println(XmlConverter.convertToAndroidLayout("<linear w=\"*\" h=\"123\"></linear>"));
    }

    public static void testNonMappedNodeName() throws IOException, SAXException, ParserConfigurationException {
        System.out.println(XmlConverter.convertToAndroidLayout(("<others></others>")));
    }

    public static void testChildrenNode() throws ParserConfigurationException, SAXException, IOException {
        System.out.println(XmlConverter.convertToAndroidLayout("<linear w=\"*\" h=\"123\"><text id=\"aaa\"/><button bg=\"#ffffff\"/><Button\n" +
                "        android:layout_width=\"wrap_content\"\n" +
                "        android:textAllCaps=\"false\"\n" +
                "        android:layout_height=\"wrap_content\"\n" +
                "        android:onClick=\"testAttrNameMap\"\n" +
                "        android:text=\"testAttrNameMap\" /></linear>"));
    }

    public static void testTextContent() throws ParserConfigurationException, SAXException, IOException {
        System.out.println(XmlConverter.convertToAndroidLayout("<linear><text id=\"aaa\">some text</text></linear>"));
    }


}