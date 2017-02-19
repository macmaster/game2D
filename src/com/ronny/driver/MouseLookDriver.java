package com.ronny.driver;

import java.awt.*;
import java.awt.event.*;

import javax.swing.SwingUtilities;

/** KeyboardDriver
 * @author ronny <br>
 *
 * Capture and display AWT Events from keyboard and elswhere. <br>
 */
public class MouseLookDriver extends Core implements KeyListener, MouseMotionListener, MouseWheelListener {

    private Robot robot;
    private Image backgroundImage;
    private float imageScale;
    private int imageWidth, imageHeight;
    private Point mouseLocation, centerLocation, imageLocation;
    private boolean recenteringMode, relativeMode;

    public static void main(String[] args) {
        Core driver = new MouseLookDriver();
        driver.run();
    }

    /** KeyboardDriver <br>
     * 
     * Constructs a new KeyboardDriver Object. <br>
     */
    public MouseLookDriver() {
        Window window = screen.getFullScreenWindow();
        window.setFocusTraversalKeysEnabled(false);
        window.addKeyListener(this);
        window.addMouseMotionListener(this);
        window.addMouseWheelListener(this);

        // set invisible cursor.
        Toolkit kit = Toolkit.getDefaultToolkit();
        window.setCursor(kit.createCustomCursor(kit.getImage(""), new Point(0, 0), "invisible"));

        try { // create the robot.
            this.robot = new Robot();
            // this.recenterMouse();
            // this.mouseLocation.setLocation(centerLocation);
        } catch (AWTException e) {
            e.printStackTrace();
            screen.exitScreenMode();
            System.err.println("Couldn't create robot. exiting...");
            System.exit(1); // error
        }

        // points and mouse modes.
        this.relativeMode = true;
        this.recenteringMode = false;
        this.mouseLocation = new Point();
        this.centerLocation = new Point();
        this.imageLocation = new Point();
        this.imageScale = 1;
        this.backgroundImage = loadImage("/nyc.jpg");
        this.imageWidth = backgroundImage.getWidth(null);
        this.imageHeight = backgroundImage.getHeight(null);
        this.imageLocation.x = (-imageWidth * 3 / 4);
        this.imageLocation.y = (-imageHeight * 5 / 4);

    }

    /**
     * Use the robot class to recenter the mouse <br>
     * Available on most modern platforms... <br>
     */
    private synchronized void recenterMouse() {
        Window window = screen.getFullScreenWindow();
        if (window.isShowing() && robot != null) {
            recenteringMode = true;
            centerLocation.x = window.getWidth() / 2;
            centerLocation.y = window.getHeight() / 2;
            SwingUtilities.convertPointToScreen(centerLocation, window);
            robot.mouseMove(centerLocation.x, centerLocation.y);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        // verify / clip / saturate image location
        int width = screen.getWidth(), height = screen.getHeight();
        imageWidth = (int) (backgroundImage.getWidth(null) * imageScale);
        imageHeight = (int) (backgroundImage.getHeight(null) * imageScale);
        imageHeight = Math.max(height, imageHeight);
        if (imageLocation.x > 0)
            imageLocation.x = 0;
        if (imageLocation.y > 0)
            imageLocation.y = 0;
        if (imageLocation.x < width - imageWidth)
            imageLocation.x = width - imageWidth;
        if (imageLocation.y < height - imageHeight)
            imageLocation.y = height - imageHeight;

        // g.drawImage(backgroundImage, imageLocation.x, imageLocation.y, null);
        int x = imageLocation.x, y = imageLocation.y;
        g.drawImage(backgroundImage, x, y, imageWidth, imageHeight, null);

        g.setColor(FOREGROUND_COLOR);
        g.drawString("Press Escape to exit.", 5, FONT_SIZE);
    }

    public void keyPressed(KeyEvent event) {
        // TODO Auto-generated method stub
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE) {
            stop();
        } else if (keyCode == KeyEvent.VK_SPACE) {
            relativeMode = !relativeMode;
        }
        event.consume();
    }

    public void keyReleased(KeyEvent event) {
        // TODO Auto-generated method stub
    }

    public void keyTyped(KeyEvent event) {
        // TODO Auto-generated method stub
    }

    public void mouseDragged(MouseEvent event) {
        // TODO Auto-generated method stub
        mouseMoved(event);
    }

    public void mouseMoved(MouseEvent event) {
        // TODO Auto-generated method stub
        if (recenteringMode && centerLocation.equals(event.getPoint())) {
            recenteringMode = false;
        } else {
            // calculate mouse movement.
            int dx = event.getX() - mouseLocation.x;
            int dy = event.getY() - mouseLocation.y;
            imageLocation.x += dx;
            imageLocation.y += dy;
            if (relativeMode) {
                recenterMouse();
            }
        }

        // update mouse location.
        mouseLocation.setLocation(event.getPoint());
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        // TODO Auto-generated method stub
        int clicks = event.getWheelRotation();
        float floor = 0.35f, ceiling = 2.0f;
        this.imageScale -= clicks * 0.05;
        this.imageScale = Math.max(floor, imageScale);
        this.imageScale = Math.min(imageScale, ceiling);
    }
}
