package RRT;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Espace{
	
	// Taille
	
	public final int taille;
	public final int hash;
	public final HashSet<Obstacle> obstacles;
	public final Random rand;
	// Découpage et rangement des points
	
	public Box[] cutInBoxes;
	public int totalPoints = 0;
	
	// Début et fin:
	
	public boolean initialized;
	public boolean pathFound;
	
	public Point[] parcours;
	public LinkedList<LinkedList<Point>> path;
	public double ampMax;		// taille ùmaximale des liens
	
	public double activeRadius;					// rayon des points affichés lors de la recherche
	public double highlightRadius;			// rayon des points du chemin trouvé
	public double endRadius;					// rayon des deux extrémités
	
	
	
	// _______________________________________________________________CONSTRUCTEUR ET RESET________________________________________________________________
	
	public Espace() throws InterruptedException{
		
		this.initialized = false;
		this.pathFound = false;
		this.taille = RRT.taille;
		this.hash = RRT.nbBoxes;
		this.cutInBoxes = new Box[hash * hash];
		this.rand = new Random();
		this.parcours = RRT.parcours;
		this.path = new LinkedList<LinkedList<Point>>();
		this.addPoint(parcours[0], parcours[1]);
		this.obstacles = RRT.obstacles;
		this.ampMax = RRT.ampMax;
		this.activeRadius = RRT.activeRadius;
		this.highlightRadius = RRT.highlightRadius;
		this.endRadius = RRT.endRadius;
	}
	
	public void reset(int step){
		RRT.draw.clear();
		cutInBoxes = new Box[hash * hash];
		initialized = false;
		pathFound = false;
		RRT.draw.setPenColor(Color.RED);
		for (Point checkpoint : parcours) RRT.draw.circle(checkpoint.x, checkpoint.y, endRadius);
		for (Obstacle obstacle : obstacles) obstacle.draw();
	}
	
	// ____________________________________________________________GENERATION DE POINT ALEATOIRE__________________________________________________________

	
	public Point randomPoint(boolean method, Point target) { // methode=true si on veut utiliser la génération d'un point là où il y en a le moins
											// false si on le fait complétement aléatoirement
		
		// A compléter avec une loi aléatoire qui favorise la progression de l'arbre
		/*if(method) {
			double r = Math.random();
			int i = 0;
			int sum = 0;									// On tire un nombre entre 0 et 1
			while(r < sum) {								
				sum += cutInBoxes[i].pop/totalPoints;		// On découpe l'intervalle comme suit [0, n1/tot[ puis [n1/tot, n1+n2/tot[ etc
				i++;									// et trouve ainsi une case où il y a peu de points
			}
			Point p = new Point(((i/hash)+Math.random())*((double)taille/(double)hash), ((i-hash*(i/hash))+Math.random())*((double)taille/(double)hash));
			if(p.isValid()) {
				return p;
			}
			else {
				return this.randomPoint(method);
			}
		}*/
		
		
		Point p = new Point(Math.random()*taille, Math.random()*taille);
		return p;
	}
	
	// _____________________________________________________ METHODE GRAPHIQUE : AFFICHAGE DU CHEMIN _____________________________________________________
	
	public void plotPath() {
		RRT.draw.setPenColor(Color.RED);
		for (Point checkpoint : parcours) RRT.draw.circle(checkpoint.x, checkpoint.y, endRadius);
		for (Obstacle obstacle : obstacles) obstacle.draw();
		int k;
		Point P1;
		Point P2;
		for(LinkedList<Point> intermediatePath : path) {
			k = intermediatePath.size();
			if (k < 2 ) continue;
			else {
				for (int i = 0 ; i < k - 1 ; i++) {
					P1 = intermediatePath.get(i);
					P2 = intermediatePath.get(i + 1);
					RRT.draw.setPenColor(Color.GREEN);
					RRT.draw.filledCircle(P1.x, P1.y, highlightRadius);
					RRT.draw.line(P1.x, P1.y, P2.x, P2.y);
				}
			}
			
		}
	}
	
	// _____________________________________________________ METHODE PRINCIPALE : AJOUT DES POINTS ______________________________________________________

	
	public void addPoint(Point p, Point target) throws InterruptedException {
		
		int boxIndex = (int)(hash * p.x / taille) + hash * (int)(hash * p.y / taille);

		if (initialized) {
			if(cutInBoxes[boxIndex] == null) {
				cutInBoxes[boxIndex] = new Box();
				Point connection = findLinkNaive(p);
				if (! isLinkValid(p, connection)) return ;
				connection.addLink(p, ampMax);
				cutInBoxes[boxIndex].addPoint(p);
			}
			else {
				Point connection = findLinkNaive(p);
				if (! isLinkValid(p, connection)) return ;
				connection.addLink(p, ampMax);
				cutInBoxes[boxIndex].addPoint(p);
			}
			
			if(p.distance(target) < endRadius) {
				target.links.add(p);
				pathFound = true;
			}
		}
		
		else {
			initialized = true;
			cutInBoxes[boxIndex] = new Box();
			cutInBoxes[boxIndex].addPoint(p);
		}
		
		RRT.draw.setPenColor(Color.BLACK);
		RRT.draw.filledCircle(p.x, p.y, highlightRadius);
		totalPoints++;
	}
	
	// _____________________________________________ DEUX METHODES DE RECHERCHE DE LIEN LE PLUS PROCHE ___________________________________________________
	
	public LinkedList<Point> getPath(Point beginning, Point end){
		LinkedList<Point> out = new LinkedList<Point>();
		Point cursor = end;
		while (!cursor.equals(beginning)) {
			out.addLast(cursor);
			cursor = cursor.links.getFirst();
		}
		out.addLast(beginning);
		return out;
	}
	
	public boolean isLinkValid(Point P, Point Q) {
		for (Obstacle obstacle : obstacles) {
			if (obstacle.linkIsIn(P, Q)) return false;
		}
		return true;
	}
	
	// _____________________________________________ DEUX METHODES DE RECHERCHE DE LIEN LE PLUS PROCHE ___________________________________________________
	
	public Point findLinkNaive(Point p) {
		
			double dMin= (double)(taille * 1000);
			int box = 0;
			int mark = 0;
			for(int i = 0; i < hash * hash ; i++) {
				if(cutInBoxes[i] != null) {
					int k = 0;
					for(Point cursor : cutInBoxes[i].points) {
						if(p.distance(cursor) < dMin) {
							dMin = p.distance(cursor);
							box = i;
							mark = k;
						}
						k++;
					}
				}
			}
		return cutInBoxes[box].points.get(mark);
	}
	
	public Point findLinkInBox(Point p, int boxIndex) {
		double dMin = (double)taille;
		int i = 0;
		int mark = 0;
		for(Point cursor : cutInBoxes[boxIndex].points) {
			if(p.distance(cursor)<dMin) {
				dMin = p.distance(cursor);
				mark = i;
			}
			i++;
		}
		return cutInBoxes[boxIndex].points.get(mark);
	}
	
	// ____________________________________________________________ METHODES "ACTIVES" ____________________________________________________________________

	
	public void findPath(boolean method) throws InterruptedException {
		
		if(parcours.length<2) {
			System.out.println("Pas assez de points de passage");
			return;
		}

		int step = 0;
		while (step < parcours.length - 1) {
			addPoint(parcours[step], parcours[step + 1]);
			while(!pathFound) {
				addPoint(randomPoint(method, parcours[step + 1]), parcours[step + 1]);
			}
			path.add(getPath(parcours[step], parcours[step + 1]));
			plotPath();
			reset(step);
			RRT.draw.setPenColor(Color.GREEN);
			plotPath();
			step++;
		}
		
		linearizeFull();
		RRT.draw.clear();
		plotPath();
		System.out.println("End.");
	}
	
	public void linearizeOrder(int order) {
		LinkedList<LinkedList<Point>> newPath = new LinkedList<LinkedList<Point>>();
		for (LinkedList<Point> intermediatePath : path) {
			int k = intermediatePath.size();
			if (k < order) newPath.add(linearizeSegmentOrder(intermediatePath, k));
			else newPath.add(linearizeSegmentOrder(intermediatePath, order));
		}
		path = newPath;
	}
	
	public LinkedList<Point> linearizeSegmentOrder(LinkedList<Point> path, int order) {
		LinkedList<Point> newPath = new LinkedList<Point>();
		newPath.add(path.getFirst());
		int pathLength = path.size();
		if (pathLength < order) {
			return path;
		}
		int k = 0;
		LinkedList<Point> points = new LinkedList<Point>();
		for (int i = 0 ; i < order ; i++) {
			points.add(path.get(i));
		}
		while (!points.getLast().equals(path.getLast())) {;
			if (isLinkValid(points.getFirst(), points.getLast())) {
				newPath.add(points.getLast());
				if (k + 2 * order - 2  + order >= pathLength) {
					if (isLinkValid(points.getLast(), path.getLast())) {
						newPath.add(path.getLast());
						return newPath;
					}
					// On ajoute la fin du chemin, et on arr�te
					for (int j = k + order - 1 ; j < pathLength ; j++) {
						newPath.add(path.get(j));
					}
					return newPath;
				}
				else {
					// On passe au segment suivant
					points = new LinkedList<Point>();
					for (int j = 0 ; j < order ; j ++) {
						points.add(path.get(k + order - 1 + j));
					}
				}
			}
			else {
				System.out.println("anormal");
				newPath.addAll(points);
				if (k + 2 * order - 1  + order >= pathLength) {
					// On ajoute la fin du chemin, et on arr�te
					for (int j = k + order ; j < pathLength ; j++) {
						newPath.add(path.get(j));
					}
					return newPath;
				}
				else {
					// On passe au segment suivant
					points = new LinkedList<Point>();
					for (int j = 0 ; j < order ; j ++) {
						points.add(path.get(k + order + j));
					}
				}
			}
			k += 1;
		}
		newPath.add(path.getLast());
		return newPath;
	}
	
	public void linearizeFull() {
		LinkedList<LinkedList<Point>> newPath = new LinkedList<LinkedList<Point>>();
		for (LinkedList<Point> intermediatePath : path) {
			newPath.add(linearizeFullSegment(intermediatePath, 0, intermediatePath.size() - 1));
		}
		path = newPath;
	}
	
	public LinkedList<Point> linearizeFullSegment(LinkedList<Point> path, int startIndex, int endIndex) {
		LinkedList<Point> newPath = new LinkedList<Point>();
		int len = endIndex - startIndex - 1;
		if (len < 3) {
			for (int i = startIndex ; i <= endIndex ; i++) {
				newPath.add(path.get(i));
			}
			return newPath;
		}
		if (isLinkValid(path.get(startIndex), path.get(endIndex))) {
			newPath.add(path.get(startIndex));
			newPath.add(path.get(endIndex));
			return newPath;
		}
		int cut = startIndex + (int)(len / 2);
		LinkedList<Point> newPath1 = linearizeFullSegment(path, startIndex, cut);
		LinkedList<Point> newPath2 = linearizeFullSegment(path, cut + 1, endIndex);
		newPath.addAll(newPath1);
		newPath.addAll(newPath2);
		return newPath;
	}
	
}
