package Astar;

import java.util.LinkedList;
import Global.TileType;

// Classe de l'algorithme Astar
public class Astar {
	
	// Prend en argument une liste de AstarCase qui correspond à un chemin et rend le chemin en sens inverse
	public static LinkedList<AstarTile> reverse(LinkedList<AstarTile> l) {
		
		LinkedList<AstarTile> n = new LinkedList<AstarTile>();
		int i = l.size()-1;
		
		while(i>=0) {
			n.add(l.get(i));
			i--;
		}
		
		return(n);
	}	
	
	// Transforme un chemin de AstarCase en un chemin de coordonnées de cases, sous la forme d'une liste de [x, y]
	public static LinkedList<int[]> pathCaseToCoordinates(LinkedList<AstarTile> liste) {
		
		LinkedList<int[]> result = new LinkedList<int[]>();
		
		for (int i = 0 ; i < liste.size() ; i++) {
			result.add(new int[] {liste.get(i).x, liste.get(i).y});
			System.out.println("[" + result.getLast()[0] + ";" + result.getLast()[1] + "]");
		}
		
		return result;
	}

	// A partir d'une grille grid, d'une case start et d'une case end, trouve un chemin évitant les obstacles grâce à l'algorithme Astar
	public static LinkedList<AstarTile> aStar(Global.Grid grid, AstarTile start, AstarTile end) {
		int n = Global.PathFinder.n;
		int m = Global.PathFinder.m;
		
		LinkedList<AstarTile> open = new LinkedList<AstarTile>();
		LinkedList<AstarTile> closed = new LinkedList<AstarTile>();

		open.add(start);
		
		while (open.size() > 0) {
			
			AstarTile current = open.get(0);
			int index = 0;
			for (int i = 0 ; i < open.size() ; i++) {
				AstarTile item = open.get(i);
				if (item.f < current.f) {
					current = item;
					index = i;
				}
			}
			
			open.remove(index);
			closed.add(current);
			
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
						if (grid.grid[new_x][new_y].tileType != TileType.OBSTACLE) {
							AstarTile new_case = new AstarTile(new_x, new_y, current);
							children.add(new_case);
						}
					}
				}
			}
		
			checkPoint:
			for (int i = 0; i < children.size() ; i++) {
				AstarTile child = children.get(i);
				for (int j = 0 ; j < closed.size() ; j++) {
					if (child.equals(closed.get(j))) {
						continue checkPoint;
					}
				}
			
				child.g = current.g + child.distanceToCase(current);
				child.h = child.distanceToCase(end);
				child.f = child.h + child.g;
			
				for (int j = 0 ; j < open.size() ; j++) {
					if (child.equals(open.get(j)) & child.g > open.get(j).g) {
						continue checkPoint;
					}
				}
			
				open.add(child);
			}
		}
		return null;
	}
}
