package com.zxbear.mvppiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.zxbear.annotation.MvpAct;
import com.zxbear.annotation.PARAS;
import com.zxbear.mvppiler.act.LayoutCreate;
import com.zxbear.mvppiler.act.RegisteredAct;

import java.io.File;
import java.io.IOException;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class MvpSet {
    //注解的节点
    private Element tE;
    //写入文件的file
    private File writeFile;
    //构建的包名
    private String conPack, prePack, mainPack;
    //构建的类
    private TypeSpec.Builder conSpec, preSpec, mainSpec;
    private String conClassName, preClassName;
    private ClassName classNameConView, classNameConPre;
    //xml布局
    private String xmlName;
    private LayoutCreate lc;
    //Activity注册
    private RegisteredAct rAct;

    private static final ClassName CLASS_BASE_CON = ClassName.get("com.zxbear.base.activty", "BaseMvpView");
    private static final ClassName CLASS_BASE_PRE = ClassName.get("com.zxbear.base.activty", "BaseMvpPresenter");
    private static final ClassName CLASS_CONTEXT = ClassName.get("android.content", "Context");
    private static final ClassName CLASS_BASE_AC = ClassName.get("com.zxbear.base.activty", "BaseMvpActivity");
    private static final ClassName CLASS_PPS = ClassName.get("com.zxbear.annotation", "PARAS");

    public void init(Element tE, String filePath, String newPack) {
        this.tE = tE;
        writeFile = new File(filePath);
        //create constract、presenter package
        String allPackPath = filePath + newPack.replace(".", "/");
        File conFile = new File(allPackPath + "/" + MvpProcessor.PATH_CONSTRACT);
        if (!conFile.exists()) {
            conFile.mkdirs();
        }
        File presenterFile = new File(allPackPath + "/" + MvpProcessor.PATH_PRESENTER);
        if (!presenterFile.exists()) {
            presenterFile.mkdirs();
        }
        //int other params
        conPack = newPack + "." + MvpProcessor.PATH_CONSTRACT;
        prePack = newPack + "." + MvpProcessor.PATH_PRESENTER;
        mainPack = tE.getEnclosingElement().toString();
        // 注解类名
        String mainClassName = tE.getSimpleName().toString();
        if (mainClassName.contains("Activity")) {
            String tempName = mainClassName.replace("Activity", "");
            conClassName = tempName + MvpProcessor.CLASS_NAME_CONSTRACT;
            preClassName = tempName + MvpProcessor.CLASS_NAME_PRESENTER;
            xmlName = "activity_" + tempName.toLowerCase() + "_layout";
        } else {
            conClassName = tE.getSimpleName() + MvpProcessor.CLASS_NAME_CONSTRACT;
            preClassName = tE.getSimpleName() + MvpProcessor.CLASS_NAME_PRESENTER;
            xmlName = "activity_" + tE.getSimpleName().toString().toLowerCase() + "_layout";
        }
        conSpec = TypeSpec.interfaceBuilder(conClassName)
                .addModifiers(Modifier.PUBLIC);
        preSpec = TypeSpec.classBuilder(preClassName)
                .addModifiers(Modifier.PUBLIC);
        mainSpec = TypeSpec.classBuilder(mainClassName)
                .addModifiers(Modifier.PUBLIC);
        classNameConView = ClassName.get(conPack, conClassName, "View");
        classNameConPre = ClassName.get(conPack, conClassName, "Presenter");
        //初始化布局xml
        String mainPath = filePath.substring(0, filePath.indexOf("main") + 4);
        String resPath = mainPath + "/res/layout";
        lc = new LayoutCreate();
        lc.initParas(resPath, xmlName);
        //初始化activity注册
        rAct = new RegisteredAct();
        rAct.init(mainPath, mainClassName, tE.getEnclosingElement().toString());
    }

    /**
     * 初始化mvp类
     */
    public MvpSet createMvpJavaFile() {
        if (tE != null) {
            createConstract();
            createPresenter();
            updateMainClassFile();
        }
        return this;
    }

    /**
     * 创建constract文件
     */
    private void createConstract() {
        TypeSpec.Builder ViewSpec = TypeSpec.interfaceBuilder("View")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addSuperinterface(CLASS_BASE_CON);
        TypeSpec.Builder preSpec = TypeSpec.interfaceBuilder("Presenter")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        conSpec.addType(ViewSpec.build());
        conSpec.addType(preSpec.build());
    }

    /**
     * 创建presenter文件
     */
    private void createPresenter() {
        //继承 BaseMvpPresenter<xxx.view>
        ParameterizedTypeName inputMapTypeOfRoot = ParameterizedTypeName.get(
                CLASS_BASE_PRE,
                classNameConView
        );
        preSpec.superclass(inputMapTypeOfRoot)
                .addSuperinterface(classNameConPre);
        //must
        ParameterSpec p1 = ParameterSpec.builder(classNameConView, "mView").build();
        ParameterSpec p2 = ParameterSpec.builder(CLASS_CONTEXT, "mContext").build();
        MethodSpec rootPreMethodSpec = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(p1)
                .addParameter(p2)
                .addStatement("super(mView, mContext)")
                .build();
        preSpec.addMethod(rootPreMethodSpec);
    }

    /**
     * 更新注解类文件
     */
    private void updateMainClassFile() {
        //注解
        AnnotationSpec.Builder asp = AnnotationSpec.builder(MvpAct.class);
        CodeBlock.Builder code = CodeBlock.builder();
        MvpAct at = tE.getAnnotation(MvpAct.class);
        if (at.onCreate() == PARAS.CREATE || at.onCreate() == PARAS.CREATED) {
            code.add("$T.CREATED", CLASS_PPS);
        } else {
            code.add("$T.CREATE_ERROR", CLASS_PPS);
        }
        asp.addMember("onCreate", code.build());
        if (at.path() != null && !at.path().isEmpty()) {
            asp.addMember("path", "\"" + at.path() + "\"");
        }
        mainSpec.addAnnotation(asp.build());
        //继承与实现
        ClassName mPresenterClass = ClassName.get(prePack, preClassName);
        ParameterizedTypeName inputMapTypeOfRoot = ParameterizedTypeName.get(
                CLASS_BASE_AC,
                mPresenterClass
        );
        mainSpec.superclass(inputMapTypeOfRoot)
                .addSuperinterface(classNameConView);
        //method - getView
        ClassName R = ClassName.get(tE.getEnclosingElement().toString(), "R");
        CodeBlock.Builder code3 = CodeBlock.builder();
        code3.add("return $T.layout." + xmlName, R);
        MethodSpec gvSpec = MethodSpec.methodBuilder("getView")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addStatement(code3.build())
                .build();
        //method - getPresenter
        CodeBlock.Builder code2 = CodeBlock.builder();
        code2.add("return new $T(this,this)", mPresenterClass);
        MethodSpec gptSpec = MethodSpec.methodBuilder("getPresenter")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(mPresenterClass)
                .addStatement(code2.build())
                .build();
        //method - setTitleBar
        MethodSpec titleSpec = MethodSpec.methodBuilder("setTitleBar")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .build();
        //method - initView
        MethodSpec initViewSpec = MethodSpec.methodBuilder("initView")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .build();
        //method - initData
        MethodSpec initDataSpec = MethodSpec.methodBuilder("initData")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .build();
        mainSpec.addMethod(gvSpec)
                .addMethod(gptSpec)
                .addMethod(titleSpec)
                .addMethod(initViewSpec)
                .addMethod(initDataSpec);
    }

    /**
     * 构建文件
     */
    public void JavaFileToWrite() {
        if (tE != null) {
            try {
                //create layout
                lc.createLinearLayout();
                //create constract
                JavaFile.builder(conPack, conSpec.build()).build().writeTo(writeFile);
                //create presenter
                JavaFile.builder(prePack, preSpec.build()).build().writeTo(writeFile);
                //reset activity
                JavaFile.builder(mainPack, mainSpec.build()).build().writeTo(writeFile);
                //activity注册
                rAct.registerAct();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
