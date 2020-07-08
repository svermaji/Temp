package com.sv;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MouseMove {
    public static long TIME_TO_WAIT_SEC = 10;
    private double maxX;
    private double maxY;

    public static void main(String[] args) {
        MouseMove mm = new MouseMove();
        mm.init();
    }

    private void init() {
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        DisplayMode dm = gs[0].getDisplayMode();
        maxX = dm.getWidth();
        maxY = dm.getHeight();
        System.out.println("maxX = " + maxX + ", maxY = " + maxY);
        MyTimerTask mtt = new MyTimerTask(this);
        new Timer().schedule(mtt, 0, TimeUnit.SECONDS.toMillis(MouseMove.TIME_TO_WAIT_SEC));
    }

    public void moveMouse() {
        try {
            Robot robot = new Robot();
            int x = (int) (Math.random() * maxX);
            int y = (int) (Math.random() * maxY);
            System.out.println("Moving mouse to x = " + x + ", y = " + y);
            robot.mouseMove(x, y);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}

class MyTimerTask extends TimerTask {

    private final MouseMove mm;

    public MyTimerTask(MouseMove mm) {
        this.mm = mm;
    }

    @Override
    public void run() {
        mm.moveMouse();
    }
}