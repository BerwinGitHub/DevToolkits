package com.berwin.devtoolkits.tools.hue;

import com.berwin.devtoolkits.utility.Utility;
import com.berwin.devtoolkits.views.BaseFrame;
import com.sun.awt.AWTUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HexFrame extends BaseFrame implements KeyListener {

    private GraphicsDevice[] displayDevices;

    public static final int RANGE = 300;
    private HueDialog dialog;


    public HexFrame() {
        try {
            /**
             * 设置图形界面外观 java的图形界面外观有3种,默认是java的金属外观,还有就是windows系统,motif系统外观.
             * 1、Metal风格 (默认) UIManager.setLookAndFeel(
             * "javax.swing.plaf.metal.MetalLookAndFeel"); 2、Windows风格
             * UIManager.setLookAndFeel(
             * "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); 3、Windows
             * Classic风格 UIManager.setLookAndFeel(
             * "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
             * 4、Motif风格 UIManager.setLookAndFeel(
             * "com.sun.java.swing.plaf.motif.MotifLookAndFeel"); 5、Mac风格
             * (需要在相关的操作系统上方可实现) String lookAndFeel =
             * "com.sun.java.swing.plaf.mac.MacLookAndFeel"
             * ;UIManager.setLookAndFeel(lookAndFeel); 6、GTK风格 (需要在相关的操作系统上方可实现)
             * String lookAndFeel =
             * "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
             * UIManager.setLookAndFeel(lookAndFeel); 7、可跨平台的默认风格 String
             * lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
             * UIManager.setLookAndFeel(lookAndFeel); 8、当前系统的风格 String
             * lookAndFeel = UIManager.getSystemLookAndFeelClassName();
             * UIManager.setLookAndFeel(lookAndFeel);
             */
            if ("Mac OS X".equals(System.getProperties().getProperty("os.name"))) {
                UIManager
                        .setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else {
                UIManager
                        .setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setLayout(null);
        this.setUndecorated(true);
        this.setResizable(false);
        this.setSize(Utility.getScreenSize());
        this.setLocationRelativeTo(null);
        this.setTitle("数码取色器");
        this.addKeyListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dialog = new HueDialog(this);
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        displayDevices = environment.getScreenDevices();
        this.adjustDisplayDevice(MouseInfo.getPointerInfo().getLocation());

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                HexFrame.this.setAlwaysOnTop(false);
                HexFrame.this.setSize(new Dimension(0, 0));
                dialog.picked();
            }
        });
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        dialog.setVisible(b);
        if (b) {
            this.start();
        } else {
        }
    }

    public void start() {
        this.setLocation(0, 0);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setAlwaysOnTop(true);
        AWTUtilities.setWindowOpacity(this, 0.01f);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    public Rectangle getDisplayDeviceBoundsByMousePoint(Point p) {
        Rectangle rect = displayDevices[0].getDefaultConfiguration().getBounds();
//        if (rect == null)
//            return new Rectangle();
        for (int i = 0; i < displayDevices.length; i++) {
            Rectangle rt = displayDevices[i].getDefaultConfiguration().getBounds();
            if (p.x >= rt.x && p.x <= rt.x + rt.width && p.y >= rt.y && p.y <= rt.y + rt.height) {
                return rt;
            }
        }
        return rect;
    }

    public void adjustDisplayDevice(Point p) {
        Rectangle rect = this.getDisplayDeviceBoundsByMousePoint(p);
        this.setLocation(rect.x, rect.y);
        this.setSize(rect.width, rect.height);
        this.dialog.adjustDisplayDevice(p, rect);
        this.dialog.mouseMoved(p);
    }

}
