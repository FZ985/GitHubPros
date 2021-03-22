//package xml1;
//
//import android.content.Context;
//import android.os.Build;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.InflateException;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.Nullable;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
//
//import java.io.ByteArrayInputStream;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import cn.xml.xml.XmlConverter;
//import xml1.inflater.ViewInflater;
//import xml1.util.Res;
//
///**
// * Description:
// * Author: jfz
// * Date: 2021-02-22 10:29
// */
//public class DynamicLayoutInflater {
//    private Context mContext;
//
//    public DynamicLayoutInflater(Context mContext) {
//        this.mContext = mContext;
//    }
//
//    public View inflate(String xml, @Nullable ViewGroup parent, boolean attachToParent) {
//        InflateContext context = newInflateContext();
//        return inflate(context, xml, parent, attachToParent);
//    }
//
//    public InflateContext newInflateContext() {
//        return new InflateContext();
//    }
//
//    public View inflate(InflateContext context, String xml, @Nullable ViewGroup parent, boolean attachToParent) {
////        View view = mLayoutInflaterDelegate.beforeInflation(context, xml, parent);
////        if (view != null)
////            return view;
//        xml = convertXml(context, xml);
////        return mLayoutInflaterDelegate.afterInflation(context, doInflation(context, xml, parent, attachToParent), xml, parent);
//        return doInflation(context, xml, parent, attachToParent);
//    }
//
//    protected View doInflation(InflateContext context, String xml, @Nullable ViewGroup parent, boolean attachToParent) {
//        try {
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            dbf.setNamespaceAware(true);
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            Document document = db.parse(new ByteArrayInputStream(xml.getBytes()));
//            return inflate(context, document.getDocumentElement(), parent, attachToParent);
//        } catch (Exception e) {
//            throw new InflateException(e);
//        }
//    }
//
//    public View inflate(InflateContext context, Node node, @Nullable ViewGroup parent, boolean attachToParent) {
//        View view = doInflation(context, node, parent, attachToParent);
////        if (view instanceof ShouldCallOnFinishInflate) {
////            ((ShouldCallOnFinishInflate) view).onFinishDynamicInflate();
////        }
//        return view;
//    }
//
//    protected View doInflation(InflateContext context, Node node, @Nullable ViewGroup parent, boolean attachToParent) {
////        View view = mLayoutInflaterDelegate.beforeInflateView(context, node, parent, attachToParent);
////        if (view != null)
////            return view;
//        HashMap<String, String> attrs = getAttributesMap(node);
//        View view = doCreateView(context, node, node.getNodeName(), parent, attrs);
//        if (parent != null) {
//            parent.addView(view); // have to add to parent to generate layout params
//            if (!attachToParent) {
//                parent.removeView(view);
//            }
//        }
//        ViewInflater<View> inflater = applyAttributes(context, view, attrs, parent);
//        if (view instanceof ViewGroup && node.hasChildNodes()) {
//            inflateChildren(context, inflater, node, (ViewGroup) view);
//            if (inflater instanceof ViewGroupInflater) {
//                applyPendingAttributesOfChildren(context, (ViewGroupInflater) inflater, (ViewGroup) view);
//            }
//        }
//        return mLayoutInflaterDelegate.afterInflateView(context, view, node, parent, attachToParent);
//    }
//
//    protected void applyAttributes(InflateContext context, View view, ViewInflater<View> setter, Map<String, String> attrs, @Nullable ViewGroup parent) {
//        if (setter != null) {
//            for (Map.Entry<String, String> entry : attrs.entrySet()) {
//                String[] attr = entry.getKey().split(":");
//                if (attr.length == 1) {
//                    applyAttribute(context, setter, view, null, attr[0], entry.getValue(), parent, attrs);
//                } else if (attr.length == 2) {
//                    applyAttribute(context, setter, view, attr[0], attr[1], entry.getValue(), parent, attrs);
//                } else {
//                    throw new InflateException("illegal attr name: " + entry.getKey());
//                }
//            }
//            setter.applyPendingAttributes(view, parent);
//        } else {
//            Log.e(LOG_TAG, "cannot set attributes for view: " + view.getClass());
//        }
//
//    }
//
//    protected void applyAttribute(InflateContext context, ViewInflater<View> inflater, View view, String ns, String attrName, String value, ViewGroup parent, Map<String, String> attrs) {
//        if (mLayoutInflaterDelegate.beforeApplyAttribute(context, inflater, view, ns, attrName, value, parent, attrs)) {
//            return;
//        }
//        boolean isDynamic = isDynamicValue(value);
//        if ((isDynamic && mInflateFlags == FLAG_IGNORES_DYNAMIC_ATTRS)
//                || (!isDynamic && mInflateFlags == FLAG_JUST_DYNAMIC_ATTRS)) {
//            return;
//        }
//        inflater.setAttr(view, ns, attrName, value, parent, attrs);
//        mLayoutInflaterDelegate.afterApplyAttribute(context, inflater, view, ns, attrName, value, parent, attrs);
//
//    }
//
//    protected View doCreateView(InflateContext context, Node node, String viewName, ViewGroup parent, HashMap<String, String> attrs) {
////        View view = mLayoutInflaterDelegate.beforeCreateView(context, node, viewName, parent, attrs);
////        if (view != null)
////            return view;
////        return mLayoutInflaterDelegate.afterCreateView(context, createViewForName(viewName, attrs), node, viewName, parent, attrs);
//        return createViewForName(viewName, attrs);
//    }
//
//    public View createViewForName(String name, HashMap<String, String> attrs) {
//        try {
//            if (name.equals("View")) {
//                return new View(mContext);
//            }
//            if (!name.contains(".")) {
//                name = "android.widget." + name;
//            }
////            ViewCreator<?> creator = mViewCreators.get(name);
////            if (creator != null) {
////                return creator.create(mContext, attrs);
////            }
//            Class<?> clazz = Class.forName(name);
//            String style = attrs.get("style");
//            if (style == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                return (View) clazz.getConstructor(Context.class).newInstance(mContext);
//            } else {
//                int styleRes = Res.parseStyle(mContext, style);
//                return (View) clazz.getConstructor(Context.class, AttributeSet.class, int.class, int.class)
//                        .newInstance(mContext, null, 0, styleRes);
//            }
//        } catch (Exception e) {
//            throw new InflateException(e);
//        }
//    }
//
//    public HashMap<String, String> getAttributesMap(Node currentNode) {
//        NamedNodeMap attributeMap = currentNode.getAttributes();
//        int attributeCount = attributeMap.getLength();
//        HashMap<String, String> attributes = new HashMap<>(attributeCount);
//        for (int j = 0; j < attributeCount; j++) {
//            Node attr = attributeMap.item(j);
//            String nodeName = attr.getNodeName();
//            attributes.put(nodeName, attr.getNodeValue());
//        }
//        return attributes;
//    }
//
//    protected String convertXml(InflateContext context, String xml) {
////        String str = mLayoutInflaterDelegate.beforeConvertXml(context, xml);
////        if (str != null)
////            return str;
//        try {
////            return mLayoutInflaterDelegate.afterConvertXml(context, XmlConverter.convertToAndroidLayout(xml));
//            return XmlConverter.convertToAndroidLayout(xml);
//        } catch (Exception e) {
//            throw new InflateException(e);
//        }
//    }
//}