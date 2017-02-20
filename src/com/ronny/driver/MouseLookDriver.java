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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;

import javax.swing.ImageIcon;
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
		Image backgroundImage = null;
		if (new File("nyc.png").exists()) {
			backgroundImage = new ImageIcon("nyc.png").getImage();
		} else if (new File("nyc.jpg").exists()) {
			backgroundImage = new ImageIcon("nyc.jpg").getImage();
		} else {
			backgroundImage = loadImage("/nyc2.jpg");
		}
		
		Core driver = new MouseLookDriver(backgroundImage);
		driver.run();
	}
	
	/** MouseLookDriver <br>
	 * 
	 * Constructs a new KeyboardDriver Object. <br>
	 */
	public MouseLookDriver(Image image) {
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
		this.backgroundImage = image;
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
		// bound x and y.
		imageLocation.x = (int) limit(imageLocation.x, ((width - imageWidth) - centerLocation.x) / imageScale, 0);
		imageLocation.y = (int) limit(imageLocation.y, ((height - imageHeight) - centerLocation.y) / imageScale, 0);
		int x = (int) (imageLocation.x * imageScale + centerLocation.x);
		int y = (int) (imageLocation.y * imageScale + centerLocation.y);
		x = (int) limit(x, width - imageWidth, 0);
		y = (int) limit(y, height - imageHeight, 0);
		
		// g.drawImage(backgroundImage, imageLocation.x, imageLocation.y, null);
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
		mouseLocation.setLocation(centerLocation);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event. MouseWheelEvent)
	 */
	public void mouseWheelMoved(MouseWheelEvent event) {
		// TODO Auto-generated method stub
		int clicks = event.getWheelRotation();
		final float floor = 0.35f, ceiling = 2.0f;
		float dscale = (float) (clicks * 0.1);
		this.imageScale -= dscale;
		this.imageScale = this.limit(imageScale, floor, ceiling);
	}
	
	private float limit(float value, float floor, float ceiling) {
		value = Math.max(floor, value);
		value = Math.min(value, ceiling);
		return value;
	}
}
