package Entity;

import java.awt.image.BufferedImage;

public class Animation {

	private BufferedImage[] frames;
	private int currentFrame;
	private int numberOfFrames;
	private int count;
	private int delay;
	private int timesPlayed;

	public Animation() {
		timesPlayed = 0;
	}

	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		count = 0;
		timesPlayed = 0;
		delay = 2;
		numberOfFrames = frames.length;
	}

	public void setDelay(int i) {
		delay = i;
	}

	public void setFrame(int i) {
		currentFrame = i;
	}

	public void setNumFrames(int i) {
		numberOfFrames = i;
	}

	public void update() {

		if (delay == -1) {
			return;
		}
		count++;

		if (count == delay) {
			currentFrame++;
			count = 0;
		}
		if (currentFrame == numberOfFrames) {
			currentFrame = 0;
			timesPlayed++;
		}
	}

	public int getFrame() {
		return currentFrame;
	}

	public int getCount() {
		return count;
	}

	public BufferedImage getImage() {
		return frames[currentFrame];
	}

	public boolean hasPlayedOnce() {
		return timesPlayed > 0;
	}

	public boolean hasPlayed(int i) {
		return timesPlayed == i;
	}
}