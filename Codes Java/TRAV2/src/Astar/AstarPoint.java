package Astar;

import Delaunay.Point;

// Sp�cification d'une case pour l'algorithme Astar
public class AstarPoint extends Point{
	
	// Attributs sp�cifiques � l'utilisation d'une case dans Astar
	public double g = 0; /* Estimation de la distance par rapport au point de d�part */
	public double h = 0; /* Estimation de la distance par rapport au point d'arriv�e */
	public double f = 0; /* Somme de g et h */
	public AstarPoint parent; /* Tuile pr�c�dente sur le chemin */
	
	public AstarPoint(double x, double y) {
		// Construit une case � partir de ses coordonn�es
		super(x, y);
	}
	
	public AstarPoint(Point point) {
		// Construit une AstarPoint � partir d'une Tile
		super(point.x, point.y);
	}
	
	public AstarPoint(double x, double y, AstarPoint parent) {
		// Construit une case � partir de ses coordonn�es et de son parent
		super(x, y);
		this.parent = parent;
	}
}
