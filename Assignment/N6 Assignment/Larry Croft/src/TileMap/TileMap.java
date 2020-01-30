package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class TileMap {

	// position
	private double x;
	private double y;

	// bounds
	private int x_min;
	private int y_min;
	private int x_max;
	private int y_max;

	private double tween;

	// map
	private int[][] map;
	private int tileSize;
	private int numberOfRows;
	private int numberOfColumns;
	private int width;
	private int height;

	// tileset
	private BufferedImage tileset;
	private int numberOfTilesAcross;
	private Tile[][] tiles;

	// drawing
	private int rowOffset;
	private int columnOffset;
	private int numberOfRowsToDraw;
	private int numberOfColumnsToDraw;

	// effects
	private boolean shaking;
	private int intensity;

	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numberOfRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numberOfColumnsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
	}

	public void loadTiles(String s) {

		try {

			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			numberOfTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numberOfTilesAcross];

			BufferedImage subimage;
			for (int column = 0; column < numberOfTilesAcross; column++) {
				subimage = tileset.getSubimage(column * tileSize, 0, tileSize, tileSize);
				tiles[0][column] = new Tile(subimage, Tile.NORMAL);
				subimage = tileset.getSubimage(column * tileSize, tileSize, tileSize, tileSize);
				tiles[1][column] = new Tile(subimage, Tile.BLOCKED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadMap(String s) {

		try {
			InputStream inputStream = getClass().getResourceAsStream(s);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			numberOfColumns = Integer.parseInt(bufferedReader.readLine());
			numberOfRows = Integer.parseInt(bufferedReader.readLine());
			map = new int[numberOfRows][numberOfColumns];
			width = numberOfColumns * tileSize;
			height = numberOfRows * tileSize;

			x_min = GamePanel.WIDTH - width;
			x_max = 0;
			y_min = GamePanel.HEIGHT - height;
			y_max = 0;

			String delims = "\\s+";
			for (int row = 0; row < numberOfRows; row++) {
				String line = bufferedReader.readLine();
				String[] tokens = line.split(delims);
				for (int column = 0; column < numberOfColumns; column++) {
					map[row][column] = Integer.parseInt(tokens[column]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getTileSize() {
		return tileSize;
	}

	public double get_x() {
		return x;
	}

	public double get_y() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public int getNumberOfColumns() {
		return numberOfColumns;
	}

	public int getType(int row, int col) {
		int rowsColumns = map[row][col];
		int rows = rowsColumns / numberOfTilesAcross;
		int columns = rowsColumns % numberOfTilesAcross;
		return tiles[rows][columns].getType();
	}

	public boolean isShaking() {
		return shaking;
	}

	public void setTween(double d) {
		tween = d;
	}

	public void setShaking(boolean b, int i) {
		shaking = b;
		intensity = i;
	}

	public void setBounds(int i1, int i2, int i3, int i4) {
		x_min = GamePanel.WIDTH - i1;
		y_min = GamePanel.WIDTH - i2;
		x_max = i3;
		y_max = i4;
	}

	public void setPosition(double x, double y) {

		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;

		fixBounds();

		columnOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;
	}

	public void fixBounds() {
		if (x < x_min) {
			x = x_min;
		}
		if (y < y_min) {
			y = y_min;
		}
		if (x > x_max) {
			x = x_max;
		}
		if (y > y_max) {
			y = y_max;
		}
	}

	public void update() {
		if (shaking) {
			this.x += Math.random() * intensity - intensity / 2;
			this.y += Math.random() * intensity - intensity / 2;
		}
	}

	public void draw(Graphics2D g) {

		for (int row = rowOffset; row < rowOffset + numberOfRowsToDraw; row++) {
			if (row >= numberOfRows) {
				break;
			}
			for (int column = columnOffset; column < columnOffset + numberOfColumnsToDraw; column++) {
				if (column >= numberOfColumns) {
					break;
				}
				if (map[row][column] == 0) {
					continue;
				}
				int rowsColums = map[row][column];
				int rows = rowsColums / numberOfTilesAcross;
				int columns = rowsColums % numberOfTilesAcross;
				g.drawImage(tiles[rows][columns].getImage(), (int) x + column * tileSize, (int) y + row * tileSize,
						null);
			}
		}
	}
}
