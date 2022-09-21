package com.zxbear.mvppiler.print;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class ElementPrint {
    private static ElementPrint instance;
    private static ProcessingEnvironment processingEnv;

    public static ElementPrint getInstance() {
        if (null == instance) {
            synchronized (ElementPrint.class) {
                if (null == instance) {
                    instance = new ElementPrint();
                }
            }
        }
        return instance;
    }

    private ElementPrint() {
    }

    public void init(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    /**
     * 错误信息打印
     *
     * @param msg
     * @param element
     */
    public void errorPrint(String msg, Element element) {
        if (processingEnv != null) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
        }
    }

    public void printWarning(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, msg);
    }

    public void printError(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
    }


    public void printNote(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
    }

}
