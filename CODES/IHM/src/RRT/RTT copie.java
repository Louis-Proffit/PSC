import java.awt.Color;



/*
 * 
 * 	Classe abstraite RTT est la classe "globale" du programme. Elle contient la fonction main à executer pour afficher le rendu
 * 
 * 	Les classes Espace, Point, Box, Obstacle sont des sous-clesses de RTT, cette structure est essentiel pour l'affichage. L'affichage prend des instructions 
 * dans les différentes classes du programme et doit donc être commun à toutes les classes.
 * 
 */

public abstract class RTT{
	
	static final Draw draw = new Draw(); // création de l'affichage
	static Point start;					// point de départ
	static Point end; 					// point à relier au point de départ
	
	final static double ampMax = 2;		// taille ùmaximale des liens
	
	final double activeRadius = 0.1;					// rayon des points affichés lors de la recherche
	final static double highlightRadius = 0.5;			// rayon des points du chemin trouvé
	final static double endRadius = 1;					// rayon des deux extrémités
	
	static void highlight() {
		
		// affiche le chemin trouvé
		
		
		draw.setPenColor(Color.green);
		Point cursor=end;
		while(cursor!=start) {
			draw.filledCircle(end.x, end.y, endRadius);
			draw.filledCircle(cursor.x, cursor.y, highlightRadius);
			draw.line(cursor.x, cursor.y, cursor.links.getFirst().x, cursor.links.getFirst().y);
			cursor = cursor.links.getFirst();
			draw.filledCircle(start.x, start.y, endRadius);
			
		}
	}
	
	public static void main(String args[]) {
		start = new Point(95,95);
		end =  new Point(20,20);
		int nbBoxes=4;
		int taille=100;
		
		// Création de l'espace
		Espace e= new Espace(start, end, taille, nbBoxes);
		// Lancememnt
		e.findPath();
	}
}