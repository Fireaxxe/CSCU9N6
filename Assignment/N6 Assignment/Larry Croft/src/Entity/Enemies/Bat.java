package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Entity.Enemy;
import Handlers.Content;
import TileMap.TileMap;


public class Bat extends Enemy {
	
	private BufferedImage[] inactiveSprites;	
	private int tick;
	private double random_a;
	private double random_b;
	
	public Bat(TileMap tm) {
		
		super(tm);
		
		health = maxHealth = 2;
		
		width = 39;
		height = 20;
		collision_width = 25;
		collision_height = 15;
		
		damage = 1;
		moveSpeed = 5;
		
		inactiveSprites = Content.Bat[0];
		
		animation.setFrames(inactiveSprites);
		animation.setDelay(4);
		
		tick = 0;
		random_a = Math.random() * 0.06 + 0.07;
		random_b = Math.random() * 0.06 + 0.07;
		
	}
	
	public void update() {
		
		// check if done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount == 6) flinching = false;
		}
		
		tick++;
		x = Math.sin(random_a * tick) + x;
		y = Math.sin(random_b * tick) + y;
		
		// update animation
		animation.update();		
	}
	
	public void draw(Graphics2D g) {
		
		if(flinching) {
			if(flinchCount == 0 || flinchCount == 2) return;
		}
		
		super.draw(g);		
	}	
}
