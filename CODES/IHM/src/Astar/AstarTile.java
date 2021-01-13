package Astar;

import Global.Tile;

// Sp�cification d'une case pour l'algorithme Astar
public class AstarTile extends Global.Tile{
	
	// Attributs sp�cifiques � l'utilisation d'une case dans Astar
	public double g = 0; /* Estimation de la distance par rapport au point de d�part */
	public double h = 0; /* Estimation de la distance par rapport au point d'arriv�e */
	public double f = 0; /* Somme de g et h */
	public AstarTile parent; /* Tuile pr�c�dente sur le chemin */
	
	// Construit une case � partir de ses coordonn�es
	public AstarTile(int x, int y) {
		super(x, y);
	}
	
	// Construit une AstarTile � partir d'une Tile
	public AstarTile(Tile tile) {
		super(tile.x, tile.y);
	}
	
	// Construit une case � partir de ses coordonn�es et de son parent
	public AstarTile(int x, int y, AstarTile parent) {
		super(x, y);
		this.parent = parent;
	}
}
