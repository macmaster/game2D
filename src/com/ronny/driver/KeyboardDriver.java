package com.ronny.driver;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/** KeyboardDriver
 * @author ronny <br>
 *
 * Capture and display AWT Events from keyboard and elswhere. <br>
 */
public class KeyboardDriver extends Core implements KeyListener {
	
	public List<String> messages;
	private int capacity = 0;
	
	public static void main(String[] args) {
		Core driver = new KeyboardDriver();
		driver.run();
	}
	
	/** KeyboardDriver <br>
	 * 
	 * Constructs a new KeyboardDriver Object. <br>
	 */
	public KeyboardDriver() {
		Window window = screen.getFullScreenWindow();
		window.setFocusTraversalKeysEnabled(false);
		window.addKeyListener(this);
		
		// enqueue welcome message.
		this.messages = new ArrayList<String>();
		this.capacity = screen.getHeight() / FONT_SIZE;
		addMessage("KeyboardDriver: Press Escape to exit.");
	}
	
	public void addMessage(String message) {
		messages.add(message);
		if (messages.size() > capacity) {
			messages.remove(0);
		}
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
		
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
		
		
		g.setColor(FOREGROUND_COLOR);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (int i = 0; i < messages.size(); i++) {
			g.drawString(messages.get(i), 5, ((i + 1) * FONT_SIZE));
		}
	}
	
	public void keyPressed(KeyEvent event) {
		// TODO Auto-generated method stub
		int keyCode = event.getKeyCode();
		if (keyCode == KeyEvent.VK_ESCAPE) {
			stop();
		} else {
			addMessage("Pressed: " + KeyEvent.getKeyText(keyCode));
			event.consume();
		}
	}
	
	public void keyReleased(KeyEvent event) {
		// TODO Auto-generated method stub
		int keyCode = event.getKeyCode();
		addMessage("Released: " + KeyEvent.getKeyText(keyCode));
		event.consume();
	}
	
	public void keyTyped(KeyEvent event) {
		// TODO Auto-generated method stub
		int keyCode = event.getKeyCode();
		addMessage("Typed: " + KeyEvent.getKeyText(keyCode));
		event.consume();
	}
	
}
