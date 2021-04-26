package RRT;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedList;



/*
 * 
 * 	Classe abstraite RTT est la classe "globale" du programme. Elle contient la fonction main à executer pour afficher le rendu
 * 
 * 	Les classes Espace, Point, Box, Obstacle sont des sous-clesses de RTT, cette structure est essentiel pour l'affichage. L'affichage prend des instructions 
 * dans les différentes classes du programme et doit donc être commun à toutes les classes.
 * 
 */

public class RRT{
	
	public static Draw draw = new Draw(); // création de l'affichage
	
	public static Point[] parcours;			// points à relier
	public static HashSet<Obstacle> obstacles;
	
	public static double ampMax = 15;		// taille ùmaximale des liens
	
	public static double activeRadius = 0.1;					// rayon des points affichés lors de la recherche
	public static double highlightRadius = 1;			// rayon des points du chemin trouvé
	public static double endRadius = 60;					// rayon des deux extrémités
	public static int nbBoxes = 500;
	public static int taille = 1000;
	
	public static void main(String args[]) throws InterruptedException {
		
		// Initialisation
		init();
		
		// Cr�ation de l'espace
		Espace espace = new Espace();
		
		// Lancememnt
		espace.findPath(false);
	}
	
	public static void addObstacle(TypeObstacle type, Point center, double radius) {
		Obstacle obstacle = new Obstacle(type, center, radius);
		obstacles.add(obstacle);
		obstacle.draw();
	}
	
	public static void init() {
		parcours =  new Point[] {new Point(900,900), new Point(200,300), new Point(100,800), new Point(500, 900), new Point(900, 900)};
		obstacles = new HashSet<Obstacle>();
		addObstacle(TypeObstacle.CIRCLE, new Point(500, 550), 20);
		addObstacle(TypeObstacle.CIRCLE, new Point(400, 600), 50);
		addObstacle(TypeObstacle.CIRCLE, new Point(150, 500), 100);
		addObstacle(TypeObstacle.CIRCLE, new Point(500, 400), 100);
		addObstacle(TypeObstacle.CIRCLE, new Point(800, 700), 100);
		addObstacle(TypeObstacle.CIRCLE, new Point(700, 900), 100);
		draw.setXscale(0, taille);
		draw.setYscale(0, taille);
		draw.setPenColor(Color.RED);
		for (Point checkpoint : parcours) RRT.draw.circle(checkpoint.x, checkpoint.y, endRadius);
		for (Obstacle obstacle : obstacles) obstacle.draw();
	}
	
	public static LinkedList<Point> highlight(int beginning, int end) {
		LinkedList<Point> out = new LinkedList<Point>();
		draw.setPenColor(Color.green);
		Point cursor = parcours[end];
		while(cursor != parcours[beginning]) {
			out.add(cursor);
			draw.filledCircle(cursor.x, cursor.y, highlightRadius);
			draw.line(cursor.x, cursor.y, cursor.links.getFirst().x, cursor.links.getFirst().y);
			cursor = cursor.links.getFirst();
		}
		return out;
	}

}