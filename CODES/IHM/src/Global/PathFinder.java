package Global;

import java.util.LinkedList;

import Astar.Astar;
import Astar.AstarTile;
import Graphics.LocalGraphics;

// Classe a executer pour faire une simulation
public class PathFinder {
	
	// Dimensions en cases de la grille
	public static int n = 100;
	public static int m = 100;
	
	// Dimensions en pixels de la grille
	public static int frameWidth = 1000;
	public static int frameHeight = 1000;
	
	// Données graphiques
	public static int graphicRefreshPerPathRefresh = 10; /* Nombre de refresh graphiques avant un calcul complet des chemins */
	public static int graphicRefreshDelay = 20; /* Délai de rafraichissement graphique */
	
	//Grille
	public static Grid grid;
	
	// Fcontion main, a exectuer pour obtenir la simulation
	public static void main(String[] args) {
		/* Initialisation des données de grid */
		init(); 
		
		/* Création de l'objet graphique */
		LocalGraphics localGraphics = new LocalGraphics(n, m, frameWidth, frameHeight, grid); 
		
		/* Boucle d'affichage */
		try { 
			while (localGraphics.frame.isVisible()) {
				for (int i = 0 ; i < graphicRefreshPerPathRefresh ; i++) {
					Thread.sleep(graphicRefreshDelay);
					localGraphics.refreshGraphics();
					localGraphics.refreshDrones();
					localGraphics.refreshEnemy();
				}
				refreshPath();
				//System.out.println("Nombre de drones :" + grid.drones.size());
				//System.out.println("Nombre d'obstacles :" + grid.obstacles.size());
				//System.out.println("Nombre de checkpoints :" + grid.checkpoints.size());
				//System.out.println("Nombre d'ennemis :" + grid.ennemies.size());
				//System.out.println(grid.path.toString());
			}
		}
		catch (InterruptedException e){
			System.out.println("Fin de simulation");
		}
	}
	
	// Trouve le chemin entre le checkpoint i et i+1. Si i le dernier indice de checkpoints, entre i et 0
	public static LinkedList<Tile> intermediatePathFinder(Tile startTile, Tile endTile) {
		AstarTile astarStartTile = new AstarTile(startTile);
		AstarTile astarEndTile = new AstarTile(endTile);
		LinkedList<AstarTile> astarResult = Astar.aStar(grid, astarStartTile, astarEndTile);
		LinkedList<Tile> result = new LinkedList<Tile>();
		for (AstarTile e : astarResult) {
			result.add((Tile)e);
		}
		return result;
	}
	
	// Trouve le chemin qui relie tous les checkpoints
	public static LinkedList<Tile> pathFinder() {
		
		int numberOfCheckpoints = grid.checkpoints.size();
		
		if (numberOfCheckpoints < 2) {
			System.out.println("Pas assez de checkpoints");
			return null;
		}
		
		LinkedList<Tile> result = new LinkedList<Tile>();
		
		for (int i = 0 ; i < numberOfCheckpoints - 1; i++) {
			result.addAll(intermediatePathFinder(grid.checkpoints.get(i), grid.checkpoints.get(i + 1)));
		}
		result.addAll(intermediatePathFinder(grid.checkpoints.get(numberOfCheckpoints - 1), grid.checkpoints.get(0)));
		
		return result;
	}
	
	// Met à jour le chemin qui relie les checkpoints
	public static void refreshPath() {
		grid.path = pathFinder();
	}
	
	// Initialise toutes les variables
	public static void init() {
		grid = new Grid(n, m);
		initDrones();
		initObstacles();
		initCheckpoints();
		initEnnemies();
		initPath();
	}
	
	// Initialise le chemin
	public static void initPath() {
		grid.path = pathFinder();
	}
	
	// Initialise les drones
	public static void initDrones() {
		grid.drones = new LinkedList<Tile>();
		addDrone(5, 5);
	}
	
	// Initialise les obstacles
	public static void initObstacles() {
		grid.obstacles = new LinkedList<Tile>();
		addObstacle(63, 48);
	}
	
	// Initialise les checkpoints
	public static void initCheckpoints() {
		grid.checkpoints = new LinkedList<Tile>();
		addCheckpoint(20, 20);
		addCheckpoint(20, 80);
		addCheckpoint(80, 80);
	}
	
	// Iitialise les ennemis
	public static void initEnnemies() {
		grid.ennemies = new LinkedList<Tile>();
		addEnnemy(58, 64);
	}
	
	// Ajoute un drone à la simulation 
	public static void addDrone(int x, int y) {
		grid.drones.add(new Tile(x, y));
		grid.grid[x][y].tileContainer = TileContainer.DRONE;
	}
	
	// Ajoute un obstacle à la simulation 
	public static void addObstacle(int x, int y) {
		grid.obstacles.add(new Tile(x, y));
		grid.grid[x][y].tileType = TileType.OBSTACLE;
	}
	
	// Ajoute un checkpoint à la simulation 
	public static void addCheckpoint(int x, int y) {
		grid.checkpoints.add(new Tile(x, y));
		grid.grid[x][y].tileType = TileType.CHECKPOINT;
	}
	
	// Ajoute un ennemi à la simulation 
	public static void addEnnemy(int x, int y) {
		grid.ennemies.add(new Tile(x, y));
		grid.grid[x][y].tileContainer = TileContainer.ENNEMY;
	}
}
