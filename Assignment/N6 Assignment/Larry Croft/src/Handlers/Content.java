package Handlers;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

// this class loads resources on boot.
// spritesheets are taken from here.

public class Content {

	public static BufferedImage[][] EnergyParticle = load("/Sprites/Player/EnergyParticle.gif", 5, 5);
	public static BufferedImage[][] Explosion = load("/Sprites/Enemies/Explosion.gif", 30, 30);
	public static BufferedImage[][] Bat = load("/Sprites/Enemies/Bat.gif", 39, 20);
	public static BufferedImage[][] Songoku = load("/Sprites/Enemies/Songoku.gif", 30, 30);
	public static BufferedImage[][] Spiky = load("/Sprites/Enemies/Spiky.gif", 25, 25);
	public static BufferedImage[][] DarkEnergy = load("/Sprites/Enemies/DarkEnergy.gif", 20, 20);

	public static BufferedImage[][] load(String s, int w, int h) {
		BufferedImage[][] loadResources;
		try {
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));
			int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;
			loadResources = new BufferedImage[height][width];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					loadResources[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return loadResources;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
}
