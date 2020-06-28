package com.berwin.devtoolkits.views;


import com.berwin.devtoolkits.entity.Tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class MainView {

    private java.util.List<Tool> tools = new ArrayList<Tool>();

    public MainView() {
        this.initDatas();
        this.initTray();
    }

    private void initDatas() {
        tools.add(new Tool("取色器", KeyEvent.VK_I, "com.berwin.devtoolkits.tools.hue.HexFrame"));
        tools.add(new Tool("", -1, "separator"));
        tools.add(new Tool("退出", KeyEvent.VK_E, "exit"));
    }

    private void initTray() {
        if (SystemTray.isSupported()) {
            ImageIcon icon = new ImageIcon("res/tools.png");
            Image image = icon.getImage();
            TrayIcon trayIcon = new TrayIcon(image);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                }
            });
            trayIcon.setToolTip("开发工具集");
            trayIcon.setPopupMenu(this.initPopupMenu());
            SystemTray systemTray = SystemTray.getSystemTray();
            try {
                systemTray.add(trayIcon);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "不支持系统托盘");
        }
    }

    private PopupMenu initPopupMenu() {
        PopupMenu popupMenu = new PopupMenu();
        for (Tool tool : tools) {
            if (tool.getClazz().equals("separator")) {
                popupMenu.addSeparator();
            } else if (tool.getClazz().equals("exit")) {
                MenuItem exitItem = tool.getMenuItem();
                popupMenu.add(exitItem);
                exitItem.addActionListener(e -> {
                    System.exit(0);
                });
            } else {
                MenuItem item = tool.getMenuItem();
                popupMenu.add(item);
                item.addActionListener(e -> {
                    Class clazz = null;
                    try {
                        clazz = Class.forName(tool.getClazz());
                        Constructor constructor = clazz.getConstructor();
                        BaseFrame frame = (BaseFrame) constructor.newInstance();
                        frame.setVisible(true);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });
            }
        }
        return popupMenu;
    }
}
