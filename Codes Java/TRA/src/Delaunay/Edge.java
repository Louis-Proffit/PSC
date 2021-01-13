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
	
	public void draw(Draw draw) {
		draw.line(P1.x, P1.y, P2.x, P2.y);
	}
	
	public Point getOtherEnd(Point point) {
		if (this.P1.equals(point)) return this.P2;
		if (this.P2.equals(point)) return this.P1;
		return null;
	}
	
	public boolean intersects(Edge edge) {
		double a, b, c, d, e, f;
		Point vector1 = new Point(this.P2.x - this.P1.x, this.P2.y - this.P1.y);
		Point vector2 = new Point(edge.P2.x - edge.P1.x, edge.P2.y - edge.P1.y);
		a = vector1.y;
		b = -vector1.x;
		c = - a * this.P1.x - b * this.P1.y;
		d = vector2.y;
		e = -vector2.x;
		f = - d * edge.P1.x - e * edge.P1.y;
		if (Math.abs(a * e - b * d) < Controller.tolerance) {// Les deux segments sont parrallèles
			if (this.containsStrictlyPoint(edge.P1) | this.containsStrictlyPoint(edge.P2) | edge.containsStrictlyPoint(this.P1) | edge.containsStrictlyPoint(this.P1)) return true;
			return false;
		}
		else { // Les droites portant les segments se coupent en (x, y)
			double x = (f * b - c * e) / (a * e - d * b);
			double y = (d * c - a * f) / (a * e - d * b);
			Point intersection = new Point(x, y);
			if (this.containsStrictlyPoint(intersection) & edge.containsStrictlyPoint(intersection)) return true;
			return false;
		}
	}
	
	public boolean containsStrictlyPoint(Point point) {
		if (point.distance(this.P1) < Controller.tolerance | point.distance(this.P2) < Controller.tolerance) return false;
		double t1 = (point.x - this.P2.x) / (this.P1.x - this.P2.x); 
		double t2 = (point.y - this.P2.y) / (this.P1.y - this.P2.y);
		if (Math.abs(t2 - t1) < Controller.tolerance) {
			if (t1 < 0 | t1 > 1) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	public boolean makesStrictPositiveAngleWith(Edge edge) {
		double thisOrthogonalX = (this.P2.y - this.P1.y);
		double thisOrthogonalY = - (this.P2.x - this.P1.x);
		double scalarEdgeWithThisOrthogonal = thisOrthogonalX * (edge.P2.x - edge.P1.x) + thisOrthogonalY * (edge.P2.y - edge.P1.y);
		return (scalarEdgeWithThisOrthogonal < 0);
	}
	
	public boolean makesPositiveAngleWith(Edge edge) {
		double thisOrthogonalX = (this.P2.y - this.P1.y);
		double thisOrthogonalY = - (this.P2.x - this.P1.x);
		double scalarEdgeWithThisOrthogonal = thisOrthogonalX * (edge.P2.x - edge.P1.x) + thisOrthogonalY * (edge.P2.y - edge.P1.y);
		return (scalarEdgeWithThisOrthogonal <= 0);
	}
}
