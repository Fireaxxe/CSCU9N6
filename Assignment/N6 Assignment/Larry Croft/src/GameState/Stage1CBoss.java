package GameState;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import Audio.JukeBox;
import Entity.Enemy;
import Entity.EnergyParticle;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Entity.PlayerSave;
import Entity.Portal;
import Entity.Level1Boss;
import Entity.Enemies.DarkEnergy;
import Entity.Relic.BottomLeftPiece;
import Entity.Relic.BottomRightPiece;
import Entity.Relic.TopLeftPiece;
import Entity.Relic.TopRightPiece;
import Handlers.Keys;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Stage1CBoss extends GameState {

	private Background temple;
	private Player player1;
	private TileMap tileMap;
	private ArrayList<Enemy> enemies;
	private ArrayList<EnergyParticle> energyParticles;
	private ArrayList<Explosion> explosions;
	private HUD hud;
	private TopLeftPiece topLeftPiece;
	private TopRightPiece topRightPiece;
	private BottomLeftPiece bottomLeftPiece;
	private BottomRightPiece bottomRightPiece;
	private Portal portal;
	private Level1Boss boss;

	// events
	private boolean blockInput = false;
	private int eventCount = 0;
	private boolean eventStart;
	private ArrayList<Rectangle> transitionBox;
	private boolean eventFinish;
	private boolean eventDead;
	private boolean eventPortal;
	private boolean flash;
	private boolean eventBossDead;

	public Stage1CBoss(GameStateManager gameStates) {
		super(gameStates);
		initialise();
	}

	public void initialise() {

		// backgrounds
		temple = new Background("/Backgrounds/dungeon.gif", 0.5, 0);

		// tilemap
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/tileset.gif");
		tileMap.loadMap("/Maps/stage1CBoss.map");
		tileMap.setPosition(140, 0);
		tileMap.setTween(1);

		// player
		player1 = new Player(tileMap);
		player1.setPosition(50, 190);
		player1.setHealth(PlayerSave.getHealth());
		player1.setLives(PlayerSave.getLives());
		player1.setTime(PlayerSave.getTime());

		// explosions
		explosions = new ArrayList<Explosion>();

		// enemies
		enemies = new ArrayList<Enemy>();
		populateEnemies();

		// energy particle
		energyParticles = new ArrayList<EnergyParticle>();

		// init player
		player1.initialise(enemies, energyParticles);

		// hud
		hud = new HUD(player1);

		// portal
		portal = new Portal(tileMap);
		portal.setPosition(160, 154);

		// artifact
		topLeftPiece = new TopLeftPiece(tileMap);
		topRightPiece = new TopRightPiece(tileMap);
		bottomLeftPiece = new BottomLeftPiece(tileMap);
		bottomRightPiece = new BottomRightPiece(tileMap);
		topLeftPiece.setPosition(152, 102);
		topRightPiece.setPosition(162, 102);
		bottomLeftPiece.setPosition(152, 112);
		bottomRightPiece.setPosition(162, 112);

		// start event
		eventStart = blockInput = true;
		transitionBox = new ArrayList<Rectangle>();
		eventStart();

		// sfx
		JukeBox.load("/SFX/teleport.mp3", "teleport");
		JukeBox.load("/SFX/explode.mp3", "explode");
		JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");
		JukeBox.load("/SFX/gameover.mp3", "gameover");
		JukeBox.load("/SFX/playerdie.mp3", "playerdie");
		

		// music
		JukeBox.load("/Music/level1boss.mp3", "level1boss");
		JukeBox.load("/Music/win.mp3", "win");
	}

	private void populateEnemies() {
		enemies.clear();
		boss = new Level1Boss(tileMap, player1, enemies, explosions);
		boss.setPosition(-9000, 9000);
		enemies.add(boss);
	}

	public void update() {

		// check keys
		handleInput();

		// check if boss dead event should start
		if (!eventFinish && boss.isDead()) {
			eventBossDead = blockInput = true;
		}

		// check if player dead event should start
		if (player1.getHealth() == 0 || player1.get_y() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}

		// play events
		if (eventStart) {
			eventStart();
		}
		if (eventDead) {
			eventDead();
		}
		if (eventFinish) {
			eventFinish();
		}
		if (eventPortal) {
			eventPortal();
		}
		if (eventBossDead) {
			eventBossDead();
		}

		// move backgrounds
		temple.setPosition(tileMap.get_x(), tileMap.get_y());

		// update player
		player1.update();

		// update tilemap
		tileMap.setPosition(GamePanel.WIDTH / 2 - player1.get_x(), GamePanel.HEIGHT / 2 - player1.get_y());
		tileMap.update();
		tileMap.fixBounds();

		// update enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			enemy.update();
			if (enemy.isDead() || enemy.shouldRemove()) {
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(tileMap, enemy.get_x(), enemy.get_y()));
			}
		}

		// update explosions
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if (explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}

		// update portal
		portal.update();

		// update artfact
		topLeftPiece.update();
		topRightPiece.update();
		bottomLeftPiece.update();
		bottomRightPiece.update();

	}

	public void draw(Graphics2D g) {

		// draw background
		temple.draw(g);

		// draw tilemap
		tileMap.draw(g);

		// draw portal
		portal.draw(g);

		// draw enemies
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}

		// draw explosions
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).draw(g);
		}

		// draw artifact
		topLeftPiece.draw(g);
		topRightPiece.draw(g);
		bottomLeftPiece.draw(g);
		bottomRightPiece.draw(g);

		// draw player
		player1.draw(g);

		// draw hud
		hud.draw(g);

		// draw transition boxes
		g.setColor(java.awt.Color.BLACK);
		for (int i = 0; i < transitionBox.size(); i++) {
			g.fill(transitionBox.get(i));
		}

		// flash
		if (flash) {
			g.setColor(java.awt.Color.WHITE);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		}

	}

	public void handleInput() {
		if (Keys.isPressed(Keys.ESCAPE)) {
			gameStates.setPaused(true);
		}
		if (blockInput || player1.getHealth() == 0) {
			return;
		}
		player1.setUp(Keys.keyState[Keys.UP]);
		player1.setLeft(Keys.keyState[Keys.LEFT]);
		player1.setDown(Keys.keyState[Keys.DOWN]);
		player1.setRight(Keys.keyState[Keys.RIGHT]);
		player1.setJumping(Keys.keyState[Keys.SPACE]);
		player1.setRunning(Keys.keyState[Keys.RUNNING]);
		if (Keys.isPressed(Keys.SLASH)) {
			player1.setAttacking();
		}
		if (Keys.isPressed(Keys.CHARGE)) {
			player1.setCharging();
		}
	}

