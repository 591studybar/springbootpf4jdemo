package com.example.pf4jdemo.listener;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.stereotype.Component;

/**
 * @Author sharplee
 * @Date 2020/3/16 16:11
 * @Version 1.0
 * @PackageName com.example.pf4jdemo.listener
 * @ClassName ApplicationRunlistener
 * @JavaFile com.example.pf4jdemo.listener.ApplicationRunlistener.java
 */
@Component
public class ApplicationRunlistener implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(()->{
            for(;;){
              //  System.out.println("1");
                //System.out.println("ppppppppppppppppppppppp");
            }
        }).start();
    }
}
