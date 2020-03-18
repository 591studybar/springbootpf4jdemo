package com.example.pf4jdemo.controller;

import com.example.pf4jdemo.api.Printers;
import com.java.api.Printer;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author sharplee
 * @Date 2020/3/3 10:52
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.controller
 * @ClassName IndexController
 * @JavaFile com.example.pf4jdemo.controller.IndexController.java
 */
@RestController
public class IndexController {

    //@Autowired
   // private PluginManager pluginManager;

    //@Qualifier("com.java.serviceImpl.HPPrinter")
    //@Autowired
    //private Printers printers;

//     @Qualifier("com.java.serviceImpl.HPPrinter")
//     @Autowired
//     private Printer pp;

    //@Autowired
    //private List<Printer> printerslist;

    @PreAuthorize("hasAuthority('jj')")
    @GetMapping("test")
    public String test(){
      //  System.out.println(pp);

        //printers.printList();

        //printerslist.stream().forEach(e->e.print("vvv"));
        return "test";
    }


    @GetMapping("tt")
    public String tes(){
//        List<PluginWrapper> plugins = pluginManager.getPlugins();
//
//        pluginManager.loadPlugins();
//
//        List<PluginWrapper> plugins1 = pluginManager.getPlugins();
//        System.out.println("---------------------------------");
//        List<com.java.api.MenuService> m=  pluginManager.getExtensions(com.java.api.MenuService.class);
//        System.out.println(m.size());
//        m.stream().forEach(e-> System.out.println(e.print("ss")));
//        System.out.println("----------------------------------");
//        return "tt"+plugins.size()+plugins1.size();
        return "tt";
    }
}
