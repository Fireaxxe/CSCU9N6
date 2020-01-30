package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Handlers.Keys;
import Main.GamePanel;

public class DreamState extends GameState {

	private float hue;
	private Color color;

	private double angle;
	private BufferedImage image;

	public DreamState(GameStateManager gameStates) {
		super(gameStates);
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/PlayerSprites.gif")).getSubimage(0, 0,
					40, 40);
		} catch (Exception e) {
		}
	}

	public void initialise() {
	}

	public void update() {
		handleInput();
		color = Color.getHSBColor(hue, 1f, 1f);
		hue += 0.01;
		if (hue > 1) {
			hue = 0;
		}
		angle += 0.1;
	}

	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		AffineTransform affineTransform = new AffineTransform();
		affineTransform.translate(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2);
		affineTransform.rotate(angle);
		g.drawImage(image, affineTransform, null);
	}

	public void handleInput() {
		if (Keys.isPressed(Keys.ESCAPE)) {
			gameStates.setState(GameStateManager.MENUSTATE);
		}
	}
}
