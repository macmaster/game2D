package com.ronny.driver;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

/** KeyboardDriver
 * @author ronny <br>
 *
 * Capture and display AWT Events from keyboard and elswhere. <br>
 */
public class MouseLookDriver extends Core implements KeyListener, MouseMotionListener {
	
	private Robot robot;
	private Image backgroundImage;
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
		
		// set invisible cursor.
		Toolkit kit = Toolkit.getDefaultToolkit();
		window.setCursor(kit.createCustomCursor(kit.getImage(""), new Point(0, 0), "invisible"));
		
		try { // create the robot.
			this.robot = new Robot();
			//this.recenterMouse();
			//this.mouseLocation.setLocation(centerLocation);
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
		this.backgroundImage = loadImage("/nyc.jpg");
		
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
		int imageWidth = backgroundImage.getWidth(null), imageHeight = backgroundImage.getHeight(null);
		if (imageLocation.x > 0) imageLocation.x = 0;
		if (imageLocation.y > 0) imageLocation.y = 0;
		if (imageLocation.x < width - imageWidth) imageLocation.x = width - imageWidth;
		if (imageLocation.y < height - imageHeight) imageLocation.y = height - imageHeight;
		
		//g.drawImage(backgroundImage, imageLocation.x, imageLocation.y, null);
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
}
