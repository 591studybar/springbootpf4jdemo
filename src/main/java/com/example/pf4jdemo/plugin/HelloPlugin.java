package com.example.pf4jdemo.plugin;

import com.example.pf4jdemo.api.HelloService;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

/**
 * @Author sharplee
 * @Date 2020/3/3 11:37
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.plugin
 * @ClassName HelloPlugin
 * @JavaFile com.example.pf4jdemo.plugin.HelloPlugin.java
 */
public class HelloPlugin extends Plugin {

    public HelloPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("HelloPlugin 启动");
        super.start();
    }

    @Override
    public void stop() {
        System.out.println("HelloPlugin 停止");
        super.stop();
    }

    @Override
    public void delete() {
        System.out.println("HelloPlugin 删除");
        super.delete();
    }

    //@Extension
//    public static class HelloSe implements HelloService {
//
//
//        @Override
//        public String sayHello(String hello) {
//            return "kk"+hello;
//        }
//    }
}
