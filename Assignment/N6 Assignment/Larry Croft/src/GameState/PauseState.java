package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import Audio.JukeBox;
import Handlers.Keys;
import Main.GamePanel;

public class PauseState extends GameState {

	private Font font;
	private String gamePause = new String("Game Paused");
	private String pressESC = new String("Press ESC to continue");
	private String pressSPACE = new String("Press SPACE to go to Main Menu");

	public PauseState(GameStateManager gameStates) {

		super(gameStates);

		// fonts
		font = new Font("Century Gothic", Font.PLAIN, 14);
	}

	public void initialise() {
	}

	public void update() {
		handleInput();
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString(gamePause, GamePanel.WIDTH / 3 + gamePause.length() / 2, GamePanel.HEIGHT / 2);
		g.drawString(pressESC, 10, GamePanel.HEIGHT / 2 + 75);
		g.drawString(pressSPACE, 10, GamePanel.HEIGHT / 2 + 100);
	}

	public void handleInput() {
		if (Keys.isPressed(Keys.ESCAPE)) {
			gameStates.setPaused(false);
		}
		if (Keys.isPressed(Keys.SPACE)) {
			gameStates.setPaused(false);
			gameStates.setState(GameStateManager.MENUSTATE);
			JukeBox.stop("level1");
			JukeBox.stop("level2");
			JukeBox.stop("level2run");
			JukeBox.stop("level1boss");
		}
	}
}
