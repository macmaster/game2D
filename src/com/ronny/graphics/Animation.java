package com.ronny.graphics;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class Animation {
	private int frameIndex;
	private List<AnimationFrame> frames;
	
	private long animationTime;
	private long totalDuration;
	
	/** Animation <br>
	 * 
	 * Constructs a new Animation Object. <br>
	 */
	public Animation() {
		this.frames = new ArrayList<AnimationFrame>();
		this.totalDuration = 0;
		this.restart();
	}
	
	/**
	 * Make a deep copy of the animation.
	 */
	public Animation clone() {
		Animation animation = new Animation();
		animation.totalDuration = totalDuration;
		for (AnimationFrame frame : frames) {
			animation.frames.add(new AnimationFrame(frame.image, frame.endTime));
		}
		animation.restart();
		return animation;
	}
	
	public synchronized void restart() {
		this.animationTime = 0;
		this.frameIndex = 0;
	}
	
	/** add() <br>
	 * 
	 * Add a frame to the animation. <br>
	 */
	public synchronized void add(Image image, long duration) {
		this.totalDuration += duration;
		this.frames.add(new AnimationFrame(image, totalDuration));
	}
	
	/** update() <br>
	 * 
	 * Update the animation image to the latest. <br>
	 * @param elapsedTime time that has passed.
	 */
	public synchronized void update(long elapsedTime) {
		if (this.frames.size() > 1) {
			this.animationTime += elapsedTime;
			if (this.animationTime >= this.totalDuration) {
				this.animationTime %= this.totalDuration;
				this.frameIndex = 0;
			}
			while (this.animationTime >= frames.get(frameIndex).endTime) {
				this.frameIndex += 1;
			}
		}
	}
	
	/** getImage() <br>
	 * 
	 * Get the current animation frame image. <br>
	 */
	public synchronized Image getImage() {
		if (frames.size() < 1) {
			return null;
		} else {
			return frames.get(frameIndex).image;
		}
	}
	
	/** AnimationFrame
	 * @author ronny <br>
	 *
	 * Container for animation images linked to duration. <br>
	 */
	private class AnimationFrame {
		private Image image;
		private long endTime;
		
		private AnimationFrame(Image image, long endTime) {
			this.image = image;
			this.endTime = endTime;
		}
	}
}
