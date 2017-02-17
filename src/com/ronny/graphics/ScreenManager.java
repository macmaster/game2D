package com.ronny.graphics;

import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class ScreenManager extends JFrame {
	
	// window configurations
	private int width, height;
	private final String DEFAULT_TITLE = "com.ronny.game";
	private static final int DEFAULT_WIDTH = 400, DEFAULT_HEIGHT = 400;
	private static final long serialVersionUID = 1L;
	
	// gpu configurations.
	private DisplayMode displayMode;
	private GraphicsEnvironment environment;
	private GraphicsDevice device;
	
	/** ScreenManager <br>
	 * 
	 * Constructs a new ScreenManager Object. <br>
	 * Uses the default display mode.
	 */
	public ScreenManager() {
		this.environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		this.device = this.environment.getDefaultScreenDevice();
		this.displayMode = this.device.getDisplayMode();
	}
	
	/** ScreenManager <br>
	 * 
	 * Constructs a new ScreenManager Object. <br>
	 * @param displayMode custom display mode.
	 */
	public ScreenManager(DisplayMode displayMode) {
		this();
		this.setDisplayMode(displayMode);
	}
	
	/** enterFullScreenMode() <br>
	 * 
	 * Enters full screen mode. <br>
	 * @return true upon success.
	 */
	public boolean enterFullScreenMode() {
		if (this.device.isFullScreenSupported()) {
			this.setUndecorated(true);
			this.setResizable(false);
			this.device.setFullScreenWindow(this);
			this.width = this.displayMode.getWidth();
			this.height = this.displayMode.getHeight();
			
			// active rendering 
			// double buffering + page flip.
			this.setIgnoreRepaint(true);
			this.createBufferStrategy(2);
			return true;
		}
		return false;
	}
	
	/** exitScreenMode() <br>
	 * 
	 * Cleans up the window before closing. <br>
	 */
	public void exitScreenMode() {
		this.dispose();
		this.height = 0;
		this.width = 0;
		this.device.setFullScreenWindow(null);
	}
	
	@Override
	public Graphics2D getGraphics() {
		BufferStrategy strategy = this.getBufferStrategy();
		if (strategy != null) {
			return (Graphics2D) strategy.getDrawGraphics();
		}
		return (Graphics2D) super.getGraphics();
	}
	
	/** update() <br>
	 * 
	 * Must call this to update the screen after rendering. <br>
	 * Syncs the display with the rendering buffer.
	 */
	public void update() {
		BufferStrategy strategy = this.getBufferStrategy();
		if (strategy != null && !strategy.contentsLost()) {
			strategy.show();
		}
		Toolkit.getDefaultToolkit().sync();
	}
	
	/** createCompatibleImage() <br>
	 * 
	 * TODO: Document Method <br>
	 * @param w
	 * @param h
	 * @param transparancy
	 * @return
	 */
	public BufferedImage createCompatibleImage(int w, int h, int transparancy) {
		Window window = this.device.getFullScreenWindow();
		if (window != null) {
			GraphicsConfiguration gc = window.getGraphicsConfiguration();
			return gc.createCompatibleImage(w, h, transparancy);
		}
		return null;
	}
	
	public DisplayMode getDisplayMode() {
		return this.device.getDisplayMode();
	}
	
	public boolean setDisplayMode(DisplayMode displayMode) {
		if (this.device.isDisplayChangeSupported()) {
			this.displayMode = displayMode;
			this.device.setDisplayMode(displayMode);
			return true;
		} else {
			return false;
		}
	}
	
	public JFrame getFullScreenWindow() {
		return (JFrame) this.device.getFullScreenWindow();
	}
	
	@Override
	public int getWidth() {
		return this.width;
	}
	
	@Override
	public int getHeight() {
		return this.height;
	}
	
	/** enterWindowScreenMode() <br>
	 * 
	 * Switches to window mode. <br>
	 */
	@Deprecated
	public void enterWindowScreenMode() {
		this.setUndecorated(false);
		this.setResizable(true);
		this.setTitle(DEFAULT_TITLE);
		this.width = DEFAULT_WIDTH;
		this.height = DEFAULT_HEIGHT;
		this.setSize(this.width, this.height);
		this.setVisible(true);
		this.setDefaultCloseOperation(3);
		
		// active rendering 
		// double buffering + page flip.
		this.setIgnoreRepaint(true);
		this.createBufferStrategy(2);
	}
	
}
