package com.ronny.driver;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import com.ronny.graphics.Animation;
import com.ronny.graphics.ScreenManager;

public class AnimationDriver {
	public ScreenManager screen = new ScreenManager();
	public Animation animation = new Animation();
	public Image backgroundImage;
	private int n = 100;
	private int[] w = new int[this.n];
	private int[] h = new int[this.n];
	
	public static void main(String[] args) {
		AnimationDriver driver = new AnimationDriver();
		driver.run();
	}
	
	public void run() {
		try {
			this.screen.enterFullScreenMode();
			int width = this.screen.getWidth();
			int height = this.screen.getHeight();
			for (int i = 0; i < n; i++) {
				this.w[i] = (int) (Math.random() * (double) width);
				this.h[i] = (int) (Math.random() * (double) height);
			}
			this.loadAnimation();
			this.loopAnimation(10000);
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			this.screen.exitScreenMode();
		}
	}
	
	public void loopAnimation(long loopTime) throws InterruptedException {
		long startTime;
		long currTime = startTime = System.currentTimeMillis();
		while (currTime - startTime < loopTime) {
			long elapsedTime = System.currentTimeMillis() - currTime;
			this.animation.update(elapsedTime);
			currTime += elapsedTime;
			Graphics2D g = this.screen.getGraphics();
			this.draw(g);
			g.dispose();
			this.screen.update();
			Thread.sleep(20);
		}
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(this.backgroundImage, 0, 0, null);
		for (int i = 0; i < n; i++) {
			g.drawImage(this.animation.getImage(), w[i], h[i], null);
		}
	}
	
	public void loadAnimation() {
		this.backgroundImage = this.loadImage("/bg.jpg");
		Image heart1 = this.loadImage("/heart1.png");
		Image heart2 = this.loadImage("/heart2.png");
		Image heart3 = this.loadImage("/heart3.png");
		this.animation.add(heart1, 50);
		this.animation.add(heart2, 50);
		this.animation.add(heart3, 50);
		this.animation.add(heart2, 50);
		this.animation.add(heart1, 200);
	}
	
	public Image loadImage(String name) {
		return new ImageIcon(this.getClass().getResource(name)).getImage();
	}
}
