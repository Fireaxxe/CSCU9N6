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
import Entity.Enemies.Songoku;
import Handlers.Keys;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Stage1B extends GameState {

	private Background dungeon;
	private Player player;
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
	private boolean eventQuake;

	public Stage1B(GameStateManager gameStates) {
		super(gameStates);
		initialise();
	}

	public void initialise() {

		// backgrounds
		dungeon = new Background("/Backgrounds/dungeon.gif", 0.5, 0);

		// tilemap
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/tileset.gif");
		tileMap.loadMap("/Maps/stage1B.map");
		tileMap.setPosition(140, 0);
		tileMap.setTween(1);

		// player
		player = new Player(tileMap);
		player.setPosition(313, 396);
		player.setHealth(PlayerSave.getHealth());
		player.setLives(PlayerSave.getLives());
		player.setTime(PlayerSave.getTime());

		// enemies
		enemies = new ArrayList<Enemy>();
		eprojectiles = new ArrayList<EnemyProjectile>();
		populateEnemies();

		// energy particle
		energyParticles = new ArrayList<EnergyParticle>();

		player.initialise(enemies, energyParticles);

		// explosions
		explosions = new ArrayList<Explosion>();

		// hud
		hud = new HUD(player);

		// title and subtitle
		try {
			geonhaTempleText = ImageIO.read(getClass().getResourceAsStream("/HUD/GeonhaTemple.gif"));
			title = new Title(geonhaTempleText.getSubimage(0, 0, 178, 20));
			title.set_y(60);
			subtitle = new Title(geonhaTempleText.getSubimage(0, 33, 91, 13));
			subtitle.set_y(85);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// teleport
		teleport = new Teleport(tileMap);
		teleport.setPosition(2805, 670);

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
		JukeBox.load("/Music/level2.mp3", "level2");
		JukeBox.loop("level2", 600, JukeBox.getFrames("level2") - 2200);
		JukeBox.load("/Music/level2run.mp3", "level2run");
	}

	private void populateEnemies() {
		enemies.clear();

		Spiky spiky;
		Bat bat;
		Songoku songoku;

		spiky = new Spiky(tileMap, player);
		spiky.setPosition(41, 492);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player);
		spiky.setPosition(101, 580);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player);
		spiky.setPosition(40, 705);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player);
		spiky.setPosition(98, 788);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player);
		spiky.setPosition(371, 1049);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player);
		spiky.setPosition(671, 1049);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player);
		spiky.setPosition(971, 1049);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player);
		spiky.setPosition(1184, 605);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player);
		spiky.setPosition(881, 556);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player);
		spiky.setPosition(2389, 779);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player);
		spiky.setPosition(2806, 674);
		enemies.add(spiky);
		spiky = new Spiky(tileMap, player);
		spiky.setPosition(1874, 876);
		enemies.add(spiky);
		
		bat = new Bat(tileMap);
		bat.setPosition(2873, 757);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(2878, 887);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(2870, 932);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(2494, 1005);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(1125, 577);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(791, 579);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(2734, 69);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(2658, 81);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(2594, 77);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(1705, 637);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(1282, 471);
		enemies.add(bat);


		songoku = new Songoku(tileMap, player, enemies);
		songoku.setPosition(2683, 662);
		enemies.add(songoku);
		songoku = new Songoku(tileMap, player, enemies);
		songoku.setPosition(2297, 1017);
		enemies.add(songoku);
		songoku = new Songoku(tileMap, player, enemies);
		songoku.setPosition(2416, 1036);
		enemies.add(songoku);
		songoku = new Songoku(tileMap, player, enemies);
		songoku.setPosition(2381, 774);
		enemies.add(songoku);
		songoku = new Songoku(tileMap, player, enemies);
		songoku.setPosition(2447, 717);
		enemies.add(songoku);
		songoku = new Songoku(tileMap, player, enemies);
		songoku.setPosition(1761, 855);
		enemies.add(songoku);
		songoku = new Songoku(tileMap, player, enemies);
		songoku.setPosition(368, 1051);
		enemies.add(songoku);
		songoku = new Songoku(tileMap, player, enemies);
		songoku.setPosition(876, 1048);
		enemies.add(songoku);
		songoku = new Songoku(tileMap, player, enemies);
		songoku.setPosition(1639, 456);
		enemies.add(songoku);
	}

	public void update() {

		// check keys
		handleInput();

		// check if quake event should start
		if (player.get_x() > 2175 && !tileMap.isShaking()) {
			eventQuake = blockInput = true;
		}

		// check if end of level event should start
		if (teleport.contains(player)) {
			eventFinish = blockInput = true;
		}

		// play events
		if (eventStart) {
			eventStart();
		}
		if (eventDead) {
			eventDead();
		}
		if (eventQuake) {
			eventQuake();
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
		dungeon.setPosition(tileMap.get_x(), tileMap.get_y());

		// update player
		player.update();
		if (player.getHealth() == 0 || player.get_y() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}

		// update tilemap
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.get_x(), GamePanel.HEIGHT / 2 - player.get_y());
		tileMap.update();
		tileMap.fixBounds();

		// update enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if (e.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(tileMap, e.get_x(), e.get_y()));
			}
		}

		// update enemy projectiles
		for (int i = 0; i < eprojectiles.size(); i++) {
			EnemyProjectile ep = eprojectiles.get(i);
			ep.update();
			if (ep.shouldRemove()) {
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
		dungeon.draw(g);

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
		player.draw(g);

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
		if (blockInput || player.getHealth() == 0) {
			return;
		}
		player.setUp(Keys.keyState[Keys.UP]);
		player.setLeft(Keys.keyState[Keys.LEFT]);
		player.setDown(Keys.keyState[Keys.DOWN]);
		player.setRight(Keys.keyState[Keys.RIGHT]);
		player.setJumping(Keys.keyState[Keys.SPACE]);
		player.setRunning(Keys.keyState[Keys.RUNNING]);
		if (Keys.isPressed(Keys.SLASH)) {
			player.setAttacking();
		}
		if (Keys.isPressed(Keys.CHARGE)) {
			player.setCharging();
		}
	}

///////////////////////////////////////////////////////
//////////////////// EVENTS
///////////////////////////////////////////////////////

	// reset level
	private void reset() {
		player.loseLife();
		player.reset();
		player.setPosition(300, 131);
		populateEnemies();
		blockInput = true;
		eventCount = 0;
		tileMap.setShaking(false, 0);
		eventStart = true;
		eventStart();
		title = new Title(geonhaTempleText.getSubimage(0, 0, 178, 20));
		title.set_y(60);
		subtitle = new Title(geonhaTempleText.getSubimage(0, 33, 91, 13));
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
			JukeBox.stop("level1");
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
			JukeBox.play("playerdie");
			player.setDead();
		}
		JukeBox.stop("level2run");
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
			if (player.getLives() == 0) {
				gameStates.setState(GameStateManager.MENUSTATE);
				JukeBox.stop("level2run");
				JukeBox.stop("level2");
				JukeBox.play("gameover");
			} else {
				eventDead = blockInput = false;
				eventCount = 0;
				reset();
				JukeBox.loop("level2");
			}
		}
	}

	// earthquake
	private void eventQuake() {
		eventCount++;
		if (eventCount == 1) {
			player.stop();
			player.setPosition(2175, player.get_y());
		}
		if (eventCount == 60) {
			player.setEmote(Player.CONFUSED);
			JukeBox.stop("level2");
		}
		if (eventCount == 120) {
			player.setEmote(Player.NONE);
		}
		if (eventCount == 150) {
			tileMap.setShaking(true, 10);
		}
		if (eventCount == 180) {
			player.setEmote(Player.SURPRISED);
		}
		if (eventCount == 300) {
			player.setEmote(Player.NONE);
			eventQuake = blockInput = false;
			eventCount = 0;
			JukeBox.loop("level2run");
		}
	}

	// finished level
	private void eventFinish() {
		eventCount++;
		if (eventCount == 1) {
			JukeBox.play("teleport");
			player.setTeleporting(true);
			player.stop();
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
			PlayerSave.setHealth(player.getHealth());
			PlayerSave.setLives(player.getLives());
			PlayerSave.setTime(player.getTime());
			gameStates.setState(GameStateManager.LEVEL1CBOSS);
		}
	}
}