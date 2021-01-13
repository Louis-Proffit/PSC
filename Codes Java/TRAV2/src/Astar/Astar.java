package Astar;

import java.util.LinkedList;

// Classe de l'algorithme Astar
public class Astar {
	
	public static LinkedList<AstarPoint> reverse(LinkedList<AstarPoint> l) {
		// Prend en argument une liste de AstarCase qui correspond à un chemin et rend le chemin en sens inverse
		LinkedList<AstarPoint> n = new LinkedList<AstarPoint>();
		int i = l.size()-1;
		while(i>=0) {
			n.add(l.get(i));
			i--;
		}
		return n;
	}

	public static LinkedList<AstarPoint> aStar(AstarGraph graph, AstarPoint startPoint, AstarPoint endPoint) {
		// A partir d'une grille grid, d'une case startPoint et d'une case endPoint, trouve un chemin évitant les obstacles grâce à l'algorithme Astar
		LinkedList<AstarPoint> open = new LinkedList<AstarPoint>();
		LinkedList<AstarPoint> closed = new LinkedList<AstarPoint>();
		LinkedList<AstarPoint> children = new LinkedList<AstarPoint>();
		open.add(startPoint);
		
		while (open.size() > 0) {
			AstarPoint current = open.get(0);
			int index1 = 0;
			int index2 = 0;
			for (AstarPoint item : open) {
				if (item.f < current.f) {
					current = item;
					index1 = index2;
				}
				index2 += 1;
			}
			
			open.remove(index1);
			closed.add(current);
			
			if (current.equals(endPoint)) {
				LinkedList<AstarPoint> path = new LinkedList<AstarPoint>();
				while (current != null) {
					path.add(current);
					current = current.parent;
				}
				return path;
			}
			
			children = new LinkedList<AstarPoint>();
			
			for (AstarPoint point : graph.getConnectedPoints(current)) {
				children.add(point);
			}
			
			checkPoint:
			for (AstarPoint child : children) {
				for (AstarPoint close : closed) {
					if (child.equals(close)) {
						continue checkPoint;
					}
				}
			
				child.g = current.g + child.distance(current);
				child.h = child.distance(endPoint);
				child.f = child.h + child.g;
				child.parent = current;
				for (AstarPoint op : open) {
					if (child.equals(op) & child.g >= op.g) {
						continue checkPoint;
					}
				}
			
				open.add(child);
			}
		}
		return null;
	}
}
