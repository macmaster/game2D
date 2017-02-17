package com.ronny.driver;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;

import javax.swing.ImageIcon;

import com.ronny.graphics.ScreenManager;

/** Core
 * @author ronny <br>
 *
 * Driver wrapper for implementing Game Managers / Test Drivers. <br>
 */
public abstract class Core {
	
	// screen window global parameters.
	protected final static int FONT_SIZE = 24;
	protected final static Color BACKGROUND_COLOR = Color.GRAY;
	protected final static Color FOREGROUND_COLOR = Color.GREEN;
	
	// yield time per loop cycle in ms
	private final int CYCLE_TIME = 20;
	
	protected boolean running;
	protected ScreenManager screen;
	
	/** Core <br>
	 * 
	 * Constructs a new Core Object. <br>
	 */
	public Core() {
		this.running = false;
		this.screen = new ScreenManager();
		
		// default window configuration.
		this.screen.enterFullScreenMode();
		Window window = this.screen.getFullScreenWindow();
		window.setFont(new Font("Times New Roman", Font.BOLD|Font.ITALIC, FONT_SIZE));
		window.setForeground(FOREGROUND_COLOR);
		window.setBackground(BACKGROUND_COLOR);
		
	}
	
	/**
	 * Start the game driver. <br>
	 */
	public void run() {
		try {
			running = true;
			// run game loop.
			gameLoop();
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			screen.exitScreenMode();
		}
	}
	
	/**
	 * Stop the game driver. <br>
	 */
	public void stop() {
		running = false;
	}
	
	/**
	 * Primary game processing loop. <br>
	 */
	public void gameLoop() throws InterruptedException {
		long startTime = System.currentTimeMillis();
		long currTime = startTime;
		while (running) {
			long elapsedTime = System.currentTimeMillis() - currTime;
			currTime += elapsedTime;
			update(elapsedTime);
			
			// draw and update screen buffer.
			Graphics2D g = screen.getGraphics();
			this.draw(g);
			g.dispose();
			screen.update();
			
			Thread.sleep(CYCLE_TIME);
		}
	}
	
	/**
	 * Load an image as a resource. <br>
	 */
	public Image loadImage(String name) {
		return new ImageIcon(this.getClass().getResource(name)).getImage();
	}
	
	/**
	 * Draws to the screen. Must be overridden. <br>
	 */
	public abstract void draw(Graphics2D g);
	
	/**
	 * Updates the game objects off the game clock. <br>
	 * Overriding is recommended.
	 */
	public void update(long elapsedTime) {
		// update core game objects
	}
}
