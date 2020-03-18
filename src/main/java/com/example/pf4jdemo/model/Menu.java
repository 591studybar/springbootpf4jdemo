package com.example.pf4jdemo.model;

/**
 * @Author sharplee
 * @Date 2020/3/3 14:42
 * @Version 1.0
 * @PackageName com.java.model
 * @ClassName Menu
 * @JavaFile com.java.model.Menu.java
 */
public class Menu {
    private Long id;
    private String name;
    private String url;
    private String operator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
