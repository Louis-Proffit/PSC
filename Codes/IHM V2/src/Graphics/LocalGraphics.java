package Graphics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashSet;

import Global.Checkpoint;
import Global.Drone;
import Global.Enemy;
import Global.Grid;
import Global.MouseState;
import Global.Obstacle;
import Global.PathFinder;
import Global.Tile;

public class LocalGraphics {
	
	public int n;
	public int m;
	
	public int frameWidth;
	public int frameHeight;
	
	public float tileWidth;
	public float tileHeight;
	
	public Frame frame;
	public Window window;
	public Graphics graphics;
	
	public Grid grid;
	
	public MouseState mouseState;
	
	public LocalGraphics(int n, int m, int frameWidth, int frameHeight, Grid grid) {
		this.n = n;
		this.m = m;
		
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		
		this.tileWidth = frameWidth / n;
		this.tileHeight = frameHeight / m;
		
		this.grid = grid;
		this.frame = new Frame(frameWidth, frameHeight, grid);
		this.window = frame.window;
		this.graphics = window.getGraphics();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("Interruption impromptue");
		}
		window.localGraphics = this;
		mouseState = MouseState.EMPTY;
	}
	
	public int[] getTileFromPoint(Point P) {
		int x = P.x - 8;
		int y = P.y - 31;
		
		int i = (int)(x / tileWidth);
		int j = (int)(y / tileHeight);
		
		i = Math.min(Math.abs(i), n - 1);
		j = Math.min(Math.abs(j), m - 1);
		
		return new int[] {i, j};
	}
	
	public void onMouseClicked(MouseEvent ev) {
		int[] coordinates = getTileFromPoint(ev.getPoint());
		int x = coordinates[0];
		int y = coordinates[1];
		Tile tile = new Tile(x, y);
		boolean isObstacle = PathFinder.grid.isObstacle(x, y);
		boolean isDrone = PathFinder.grid.isDrone(x, y);
		boolean isEnemy = PathFinder.grid.isEnemy(x, y);
		boolean isCheckpoint = PathFinder.grid.isCheckpoint(x, y);
		
		switch(mouseState) {
			case EMPTY:
				if (isObstacle) {
					PathFinder.grid.obstacles.remove(tile);
				}
				if (isEnemy) PathFinder.grid.enemies.remove(tile);
				if (isObstacle) {
					PathFinder.grid.obstacles.remove(tile);
				}
				if (isCheckpoint) {
					PathFinder.grid.checkpoints.remove(tile);
				}
				break;
			case DRONE:
				if (!isObstacle & ! isDrone) PathFinder.addDrone(x, y);
				break;
			case OBSTACLE:
				if (!isDrone & !isCheckpoint & !isEnemy & !isObstacle) {
					PathFinder.addObstacle(x, y);
				}
				break;
			case ENEMY:
				if (!isObstacle & !isEnemy) PathFinder.addEnemy(x, y);
				break;
			case CHECKPOINT:
				if (!isObstacle & !isCheckpoint) {
					PathFinder.addCheckpoint(x, y);
				}
				break;
		}
	}
	
	public void keyTyped(KeyEvent ev) {
		char key = ev.getKeyChar();
		// Quitter
		if (key == 'q') System.exit(0);
		// Empty : gomme
		else if (key == 'e') mouseState = MouseState.EMPTY;
		// Drone
		else if (key == 'd') mouseState = MouseState.DRONE;
		// Obstacle
		else if (key == 'o') mouseState = MouseState.OBSTACLE;
		// Checkpoint
		else if (key == 'c') mouseState = MouseState.CHECKPOINT;
		// Ennemi
		else if (key == 'x') mouseState = MouseState.ENEMY;
	}
	
	public void repaint() {
		window.reset(graphics);
		paintPath();
		for (Drone drone : grid.drones) paintDrone(drone);
		for (Enemy enemy : grid.enemies) paintEnemy(enemy);
		for (Obstacle obstacle : grid.obstacles) paintObstacle(obstacle);
		for (Checkpoint checkpoint : grid.checkpoints) paintCheckpoint(checkpoint);
	}
	
	public void repaintWithPath() {
		window.reset(graphics);
		paintPath();
		for (Drone drone : grid.drones) paintDrone(drone);
		for (Enemy enemy : grid.enemies) paintEnemy(enemy);
		for (Obstacle obstacle : grid.obstacles) paintObstacle(obstacle);
		for (Checkpoint checkpoint : grid.checkpoints) paintCheckpoint(checkpoint);
	}
	
	public void paintPath() {
		window.paintPath(graphics);
	}

	public void paintDrone(Drone drone) {
		window.paintDrone(drone, graphics);
	}
	
	public void paintObstacle(Obstacle obstacle) {
		window.paintObstacle(obstacle, graphics);
	}

	public void paintCheckpoint(Tile tile) {
		window.paintCheckpoint(tile, graphics);
	}
	
	public void paintEnemy(Enemy enemy) {
		window.paintEnemy(enemy, graphics);
	}

	public void paintEmpty(Tile tile) {
		window.paintEmpty(tile, graphics);
	}
}
