package Entity;

import java.awt.Rectangle;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

public abstract class MapObject {

	// tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double x_map;
	protected double y_map;

	// position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;

	// dimensions
	protected int width;
	protected int height;

	// collision box
	protected int collision_width;
	protected int collision_height;

	// collision
	protected int currentRow;
	protected int currentColumn;
	protected double x_dest;
	protected double y_dest;
	protected double x_temporary;
	protected double y_temporary;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;

	// animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;

	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;

	// movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;

	// constructor
	public MapObject(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
		animation = new Animation();
		facingRight = true;
	}

	public boolean intersects(MapObject object) {
		Rectangle rectangle_1 = getRectangle();
		Rectangle rectangle_2 = object.getRectangle();
		return rectangle_1.intersects(rectangle_2);
	}

	public boolean intersects(Rectangle rectangle) {
		return getRectangle().intersects(rectangle);
	}

	public boolean contains(MapObject object) {
		Rectangle rectangle_1 = getRectangle();
		Rectangle rectangle_2 = object.getRectangle();
		return rectangle_1.contains(rectangle_2);
	}

	public boolean contains(Rectangle rectangle) {
		return getRectangle().contains(rectangle);
	}

	public Rectangle getRectangle() {
		return new Rectangle((int) x - collision_width / 2, (int) y - collision_height / 2, collision_width,
				collision_height);
	}

	public void calculateCorners(double x, double y) {
		int leftTile = (int) (x - collision_width / 2) / tileSize;
		int rightTile = (int) (x + collision_width / 2 - 1) / tileSize;
		int topTile = (int) (y - collision_height / 2) / tileSize;
		int bottomTile = (int) (y + collision_height / 2 - 1) / tileSize;
		if (topTile < 0 || bottomTile >= tileMap.getNumberOfRows() || leftTile < 0
				|| rightTile >= tileMap.getNumberOfColumns()) {
			topLeft = topRight = bottomLeft = bottomRight = false;
			return;
		}
		int topLeftTile = tileMap.getType(topTile, leftTile);
		int topRightTile = tileMap.getType(topTile, rightTile);
		int bottomLeftTile = tileMap.getType(bottomTile, leftTile);
		int bottomRightTile = tileMap.getType(bottomTile, rightTile);
		topLeft = topLeftTile == Tile.BLOCKED;
		topRight = topRightTile == Tile.BLOCKED;
		bottomLeft = bottomLeftTile == Tile.BLOCKED;
		bottomRight = bottomRightTile == Tile.BLOCKED;
	}

	public void checkTileMapCollision() {

		currentColumn = (int) x / tileSize;
		currentRow = (int) y / tileSize;

		x_dest = x + dx;
		y_dest = y + dy;

		x_temporary = x;
		y_temporary = y;

		calculateCorners(x, y_dest);
		if (dy < 0) {
			if (topLeft || topRight) {
				dy = 0;
				y_temporary = currentRow * tileSize + collision_height / 2;
			} else {
				y_temporary += dy;
			}
		}
		if (dy > 0) {
			if (bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				y_temporary = (currentRow + 1) * tileSize - collision_height / 2;
			} else {
				y_temporary += dy;
			}
		}

		calculateCorners(x_dest, y);
		if (dx < 0) {
			if (topLeft || bottomLeft) {
				dx = 0;
				x_temporary = currentColumn * tileSize + collision_width / 2;
			} else {
				x_temporary += dx;
			}
		}
		if (dx > 0) {
			if (topRight || bottomRight) {
				dx = 0;
				x_temporary = (currentColumn + 1) * tileSize - collision_width / 2;
			} else {
				x_temporary += dx;
			}
		}

		if (!falling) {
			calculateCorners(x, y_dest + 1);
			if (!bottomLeft && !bottomRight) {
				falling = true;
			}
		}

	}

	public int get_x() {
		return (int) x;
	}

	public int get_y() {
		return (int) y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getCollisionWidth() {
		return collision_width;
	}

	public int getCollisionHeight() {
		return collision_height;
	}

	public boolean isFacingRight() {
		return facingRight;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void setMapPosition() {
		x_map = tileMap.get_x();
		y_map = tileMap.get_y();
	}

	public void setLeft(boolean b) {
		left = b;
	}

	public void setRight(boolean b) {
		right = b;
	}

	public void setUp(boolean b) {
		up = b;
	}

	public void setDown(boolean b) {
		down = b;
	}

	public void setJumping(boolean b) {
		jumping = b;
	}

	public boolean notOnScreen() {
		return x + x_map + width < 0 || x + x_map - width > GamePanel.WIDTH || y + y_map + height < 0
				|| y + y_map - height > GamePanel.HEIGHT;
	}

	public void draw(java.awt.Graphics2D g) {
		setMapPosition();
		if (facingRight) {
			g.drawImage(animation.getImage(), (int) (x + x_map - width / 2), (int) (y + y_map - height / 2), null);
		} else {
			g.drawImage(animation.getImage(), (int) (x + x_map - width / 2 + width), (int) (y + y_map - height / 2),
					-width, height, null);
		}
		// draw collision box
		// Rectangle r = getRectangle();
		// r.x += xmap;
		// r.y += ymap;
		// g.draw(r);
	}
}