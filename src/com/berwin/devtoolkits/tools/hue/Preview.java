package com.berwin.devtoolkits.tools.hue;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Preview extends JPanel implements Runnable {

    private HexFrame main;

    private HueDialog dialog;
    private Robot robot;
    private BufferedImage imageBuf;

    private float scale = 0.05f;
    private int targetWidth = (int) (1 / scale);

    private boolean isRunning = false;

    public Preview(HexFrame main, HueDialog dialog) {
        this.main = main;
        this.dialog = dialog;
        this.setLayout(null);
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        this.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(this.imageBuf, 0, 0, HexFrame.RANGE, HexFrame.RANGE, 0, 0, (int) (HexFrame.RANGE * scale), (int) (HexFrame.RANGE * scale), null);

        // 画线
        g.setColor(Color.CYAN);
        int center = HexFrame.RANGE / 2;
        int x = (HexFrame.RANGE - targetWidth) / 2;
        int y = (HexFrame.RANGE - targetWidth) / 2;
        g.drawRect(x, y, targetWidth, targetWidth);
        int length = 20;
        // left
        g.drawLine(x, center, x - length, center);
        // right
        g.drawLine(x + targetWidth, center, x + targetWidth + length, center);
        // top
        g.drawLine(center, y, center, y - length);
        // bottom
        g.drawLine(center, y + targetWidth, center, y + targetWidth + length);

        // 画矩形
        // g.fillRect(0, Hex.RANGE + 20, 50, 50);
    }

    private BufferedImage screenShot(Point p, int range) {
        int half = range / 2;
        Rectangle rectangle = new Rectangle((int) p.getX() - half, (int) p.getY() - half, range, range);
        return this.robot.createScreenCapture(rectangle);
    }

    public void refresh() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        this.main.adjustDisplayDevice(p);
        this.imageBuf = this.screenShot(p, (int) (HexFrame.RANGE * scale));
        int pixel = this.imageBuf.getRGB(this.imageBuf.getWidth() / 2, this.imageBuf.getHeight() / 2);
        int r = (pixel & 0xff0000) >> 16;
        int g = (pixel & 0xff00) >> 8;
        int b = (pixel & 0xff);
        this.dialog.updateTextField(new Color(r, g, b));
        this.repaint();
    }

    @Override
    public void run() {
        while (this.isRunning()) {
            this.refresh();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void start() {
        this.setBounds(0, 0, HexFrame.RANGE, HexFrame.RANGE);
        this.isRunning = true;
        new Thread(this).start();
    }
}
