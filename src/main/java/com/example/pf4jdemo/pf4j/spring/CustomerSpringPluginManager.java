package com.example.pf4jdemo.pf4j.spring;

import com.example.pf4jdemo.pf4j.registry.Pf4jDynamicControllerRegistry;
import org.pf4j.ExtensionFactory;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.nio.file.Path;

/**
 * @Author sharplee
 * @Date 2020/3/9 15:09
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.pf4j.spring
 * @ClassName CustomerSpringPluginManager
 * @JavaFile com.example.pf4jdemo.pf4j.spring.CustomerSpringPluginManager.java
 */
public class CustomerSpringPluginManager extends SpringPluginManager {

    private ApplicationContext applicationContext;

    @Autowired
    private Pf4jDynamicControllerRegistry pf4jDynamicControllerRegistry;


    public CustomerSpringPluginManager(){
        super();
    }

    public CustomerSpringPluginManager(Path pluginsRoot) {
        super(pluginsRoot);
    }

    @Override
    protected ExtensionFactory createExtensionFactory() {
        return super.createExtensionFactory();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       this.applicationContext =applicationContext;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        //return super.getApplicationContext();
        return applicationContext;
    }

    @Override
    @PostConstruct
    public void init() {
        this.loadPlugins();
        this.startPlugins();
        //super.init();
        AbstractAutowireCapableBeanFactory beanFactory = (AbstractAutowireCapableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        CustomerSpringExtensionInjector customerSpringExtensionInjector = new CustomerSpringExtensionInjector(this,beanFactory,pf4jDynamicControllerRegistry);
        customerSpringExtensionInjector.injectExtensions();


    }
}

