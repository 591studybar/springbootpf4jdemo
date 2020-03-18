package com.example.pf4jdemo.pf4j.spring;

import com.example.pf4jdemo.pf4j.registry.Pf4jDynamicControllerRegistry;
import com.example.pf4jdemo.utils.ClassUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.pf4j.spring.ExtensionsInjector;
import org.pf4j.spring.SpringPlugin;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * @Author sharplee
 * @Date 2020/3/9 15:27
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.pf4j.spring
 * @ClassName CustomerSpringExtensionInjector
 * @JavaFile com.example.pf4jdemo.pf4j.spring.CustomerSpringExtensionInjector.java
 */
@Slf4j
public class CustomerSpringExtensionInjector extends ExtensionsInjector {



    protected Pf4jDynamicControllerRegistry pf4jDynamicControllerRegistry;

    public CustomerSpringExtensionInjector(PluginManager pluginManager, AbstractAutowireCapableBeanFactory beanFactory,Pf4jDynamicControllerRegistry pf4jDynamicControllerRegistry) {
        super(pluginManager, beanFactory);
        this.pf4jDynamicControllerRegistry = pf4jDynamicControllerRegistry;
    }

    @SneakyThrows
    @Override
    public void injectExtensions() {
        //super.injectExtensions();
        // add extensions from classpath (non plugin)
        Set<String> extensionClassNames = pluginManager.getExtensionClassNames(null);
        for (String extensionClassName : extensionClassNames) {
            try {
                log.debug("Register extension '{}' as bean", extensionClassName);
                Class<?> extensionClass = getClass().getClassLoader().loadClass(extensionClassName);
                registerExtension(extensionClass);
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage(), e);
            }
        }


        // add extensions for each started plugin
        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
        for (PluginWrapper plugin : startedPlugins) {
            log.debug("Registering extensions of the plugin '{}' as beans", plugin.getPluginId());

            String path = this.getClass().getResource("/").getPath();

            String packageName = "com.java.plugindemo.config.Config";

            String res = packageName.replace(".", "/");

            String filepath = path+res+".class";
            File f = new File(filepath);
            System.out.println("ff"+f);
            System.out.println(res);

            ClassUtil classutil = new ClassUtil();
            byte[] bytes = classutil.generateClass();

            if(!f.exists()){
                f.getParentFile().mkdirs();
            }

            if(plugin.getPlugin().getWrapper().getPluginId().equals("bootplugin")) {
                FileUtils.writeByteArrayToFile(f, bytes);
            }

            if(plugin.getPlugin() instanceof SpringPlugin){
                GenericApplicationContext applicationContext = (GenericApplicationContext) ((SpringPlugin) plugin.getPlugin()).getApplicationContext();
                AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
                DefaultListableBeanFactory defaultListableBeanFactory = applicationContext.getDefaultListableBeanFactory();
                String[] restControllerNames = defaultListableBeanFactory.getBeanNamesForAnnotation(RestController.class);
                String[] serviceNames = defaultListableBeanFactory.getBeanNamesForAnnotation(Service.class);

                String[] controllerNames = defaultListableBeanFactory.getBeanNamesForAnnotation(Controller.class);

                URL url = plugin.getPlugin().getWrapper().getPluginClassLoader().getResource("templates");

                if(url!=null){
                    System.out.println(url.getFile());
                }


                System.out.println("url:"+url);


                try {
                    Resource resource = applicationContext.getResource("classpath:**");

                    //Object bean = applicationContext.getBean("com.java.plugindemo.model.Article");
                    //System.out.println("bean:"+bean);
                    System.out.println("bool:"+resource.exists());

                    String[] aClass1 = applicationContext.getDefaultListableBeanFactory().getBeanNamesForAnnotation(RestController.class);
                    //Object testService = applicationContext.getBean("testService");
                    //System.out.println("testService:"+testService);
                    System.out.println("aclass11"+aClass1.length);
                    System.out.println("aclass1"+aClass1);
                    RestController[] declaredAnnotationsByType = plugin.getPlugin().getWrapper().getPluginClassLoader().getClass().getDeclaredAnnotationsByType(RestController.class);
                    System.out.println("length:"+declaredAnnotationsByType.length);
                    Class<?> aClass = plugin.getPlugin().getWrapper().getPluginClassLoader().loadClass("com.java.plugindemo.controller.TestController");
                    System.out.println("a class:"+aClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }



                log.info("cs"+controllerNames.length);
                log.info("ss"+serviceNames.length);



                for(String s:controllerNames){
                    log.info("controller is :"+s);
                }





                if(plugin.getPlugin().getWrapper().getPluginId().equals("bootplugin")){
                    try {
                        System.out.println("------");
                        System.out.println(applicationContext.getClassLoader().getClass());
                        Class<?> config = plugin.getPluginClassLoader().getClass();
                        System.out.println("config class:"+config.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                System.out.println(this.getClass().getResource("/").getPath());




                //System.out.println(System.getProperty("java.class.path"));

                for(String controller:controllerNames){
                   pf4jDynamicControllerRegistry.registerController(controller,applicationContext.getBean(controller));
                }
//                for(String controller:restControllerNames){
//                    pf4jDynamicControllerRegistry.registerController(controller,applicationContext.getBean(controller));
//                }

                for(String se:serviceNames){
                    log.info("service is :"+se);
                }
                RequestMappingHandlerMapping bean = applicationContext.getBean(RequestMappingHandlerMapping.class);

                log.info("plugin bean:"+bean);

                String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
                for(String a:beanDefinitionNames){
                    log.info("a is :"+a);
                }
                log.info("----plugin------"+plugin);

            }

            extensionClassNames = pluginManager.getExtensionClassNames(plugin.getPluginId());
            for (String extensionClassName : extensionClassNames) {
                try {
                    log.debug("Register extension '{}' as bean", extensionClassName);
                    Class<?> extensionClass = plugin.getPluginClassLoader().loadClass(extensionClassName);
                    registerExtension(extensionClass);
                } catch (ClassNotFoundException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    protected void registerExtension(Class<?> extensionClass) {

        Object extension = this.pluginManager.getExtensionFactory().create(extensionClass);
        System.out.println(extension.getClass().getName());
        System.out.println(extension);
        // 这个主要是把插件中的bean注册到applicationcontext上下问中，这样的话，就可以通过applicationcontext来获取插件中的bean，也可以使用通过autowired来连接插件中的bean。
        // 连接插件中的bean 得重新新建一个class bean 来autowired ,再config中配置depends-on 之后才能获取到依赖。
        this.beanFactory.registerSingleton(extension.getClass().getName(), extension);

        if(this.pluginManager instanceof SpringPluginManager){
            System.out.println("");
        }


    }
}
