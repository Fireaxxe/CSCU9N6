package GameState;

import java.awt.Graphics2D;

public abstract class GameState {

	protected GameStateManager gameStates;

	public GameState(GameStateManager gameStates) {
		this.gameStates = gameStates;
	}

	public abstract void initialise();

	public abstract void update();

	public abstract void draw(Graphics2D g);

	public abstract void handleInput();

}
