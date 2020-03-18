package com.example.pf4jdemo.listener;

import org.springframework.context.ApplicationEvent;

import java.io.File;

/**
 * @Author sharplee
 * @Date 2020/3/17 15:17
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.listener
 * @ClassName FileCreateEvent
 * @JavaFile com.example.pf4jdemo.listener.FileCreateEvent.java
 */

public class FileCreateEvent extends ApplicationEvent {
    private File file;
    private boolean isFile;

    public FileCreateEvent(Object source) {
        super(source);
    }
}
