package com.example.pf4jdemo.utils;

/**
 * @Author sharplee
 * @Date 2020/3/4 10:10
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.utils
 * @ClassName Test
 * @JavaFile com.example.pf4jdemo.utils.Test.java
 */
public class Test {

    private Thread th =null;

    public Test(Runnable r){
        th = new Thread(r);
        if(th!=null){
            th.start();
        }
    }
}
