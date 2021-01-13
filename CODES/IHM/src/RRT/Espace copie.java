import java.awt.Color;
import java.util.LinkedList;

public class Espace extends RTT{
	
	// Taille
	
	final int taille;
	final int hash;
	
	// Découpage et rangement des points
	
	Box[] cutInBoxes;
	int totalPoints=0;
	
	// Obstacles dans la progression:
	
	LinkedList<Obstacle> obstacles;
	
	// Début et fin:
	
	boolean initialized;
	boolean pathFound;
	
	
	
	
	// __________________________________________________________________________________________________________________________________________________

	
	Espace(Point start, Point end, int t, int n){
		
		taille=t;
		draw.setXscale(0, taille);
		draw.setYscale(0, taille);				// adapte la taille de la fenetre d'affichage à la taille de l'espace
		
		hash=n;
		cutInBoxes=new Box[hash*hash];

		initialized=false;
		pathFound=false;
		
		this.addPoint(start);
		draw.setPenColor(Color.RED);
		draw.filledCircle(start.x, start.y, endRadius);
		draw.filledCircle(end.x, end.y, endRadius);
		draw.setPenColor(Color.BLACK);

		obstacles=new LinkedList<Obstacle>();
	}
	
	// __________________________________________________________________________________________________________________________________________________

	
	void addObstacle(TypeObstacle type, Point center, double radius) {
		Obstacle obs = new Obstacle(type, center, radius);
		obs.draw();
	}
	
	// __________________________________________________________________________________________________________________________________________________

	
	boolean isValid(Point p) {

		/*for(Obstacle obs: obstacles) {
			if(obs.isIn(p)) {
				return false;
			}
		}*/
		return true;
	}
	
	// __________________________________________________________________________________________________________________________________________________

	
	Point randomPoint(boolean method) { // methode=true si on veut utiliser la génération d'un point là où il y en a le moins
											// false si on le fait complétement aléatoirement
		
		// A compléter avec une loi aléatoire qui favorise la progression de l'arbre
		if(method) {
			double r=Math.random();
			int i=0;
			int sum=0;									// On tire un nombre entre 0 et 1
			while(r<sum) {								
				sum+=cutInBoxes[i].pop/totalPoints;		// On découpe l'intervalle comme suit [0, n1/tot[ puis [n1/tot, n1+n2/tot[ etc
				i++;									// et trouve ainsi une case où il y a peu de points
			}
			Point p=new Point(((i/hash)+Math.random())*((double)taille/(double)hash), ((i-hash*(i/hash))+Math.random())*((double)taille/(double)hash));
			if(this.isValid(p)) {
				return p;
			}
			else {
				return this.randomPoint(method);
			}
		}
		// 
		else {
			Point p=new Point(Math.random()*taille, Math.random()*taille);
			if(this.isValid(p)) {
				return p;
			}
			return this.randomPoint(method);
		}
	}
	
	// __________________________________________________________________________________________________________________________________________________

	
	static double distance(Point p1, Point p2) {
		return Math.sqrt(Math.pow((p1.x-p2.x),2)+Math.pow((p1.y-p2.y),2));
	}
	
	// _____________________________________________________ METHODE PRINCIPALE : AJOUT DES POINTS ______________________________________________________

	
	void addPoint(Point p) {
		
		int indice = (((int)(hash*p.x))/taille)+hash*(((int)(hash*p.y))/taille);

		draw.setPenColor(Color.BLACK);
		if(initialized) {
			
			if(cutInBoxes[indice]==null) {
				cutInBoxes[indice]=new Box();
				Point.addLink(p, findLinkNaive(p), ampMax);
				cutInBoxes[indice].addPoint(p);
			}
			else {
				Point.addLink(p, findLinkInBox(p, indice), ampMax);
				cutInBoxes[indice].addPoint(p);
			}
			
			if(distance(p, end)<=ampMax) {
				p.links.add(end);
				end.links.add(p);
				pathFound=true;
			}
		}
		else {
			initialized=true;
			cutInBoxes[indice]=new Box();
			cutInBoxes[indice].addPoint(p);
		}
		draw.filledCircle(p.x, p.y, 0.005);
		totalPoints++;
	}
	
	// __________________________________________ DEUX METHODES DE RECHERCHE DE LIEN LE PLUS PROCHE ___________________________________________________

	
	Point findLinkNaive(Point p) {
		
			double dMin=(double)taille*1000;
			int box=0;
			int mark=0;
			for(int i=0; i<hash*hash; i++) {
				if(cutInBoxes[i]!=null) {
					int k=0;
					for(Point cursor : cutInBoxes[i].points) {
						if(distance(cursor, p)<dMin) {
							dMin=distance(cursor, p);
							box=i;
							mark=k;
						}
						k++;
					}
				}
			}
		return cutInBoxes[box].points.get(mark);
	}
	
	Point findLinkInBox(Point p, int indice) {
		

			double dMin=(double)taille;
			int i=0;
			int mark=0;
			for(Point cursor : cutInBoxes[indice].points) {
				if(distance(cursor, p)<dMin) {
					dMin=distance(cursor, p);
					mark=i;
				}
				i++;
			}
			return cutInBoxes[indice].points.get(mark);

	}
	
	// ____________________________________________________________ METHODE "ACTIVE" ______________________________________________________________

	
	void findPath() {
		while(!pathFound) {
			this.addPoint(this.randomPoint(false));
		}
		highlight();
		System.out.println("End.");
	}
}
