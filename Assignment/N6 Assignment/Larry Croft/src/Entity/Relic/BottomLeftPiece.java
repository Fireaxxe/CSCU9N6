package Entity.Relic;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.MapObject;
import TileMap.TileMap;

public class BottomLeftPiece extends MapObject {
	
	private BufferedImage[] sprites;
	
	public BottomLeftPiece(TileMap tm) {
		super(tm);
		try {	
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream("/Sprites/Other/Relic.gif")
			);
			sprites = new BufferedImage[1];
			width = height = 4;
			sprites[0] = spritesheet.getSubimage(0, 10, 10, 10);
			animation.setFrames(sprites);
			animation.setDelay(-1);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		x += dx;
		y += dy;
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}	
}
