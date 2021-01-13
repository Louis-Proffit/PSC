package Graphics;

import java.awt.Color;
import java.util.HashSet;

import Delaunay.Edge;
import Delaunay.Point;

public class Obstacles {

	public HashSet<Edge> forbiddenEdges; // Les points forment le polygone dans le sens horaire
	
	public Obstacles() {
		this.forbiddenEdges = new HashSet<Edge>();
	}
	
	public void add(Point point1, Point point2) {
		forbiddenEdges.add(new Edge(point1, point2));
	}
	
	public void draw(Draw draw) {
		for (Edge forbiddenEdge : forbiddenEdges) {
			draw.setPenColor(Color.RED);
			forbiddenEdge.draw(draw);
		}
	}
	
	public boolean isValidLink(Point P1, Point P2) {
		for (Edge forbiddenEdge : forbiddenEdges) {
			if (forbiddenEdge.intersects(new Edge(P1, P2))) return false;
		}
		return true;
	}
}
