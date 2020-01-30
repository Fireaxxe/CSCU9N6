package Entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import Handlers.Content;
import TileMap.TileMap;

public class Explosion extends MapObject {

	private BufferedImage[] sprites;
	private boolean remove;
	private Point[] points;
	private int speed;
	private double diagonalSpeed;

	public Explosion(TileMap tm, int x, int y) {
		
		super(tm);
		
		this.x = x;
		this.y = y;

		width = 30;
		height = 30;

		speed = 2;
		diagonalSpeed = 1.41;

		sprites = Content.Explosion[0];

		animation.setFrames(sprites);
		animation.setDelay(6);

		points = new Point[8];
		for (int i = 0; i < points.length; i++) {
			points[i] = new Point(x, y);
		}

	}

	public void update() {
		animation.update();
		if (animation.hasPlayedOnce()) {
			remove = true;
		}
		points[0].x += speed;
		points[1].x += diagonalSpeed;
		points[1].y += diagonalSpeed;
		points[2].y += speed;
		points[3].x -= diagonalSpeed;
		points[3].y += diagonalSpeed;
		points[4].x -= speed;
		points[5].x -= diagonalSpeed;
		points[5].y -= diagonalSpeed;
		points[6].y -= speed;
		points[7].x += diagonalSpeed;
		points[7].y -= diagonalSpeed;
	}

	public boolean shouldRemove() {
		return remove;
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		for (int i = 0; i < points.length; i++) {
			g.drawImage(animation.getImage(), (int) (points[i].x + x_map - width / 2),
					(int) (points[i].y + y_map - height / 2), null);
		}
	}
}