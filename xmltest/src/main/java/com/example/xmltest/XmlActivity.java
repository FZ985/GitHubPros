package com.example.xmltest;

import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Description:
 * Author: jfz
 * Date: 2021-02-03 17:44
 */
public class XmlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //从资源文件夹assets下的xml 创建输入流，将文件输入
            InputStream as = this.getAssets().open("activity_xml.xml");
            //实例化一个xml解析器
            XmlPullParser parser = Xml.newPullParser();
            //将输入流输入给xml解析器
            parser.setInput(as, "utf-8");
            AttributeSet attrs = Xml.asAttributeSet(parser);
            advanceToRootNode(parser);
            View view = getLayoutInflater().inflate(parser, (FrameLayout) getWindow().getDecorView(), false);
            setContentView(view);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private void advanceToRootNode(XmlPullParser parser)
            throws InflateException, IOException, XmlPullParserException {
        // Look for the root node.
        int type;
        while ((type = parser.next()) != XmlPullParser.START_TAG &&
                type != XmlPullParser.END_DOCUMENT) {
            // Empty
        }

        if (type != XmlPullParser.START_TAG) {
            throw new InflateException(parser.getPositionDescription()
                    + ": No start tag found!");
        }
    }
}