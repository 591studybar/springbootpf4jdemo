package com.example.pf4jdemo.api;

import com.java.api.Printer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

/**
 * @Author sharplee
 * @Date 2020/3/9 17:17
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.api
 * @ClassName Printers
 * @JavaFile com.example.pf4jdemo.api.Printers.java
 */
public class Printers {

    @Autowired
    private List<Printer> printers;

    @Qualifier("com.java.serviceImpl.HPPrinter")
    @Autowired
    private Printer pp;

    public void printList(){
        System.out.println(printers.size() );
        for (Printer p : printers){
            p.print("xxxxx");
        }
        System.out.println(pp);
    }

}
