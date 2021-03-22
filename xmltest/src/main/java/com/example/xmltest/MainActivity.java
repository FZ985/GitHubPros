package com.example.xmltest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import cn.xml.XmlConverter2;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testNodeNameMap(View view) {



        try {
            XmlConverterTest.testNodeNameMap();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void testAttrNameMap(View view) {
        try {
            XmlConverterTest.testAttrNameMap();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void testNonMappedNodeName(View view) {
        try {
            XmlConverterTest.testNonMappedNodeName();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void testChildrenNode(View view) {
        try {
            XmlConverterTest.testChildrenNode();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testTextContent(View view) {
        try {

//            XmlConverterTest.testTextContent();
            String dd = XmlConverter2.convertToAndroidLayout("<LinearLayout layout_width=\"match_parent\" layout_height=\"123\"><TextView layout_width=\"wrap_content\"></TextView></LinearLayout>");
            System.out.println("dddd:" + dd);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startXml(View view) {
        startActivity(new Intent(this, XmlActivity.class));
    }

}