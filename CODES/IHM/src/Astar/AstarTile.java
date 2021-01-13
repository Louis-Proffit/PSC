package Astar;

import Global.Tile;

// Spécification d'une case pour l'algorithme Astar
public class AstarTile extends Global.Tile{
	
	// Attributs spécifiques à l'utilisation d'une case dans Astar
	public double g = 0; /* Estimation de la distance par rapport au point de départ */
	public double h = 0; /* Estimation de la distance par rapport au point d'arrivée */
	public double f = 0; /* Somme de g et h */
	public AstarTile parent; /* Tuile précédente sur le chemin */
	
	// Construit une case à partir de ses coordonnées
	public AstarTile(int x, int y) {
		super(x, y);
	}
	
	// Construit une AstarTile à partir d'une Tile
	public AstarTile(Tile tile) {
		super(tile.x, tile.y);
	}
	
	// Construit une case à partir de ses coordonnées et de son parent
	public AstarTile(int x, int y, AstarTile parent) {
		super(x, y);
		this.parent = parent;
	}
}
