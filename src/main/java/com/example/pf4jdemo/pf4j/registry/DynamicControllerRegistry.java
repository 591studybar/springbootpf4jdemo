package com.example.pf4jdemo.pf4j.registry;

import java.io.IOException;

/**
 * @Author sharplee
 * @Date 2020/3/11 10:45
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.pf4j.registry
 * @ClassName DynamicControllerRegistry
 * @JavaFile com.example.pf4jdemo.pf4j.registry.DynamicControllerRegistry.java
 */
public interface DynamicControllerRegistry {
    /**
     * 动态注册SpringMVC Controller到Spring上下文
     * @param controllerBeanName	: The name of controller
     * @param controller			: The instance of controller
     */
    public void registerController(String controllerBeanName, Object controller);

    /**
     * 动态从Spring上下文删除SpringMVC Controller
     * @param controllerBeanName		: The name of controller
     * @throws IOException if io error
     */
    public void removeController(String controllerBeanName) throws IOException;
}
