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
	public static int frameWidth = 700;
	public static int frameHeight = 700;
	
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
		
		/**initFromImage("C:\\Users\\Louis Proffit\\OneDrive\\Git\\PSC\\Codes Java\\IHM\\src\\background1.jpg");*/
		init();
		try { 
			/* Boucle d'affichage */
			while (localGraphics.frame.isActive()) {
				for (int i = 0 ; i < graphicRefreshPerPathRefresh ; i++) {
					tStart = System.currentTimeMillis();
					refreshEnemy();
					refreshDrones();
					refreshGraphics();
					tEnd = System.currentTimeMillis();
					if (tEnd - tStart < frameDuration) {
						Thread.sleep(frameDuration - (tEnd - tStart));
					}
				}
				refreshPath();
			}
		}
		catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public static void refreshGraphics() {
		localGraphics.repaint();
	}

	public static void refreshEnemy() {
		for (Enemy enemy : grid.enemies) {
			enemy.refresh();
		}
	}
	
	public static void refreshPath() {
		// Met à jour le chemin qui relie les checkpoints
		grid.path = AstarPathFinder.pathFinder(grid);
		localGraphics.repaintWithPath();
	}

	public static void init() {
		// Initialise toutes les variables
		grid = new Grid(n, m);
		initGraphics();
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
		refreshGraphics();
	}
	 
	public static void addDrone(int x, int y) {
		System.out.println("Drone added");
		Drone drone = new Drone(x, y);
		grid.drones.add(drone);
	}
	
	public static void addEnemy(int x, int y) {
		System.out.println("Enemy added");
		Enemy enemy = new Enemy(x, y);
		grid.enemies.add(enemy);
	}
	
	public static void addCheckpoint(int x, int y) {
		System.out.println("Checkpoint added");
		Checkpoint checkpoint = new Checkpoint(x, y);
		grid.checkpoints.add(checkpoint);
	}
	
	public static void addObstacle(int x, int y) {
		Obstacle obstacle = new Obstacle(x, y);
		if (grid.obstacles.add(obstacle)) System.out.println("Obstacle added :" + x + ";" + y);;
	}
	
	public static void refreshDrones() {
		for (Drone drone : grid.drones) {
			drone.move();
		}
	}
}
