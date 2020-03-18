package com.example.pf4jdemo.utils;

/**
 * @Author sharplee
 * @Date 2020/3/4 10:07
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.utils
 * @ClassName ThreadTest
 * @JavaFile com.example.pf4jdemo.utils.ThreadTest.java
 */
public class ThreadTest  implements Runnable{
    private String path="";
    public ThreadTest(String path){
        this.path= path;
    }
    @Override
    public void run() {
        while(true){
            System.out.println(path);
        }
    }
}