///////////////////////////////////////////////////////
//////////////////// EVENTS
///////////////////////////////////////////////////////

	// reset level
	private void reset() {
		player1.reset();
		player1.setPosition(50, 190);
		populateEnemies();
		eventStart = blockInput = true;
		eventCount = 0;
		eventStart();
	}

	// level started
	private void eventStart() {
		eventCount++;
		if (eventCount == 1) {
			transitionBox.clear();
			transitionBox.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			transitionBox.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			transitionBox.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			transitionBox.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			if (!portal.isSpriteOpened()) {
				tileMap.setShaking(true, 10);
			}
			JukeBox.stop("level2run");
		}
		if (eventCount > 1 && eventCount < 60) {
			transitionBox.get(0).height -= 4;
			transitionBox.get(1).width -= 6;
			transitionBox.get(2).y += 4;
			transitionBox.get(3).x += 6;
		}
		if (eventCount == 60) {
			eventStart = blockInput = false;
			eventCount = 0;
			eventPortal = blockInput = true;
			transitionBox.clear();

		}
	}

	// player has died
	private void eventDead() {
		eventCount++;
		if (eventCount == 1) {
			player1.stop();
			JukeBox.play("playerdie");
			player1.setDead();
		}
		if (eventCount == 60) {
			transitionBox.clear();
			transitionBox.add(new Rectangle(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		} else if (eventCount > 60) {
			transitionBox.get(0).x -= 6;
			transitionBox.get(0).y -= 4;
			transitionBox.get(0).width += 12;
			transitionBox.get(0).height += 8;
		}
		if (eventCount >= 120) {
			if (player1.getLives() == 0) {
				gameStates.setState(GameStateManager.MENUSTATE);
				JukeBox.stop("level1boss");
				JukeBox.play("gameover");
			} else {
				eventDead = blockInput = false;
				eventCount = 0;
				player1.loseLife();
				reset();
			}
		}
	}

	// finished level
	private void eventFinish() {
		eventCount++;
		if (eventCount == 1) {
			transitionBox.clear();
			transitionBox.add(new Rectangle(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		} else if (eventCount > 1) {
			transitionBox.get(0).x -= 6;
			transitionBox.get(0).y -= 4;
			transitionBox.get(0).width += 12;
			transitionBox.get(0).height += 8;
		}
		if (eventCount == 60) {
			PlayerSave.setHealth(player1.getHealth());
			PlayerSave.setLives(player1.getLives());
			PlayerSave.setTime(player1.getTime());
			gameStates.setState(GameStateManager.DREAMSTATE);
		}

	}

	private void eventPortal() {
		eventCount++;
		if (eventCount == 1) {
			if (portal.isSpriteOpened()) {
				eventCount = 360;
			}
		}
		if (eventCount > 60 && eventCount < 180) {
			energyParticles.add(new EnergyParticle(tileMap, 157, 107, (int) (Math.random() * 4)));
		}
		if (eventCount >= 160 && eventCount <= 180) {
			if (eventCount % 4 == 0 || eventCount % 4 == 1) {
				flash = true;
			} else {
				flash = false;
			}
		}
		if (eventCount == 181) {
			tileMap.setShaking(false, 0);
			flash = false;
			topLeftPiece.setVector(-0.3, -0.3);
			topRightPiece.setVector(0.3, -0.3);
			bottomLeftPiece.setVector(-0.3, 0.3);
			bottomRightPiece.setVector(0.3, 0.3);
			player1.setEmote(Player.SURPRISED);
		}
		if (eventCount == 240) {
			topLeftPiece.setVector(0, -5);
			topRightPiece.setVector(0, -5);
			bottomLeftPiece.setVector(0, -5);
			bottomRightPiece.setVector(0, -5);
		}
		if (eventCount == 300) {
			player1.setEmote(Player.NONE);
			portal.setOpeningSprite();
		}
		if (eventCount == 360) {
			flash = true;
			boss.setPosition(160, 160);
			DarkEnergy darkEnergy;
			for (int i = 0; i < 20; i++) {
				darkEnergy = new DarkEnergy(tileMap);
				darkEnergy.setPosition(160, 160);
				darkEnergy.setVector(Math.random() * 10 - 5, Math.random() * -2 - 3);
				enemies.add(darkEnergy);
			}
		}
		if (eventCount == 362) {
			flash = false;
			JukeBox.loop("level1boss", 0, 60000, JukeBox.getFrames("level1boss") - 4000);
		}
		if (eventCount == 420) {
			eventPortal = blockInput = false;
			eventCount = 0;
			boss.setActive();
		}

	}

	public void eventBossDead() {
		eventCount++;
		if (eventCount == 1) {
			player1.stop();
			JukeBox.stop("level1boss");
			enemies.clear();
		}
		if (eventCount <= 120 && eventCount % 15 == 0) {
			explosions.add(new Explosion(tileMap, boss.get_x(), boss.get_y()));
			JukeBox.play("explode");
		}
		if (eventCount == 180) {
			JukeBox.play("win");
		}
		if (eventCount == 390) {
			eventBossDead = false;
			eventCount = 0;
			eventFinish = true;
		}
	}
}