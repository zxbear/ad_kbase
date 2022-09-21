package com.zxbear.mvppiler.act;

import com.zxbear.mvppiler.print.ElementPrint;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class RegisteredAct {
    private String manifestPath;
    private String ActName;
    private String addActPath;
    private Document manifestDoc;
    private static final String FILE_ANDROID_MANIFEST = "AndroidManifest";
    private static final String NOTE_MANIFEST = "manifest";
    private static final String NOTE_PACKAGE = "package";
    private static final String NOTE_APPLICATION = "application";
    private static final String NOTE_ACTIVITY = "activity";
    private static final String NOTE_TAG_ANDROID_NAME = "android:name";

    public void init(String mainPack, String ActName, String replenishPack) {
        this.manifestPath = mainPack + "/" + FILE_ANDROID_MANIFEST + ".xml";
        this.ActName = ActName;
        this.addActPath = replenishPack + "." + ActName;
    }

    public void registerAct() {
        File manifestXml = new File(manifestPath);
        if (manifestXml.exists()) {
            if (!hasActNote(manifestXml)) {
                writeActNameToManifest();
            } else {
                ElementPrint.getInstance().printNote("RegisteredAct => The " + NOTE_TAG_ANDROID_NAME + "=" + addActPath +
                        " name is exists,in " + NOTE_ACTIVITY + " NOTE.");
            }
        } else {
            ElementPrint.getInstance().printError("The AndroidManifest.xml not exists,register Activity fail!");
        }
    }

    private boolean hasActNote(File manifestXml) {
        //Document解析xml文件，遍历activity节点，是否存在ActName值
        try {
            manifestDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(manifestXml);
            NodeList acts = manifestDoc.getElementsByTagName(NOTE_ACTIVITY);
            for (int i = 0; i < acts.getLength(); i++) {
                Element actNote = (Element) acts.item(i);
                String adName = actNote.getAttribute(NOTE_TAG_ANDROID_NAME);
                if (adName != null && adName.contains(ActName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            ElementPrint.getInstance().printError("The AndroidManifest.xml analysis fail,message: " + e.getMessage());
        }
        return false;
    }

    private void writeActNameToManifest() {
        Element manifestEl;
        String manifestPackName;
        NodeList nodeList1 = manifestDoc.getElementsByTagName(NOTE_MANIFEST);
        if (nodeList1.getLength() > 0) {
            manifestEl = (Element) nodeList1.item(0);
            if (manifestEl != null) {
                manifestPackName = manifestEl.getAttribute(NOTE_PACKAGE);
            } else {
                ElementPrint.getInstance().printError("The AndroidManifest.xml not manifest NOTE!");
                return;
            }
        } else {
            ElementPrint.getInstance().printError("The AndroidManifest.xml not manifest NOTE!");
            return;
        }
        NodeList nodeList2 = manifestDoc.getElementsByTagName(NOTE_APPLICATION);
        Element appEl;
        if (nodeList2.getLength() == 0) {
            //构建application
            appEl = manifestDoc.createElement(NOTE_APPLICATION);
            manifestEl.appendChild(appEl);
        } else {
            appEl = (Element) nodeList2.item(0);
        }
        if (null != manifestPackName && addActPath.contains(manifestPackName)) {
            addActPath = addActPath.replace(manifestPackName, "");
        }
        if (appEl != null) {
            Element addActEl = manifestDoc.createElement(NOTE_ACTIVITY);
            addActEl.setAttribute(NOTE_TAG_ANDROID_NAME, addActPath);
            appEl.appendChild(addActEl);
            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.transform(new DOMSource(manifestDoc), new StreamResult(new File(manifestPath)));
            } catch (Exception e) {
                ElementPrint.getInstance().printError("The AndroidManifest.xml add activity note fail," + e.getMessage());
            }
        } else {
            ElementPrint.getInstance().printWarning("The AndroidManifest.xml " + NOTE_APPLICATION + " note fail");
        }
    }
}
