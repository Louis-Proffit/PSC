package Astar;

import Delaunay.Point;

// Spécification d'une case pour l'algorithme Astar
public class AstarPoint extends Point{
	
	// Attributs spécifiques à l'utilisation d'une case dans Astar
	public double g = 0; /* Estimation de la distance par rapport au point de départ */
	public double h = 0; /* Estimation de la distance par rapport au point d'arrivée */
	public double f = 0; /* Somme de g et h */
	public AstarPoint parent; /* Tuile précédente sur le chemin */
	
	public AstarPoint(double x, double y) {
		// Construit une case à partir de ses coordonnées
		super(x, y);
	}
	
	public AstarPoint(Point point) {
		// Construit une AstarPoint à partir d'une Tile
		super(point.x, point.y);
	}
	
	public AstarPoint(double x, double y, AstarPoint parent) {
		// Construit une case à partir de ses coordonnées et de son parent
		super(x, y);
		this.parent = parent;
	}
}
