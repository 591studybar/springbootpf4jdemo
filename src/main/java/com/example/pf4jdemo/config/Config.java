package com.example.pf4jdemo.config;

import com.example.pf4jdemo.api.Printers;
import com.example.pf4jdemo.listener.FileOperate;
import com.example.pf4jdemo.model.Test1;
import com.example.pf4jdemo.pf4j.spring.CustomerSpringPluginManager;
import com.example.pf4jdemo.utils.Test;
import com.example.pf4jdemo.utils.ThreadTest;
import com.java.api.Printer;
import org.pf4j.PluginManager;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @Author sharplee
 * @Date 2020/3/4 10:12
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.config
 * @ClassName Config
 * @JavaFile com.example.pf4jdemo.config.Config.java
 */
@Configuration
public class Config {

    @Bean
    public Runnable r(){
        return new ThreadTest("paths");
    }

    @Bean
    public Test t (){
        //return new Test(r());
        return null;
    }

    @Bean
    public PluginManager pluginManager(){
        //return new SpringPluginManager();
        //return null;
        return new CustomerSpringPluginManager();
    }

    @Bean
    @DependsOn("pluginManager")
    public Printers printers(){
        return new Printers();
    }

    //@Bean
    public Test1 test(){
        return  new Test1();
    }

}
