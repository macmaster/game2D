package com.ronny.driver;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;

/** KeyboardDriver
 * @author ronny <br>
 *
 * Capture and display AWT Events from keyboard and elswhere. <br>
 */
public class MouseTrackDriver extends Core implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	
	public LinkedList<Point> trail;
	public boolean trailing;
	private final int capacity = 100;
	
	// background color scrolling.
	private int colorIndex;
	public final Color[] colors = { new Color(140, 217, 179), new Color(128, 191, 255), new Color(217, 140, 140),
			new Color(217, 179, 255), new Color(163, 163, 194) };
	private Color BACKGROUND_COLOR = Core.BACKGROUND_COLOR;
	
	public static void main(String[] args) {
		Core driver = new MouseTrackDriver();
		driver.run();
	}
	
	/** KeyboardDriver <br>
	 * 
	 * Constructs a new KeyboardDriver Object. <br>
	 */
	public MouseTrackDriver() {
		Window window = screen.getFullScreenWindow();
		window.setFocusTraversalKeysEnabled(false);
		window.addKeyListener(this);
		window.addMouseListener(this);
		window.addMouseMotionListener(this);
		window.addMouseWheelListener(this);
		
		// enqueue welcome message.
		this.colorIndex = 0;
		this.trailing = true;
		this.trail = new LinkedList<Point>();
	}
	
	@Override
	public void draw(Graphics2D g) {
		// // print message queue.
		// Window window = screen.getFullScreenWindow();
		//
		// int red = (int) (Math.random() * 255);
		// int blue = (int) (Math.random() * 255);
		// int green = (int) (Math.random() * 255);
		// Color RANDOM_COLOR = new Color(red, blue, green);
		
		// background color.
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
		
		// exit message.
		g.setColor(FOREGROUND_COLOR);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawString("RainbowSnake: Press Escape to exit.", 5, FONT_SIZE);
		
		// print trail
		for (int i = 0; i < trail.size() && trailing; i++) {
			Point p = trail.get(i);
			//g.drawString("[" + (n - i) + "]", p.x, p.y);
			
			int red = (int) (Math.random() * 255);
			int blue = (int) (Math.random() * 255);
			int green = (int) (Math.random() * 255);
			Color RANDOM_COLOR = new Color(red, blue, green);
			g.setColor(RANDOM_COLOR);
			g.drawString("[<" + (i + 1) + ">]", p.x, p.y);
		}
	}
	
	public void keyPressed(KeyEvent event) {
		// TODO Auto-generated method stub
		int keyCode = event.getKeyCode();
		if (keyCode == KeyEvent.VK_ESCAPE) {
			stop();
		}
		event.consume();
	}
	
	public void keyReleased(KeyEvent event) {
		// TODO Auto-generated method stub
	}
	
	public void keyTyped(KeyEvent event) {
		// TODO Auto-generated method stub
	}
	
	public void mouseWheelMoved(MouseWheelEvent event) {
		// TODO Auto-generated method stub
		colorIndex += (event.getWheelRotation());
		colorIndex %= colors.length;
		colorIndex = (colorIndex < 0) ? colorIndex + colors.length : colorIndex;
		BACKGROUND_COLOR = colors[colorIndex];
		screen.getFullScreenWindow().setBackground(BACKGROUND_COLOR);
	}
	
	public void mouseDragged(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseMoved(MouseEvent event) {
		// TODO Auto-generated method stub
		mouseReleased(event);
		
	}
	
	public void mouseClicked(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseEntered(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseExited(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub
		Point p = new Point(event.getX(), event.getY());
		trail.addFirst(p);
		if (trail.size() > capacity) {
			trail.removeLast();
		}
	}
	
}
