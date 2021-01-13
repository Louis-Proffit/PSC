package Graphics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import Global.Drone;
import Global.Enemy;
import Global.Grid;
import Global.MouseState;
import Global.Obstacle;
import Global.Tile;

// Objet à construire pour obtenir un rendu graphique
public class LocalGraphics {
	
	// Nombre de cases en abscisses et en ordonnées
	public int n;
	public int m;
	
	// Largeur et hauteur en pixels de la fênetre
	public int frameWidth;
	public int frameHeight;
	
	// Largeur et hauteur en pixels d'une case
	public float tileWidth;
	public float tileHeight;
	
	// Champs graphiques
	public Frame frame;
	public Window window;
	public Graphics graphics;
	
	//Grille
	public Grid grid;
	
	// Etat de la souris, conditionne l'action à effectuer si on clique
	public MouseState mouseState;
	
	// Construit un affichage de n*m cases, tileWidth*tileHeight pixels, à partir d'une grille grid
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
	
	// Renvoie les coordonnées en nombre de cases d'un point. Fonction auxilliaire pour la méthode onMouseClicked
	public int[] getTileFromPoint(Point P) {
		int x = P.x - 8;
		int y = P.y - 31;
		
		int i = (int)(x / tileWidth);
		int j = (int)(y / tileHeight);
		
		i = Math.min(Math.abs(i), n - 1);
		j = Math.min(Math.abs(j), m - 1);
		
		return new int[] {i, j};
	}
	
	// Change la grille en fonction des deux paramètres writingType et writingContainer
	public void onMouseClicked(MouseEvent ev) {
		int[] coordinates = getTileFromPoint(ev.getPoint());
		int x = coordinates[0];
		int y = coordinates[1];
		Tile oldTile = grid.tilesGrid[x][y];
		Tile newTile;
		
		if (mouseState == MouseState.EMPTY ) {
			if (oldTile.isCheckpoint ) {
				oldTile.isCheckpoint = false;
				grid.checkpoints.remove(oldTile);
				grid.pathHasChanged = true;
			}
			else if (oldTile.getClass() == Tile.class) return;
			newTile = new Tile(oldTile);
		}
		else if (mouseState == MouseState.DRONE ){
			if (oldTile.getClass() == Drone.class) return;
			newTile = new Drone(oldTile);
			grid.drones.add((Drone)newTile);
		}
		else if (mouseState == MouseState.OBSTACLE ){
			if (oldTile.getClass() == Obstacle.class) return;
			newTile = new Obstacle(oldTile);
			grid.obstacles.add((Obstacle)newTile);
			if (grid.path.contains(oldTile)) grid.pathHasChanged = true;
		}
		else if (mouseState == MouseState.ENEMY ){
			if (oldTile.getClass() == Enemy.class) return;
			newTile = new Enemy(oldTile);
			grid.enemies.add((Enemy)newTile);
		}
		else {
			if ((oldTile.getClass() == Tile.class) | (oldTile.getClass() == Drone.class) | (oldTile.getClass() == Enemy.class)) {
				oldTile.isCheckpoint = true;
				grid.checkpoints.add(oldTile);
				grid.toRepaint.add(oldTile);
				grid.pathHasChanged = true;
			}
			return;
		}
		
		grid.tilesGrid[x][y] = newTile;
		grid.toRepaint.add(newTile);
		
		if (oldTile.getClass() == Drone.class) grid.drones.remove(oldTile);
		if (oldTile.getClass() == Obstacle.class) grid.obstacles.remove(oldTile);
		if (oldTile.getClass() == Enemy.class) grid.enemies.remove(oldTile);
	}
	
	// Règle le type de dessin de mouseState : e->EMPTY/EMPTY, d->EMPTY/DRONE, o->OBSTACLE/EMPTY, x->EMPTY/ENNEMY, c->CHECKPOINT/EMPTY
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
	
	// Met à jour l'affichage
	public void refreshGraphics() {
		for (Tile toRepaint : grid.toRepaint) {
			toRepaint.repaint();
		}
		grid.toRepaint = new HashSet<Tile>();
	}
	
	public void refreshGraphicsPath() {
		window.paintPath(graphics);
		for (Drone drone : grid.drones) drone.repaint();
		for (Obstacle obstacle : grid.obstacles) obstacle.repaint();
		for (Enemy enemy : grid.enemies) enemy.repaint();
		for (Tile checkpoint : grid.checkpoints) checkpoint.repaint();
		grid.toRepaint = new HashSet<Tile>();
		grid.pathHasChanged = false;		
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
