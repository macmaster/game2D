package com.ronny.driver;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;

import com.ronny.graphics.Animation;
import com.ronny.graphics.ScreenManager;
import com.ronny.graphics.Sprite;

public class SpriteDriver {
	public ScreenManager screen;
	public Animation animation;
	public Image backgroundImage;
	
	public final int n = 10, loopTime = 20000;
	public Sprite[] sprites = new Sprite[n];
	
	public static void main(String[] args) {
		SpriteDriver driver = new SpriteDriver();
		driver.run();
	}
	
	public SpriteDriver() {
		this.screen = new ScreenManager();
		this.animation = new Animation();
		for (int i = 0; i < n; i++) {
			this.sprites[i] = new Sprite(animation);
		}
	}
	
	public void run() {
		try {
			this.screen.enterFullScreenMode();
			this.loadAnimation();
			this.loopAnimation(loopTime);
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
			update(elapsedTime);
			currTime += elapsedTime;
			
			Graphics2D g = this.screen.getGraphics();
			draw(g);
			g.dispose();
			
			this.screen.update();
			Thread.sleep(20);
		}
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(this.backgroundImage, 0, 0, null);
		AffineTransform transform = new AffineTransform();
		for (int i = 0; i < n; i++) {
			int x = Math.round(this.sprites[i].getX());
			int y = Math.round(this.sprites[i].getY());
			transform.setToTranslation(x, y);
			transform.scale(1, 1);
			g.drawImage(this.sprites[i].getImage(), transform, null);
		}
	}
	
	public void loadAnimation() {
		this.backgroundImage = this.loadImage("/bg.jpg");
		Image heart1 = this.loadImage("/heart1.png");
		Image heart2 = this.loadImage("/heart2.png");
		Image heart3 = this.loadImage("/heart3.png");
		this.animation.add(heart1, 1500);
		this.animation.add(heart2, 1500);
		this.animation.add(heart3, 1500);
		this.animation.add(heart2, 1500);
		this.animation.add(heart1, 1500);
		
		int width = this.screen.getWidth();
		int height = this.screen.getHeight();
		
		for (int i = 0; i < n; i++) {
			Sprite sprite = this.sprites[i];
			sprite.setX((float) (Math.random() * width));
			sprite.setY((float) (Math.random() * height));
			sprite.setVelocityX((float) (Math.random() * 0.5));
			sprite.setVelocityY((float) (Math.random() * 0.5));
		}
	}
	
	public void update(long elapsedTime) {
		for (int i = 0; i < n; i++) {
			Sprite sprite = this.sprites[i];
			int spriteX = (int) sprite.getX(), spriteY = (int) sprite.getY();
			int spriteW = (int) sprite.getWidth(), spriteH = (int) sprite.getHeight();
			float spriteVx = Math.abs(sprite.getVelocityX()), spriteVy = Math.abs(sprite.getVelocityY());
			
			// reflect x
			if (spriteX < 0) {
				sprite.setVelocityX(spriteVx);
			} else if (spriteX + spriteW >= screen.getWidth()) {
				sprite.setVelocityX(-spriteVx);
			}
			
			// reflect y
			if (spriteY < 0) {
				sprite.setVelocityY(spriteVy);
			} else if (spriteY + spriteH >= screen.getHeight()) {
				sprite.setVelocityY(-spriteVy);
			}
			
			sprite.update(elapsedTime);
		}
	}
	
	public Image loadImage(String name) {
		return new ImageIcon(this.getClass().getResource(name)).getImage();
	}
}
