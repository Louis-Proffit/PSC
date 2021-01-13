package Global;

import java.util.HashSet;
import java.util.LinkedList;

// Objet grille
public class Grid{

	// Ensemble de cases Tile
	public Tile[][] tilesGrid;

	// Dimensions
	public int n;
	public int m;

	// Listes des éléments présents sur la grille
	public HashSet<Drone> drones = new HashSet<Drone>();
	public HashSet<Obstacle> obstacles = new HashSet<Obstacle>();	
	public LinkedList<Tile> checkpoints = new LinkedList<Tile>();
	public HashSet<Enemy> enemies = new HashSet<Enemy>();	

	// Chemin de cases contigües qui relie les checkpoints
	public LinkedList<Tile> path = new LinkedList<Tile>();
	public boolean pathReady;

	// Liste des cases à faire évoluer graphiquement
	public HashSet<Tile> toRepaint = new HashSet<Tile>();

	// Indiquent les changement sur la grille lors de la dernière frame
	public boolean pathHasChanged;
	public boolean graphicsHasChanged;


	public Grid(int n, int m){
		// Constructeur à partir d'une taille (n, m)
		this.n = n;
		this.m = m;
		tilesGrid = new Tile[n][m];
		Tile tile = new Tile(0, 0);
		for (int i = 0 ; i < n ; i++) {
			for (int j = 0 ; j < m ; j++) {
				tile = new Tile(i, j);
				tilesGrid[i][j] = tile;
				toRepaint.add(tile);
			}
		}
		pathHasChanged = false;
		graphicsHasChanged = false;
		pathReady = false;
	}

	@Override
	public String toString() {
		String S = "";
		for (int i = 0 ; i < n ; i++) {
			for (int j = 0 ; j < m ; j++) {
				S += " : " + tilesGrid[i][j].toString() + " : " + tilesGrid[i][j].getClass().toString();
			}
		}
		return S;
	}
}
