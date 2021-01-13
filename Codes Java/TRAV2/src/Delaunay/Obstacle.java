package Delaunay;


import Graphics.Draw;

public class Obstacle {
	
	ChainList bound;
	int len;
	
	public Obstacle(Point[] points) {
		this.bound = new ChainList(points);
		this.len = points.length;
	}
	
	public boolean triangulationLinkIsCorrect(Point P, Point Q) {
		// Returns true if and only if two points from the triangulation are correctly linked
		if (bound.contains(P, len) & bound.contains(Q, len)) {
			return bound.isAnEdge(P, Q, len);
		}
		return true;
	}
	
	public boolean linkIsCorrect(Point P, Point Q) {
		// Returns true if and only if two points randomly placed are correctly linked
		return !bound.edgeCrossesChain(new Edge(P,Q), len);
	}
	
	public void draw(Draw draw) {
		bound.draw(draw, len);
	}
}
