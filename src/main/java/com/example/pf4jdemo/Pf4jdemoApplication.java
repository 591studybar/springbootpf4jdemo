package com.example.pf4jdemo;

import com.example.pf4jdemo.api.HelloService;
import com.example.pf4jdemo.api.MenuService;
import com.example.pf4jdemo.pf4j.CustomerPluginManager;
import com.example.pf4jdemo.plugin.HelloPlugin;
import com.java.api.Printer;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.*;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Slf4j
@SpringBootApplication
public class Pf4jdemoApplication {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Pf4jdemoApplication.class, args);

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for(String name:beanDefinitionNames) {
            log.info("bean is {}",name);
        }
       //final PluginManager manager = new SpringPluginManager();
        PluginManager manager = applicationContext.getBean(PluginManager.class);
        //manager.loadPlugins();
        //manager.startPlugins();




        List<HelloService> helloServices = manager.getExtensions(HelloService.class);

        System.out.println(helloServices.size()+"ll");


        PluginWrapper printer1 = manager.getPlugin("printer1");

//        List<Printer> printer11 = manager.getExtensions("printer1");
//
//        System.out.println(printer11.size());
//
//        printer11.stream().forEach(o->o.print("==================="));
//
//        printer11.stream().forEach(o->{if(o.getClass().isAssignableFrom(Printer.class)){o.print("++++++++++++");}});

        helloServices.stream().forEach(helloService -> System.out.println(helloService.sayHello("............")));

        List<MenuService> menuServices = manager.getExtensions(MenuService.class);

        System.out.println("menus:"+menuServices.size());
//        PluginRepository repository = ((CustomerPluginManager) manager).getRepository();
//        List<Path> pluginPaths = repository.getPluginPaths();
//        pluginPaths.stream().forEach(System.out::println);


//        Path root = manager.getPluginsRoot();
//
//
//        final List<File> jars = FileUtils.getJars(root);
//
//        jars.stream().forEach(System.out::println);
//
//        System.out.println( root.toFile().getCanonicalFile().toURI().toURL());


//        List<HelloService> hello = manager.getExtensions(HelloService.class);
//
//        System.out.println(hello.size());
//
//        hello.stream().forEach(helloService -> System.out.println(helloService.sayHello("ppp")));
//
//        List<MenuService> menus = manager.getExtensions(MenuService.class);
//
//        System.out.println(menus.size());
//
//        menus.stream().forEach(e-> System.out.println(e.print("ss")));


        List<Printer> printers = manager.getExtensions(Printer.class);
        System.out.println(printers.size());
        printers.stream().forEach(printer -> printer.print("000000-----------"));


        System.out.println(Printer.class.getClassLoader());

        System.out.println(MenuService.class.getClassLoader());

        System.out.println(HelloService.class.getClassLoader());

        System.out.println(manager.whichPlugin(HelloService.class));

        System.out.println(manager.whichPlugin(Printer.class));

        System.out.println("Extensions added by classpath:");
        Set<String> extensionClassNames = manager.getExtensionClassNames(null);
        for (String extension : extensionClassNames) {
            System.out.println("   " + extension);
        }

        System.out.println("Extension classes by classpath:");
        List<Class<? extends HelloService>> greetingsClasses = manager.getExtensionClasses(HelloService.class);
        for (Class<? extends HelloService> greeting : greetingsClasses) {
            System.out.println("   Class: " + greeting.getCanonicalName());
        }

        // print extensions ids for each started plugin
        List<PluginWrapper> startedPlugins = manager.getStartedPlugins();
        for (PluginWrapper plugin : startedPlugins) {
            String pluginId = plugin.getDescriptor().getPluginId();
            System.out.println(String.format("Extensions added by plugin '%s':", pluginId));
            extensionClassNames = manager.getExtensionClassNames(pluginId);
            for (String extension : extensionClassNames) {
                System.out.println("   " + extension);
            }
        }

        //System.out.println(applicationContext.getBean("com.java.serviceImpl.HPPrinter"));

        Printer printer = (Printer) applicationContext.getBean("com.java.serviceImpl.HPPrinter");


        PluginWrapper bootplugin = manager.getPlugin("bootplugin");
        System.out.println();

        printer.print("test");


    }


}
