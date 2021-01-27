package Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

import Global.Obstacle;

public class AstarBis {
	
	public static int iteration = 0;
	public static ArrayList<Integer> visites = new ArrayList<Integer>();

	
	// Prend en argument une liste de AstarTile qui correspond à un chemin et rend le chemin en sens inverse
		public static LinkedList<AstarTile> reverse(LinkedList<AstarTile> l) {
			
			LinkedList<AstarTile> n = new LinkedList<AstarTile>();
			int i = l.size()-1;
			
			while(i>=0) {
				n.add(l.get(i));
				i--;
			}
			
			return(n);
		}	
		
		// Transforme un chemin de AstarCase en un chemin de coordonnées de cases et les affiche, sous la forme d'une liste de [x, y]
		public static LinkedList<int[]> pathCaseToCoordinates(LinkedList<AstarTile> liste) {
			
			LinkedList<int[]> result = new LinkedList<int[]>();
			
			for (int i = 0 ; i < liste.size() ; i++) {
				result.add(new int[] {liste.get(i).x, liste.get(i).y});
				System.out.println("[" + result.getLast()[0] + ";" + result.getLast()[1] + "]");
			}
			
			return result;
		}

		public static void init(int n, int m) {
			visites.clear();
			for (int i = 0 ; i < n * m ; i++) visites.add(0);
		}
		
		// A partir d'une grille grid, d'une case start et d'une case end, trouve un chemin évitant les obstacles grâce à l'algorithme Astar
		public static LinkedList<AstarTile> aStar(Global.Grid grid, AstarTile start, AstarTile end) {
			int n = Test.n;
			int m = Test.m;
			init(n, m);
			iteration ++;
			
			PriorityQueue<AstarTile> open = new PriorityQueue<AstarTile>();
			
			open.add(start);
			
			while (open.size() > 0) {
				
				AstarTile current = open.poll();
				
				open.remove(current);
				visites.set(n * current.x + current.y, iteration);
				
				if (current.equals(end)) {
					LinkedList<AstarTile> path = new LinkedList<AstarTile>();
					while (current != null) {
						path.add(current);
						current = current.parent;
					}
					return reverse(path);
				}
				
				LinkedList<AstarTile> children = new LinkedList<AstarTile>();
				
				for (int x = -1 ; x < 2; x++) {
					for (int y = -1 ; y < 2 ; y++) {
						int new_x = current.x + x;
						int new_y = current.y + y;
						if (new_x > -1 & new_x < n & new_y > -1 & new_y < m) {
							if (!grid.isObstacle(new_x, new_y)) {
								AstarTile new_case = new AstarTile(new_x, new_y, current);
								children.add(new_case);
							}
						}
					}
				}
			
				checkPoint:
				for (int i = 0; i < children.size() ; i++) {
					AstarTile child = children.get(i);
					if (visites.get(n*child.x + child.y) == iteration) {
						continue checkPoint;
					}
				
					child.g = current.g + child.distanceToCase(current);
					child.h = child.distanceToCase(end);
					child.f = child.h + child.g;
				
					open.add(child);
				}
			}
			return new LinkedList<AstarTile>();
		}
	}
