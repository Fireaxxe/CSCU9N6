package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.Player;
import Handlers.Content;
import TileMap.TileMap;

public class Songoku extends Enemy {

	private Player player1;
	private ArrayList<Enemy> enemies;
	private BufferedImage[] inactiveSprites;
	private BufferedImage[] jumpSprites;
	private BufferedImage[] attackSprites;
	private boolean jumping;
	private static final int INACTIVE = 0;
	private static final int JUMPING = 1;
	private static final int ATTACKING = 2;
	private int attackTick;
	private int attackDelay = 30;
	private int step;

	public Songoku(TileMap tm, Player p, ArrayList<Enemy> enemy) {

		super(tm);
		player1 = p;
		enemies = enemy;

		health = maxHealth = 4;

		width = 30;
		height = 30;
		collision_width = 20;
		collision_height = 26;

		damage = 1;
		moveSpeed = 1.5;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -5;

		inactiveSprites = Content.Songoku[0];
		jumpSprites = Content.Songoku[1];
		attackSprites = Content.Songoku[2];

		animation.setFrames(inactiveSprites);
		animation.setDelay(-1);

		attackTick = 0;
	}

	private void getNextPosition() {
		if (left)
			dx = -moveSpeed;
		else if (right)
			dx = moveSpeed;
		else
			dx = 0;
		if (falling) {
			dy += fallSpeed;
			if (dy > maxFallSpeed)
				dy = maxFallSpeed;
		}
		if (jumping && !falling) {
			dy = jumpStart;
		}
	}

	public void update() {

		// check if done flinching
		if (flinching) {
			flinchCount++;
			if (flinchCount == 6)
				flinching = false;
		}

		getNextPosition();
		checkTileMapCollision();
		setPosition(x_temporary, y_temporary);

		// update animation
		animation.update();

		if (player1.get_x() < x)
			facingRight = false;
		else
			facingRight = true;

		// idle
		if (step == 0) {
			if (currentAction != INACTIVE) {
				currentAction = INACTIVE;
				animation.setFrames(inactiveSprites);
				animation.setDelay(-1);
			}
			attackTick++;
			if (attackTick >= attackDelay && Math.abs(player1.get_x() - x) < 60) {
				step++;
				attackTick = 0;
			}
		}
		// jump away
		if (step == 1) {
			if (currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(jumpSprites);
				animation.setDelay(-1);
			}
			jumping = true;
			if (facingRight) {
				left = true;
			} else {
				right = true;
			}
			if (falling) {
				step++;
			}
		}
		// attack
		if (step == 2) {
			if (dy > 0 && currentAction != ATTACKING) {
				currentAction = ATTACKING;
				animation.setFrames(attackSprites);
				animation.setDelay(3);
				DarkEnergy darkEnergy = new DarkEnergy(tileMap);
				darkEnergy.setPosition(x, y);
				if (facingRight) {
					darkEnergy.setVector(3, 3);
				} else {
					darkEnergy.setVector(-3, 3);
				}
				enemies.add(darkEnergy);
			}
			if (currentAction == ATTACKING && animation.hasPlayedOnce()) {
				step++;
				currentAction = JUMPING;
				animation.setFrames(jumpSprites);
				animation.setDelay(-1);
			}
		}
		// done attacking
		if (step == 3) {
			if (dy == 0) {
				step++;
			}
		}
		// land
		if (step == 4) {
			step = 0;
			left = right = jumping = false;
		}

	}

	public void draw(Graphics2D g) {

		if (flinching) {
			if (flinchCount == 0 || flinchCount == 2) {
				return;
			}
		}
		super.draw(g);
	}
}
