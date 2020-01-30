package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Handlers.Content;
import TileMap.TileMap;

public class EnergyParticle extends MapObject {

	private int count;
	private boolean remove;
	private BufferedImage[] sprites;
	public static int UP = 0;
	public static int LEFT = 1;
	public static int DOWN = 2;
	public static int RIGHT = 3;

	public EnergyParticle(TileMap tm, double x, double y, int direction) {
		super(tm);
		this.x = x;
		this.y = y;
		double randomDouble1 = Math.random() * 2.5 - 1.25;
		double randomDouble2 = -Math.random() - 0.8;
		if (direction == UP) {
			dx = randomDouble1;
			dy = randomDouble2;
		} else if (direction == LEFT) {
			dx = randomDouble2;
			dy = randomDouble1;
		} else if (direction == DOWN) {
			dx = randomDouble1;
			dy = -randomDouble2;
		} else {
			dx = -randomDouble2;
			dy = randomDouble1;
		}
		count = 0;
		sprites = Content.EnergyParticle[0];
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}

	public void update() {
		x += dx;
		y += dy;
		count++;
		if (count == 60) {
			remove = true;
		}
	}

	public boolean shouldRemove() {
		return remove;
	}

	public void draw(Graphics2D g) {
		super.draw(g);
	}
}