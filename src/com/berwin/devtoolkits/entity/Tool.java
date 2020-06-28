package com.berwin.devtoolkits.entity;

import java.awt.*;

public class Tool {

    /**
     * 名字
     */
    private String name;

    /**
     * 快捷键标识
     */
    private int key;

    /**
     * 类包名
     */
    private String clazz;

    public Tool(String name, int key, String clazz) {
        this.name = name;
        this.key = key;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public MenuItem getMenuItem() {
        MenuItem item = new MenuItem(this.name);
        item.setShortcut(new MenuShortcut(this.key));
        return item;
    }
}
