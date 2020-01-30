package GameState;

import Audio.JukeBox;
import Main.GamePanel;

public class GameStateManager {

	private GameState[] gameStates;
	private int currentState;
	private PauseState pauseState;
	private boolean paused;
	public static final int NUMGAMESTATES = 16;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1A = 2;
	public static final int LEVEL1B = 3;
	public static final int LEVEL1CBOSS = 4;
	public static final int DREAMSTATE = 15;

	public GameStateManager() {

		JukeBox.initialise();

		gameStates = new GameState[NUMGAMESTATES];

		pauseState = new PauseState(this);
		paused = false;

		currentState = MENUSTATE;
		loadState(currentState);

	}

	private void loadState(int state) {
		if (state == MENUSTATE) {
			gameStates[state] = new MenuState(this);
		} else if (state == LEVEL1A) {
			gameStates[state] = new Stage1A(this);
		} else if (state == LEVEL1B) {
			gameStates[state] = new Stage1B(this);
		} else if (state == LEVEL1CBOSS) {
			gameStates[state] = new Stage1CBoss(this);
		} else if (state == DREAMSTATE) {
			gameStates[state] = new DreamState(this);
		}
	}

	private void unloadState(int state) {
		gameStates[state] = null;
	}

	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}

	public void setPaused(boolean b) {
		paused = b;
	}

	public void update() {
		if (paused) {
			pauseState.update();
			return;
		}
		if (gameStates[currentState] != null) {
			gameStates[currentState].update();
		}
	}

	public void draw(java.awt.Graphics2D g) {
		if (paused) {
			pauseState.draw(g);
			return;
		}
		if (gameStates[currentState] != null) {
			gameStates[currentState].draw(g);
		} else {
			g.setColor(java.awt.Color.BLACK);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		}
	}
}