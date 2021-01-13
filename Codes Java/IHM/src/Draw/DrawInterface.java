package Draw;

import java.awt.Color;
import java.awt.Point;
import java.util.HashSet;

import Global.Drone;
import Global.Enemy;
import Global.Grid;
import Global.MouseState;
import Global.Obstacle;
import Global.Tile;

public class DrawInterface implements DrawListener{

	// Objet graphique
	public Draw draw = new Draw();

	// Nombre de cases en abscisses et en ordonnées
	public int n;
	public int m;

	// Largeur et hauteur en pixels de la fênetre
	public int frameWidth;
	public int frameHeight;

	// Largeur et hauteur en pixels d'une case
	public float tileWidth;
	public float tileHeight;

	//Grille
	public Grid grid;

	// Etat de la souris, conditionne l'action à effectuer si on clique
	public MouseState mouseState;

	public DrawInterface(int n, int m, int frameWidth, int frameHeight, Grid grid) {
		// Construit un affichage de n*m cases, tileWidth*tileHeight pixels, à partir d'une grille grid
		this.draw.setXscale(0, frameWidth);
		this.draw.setYscale(0, frameHeight);
		this.draw.addListener(this);
		this.n = n;
		this.m = m;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.tileWidth = frameWidth / n;
		this.tileHeight = frameHeight / m;
		this.grid = grid;
		mouseState = MouseState.EMPTY;
	}
	
	public int[] getTileFromPoint(Point P) {
		// Renvoie les coordonnées en nombre de cases d'un point. Fonction auxilliaire pour la méthode onMouseClicked
		int x = P.x - 8;
		int y = P.y - 31;
		int i = (int)(x / tileWidth);
		int j = (int)(y / tileHeight);
		i = Math.min(Math.abs(i), n - 1);
		j = Math.min(Math.abs(j), m - 1);
		return new int[] {i, j};
	}
	
	public void refreshGraphics() {
		// Met à jour l'affichage
		for (Tile toRepaint : grid.toRepaint) {
			toRepaint.repaint();
		}
		grid.toRepaint = new HashSet<Tile>();
	}

	public void refreshGraphicsPath() {
		paintPath();
		for (Drone drone : grid.drones) drone.repaint();
		for (Obstacle obstacle : grid.obstacles) obstacle.repaint();
		for (Enemy enemy : grid.enemies) enemy.repaint();
		for (Tile checkpoint : grid.checkpoints) checkpoint.repaint();
		grid.toRepaint = new HashSet<Tile>();
		grid.pathHasChanged = false;		
	}

	public void paintDrone(Drone drone) {
		draw.setPenColor(Color.GREEN);
		draw.filledEllipse(drone.x * tileWidth, drone.y * tileHeight, tileWidth / 2, tileHeight / 2);
		if (drone.isCheckpoint) paintCheckpoint(drone);
	}

	public void paintObstacle(Obstacle obstacle) {
		draw.setPenColor(Color.BLACK);
		draw.filledRectangle(obstacle.x * tileWidth, obstacle.y * tileHeight, tileWidth / 2, tileHeight / 2);
		if (obstacle.isCheckpoint) paintCheckpoint(obstacle);
	}

	public void paintEnemy(Enemy enemy) {
		draw.setPenColor(Color.RED);
		draw.filledEllipse(enemy.x * tileWidth, enemy.y * tileHeight, tileWidth / 2, tileHeight / 2);
		if (enemy.isCheckpoint) paintCheckpoint(enemy);
	}

	public void paintEmpty(Tile tile) {
		draw.setPenColor(Color.WHITE);
		draw.filledRectangle(tile.x * tileWidth, tile.y * tileHeight, tileWidth / 2, tileHeight / 2);
		if (tile.isCheckpoint) paintCheckpoint(tile);
	}

	public void paintCheckpoint(Tile tile) {
		draw.setPenColor(Color.BLACK);
		draw.filledEllipse((tile.x + 0.5) * tileWidth, (tile.y + 0.5) * tileHeight, 0.25 * tileWidth, 0.25 * tileWidth);
	}

	public void paintPath() {
		System.out.println("bla");
		draw.setPenColor(Color.WHITE);
		draw.filledRectangle(0, 0, frameWidth, frameHeight);
		draw.setPenColor(Color.BLACK);
		int n = grid.path.size();
		if (n <= 1) return;
		for (int i = 0 ; i < n - 1 ; i++) {
			paintLine(grid.path.get(i), grid.path.get(i + 1));
		}
		paintLine(grid.path.get(0), grid.path.getLast());
	}

	public void paintLine(Tile tile1, Tile tile2) {
		int x1 = (int)((tile1.x + 0.5)* tileWidth);
		int y1 = (int)((tile1.y + 0.5) * tileHeight);
		int x2 = (int)((tile2.x + 0.5) * tileWidth);
		int y2 = (int)((tile2.y + 0.5) * tileHeight);
		draw.line(x1,  y1,  x2,  y2);
	}

	@Override
	public void mousePressed(double x, double y) {
	}

	@Override
	public void mouseDragged(double x, double y) {
	}

	@Override
	public void mouseReleased(double x, double y) {
	}

	@Override
	public void mouseClicked(double x, double y) {
		// Change la grille en fonction des deux paramètres writingType et writingContaine
		int i = (int)(x / frameWidth);
		int j = (int)(y / frameHeight);
		Tile oldTile = grid.tilesGrid[i][j];
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

		grid.tilesGrid[i][j] = newTile;
		grid.toRepaint.add(newTile);

		if (oldTile.getClass() == Drone.class) grid.drones.remove(oldTile);
		if (oldTile.getClass() == Obstacle.class) grid.obstacles.remove(oldTile);
		if (oldTile.getClass() == Enemy.class) grid.enemies.remove(oldTile);

	}

	@Override
	public void keyTyped(char c) {
		// Règle le type de dessin de mouseState : e->EMPTY/EMPTY, d->EMPTY/DRONE, o->OBSTACLE/EMPTY, x->EMPTY/ENNEMY, c->CHECKPOINT/EMPTY
		// Quitter
		if (c == 'q') System.exit(0);
		// Empty : gomme
		else if (c == 'e') mouseState = MouseState.EMPTY;
		// Drone
		else if (c == 'd') mouseState = MouseState.DRONE;
		// Obstacle
		else if (c == 'o') mouseState = MouseState.OBSTACLE;
		// Checkpoint
		else if (c == 'c') mouseState = MouseState.CHECKPOINT;
		// Ennemi
		else if (c == 'x') mouseState = MouseState.ENEMY;
	}

	@Override
	public void keyPressed(int keycode) {
	}

	@Override
	public void keyReleased(int keycode) {
	}
}


