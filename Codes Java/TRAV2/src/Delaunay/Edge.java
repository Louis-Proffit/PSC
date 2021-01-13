package Delaunay;

import Graphics.Controller;
import Graphics.Draw;

public class Edge {
	
	public Point P1;
	public Point P2;
	
	public Edge(Point P1, Point P2) {
		this.P1 = P1;
		this.P2 = P2;
	}
	
	public boolean equals(Object edge) {
		return ( (this.P1.equals(((Edge)(edge)).P1) & this.P2.equals(((Edge)(edge)).P2) | (this.P1.equals(((Edge)(edge)).P2) & this.P2.equals(((Edge)(edge)).P1))) );
	}
	
	@Override
	public String toString() {
		return "Edge : " + P1.toString() + " ; " + P2.toString() + ".";
	}
	
	public void draw(Draw draw) {
		draw.line(P1.x, P1.y, P2.x, P2.y);
	}

	public boolean intersects(Edge edge) {
		// intersects returns false if the two segments share a common point
		if (edge.P1.equals(this.P1) | edge.P1.equals(this.P2) | edge.P2.equals(this.P1) | edge.P2.equals(this.P2)) return false;
		// intersects returns true si les deux segments se coupent sur une extrémité d'un des deux segments, sauf dans le cas précédent
		// Uses the cone P1-edge and tests if P2 is in that cone
		Point P1edgeP1Orthogonal = new Point(edge.P1.y - this.P1.y, this.P1.x - edge.P1.x);
		Point P1edgeP2Orthogonal = new Point(edge.P2.y - this.P1.y, this.P1.x - edge.P2.x);
		Point P1edgeP1 = new Point(edge.P1.x - this.P1.x, edge.P1.y - this.P1.y);
		Point P1edgeP2 = new Point(edge.P2.x - this.P1.x, edge.P2.y - this.P1.y);
		Point P1P2 = new Point (this.P2.x - this.P1.x, this.P2.y - this.P1.y);
		double scalar1 = P1edgeP1Orthogonal.scalar(P1P2);
		double scalar2 = P1edgeP2Orthogonal.scalar(P1P2);
		double scalar3 = P1edgeP2Orthogonal.scalar(P1edgeP1);
		double scalar4 = P1edgeP1Orthogonal.scalar(P1edgeP2);
		boolean P2IsInConeFromP1 = (scalar1 * scalar4 > -Controller.tolerance & scalar2 * scalar3 > -Controller.tolerance);
		// and same reverse
		Point P2edgeP1Orthogonal = new Point(edge.P1.y - this.P2.y, this.P2.x - edge.P1.x);
		Point P2edgeP2Orthogonal = new Point(edge.P2.y - this.P2.y, this.P2.x - edge.P2.x);
		Point P2edgeP1 = new Point(edge.P1.x - this.P2.x, edge.P1.y - this.P2.y);
		Point P2edgeP2 = new Point(edge.P2.x - this.P2.x, edge.P2.y - this.P2.y);
		Point P2P1 = new Point (this.P1.x - this.P2.x, this.P1.y - this.P2.y);
		scalar1 = P2edgeP1Orthogonal.scalar(P2P1);
		scalar2 = P2edgeP2Orthogonal.scalar(P2P1);
		scalar3 = P2edgeP2Orthogonal.scalar(P2edgeP1);
		scalar4 = P2edgeP1Orthogonal.scalar(P2edgeP2);
		boolean P1IsInConeFromP2 = (scalar1 * scalar4 > -Controller.tolerance & scalar2 * scalar3 > -Controller.tolerance);
		return (P1IsInConeFromP2 & P2IsInConeFromP1);
	}
}
