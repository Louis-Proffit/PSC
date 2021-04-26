package RRT;

import java.util.LinkedList;

// L'espace e est découpé en plusieurs cases de taille égale pour faciliter le stockage et le a recherche du plus proche voisin

public class Box{
	
	LinkedList<Point> points;
	int pop;
	
	Box(){
		points=new LinkedList<Point>();
		pop=0;
	}
	
	void addPoint(Point p) {
		points.add(p);
		pop++;
	}
	
	int getPop() {
		return pop;
	}

}