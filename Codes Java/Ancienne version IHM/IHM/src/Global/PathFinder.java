package Global;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import Astar.AstarPathFinder;
//import Draw.DrawInterface;
import Graphics.LocalGraphics;

public class PathFinder {
	
	// Dimensions en cases de la grille
	public static int n = 50;
	public static int m = 50;
	
	// Dimensions en pixels de la grille
	public static int frameWidth = 1000;
	public static int frameHeight = 1000;
	
	// Données graphiques
	public static int graphicRefreshPerPathRefresh = 10; /* Nombre de refresh graphiques avant un calcul complet des chemins */
	public static int frameDuration = 100; /* Délai de rafraichissement graphique */
	public static int toleranceForImage = -1000000;
	
	//Grille
	public static Grid grid;
	
	// Graphique
	public static LocalGraphics localGraphics;
	//public static DrawInterface drawInterface;
	
	
	public static void main(String[] args) {
		// Fonction main, a éxécuter pour obtenir la simulation
		long tStart = 0;
		long tEnd = 0;
		
		initFromImage("C:\\Users\\Louis Proffit\\eclipse-workspace\\IHM\\src\\background1.jpg");
		try { 
			/* Boucle d'affichage */
			while (localGraphics.frame.isActive()) {
				for (int i = 0 ; i < graphicRefreshPerPathRefresh ; i++) {
					tStart = System.currentTimeMillis();
					refreshGraphics();
					refreshEnemy();
					tEnd = System.currentTimeMillis();
					if (tEnd - tStart < frameDuration) {
						Thread.sleep(frameDuration - (tEnd - tStart));
					}
				}
				if (grid.pathHasChanged) {
					refreshPath();
				}
			}
		}
		catch (InterruptedException e){
			localGraphics.refreshGraphics();
			//drawInterface.refreshGraphics();
			System.out.println(e.getMessage());
		}
	}
	
	public static void refreshGraphics() {
		// Met à jour les graphiques
		localGraphics.refreshGraphics();
		//drawInterface.refreshGraphics();
	}

	public static void refreshEnemy() {
		for (Enemy enemy : grid.enemies) {
			enemy.refresh();
		}
	}
	
	public static LinkedList<Tile> pathFinder(Grid grid){
		return AstarPathFinder.pathFinder(grid);
	}
	
	public static void refreshPath() {
		// Met à jour le chemin qui relie les checkpoints
		grid.path = pathFinder(grid);
		localGraphics.refreshGraphicsPath();
		//drawInterface.refreshGraphics();
	}

	public static void init() {
		// Initialise toutes les variables
		grid = new Grid(n, m);
		initGraphics();
		refreshGraphics();
	}
	
	public static void initFromImage(String fileName) {
		try {
			File file = new File(fileName);
			System.out.println(file.getAbsolutePath());
			BufferedImage image = ImageIO.read(file);
			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();
			n = imageWidth;
			m = imageHeight;
			grid = new Grid(n, m);
			for (int column = 0 ; column < imageWidth ; column++)
				for (int line = 0 ; line < imageHeight ; line++) {
					System.out.println(image.getRGB(column,  line));
					if (image.getRGB(column,  line) < toleranceForImage) {
						addObstacle(column, line);
					}
				}			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initGraphics();
		refreshGraphics();
	}
	
	public static void initGraphics() {
		// Initialise les graphiques
		localGraphics = new LocalGraphics(n, m, frameWidth, frameHeight, grid);
		//drawInterface = new DrawInterface(n, m, frameWidth, frameHeight, grid);
	}
	 
	public static void addDrone(int x, int y) {
		// Ajoute un drone à la simulation
		grid.toRepaint.remove(grid.tilesGrid[x][y]);
		Drone drone = new Drone(x, y);
		grid.tilesGrid[x][y] = drone;
		grid.drones.add(drone);
		grid.toRepaint.add(drone);
	}
	
	public static void addObstacle(int x, int y) {
		// Ajoute un obstacle à la simulation
		grid.toRepaint.remove(grid.tilesGrid[x][y]);
		Obstacle obstacle = new Obstacle(x, y);
		grid.tilesGrid[x][y] = obstacle;
		grid.obstacles.add(obstacle);
		grid.toRepaint.add(obstacle);
	}
	
	public static void addCheckpoint(int x, int y) {
		// Ajoute un checkpoint à la simulation 
		Tile tile = grid.tilesGrid[x][y];
		tile.isCheckpoint = true;
		grid.checkpoints.add(tile);
	}
	
	public static void addEnemy(int x, int y) {
		// Ajoute un ennemi à la simulation 
		grid.toRepaint.remove(grid.tilesGrid[x][y]);
		Enemy enemy = new Enemy(x, y);
		grid.tilesGrid[x][y] = enemy;
		grid.enemies.add(enemy);
		grid.toRepaint.add(enemy);
	}
}
