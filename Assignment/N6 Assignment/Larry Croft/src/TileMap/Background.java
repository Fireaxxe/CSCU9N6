package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class Background {

	private BufferedImage image;
	private double x;
	private double y;
	private double dx;
	private double dy;
	private int width;
	private int height;
	private double x_scale;
	private double y_scale;

	public Background(String s) {
		this(s, 0.1);
	}

	public Background(String s, double d) {
		this(s, d, d);
	}

	public Background(String s, double d1, double d2) {
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
			width = image.getWidth();
			height = image.getHeight();
			x_scale = d1;
			y_scale = d2;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Background(String s, double ms, int x, int y, int w, int h) {
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
			image = image.getSubimage(x, y, w, h);
			width = image.getWidth();
			height = image.getHeight();
			x_scale = ms;
			y_scale = ms;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setPosition(double x, double y) {
		this.x = (x * x_scale) % width;
		this.y = (y * y_scale) % height;
	}

	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void setScale(double xscale, double yscale) {
		this.x_scale = xscale;
		this.y_scale = yscale;
	}

	public void setDimensions(int i1, int i2) {
		width = i1;
		height = i2;
	}

	public double get_x() {
		return x;
	}

	public double get_y() {
		return y;
	}

	public void update() {
		x += dx;
		while (x <= -width)
			x += width;
		while (x >= width)
			x -= width;
		y += dy;
		while (y <= -height)
			y += height;
		while (y >= height)
			y -= height;
	}

	public void draw(Graphics2D g) {

		g.drawImage(image, (int) x, (int) y, null);

		if (x < 0) {
			g.drawImage(image, (int) x + GamePanel.WIDTH, (int) y, null);
		}
		if (x > 0) {
			g.drawImage(image, (int) x - GamePanel.WIDTH, (int) y, null);
		}
		if (y < 0) {
			g.drawImage(image, (int) x, (int) y + GamePanel.HEIGHT, null);
		}
		if (y > 0) {
			g.drawImage(image, (int) x, (int) y - GamePanel.HEIGHT, null);
		}
	}
}
