package com.example.pf4jdemo.listener;

import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Author sharplee
 * @Date 2020/3/17 17:32
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.listener
 * @ClassName FileFilter
 * @JavaFile com.example.pf4jdemo.listener.FileFilter.java
 */
@Component
public class FileFilter {
    private final static String FILE_EXTENSION ="jar";



    public boolean filter(File file){
        System.out.println("-----");
       String fileName = file.getName();
       int fileIndex = fileName.lastIndexOf(".");
       String fileExtension = fileName.substring(fileIndex+1,fileName.length());
       System.out.println(fileExtension);
       if(fileExtension.equals(FILE_EXTENSION)){
           return true;
       }
       return false;
    }


}
