package com.berwin.devtoolkits.tools.hue;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class HueDialog extends Dialog implements KeyListener {

    public static final int TEXTFIELD_COLS = 20;

    private HexFrame main;

    private Preview previewBig;
    private JPanel previewSmall;

    private JPanel opPanel;

    private JTextField tfRgb;
    private JTextField tfHex;
    private JTextField tfLua;
    private JTextField tfJs;

    private Color color;
    private Rectangle displayRect;

    public HueDialog(JFrame owner) {
        super(owner, false);
        this.main = (HexFrame) owner;

        this.setLayout(new BorderLayout());

        // 南边
        JPanel south = new JPanel(new BorderLayout());
        this.add(south, BorderLayout.SOUTH);

        // 左边预览
        previewSmall = new JPanel();
        previewSmall.setBackground(Color.RED);
        south.add(previewSmall, BorderLayout.WEST);

        JPanel center = new JPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP));
        south.add(center, BorderLayout.CENTER);

        // RGB
        JPanel rgbPanel = new JPanel(new BorderLayout());
        center.add(rgbPanel);

        JLabel lblRgb = new JLabel("RGB:");
        rgbPanel.add(lblRgb, BorderLayout.WEST);

        tfRgb = new JTextField(TEXTFIELD_COLS);
        rgbPanel.add(tfRgb, BorderLayout.CENTER);

        JButton btnRgbCopy = new JButton(new ImageIcon("res/copy.png"));
        rgbPanel.add(btnRgbCopy, BorderLayout.EAST);
        btnRgbCopy.addActionListener(e -> {
            this.setSysClipboardText(tfRgb.getText());
            this.dispose();
            System.exit(0);
        });

        // Hex
        JPanel hexPanel = new JPanel(new BorderLayout());
        center.add(hexPanel);

        JLabel lblHex = new JLabel("HEX:");
        hexPanel.add(lblHex, BorderLayout.WEST);

        tfHex = new JTextField(TEXTFIELD_COLS);
        hexPanel.add(tfHex, BorderLayout.CENTER);

        JButton btnCopy = new JButton(new ImageIcon("res/copy.png"));
        hexPanel.add(btnCopy, BorderLayout.EAST);
        btnCopy.addActionListener(e -> {
            this.setSysClipboardText(tfHex.getText());
            this.dispose();
            System.exit(0);
        });

        // Lua
        JPanel luaPanel = new JPanel(new BorderLayout());
        center.add(luaPanel);

        JLabel lblLua = new JLabel("LUA:");
        luaPanel.add(lblLua, BorderLayout.WEST);

        tfLua = new JTextField(TEXTFIELD_COLS);
        luaPanel.add(tfLua, BorderLayout.CENTER);

        JButton btnLuaCopy = new JButton(new ImageIcon("res/copy.png"));
        luaPanel.add(btnLuaCopy, BorderLayout.EAST);
        btnLuaCopy.addActionListener(e -> {
            this.setSysClipboardText(tfLua.getText());
            this.dispose();
            System.exit(0);
        });

        // JS
        JPanel jsPanel = new JPanel(new BorderLayout());
        center.add(jsPanel);

        JLabel lblJs = new JLabel(" JS:");
        jsPanel.add(lblJs, BorderLayout.WEST);

        tfJs = new JTextField(TEXTFIELD_COLS);
        jsPanel.add(tfJs, BorderLayout.CENTER);

        JButton btnJsCopy = new JButton(new ImageIcon("res/copy.png"));
        jsPanel.add(btnJsCopy, BorderLayout.EAST);
        btnJsCopy.addActionListener(e -> {
            this.setSysClipboardText(tfJs.getText());
            this.dispose();
            System.exit(0);
        });

        // south
        opPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(opPanel, BorderLayout.SOUTH);
        opPanel.setVisible(false);

        JButton btnClose = new JButton("退出");
        opPanel.add(btnClose);
        btnClose.addActionListener(e -> System.exit(0));

        JButton btnRestart = new JButton("重新拾取");
        opPanel.add(btnRestart);
        btnRestart.addActionListener(e -> this.start());

        this.previewBig = new Preview(this.main, this);
        this.add(this.previewBig, BorderLayout.CENTER);

        this.addKeyListener(this);
        this.setResizable(false);
//        this.setUndecorated(true);
        this.setAlwaysOnTop(true);
        this.setSize(HexFrame.RANGE, HexFrame.RANGE + 50);
    }

    /**
     * 将字符串复制到剪切板。
     */
    public static void setSysClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }

    public void updateTextField(Color c) {
        this.color = c;
        tfRgb.setText(String.format("%d,%d,%d", c.getRed(), c.getGreen(), c.getBlue()));
        tfHex.setText("#" + this.toHex(c.getRed()) + this.toHex(c.getGreen()) + this.toHex(c.getBlue()));
        tfLua.setText(String.format("cc.c3b(%d, %d, %d)", c.getRed(), c.getGreen(), c.getBlue()));
        tfJs.setText(String.format("cc.color(%d, %d, %d)", c.getRed(), c.getGreen(), c.getBlue()));
        this.previewSmall.setBackground(c);
    }

    private String toHex(int number) {
        StringBuilder builder = new StringBuilder(
                Integer.toHexString(number & 0xff));
        while (builder.length() < 2) {
            builder.append("0");
        }
        return builder.toString().toUpperCase();
    }

    public void mouseMoved(Point p) {
        if (!this.previewBig.isRunning())
            return;
        // moveEnter事件
        int x = this.getBounds().x;
        int y = this.getBounds().y;
//        System.out.println("x:" + x + "   y:" + y);
        if (p.x >= x && p.y >= y && p.x <= x + this.getWidth() && p.y <= y + this.getHeight()) {
            int right = this.displayRect.x + this.displayRect.width;
            int bottom = this.displayRect.y + this.displayRect.height;
            if (x == this.displayRect.x && y == this.displayRect.y) {
                x = right - this.getWidth();
                y = this.displayRect.y;
//                y = bottom - this.getHeight();
                System.out.println("------>>x:" + x + "   y:" + y);
            } else {
                x = this.displayRect.x;
                y = this.displayRect.y;
            }
            this.setLocation(x, y);
        }
    }

    public void start() {
        this.opPanel.setVisible(false);
        this.main.start();
        this.previewBig.start();
    }

    public void picked() {
        this.opPanel.setVisible(true);
        this.previewBig.setRunning(false);
//        this.setUndecorated(false);
        this.setAlwaysOnTop(false);
        this.setLocation(this.displayRect.x + (this.displayRect.width - this.getWidth()) / 2, this.displayRect.y + (this.displayRect.height - this.getHeight()) / 2);
    }

    public void adjustDisplayDevice(Point p, Rectangle rect) {
        if (this.displayRect == null)
            this.displayRect = rect;
        int x = this.getX();
        int y = this.getY();
        int right = rect.x + rect.width;
        int bottom = rect.y + rect.height;
        if (x == this.displayRect.x && y == this.displayRect.y) {
            this.setLocation(rect.x, rect.y);
        } else {
//            this.setLocation(right - this.getWidth(), bottom - this.getHeight());
            this.setLocation(right - this.getWidth(), rect.y);
        }
        this.displayRect = rect;
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
}
