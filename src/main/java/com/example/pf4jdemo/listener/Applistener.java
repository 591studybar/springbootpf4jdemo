package com.example.pf4jdemo.listener;

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginLoader;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;

/**
 * @Author sharplee
 * @Date 2020/3/4 10:23
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.listener
 * @ClassName Applistener
 * @JavaFile com.example.pf4jdemo.listener.Applistener.java
 */
@Component
public class Applistener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private FileOperate fileOperate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent applicationEvent) {
//        PluginManager m = new DefaultPluginManager();
//        Path pluginsRoot = m.getPluginsRoot();
//        PluginLoader pluginLoader = ((DefaultPluginManager) m).getPluginLoader();
//
//
//
//        System.out.println(pluginsRoot.toFile().getName());
//
//        m.loadPlugins();
       // System.out.println("applicationEvent"+applicationEvent);
        if (applicationEvent.getApplicationContext().getParent() == null) {//保证只执行一次
            new Thread(()->{
                try {
                    fileOperate.checkfileexist();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ).start();
        }


    }
}
