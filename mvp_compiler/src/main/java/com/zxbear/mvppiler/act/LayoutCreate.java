package com.zxbear.mvppiler.act;



import com.zxbear.mvppiler.print.ElementPrint;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class LayoutCreate {
    private String xmlPath;
    private String xmlName;
    private boolean isInitFish = false;
    private Document doc;

    public void initParas(String path, String name) {
        this.xmlPath = path;
        this.xmlName = name;
        File file = new File(path + "/" + xmlName + ".xml");
        if (file.exists()) {
            ElementPrint.getInstance().printNote("LayoutCreate => The " + xmlName + ".xml" + " exists");
            return;
        }
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            isInitFish = true;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }


    public void createLinearLayout() {
        if (isInitFish) {
            onCreateXml("LinearLayout");
        }
    }

    public void createRelativeLayout() {
        if (isInitFish) {
            onCreateXml("RelativeLayout");
        }
    }


    /**
     * Document创建xml
     */
    private void onCreateXml(String topElement) {
        //创建顶部布局
        Element mll = doc.createElement(topElement);
        //布局标签添加
        mll.setAttribute("xmlns:android", "http://schemas.android.com/apk/res/android");
        mll.setAttribute("android:layout_width", "match_parent");
        mll.setAttribute("android:layout_height", "match_parent");
        mll.setTextContent("\n");
        mll.appendChild(getTextView());
        doc.appendChild(mll);
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            // 换行
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // 文档字符编码
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            // 写入文件
            transformer.transform(new DOMSource(doc), new StreamResult(new File(xmlPath + "/" + xmlName + ".xml")));
        } catch (Exception e) {
            ElementPrint.getInstance().printError(e.getMessage());
        }
    }

    /**
     * 创建TextView标签
     *
     * @return
     */
    private Element getTextView() {
        Element mTextView = doc.createElement("TextView");
        mTextView.setAttribute("android:layout_width", "wrap_content");
        mTextView.setAttribute("android:layout_height", "wrap_content");
        mTextView.setAttribute("android:text", "Hello My mvp activity!");
        return mTextView;
    }
}
