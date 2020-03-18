package com.example.pf4jdemo.listener;

import com.example.pf4jdemo.pf4j.registry.Pf4jDynamicControllerRegistry;
import com.example.pf4jdemo.pf4j.spring.CustomerSpringExtensionInjector;
import com.example.pf4jdemo.pf4j.spring.CustomerSpringPluginManager;
import org.apache.log4j.helpers.FileWatchdog;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * @Author sharplee
 * @Date 2020/3/17 15:24
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.listener
 * @ClassName FileOperate
 * @JavaFile com.example.pf4jdemo.listener.FileOperate.java
 */
@Component
public class FileOperate {

    @Autowired
    private FileFilter fileFilter;

    @Autowired
    private ApplicationContext applicationContext ;

    @Autowired
    private Pf4jDynamicControllerRegistry pf4jDynamicControllerRegistry;

    @Autowired
    private PluginManager customerSpringPluginManager ;

    public void checkfileexist() throws IOException, InterruptedException {
        WatchService watchService
                = FileSystems.getDefault().newWatchService();
        Path path = Paths.get("plugins");

        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE);//,
               // StandardWatchEventKinds.ENTRY_MODIFY);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                File file = new File(event.context().toString());
                Path filePath= Paths.get("plugins",file.getName());
                if(StandardWatchEventKinds.ENTRY_CREATE== kind){
                    System.out.println("创建文件成功"+event.context());
                    boolean filter = fileFilter.filter(file);
                    System.out.println(filter);
                    if (filter){
                        if(applicationContext!=null) {
                         // AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
                          //annotationConfigApplicationContext.setParent(applicationContext);
                          //annotationConfigApplicationContext.refresh();
                            String s = customerSpringPluginManager.loadPlugin(filePath);
                            System.out.println(s);
                            PluginWrapper plugin = customerSpringPluginManager.getPlugin(s);
                            customerSpringPluginManager.startPlugin(s);
                            System.out.println(plugin);
                            //customerSpringPluginManager.startPlugins();
//                            if(plugin.getPlugin() instanceof SpringPlugin)
//                            {
//                                ApplicationContext applicationContext = ((SpringPlugin) plugin.getPlugin()).getApplicationContext();
//
//                            AbstractAutowireCapableBeanFactory beanFactory = (AbstractAutowireCapableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
//                            CustomerSpringExtensionInjector customerSpringExtensionInjector = new CustomerSpringExtensionInjector(customerSpringPluginManager, beanFactory, pf4jDynamicControllerRegistry);
//                            customerSpringExtensionInjector.injectExtensions();
//                            }

                            if(plugin.getPlugin() instanceof SpringPlugin) {
                                GenericApplicationContext applicationContext = (GenericApplicationContext) ((SpringPlugin) plugin.getPlugin()).getApplicationContext();
                                AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
                                DefaultListableBeanFactory defaultListableBeanFactory = applicationContext.getDefaultListableBeanFactory();
                                String[] restControllerNames = defaultListableBeanFactory.getBeanNamesForAnnotation(RestController.class);
                                String[] serviceNames = defaultListableBeanFactory.getBeanNamesForAnnotation(Service.class);

                                String[] controllerNames = defaultListableBeanFactory.getBeanNamesForAnnotation(Controller.class);
                                System.out.println(controllerNames.length);
                                System.out.println(restControllerNames.length);

                                for(String controller:restControllerNames){
                                    pf4jDynamicControllerRegistry.registerController(controller,defaultListableBeanFactory.getBean(controller));
                                }
                            }

                            }
                    }
                }
                if(StandardWatchEventKinds.ENTRY_MODIFY== kind){
                    System.out.println("修改文件成功"+event.context());
                }
                if(StandardWatchEventKinds.ENTRY_DELETE== kind){
                    System.out.println("删除文件成功"+event.context());
                }
            }
            if(!key.isValid()) {
                key.reset();
            }
        }
    }


}
