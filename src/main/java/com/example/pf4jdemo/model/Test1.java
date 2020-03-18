package com.example.pf4jdemo.model;

import org.springframework.stereotype.Component;

/**
 * @Author sharplee
 * @Date 2020/3/17 11:19
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.model
 * @ClassName Test
 * @JavaFile com.example.pf4jdemo.model.Test.java
 */
@Component
public class Test1 {

    public Test1(){
        new Thread(()->{
            for (;;){
                //System.out.println("1");
            }
        }).start();
    }
}
