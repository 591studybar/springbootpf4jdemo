package com.example.pf4jdemo.api;

import java.nio.file.*;

/**
 * @Author sharplee
 * @Date 2020/3/3 21:56
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.api
 * @ClassName Test
 * @JavaFile com.example.pf4jdemo.api.Test.java
 */
public class Test {
    public static void main (String[] args) {
        Path path = Paths.get("plugins");

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            WatchKey key = path.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);
            startListening(watchService);


        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("done");
    }

    private static void startListening (WatchService watchService)
            throws InterruptedException {
        while (true) {
            WatchKey queuedKey = watchService.take();
            for (WatchEvent<?> watchEvent : queuedKey.pollEvents()) {
                System.out.printf("kind=%s, count=%d, context=%s Context type=%s%n ",
                        watchEvent.kind(),
                        watchEvent.count(), watchEvent.context(),
                        ((Path) watchEvent.context()).getClass());
                //do something useful with the modified file/folder here
                if (!queuedKey.reset()) {
                    break;
                }

            }
        }
    }
}
