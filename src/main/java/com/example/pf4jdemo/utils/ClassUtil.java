package com.example.pf4jdemo.utils;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/**
 * @Author sharplee
 * @Date 2020/3/13 15:08
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.utils
 * @ClassName ClassUtil
 * @JavaFile com.example.pf4jdemo.utils.ClassUtil.java
 */
public class ClassUtil {

    private ClassWriter cw = new  ClassWriter(ClassWriter.COMPUTE_FRAMES);

    private AnnotationVisitor av ;

    private MethodVisitor mv;

    public void createClassName(){

        cw.visit(V1_8,                              // Java 1.7
                ACC_PUBLIC|ACC_SUPER,                         // public class
                "com/java/plugindemo/config/Config",    // package and name
                null,                               // signature (null means not generic)
                "java/lang/Object",                 // superclass
                null); // interfaces
        {
            av = cw.visitAnnotation("Lorg/springframework/context/annotation/Configuration", true);
            av.visitEnd();
        }
        {
            av = cw.visitAnnotation("Lorg/springframework/security/config/annotation/method/configuration/E" +
                    "nableGlobalMethodSecurity",true);
            av.visit("prePostEnabled",Boolean.TRUE);
            av.visitEnd();
        }
    }

    public void createContructorMethod(){
        mv = cw.visitMethod(ACC_PUBLIC,"<init>","()V",null,null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD,0);
        mv.visitMethodInsn(INVOKESPECIAL,"java/lang/Object","<init>","()V",false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1,1);
        mv.visitEnd();
    }

    public void createField(){

    }

    public void createMethod(){
        mv = cw.visitMethod(ACC_PUBLIC,"testServiceImpl","()Lcom/java/plugindemo/service/serviceImpl/TestServiceImpl",null,null);
        {
            av = mv.visitAnnotation("Lorg/springframework/context/annotation/Bean",true);
            {
                AnnotationVisitor methodAnnotation = av.visitArray("value");
                methodAnnotation.visit(null,"testService");
                methodAnnotation.visitEnd();
            }
            av.visitEnd();

        }
        mv.visitCode();
        mv.visitTypeInsn(NEW,"com/java/plugindemo/service/serviceImpl/TestServiceImpl");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL,"com/java/plugindemo/service/serviceImpl/TestServiceImpl","<init>","()V",false);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(2,1);
        mv.visitEnd();
    }


    public  byte[] generateClass(){
        createClassName();
        createContructorMethod();
        createMethod();
        return cw.toByteArray();
    }



}
