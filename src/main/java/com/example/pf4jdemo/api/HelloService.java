package com.example.pf4jdemo.api;

import org.pf4j.ExtensionPoint;

/**
 * @Author sharplee
 * @Date 2020/3/3 11:36
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.api
 * @ClassName HelloService
 * @JavaFile com.example.pf4jdemo.api.HelloService.java
 */
public interface HelloService extends ExtensionPoint {

    String sayHello(String hello);

}
