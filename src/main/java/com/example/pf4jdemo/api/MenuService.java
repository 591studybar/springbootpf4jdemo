package com.example.pf4jdemo.api;

import com.example.pf4jdemo.model.Menu;
import org.pf4j.ExtensionPoint;

/**
 * @Author sharplee
 * @Date 2020/3/3 15:37
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.api
 * @ClassName MenuService
 * @JavaFile com.example.pf4jdemo.api.MenuService.java
 */
public interface MenuService extends ExtensionPoint {
    void addMenu(Menu menu);
    String print(String menu);
}
