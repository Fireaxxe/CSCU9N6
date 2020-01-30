package GameState;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Audio.JukeBox;
import Entity.Enemy;
import Entity.EnemyProjectile;
import Entity.EnergyParticle;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Entity.PlayerSave;
import Entity.Teleport;
import Entity.Title;
import Entity.Enemies.Bat;
import Entity.Enemies.Spiky;
import Handlers.Keys;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Stage1A extends GameState {

	private Background sky;
	private Background clouds;
	private Background mountains;
	private Player player1;
	private TileMap tileMap;
	private ArrayList<Enemy> enemies;
	private ArrayList<EnemyProjectile> eprojectiles;
	private ArrayList<EnergyParticle> energyParticles;
	private ArrayList<Explosion> explosions;
	private HUD hud;
	private BufferedImage geonhaTempleText;
	private Title title;
	private Title subtitle;
	private Teleport teleport;

	// events
	private boolean blockInput = false;
	private int eventCount = 0;
	private boolean eventStart;
	private ArrayList<Rectangle> transitionBox;
	private boolean eventFinish;
	private boolean eventDead;

	public Stage1A(GameStateManager gameStates) {

		super(gameStates);
		initialise();
	}

	public void initialise() {

		// backgrounds
		sky = new Background("/Backgrounds/sky.gif", 0);
		clouds = new Background("/Backgrounds/clouds.gif", 0.1);
		mountains = new Background("/Backgrounds/mountains.gif", 0.2);

		// tilemap
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/tileset.gif");
		tileMap.loadMap("/Maps/stage1A.map");
		tileMap.setPosition(140, 0);
		tileMap.setBounds(tileMap.getWidth() - 1 * tileMap.getTileSize(),
				tileMap.getHeight() - 2 * tileMap.getTileSize(), 0, 0);
		tileMap.setTween(1);

		// player
		player1 = new Player(tileMap);
		player1.setPosition(300, 161);
		player1.setHealth(PlayerSave.getHealth());
		player1.setLives(PlayerSave.getLives());
		player1.setTime(PlayerSave.getTime());

		// enemies
		enemies = new ArrayList<Enemy>();
		eprojectiles = new ArrayList<EnemyProjectile>();
		populateEnemies();

		// energy particle
		energyParticles = new ArrayList<EnergyParticle>();

		// init player
		player1.initialise(enemies, energyParticles);

		// explosions
		explosions = new ArrayList<Explosion>();

		// hud
		hud = new HUD(player1);

		// title and subtitle
		try {
			geonhaTempleText = ImageIO.read(getClass().getResourceAsStream("/HUD/GeonhaTemple.gif"));
			title = new Title(geonhaTempleText.getSubimage(0, 0, 178, 20));
			title.set_y(60);
			subtitle = new Title(geonhaTempleText.getSubimage(0, 20, 82, 13));
			subtitle.set_y(85);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// teleport
		teleport = new Teleport(tileMap);
		teleport.setPosition(3700, 131);

		// start event
		eventStart = true;
		transitionBox = new ArrayList<Rectangle>();
		eventStart();

		// sfx
		JukeBox.load("/SFX/teleport.mp3", "teleport");
		JukeBox.load("/SFX/explode.mp3", "explode");
		JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");
		JukeBox.load("/SFX/gameover.mp3", "gameover");
		JukeBox.load("/SFX/playerdie.mp3", "playerdie");

		// music
		JukeBox.load("/Music/level1.mp3", "level1");
		JukeBox.loop("level1", 600, JukeBox.getFrames("level1") - 2200);

	}

	private void populateEnemies() {
		enemies.clear();
		
		Spiky spiky;
		Bat bat;

		spiky = new Spiky(tileMap, player1);
		spiky.setPosition(1300, 100);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player1);
		spiky.setPosition(1320, 100);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player1);
		spiky.setPosition(1340, 100);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player1);
		spiky.setPosition(1660, 100);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player1);
		spiky.setPosition(1680, 100);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player1);
		spiky.setPosition(1700, 100);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player1);
		spiky.setPosition(2177, 100);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player1);
		spiky.setPosition(2960, 100);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player1);
		spiky.setPosition(2980, 100);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player1);
		spiky.setPosition(3000, 100);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player1);
		spiky.setPosition(4287, 125);
		enemies.add(spiky);
		
		bat = new Bat(tileMap);
		bat.setPosition(2600, 100);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(3500, 100);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(4166, 149);
		enemies.add(bat);
	}

	public void update() {

		// check keys
		handleInput();

		// check if end of level event should start
		if (teleport.contains(player1)) {
			eventFinish = blockInput = true;
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

		// move title and subtitle
		if (title != null) {
			title.update();
			if (title.shouldRemove()) {
				title = null;
			}
		}
		if (subtitle != null) {
			subtitle.update();
			if (subtitle.shouldRemove()) {
				subtitle = null;
			}
		}

		// move backgrounds
		clouds.setPosition(tileMap.get_x(), tileMap.get_y());
		mountains.setPosition(tileMap.get_x(), tileMap.get_y());

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
			if (enemy.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(tileMap, enemy.get_x(), enemy.get_y()));
			}
		}

		// update enemy projectiles
		for (int i = 0; i < eprojectiles.size(); i++) {
			EnemyProjectile enemyProjectile = eprojectiles.get(i);
			enemyProjectile.update();
			if (enemyProjectile.shouldRemove()) {
				eprojectiles.remove(i);
				i--;
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

		// update teleport
		teleport.update();

	}

	public void draw(Graphics2D g) {

		// draw background
		sky.draw(g);
		clouds.draw(g);
		mountains.draw(g);

		// draw tilemap
		tileMap.draw(g);

		// draw enemies
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}

		// draw enemy projectiles
		for (int i = 0; i < eprojectiles.size(); i++) {
			eprojectiles.get(i).draw(g);
		}

		// draw explosions
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).draw(g);
		}

		// draw player
		player1.draw(g);

		// draw teleport
		teleport.draw(g);

		// draw hud
		hud.draw(g);
		

		// draw title
		if (title != null) {
			title.draw(g);
		}
		if (subtitle != null) {
			subtitle.draw(g);
		}

		// draw transition boxes
		g.setColor(java.awt.Color.BLACK);
		for (int i = 0; i < transitionBox.size(); i++) {
			g.fill(transitionBox.get(i));
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
		player1.setPosition(300, 161);
		populateEnemies();
		blockInput = true;
		eventCount = 0;
		tileMap.setShaking(false, 0);
		eventStart = true;
		eventStart();
		title = new Title(geonhaTempleText.getSubimage(0, 0, 178, 20));
		title.set_y(60);
		subtitle = new Title(geonhaTempleText.getSubimage(0, 20, 82, 13));
		subtitle.set_y(85);
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
		}
		if (eventCount > 1 && eventCount < 60) {
			transitionBox.get(0).height -= 4;
			transitionBox.get(1).width -= 6;
			transitionBox.get(2).y += 4;
			transitionBox.get(3).x += 6;
		}
		if (eventCount == 30) {
			title.begin();
		}
		if (eventCount == 60) {
			eventStart = blockInput = false;
			eventCount = 0;
			subtitle.begin();
			transitionBox.clear();
		}
	}
	
	

	// player has died
	private void eventDead() {
		eventCount++;
		if (eventCount == 1) {
			player1.setDead();
			JukeBox.play("playerdie");
			player1.stop();
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
				JukeBox.stop("level1");	
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
			JukeBox.play("teleport");
			player1.setTeleporting(true);
			player1.stop();
		} else if (eventCount == 120) {
			transitionBox.clear();
			transitionBox.add(new Rectangle(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		} else if (eventCount > 120) {
			transitionBox.get(0).x -= 6;
			transitionBox.get(0).y -= 4;
			transitionBox.get(0).width += 12;
			transitionBox.get(0).height += 8;
			JukeBox.stop("teleport");
		}
		if (eventCount == 180) {
			PlayerSave.setHealth(player1.getHealth());
			PlayerSave.setLives(player1.getLives());
			PlayerSave.setTime(player1.getTime());
			gameStates.setState(GameStateManager.LEVEL1B);
		}
	}
}