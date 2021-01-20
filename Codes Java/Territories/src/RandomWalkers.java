import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JFrame;

public class RandomWalkers {
	
	final static boolean comments = false;
	final static boolean showPoints = true;
	final static boolean showHomeRange = true;
	
	final static moveType type = moveType.RANDOMWALK;
	final static double radius = 0.5;
	
	static int numberOfWalkers = 25;
	final static int activeScentTime = 500;				
	final static int rounds = 5000;
	static int time = 0;
	final static int mapSize = 100;
	final static Map map = new Map(mapSize);
	
	static HashMap<Integer, Walker>  walkers = new HashMap<Integer, Walker>();
	
	
	/*
	 * ______________________________________________________________________________________________________________________________________________________
	 */
	
	static void init() {
		map.draw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		if(comments)System.out.println("Coordonées initiales: ");
		
		for(int i=1; i<=numberOfWalkers; i++) {
			walkers.put(i, new Walker(Math.floor(Math.random()*mapSize), Math.floor(Math.random()*mapSize)));
			walkers.get(i).territory.put(0, walkers.get(i).toPoint());
			
			if(comments)System.out.println(walkers.get(i).toString());
			if(showPoints){
				map.draw.setPenColor(walkers.get(i).color);
				map.draw.filledCircle(walkers.get(i).x, walkers.get(i).y, radius);
			}
		}
		
		if(comments)System.out.println("");
	}
	
	
	/*
	 * ______________________________________________________________________________________________________________________________________________________
	 */
	
	
	static void oneRound() {
		time++;
		if(comments)System.out.println("Coordonées round "+time);
		
		/*
		 * 		On enlève les marques trop anciennes pour libérer ces zones
		 */
		
		if(time>activeScentTime-1) {
			for(Walker w : walkers.values()) {
				Point toErase = w.territory.remove(time-activeScentTime);
				
				if(showPoints) {
					map.draw.setPenColor(Color.WHITE);
					map.draw.filledCircle(toErase.x, toErase.y, radius);
				}
				
			}
		}
		
		/*
		 * 		Chaque walker bouge sur le territoire qui lui est accessible
		 */
		
		for(Walker w : walkers.values()) {
			Point move = w.move(type);
			while(pointIsInTerritory(w, move) || !map.contains(move)) {
				move = w.move(type);
			}
			w.moveTo(move);
			w.territory.put(time, move);
			
			
			if(comments)System.out.println(move.toString());
			if(showPoints){
				map.draw.setPenColor(w.color);
				map.draw.filledCircle(w.x, w.y, radius);
			}
			
		}

		if(comments)System.out.println("");
		
		/*
		 * 		Si oui, on affiche les territoires en utilisant les plus petits polygones convexes englobant les territoires
		 */
		
		if(showHomeRange && time%50==0 && time!=0) {
			for(Walker w : walkers.values()) {
				map.draw.setPenColor(w.color);
				ConvexHull convexhull = new ConvexHull(w.territory.values());
				LinkedList<Point> hull = convexhull.process();
				Point previous = hull.pop();
				while(!hull.isEmpty()) {
					map.draw.line(previous.x, previous.y, hull.getFirst().x, hull.getFirst().y);
					previous = hull.pop();
				}
			}
		}
	}
	
	/*
	 * ______________________________________________________________________________________________________________________________________________________
	 */
	
	static boolean pointIsInTerritory(Walker w0, Point p) {
		for(Walker w: walkers.values()) {
			if(w!=w0) {
				if(w.territoryContains(type, p)) return true;
			}
		}
		return false;
	}
	
	/*
	 * ______________________________________________________________________________________________________________________________________________________
	 */
	
	/*
	 * 		Pas utile pour l'instant
	 * 
	 * static void addWalker(Walker w) {
		numberOfWalkers++;
		w.territory.put(time, w.toPoint());
		walkers.put(numberOfWalkers, w);
	}
	
	/*
	 * ______________________________________________________________________________________________________________________________________________________
	 */
	
	/*
	
	static void removeWalker(int i) {
		if(walkers.containsKey(i)) {
			walkers.remove(i);
		}
		numberOfWalkers--;
	}
	*/
	
	
	/*
	 * ______________________________________________________________________________________________________________________________________________________
	 */
	
	
	
	static void run() {
		for(int i=0; i<rounds; i++) {
			oneRound();
		}
	}
	
	/*
	 * ______________________________________________________________________________________________________________________________________________________
	 */
	
	public static void main(String args[]) {
		init();
		run();
	}
}

