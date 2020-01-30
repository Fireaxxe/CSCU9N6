package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Audio.JukeBox;
import TileMap.TileMap;

public class Player extends MapObject {

	// references
	private ArrayList<Enemy> enemies;

	// player stuff
	private int lives;
	private int health;
	private int maxHealth;
	private int damage;
	private int chargeDamage;
	private boolean knockback;
	private boolean flinching;
	private long flinchCount;
	private int score;
	private boolean doubleJump;
	private boolean alreadyDoubleJump;
	private double doubleJumpStart;
	private ArrayList<EnergyParticle> energyParticles;
	private long time;

	// actions
	private boolean running;
	private boolean attacking;
	private boolean upattacking;
	private boolean charging;
	private int chargingTick;
	private boolean teleporting;

	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] NUMBER_OF_FRAMES = { 2, 5, 5, 1, 1, 7, 4, 3, 2, 1, 9 };
	private final int[] FRAMEWIDTHS = { 40, 40, 80, 40, 40, 40, 80, 40, 40, 40, 40 };
	private final int[] FRAMEHEIGHTS = { 40, 40, 40, 40, 40, 80, 40, 40, 40, 40, 40 };
	private final int[] SPRITEDELAYS = { 10, 3, 2, 6, 5, 3, 2, 2, 1, -1, 5 };

	private Rectangle attackRectangle;
	private Rectangle attackUpRectangle;
	private Rectangle chargingRectangle;

	// animation actions
	private static final int STANDING = 0;
	private static final int WALKING = 1;
	private static final int ATTACKING = 2;
	private static final int JUMPING = 3;
	private static final int FALLING = 4;
	private static final int UPATTACKING = 5;
	private static final int CHARGING = 6;
	private static final int RUNNING = 7;
	private static final int KNOCKBACK = 8;
	private static final int DEAD = 9;
	private static final int TELEPORTING = 10;

	// emotes
	private BufferedImage confused;
	private BufferedImage surprised;
	public static final int NONE = 0;
	public static final int CONFUSED = 1;
	public static final int SURPRISED = 2;
	private int emote = NONE;

	public Player(TileMap tm) {

		super(tm);

		attackRectangle = new Rectangle(0, 0, 0, 0);
		attackRectangle.width = 30;
		attackRectangle.height = 10;
		attackUpRectangle = new Rectangle((int) x - 15, (int) y - 45, 30, 30);
		chargingRectangle = new Rectangle(0, 0, 0, 0);
		chargingRectangle.width = 50;
		chargingRectangle.height = 40;

		width = 30;
		height = 30;
		collision_width = 15;
		collision_height = 38;

		moveSpeed = 1.6;
		maxSpeed = 1.6;
		stopSpeed = 1.6;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		doubleJumpStart = -3;

		damage = 2;
		chargeDamage = 1;

		facingRight = true;

		lives = 3;
		health = maxHealth = 5;

		// load sprites
		try {

			BufferedImage spritesheet = ImageIO
					.read(getClass().getResourceAsStream("/Sprites/Player/PlayerSprites.gif"));

			int count = 0;
			sprites = new ArrayList<BufferedImage[]>();
			for (int i = 0; i < NUMBER_OF_FRAMES.length; i++) {
				BufferedImage[] bufferedImage = new BufferedImage[NUMBER_OF_FRAMES[i]];
				for (int j = 0; j < NUMBER_OF_FRAMES[i]; j++) {
					bufferedImage[j] = spritesheet.getSubimage(j * FRAMEWIDTHS[i], count, FRAMEWIDTHS[i],
							FRAMEHEIGHTS[i]);
				}
				sprites.add(bufferedImage);
				count += FRAMEHEIGHTS[i];
			}

			// emotes
			spritesheet = ImageIO.read(getClass().getResourceAsStream("/HUD/Emotes.gif"));
			confused = spritesheet.getSubimage(0, 0, 14, 17);
			surprised = spritesheet.getSubimage(14, 0, 14, 17);

		} catch (Exception e) {
			e.printStackTrace();
		}

		energyParticles = new ArrayList<EnergyParticle>();

		setAnimation(STANDING);

		JukeBox.load("/SFX/playerjump.mp3", "playerjump");
		JukeBox.load("/SFX/playerlands.mp3", "playerlands");
		JukeBox.load("/SFX/playerattack.mp3", "playerattack");
		JukeBox.load("/SFX/playerhit.mp3", "playerhit");
		JukeBox.load("/SFX/playercharge.mp3", "playercharge");
	}

	public void initialise(ArrayList<Enemy> enemies, ArrayList<EnergyParticle> energyParticles) {
		this.enemies = enemies;
		this.energyParticles = energyParticles;
	}

	public int getHealth() {
		return health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setEmote(int i) {
		emote = i;
	}

	public void setTeleporting(boolean b) {
		teleporting = b;
	}

	public void setJumping(boolean b) {
		if (knockback) {
			return;
		}
		if (b && !jumping && falling && !alreadyDoubleJump) {
			doubleJump = true;
		}
		jumping = b;
	}

	public void setAttacking() {
		if (knockback) {
			return;
		}
		if (charging) {
			return;
		}
		if (up && !attacking) {
			upattacking = true;
		} else {
			attacking = true;
		}
	}

	public void setCharging() {
		if (knockback) {
			return;
		}
		if (!attacking && !upattacking && !charging) {
			charging = true;
			JukeBox.play("playercharge");
			chargingTick = 0;
		}
	}

	public void setRunning(boolean b) {
		if (!b) {
			running = false;
		} else if (b && !falling) {
			running = true;
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setDead() {
		health = 0;
		stop();
	}

	public String getTimeToString() {
		int minutes = (int) (time / 3600);
		int seconds = (int) ((time % 3600) / 60);
		return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long t) {
		time = t;
	}

	public void setHealth(int i) {
		health = i;
	}

	public void setLives(int i) {
		lives = i;
	}

	public void gainLife() {
		lives++;
	}

	public void loseLife() {
		lives--;
	}

	public int getLives() {
		return lives;
	}

	public void increaseScore(int score) {
		this.score += score;
	}

	public int getScore() {
		return score;
	}

	public void hit(int damage) {
		if (flinching) {
			return;
		}
		JukeBox.play("playerhit");
		stop();
		health -= damage;
		if (health < 0) {
			health = 0;
		}
		flinching = true;
		flinchCount = 0;
		if (facingRight) {
			dx = -1;
		} else {
			dx = 1;
		}
		dy = -3;
		knockback = true;
		falling = true;
		jumping = false;
	}

	public void reset() {
		health = maxHealth;
		facingRight = true;
		currentAction = -1;
		stop();
	}

	public void stop() {
		left = right = up = down = flinching = running = jumping = attacking = upattacking = charging = false;
	}

	private void getNextPosition() {

		if (knockback) {
			dy += fallSpeed * 2;
			if (!falling) {
				knockback = false;
			}
			return;
		}

		double maxSpeed = this.maxSpeed;
		if (running) {
			maxSpeed *= 1.75;
		}

		// movement
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx = maxSpeed;
			}
		} else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) {
					dx = 0;
				}
			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0) {
					dx = 0;
				}
			}
		}

		// cannot move while attacking, except in air
		if ((attacking || upattacking || charging) && !(jumping || falling)) {
			dx = 0;
		}

		// charging
		if (charging) {
			chargingTick++;
			if (facingRight) {
				dx = moveSpeed * (3 - chargingTick * 0.07);
			} else {
				dx = -moveSpeed * (3 - chargingTick * 0.07);
			}
		}

		// jumping
		if (jumping && !falling) {
			// sfx.get("jump").play();
			dy = jumpStart;
			falling = true;
			JukeBox.play("playerjump");
		}

		if (doubleJump) {
			dy = doubleJumpStart;
			alreadyDoubleJump = true;
			doubleJump = false;
			JukeBox.play("playerjump");
			for (int i = 0; i < 3; i++) {
				energyParticles.add(new EnergyParticle(tileMap, x, y + collision_height / 4, EnergyParticle.DOWN));
			}
		}

		if (!falling) {
			alreadyDoubleJump = false;
		}

		// falling
		if (falling) {
			dy += fallSpeed;
			if (dy < 0 && !jumping) {
				dy += stopJumpSpeed;
			}
			if (dy > maxFallSpeed) {
				dy = maxFallSpeed;
			}
		}
	}

	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(SPRITEDELAYS[currentAction]);
		width = FRAMEWIDTHS[currentAction];
		height = FRAMEHEIGHTS[currentAction];
	}

	public void update() {

		time++;

		// check teleporting and reset health
		if (teleporting) {
			energyParticles.add(new EnergyParticle(tileMap, x, y, EnergyParticle.UP));
			health = 5;
		}

		// update position
		boolean isFalling = falling;
		getNextPosition();
		checkTileMapCollision();
		setPosition(x_temporary, y_temporary);
		if (isFalling && !falling) {
			JukeBox.play("playerlands");
		}
		if (dx == 0) {
			x = (int) x;
		}
		// check done flinching
		if (flinching) {
			flinchCount++;
			if (flinchCount > 120) {
				flinching = false;
			}
		}

		// energy particles
		for (int i = 0; i < energyParticles.size(); i++) {
			energyParticles.get(i).update();
			if (energyParticles.get(i).shouldRemove()) {
				energyParticles.remove(i);
				i--;
			}
		}

		// check attack finished
		if (currentAction == ATTACKING || currentAction == UPATTACKING) {
			if (animation.hasPlayedOnce()) {
				attacking = false;
				upattacking = false;
			}
		}
		if (currentAction == CHARGING) {
			if (animation.hasPlayed(5)) {
				charging = false;
			}
			chargingRectangle.y = (int) y - 20;
			if (facingRight) {
				chargingRectangle.x = (int) x - 15;
			} else {
				chargingRectangle.x = (int) x - 35;
			}
			if (facingRight) {
				energyParticles.add(new EnergyParticle(tileMap, x + 30, y, EnergyParticle.RIGHT));
			} else {
				energyParticles.add(new EnergyParticle(tileMap, x - 30, y, EnergyParticle.LEFT));
			}
		}

		// check enemy interaction
		for (int i = 0; i < enemies.size(); i++) {

			Enemy enemy = enemies.get(i);

			// check attack
			if (currentAction == ATTACKING && animation.getFrame() == 3 && animation.getCount() == 0) {
				if (enemy.intersects(attackRectangle)) {
					enemy.hit(damage);
				}
			}

			// check upward attack
			if (currentAction == UPATTACKING && animation.getFrame() == 3 && animation.getCount() == 0) {
				if (enemy.intersects(attackUpRectangle)) {
					enemy.hit(damage + 3);
				}
			}

			// check charging attack
			if (currentAction == CHARGING) {
				if (animation.getCount() == 0) {
					if (enemy.intersects(chargingRectangle)) {
						enemy.hit(chargeDamage);
					}
					/*
					 * if(enemy.intersects(this)) { enemy.hit(chargeDamage); }
					 */
				}
			}

			// collision with enemy
			if (!enemy.isDead() && intersects(enemy) && !charging) {
				hit(enemy.getDamage());
			}

			if (enemy.isDead()) {
				JukeBox.play("explode", 2000);
			}
		}

		// set animation, ordered by priority
		if (teleporting) {
			if (currentAction != TELEPORTING) {
				setAnimation(TELEPORTING);
			}
		} else if (knockback) {
			if (currentAction != KNOCKBACK) {
				setAnimation(KNOCKBACK);
			}
		} else if (health == 0) {
			if (currentAction != DEAD) {
				setAnimation(DEAD);
			}
		} else if (upattacking) {
			if (currentAction != UPATTACKING) {
				JukeBox.play("playerattack");
				setAnimation(UPATTACKING);
				attackUpRectangle.x = (int) x - 15;
				attackUpRectangle.y = (int) y - 50;
			} else {
				if (animation.getFrame() == 4 && animation.getCount() == 0) {
					for (int i = 0; i < 3; i++) {
						energyParticles
								.add(new EnergyParticle(tileMap, attackUpRectangle.x + attackUpRectangle.width / 2,
										attackUpRectangle.y + 5, EnergyParticle.UP));
					}
				}
			}
		} else if (attacking) {
			if (currentAction != ATTACKING) {
				JukeBox.play("playerattack");
				setAnimation(ATTACKING);
				attackRectangle.y = (int) y - 6;
				if (facingRight) {
					attackRectangle.x = (int) x + 10;
				} else {
					attackRectangle.x = (int) x - 40;
				}
			} else {
				if (animation.getFrame() == 4 && animation.getCount() == 0) {
					for (int i = 0; i < 3; i++) {
						if (facingRight) {
							energyParticles
									.add(new EnergyParticle(tileMap, attackRectangle.x + attackRectangle.width - 4,
											attackRectangle.y + attackRectangle.height / 2, EnergyParticle.RIGHT));
						} else {
							energyParticles.add(new EnergyParticle(tileMap, attackRectangle.x + 4,
									attackRectangle.y + attackRectangle.height / 2, EnergyParticle.LEFT));
						}
					}
				}
			}
		} else if (charging) {
			if (currentAction != CHARGING) {
				setAnimation(CHARGING);
			}
		} else if (dy < 0) {
			if (currentAction != JUMPING) {
				setAnimation(JUMPING);
			}
		} else if (dy > 0) {
			if (currentAction != FALLING) {
				setAnimation(FALLING);
			}
		} else if (running && (left || right)) {
			if (currentAction != RUNNING) {
				setAnimation(RUNNING);
			}
		} else if (left || right) {
			if (currentAction != WALKING) {
				setAnimation(WALKING);
			}
		} else if (currentAction != STANDING) {
			setAnimation(STANDING);
		}

		animation.update();

		// set direction
		if (!attacking && !upattacking && !charging && !knockback) {
			if (right) {
				facingRight = true;
			}
			if (left) {
				facingRight = false;
			}
		}
	}

	public void draw(Graphics2D g) {

		// draw emote
		if (emote == CONFUSED) {
			g.drawImage(confused, (int) (x + x_map - collision_width / 2), (int) (y + y_map - 40), null);
		} else if (emote == SURPRISED) {
			g.drawImage(surprised, (int) (x + x_map - collision_width / 2), (int) (y + y_map - 40), null);
		}

		// draw energy particles
		for (int i = 0; i < energyParticles.size(); i++) {
			energyParticles.get(i).draw(g);
		}

		// flinch
		if (flinching && !knockback) {
			if (flinchCount % 10 < 5) {
				return;
			}
		}
		super.draw(g);
	}
}