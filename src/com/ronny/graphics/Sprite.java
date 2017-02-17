package com.ronny.graphics;

import java.awt.Image;

/**
 * Sprite
 * @author ronny <br>
 *
 * Wrapper class for an animation. <br>
 * Gives position and velocity to an animated object. <br>
 */
public class Sprite {
	
	/** sprite animation */
	private Animation animation;
	
	/** sprite position (pixels) */
	private float x, y;
	
	/** sprite velocity (pixels / ms) */
	private float vx, vy;
	
	/** Sprite <br>
	 * 
	 * Constructs a new Sprite Object. <br>
	 */
	public Sprite(Animation animation) {
		this.animation = animation;
		this.x = this.y = 0;
		this.vx = this.vy = 0;
	}
	
	/**
	 * Updates the sprite animation and position. <br>
	 */
	public void update(long elapsedTime) {
		this.x += this.vx * elapsedTime;
		this.y += this.vy * elapsedTime;
	}
	
	/**
	 * Get the Sprite's current image. <br>
	 */
	public Image getImage() {
		return animation.getImage();
	}
	
	/**
	 * Get the height of the current sprite image. <br>
	 */
	public int getHeight() {
		return animation.getImage().getHeight(null);
	}
	
	/**
	 * Get the width of the current sprite image. <br>
	 */
	public int getWidth() {
		return animation.getImage().getWidth(null);
	}
	
	/**
	 * position in pixels <br>
	 */
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	/**
	 * position in pixels <br>
	 */
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	/**
	 * velocity in pixels per millisecond. <br>
	 */
	public float getVelocityX() {
		return vx;
	}
	
	public void setVelocityX(float vx) {
		this.vx = vx;
	}
	
	/**
	 * velocity in pixels per millisecond. <br>
	 */
	public float getVelocityY() {
		return vy;
	}
	
	public void setVelocityY(float vy) {
		this.vy = vy;
	}
	
}
