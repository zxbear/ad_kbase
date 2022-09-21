package com.zxbear.mvppiler;


import com.google.auto.service.AutoService;
import com.zxbear.annotation.MvpAct;
import com.zxbear.annotation.PARAS;
import com.zxbear.mvppiler.print.ElementPrint;

import net.ltgt.gradle.incap.IncrementalAnnotationProcessor;
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.DYNAMIC)//支持注解器增量编译
@SupportedSourceVersion(SourceVersion.RELEASE_8)//支持的 Java 版本
@SupportedOptions("moduleName")//注解器接收的参数
public class MvpProcessor extends AbstractProcessor {
    private Filer filer;
    private Elements elementUtils;
    private String moduleName;
    public final static String PATH_ANDROID_SRC = "/src/main/java/";
    public final static String PATH_PRESENTER = "presenter";
    public final static String PATH_CONSTRACT = "constract";
    public final static String CLASS_NAME_PRESENTER = "Presenter";
    public final static String CLASS_NAME_CONSTRACT = "Constract";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        elementUtils = processingEnv.getElementUtils();
        Map<String, String> options = processingEnv.getOptions();
        moduleName = options.get("moduleName");
        ElementPrint.getInstance().init(processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(MvpAct.class.getName());
        return annotations;
    }

    //参考greendao
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //拿到ActiveMvpAn所有注解节点
        if (!set.isEmpty()) {
            Map<Element, MvpSet> mElements = new HashMap<>();
            Set<? extends Element> activeElement = roundEnvironment.getElementsAnnotatedWith(MvpAct.class);
            generatedActiveClass(mElements, activeElement);
            //创建对应的文件
            if (mElements.size() > 0) {
                Set<Map.Entry<Element, MvpSet>> mvpSet = mElements.entrySet();
                for (Map.Entry<Element, MvpSet> item : mvpSet) {
                    item.getValue().createMvpJavaFile().JavaFileToWrite();
                }
            }
            return true;
        }
        return false;
    }

    private void generatedActiveClass(Map<Element, MvpSet> mElement, Set<? extends Element> activeElement) {
        for (Element item : activeElement) {
            //判断是否需要构建mvp框架类
            MvpAct mvpAn = item.getAnnotation(MvpAct.class);
            if (mvpAn.onCreate() == PARAS.CREATE) {
                String sysNamePath = System.getProperty("user.dir").replaceAll("\\\\", "/") + "/" + moduleName;
                StringBuffer activeCreatePath = new StringBuffer(sysNamePath);
                activeCreatePath.append(PATH_ANDROID_SRC);
                boolean isCreateJavaFile = true;
                //生成的java路径
                String selfPack = mvpAn.path().isEmpty() ?
                        item.getEnclosingElement().toString() : mvpAn.path();
                //构建path
                if (selfPack.contains(".")) {
                    activeCreatePath.append(selfPack.replace(".", "/") + "/");
                } else {
                    activeCreatePath.append(selfPack);
                }
                //判断待生成文件是否存在
                String errorMsg = "";
                if (null == moduleName || moduleName.isEmpty()) {
                    errorMsg = "Please set moduleName in module gradle arguments!";
                    isCreateJavaFile = false;
                }
                String presenterClassName = item.getSimpleName() + CLASS_NAME_PRESENTER + ".java";
                File file = new File(activeCreatePath + PATH_PRESENTER + "/" + presenterClassName);
                if (file.exists()) {
                    errorMsg = "The Presenter File exists!";
                    isCreateJavaFile = false;
                }
                String constractClassName = item.getSimpleName() + CLASS_NAME_CONSTRACT + ".java";
                File file2 = new File(activeCreatePath + PATH_CONSTRACT + "/" + constractClassName);
                if (file2.exists()) {
                    errorMsg = "The Constract File exists!";
                    isCreateJavaFile = false;
                }
                if (isCreateJavaFile) {
                    MvpSet set = new MvpSet();
                    set.init(item, sysNamePath + PATH_ANDROID_SRC, selfPack);
                    mElement.put(item, set);
                } else {
                    ElementPrint.getInstance().printNote(errorMsg);
                }
            } else {
                if (mvpAn.onCreate() != PARAS.CREATED) {
                    ElementPrint.getInstance().printWarning("The MvpAct annotation onCreate param is invalid!");
                }
            }
        }
    }
}