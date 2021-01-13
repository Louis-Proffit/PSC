package Global;

import java.util.LinkedList;

// Objet grille
public class Grid{
	
	// Ensemble de cases Tile
	public Tile[][] grid;
	
	// Listes des �l�ments pr�sents sur la grille
	public LinkedList<Tile> drones;
	public LinkedList<Tile> obstacles;	
	public LinkedList<Tile> checkpoints;	
	public LinkedList<Tile> ennemies;	
	
	// Chemin de cases contig�es qui relie les checkpoints
	public LinkedList<Tile> path;
	
	// Constructeur � partir d'une taille (n, m)
	public Grid(int n, int m){
		grid = new Tile[n][m];
		for (int i = 0 ; i < n ; i++) {
			for (int j = 0 ; j < m ; j++) {
				grid[i][j] = new Tile(i, j);
			}
		}
	}
}
