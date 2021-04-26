package Delaunay;

import Graphics.Draw;

public class Triangle {
	
	public Point P1;
	public Point P2;
	public Point P3;
	
	public Triangle(Point P1, Point P2, Point P3) {
		this.P1 = P1;
		this.P2 = P2;
		this.P3 = P3;
	}
	
	public String toString() {
		return "Triangle : " + P1.toString() + " , " + P2.toString() + " , " + P3.toString();
	}
	
	public Edge[] getEdges() {
		// Returns the table of 3 edges of the triangle
		return new Edge[] {new Edge(P1,  P2), new Edge(P1,  P3), new Edge(P2,  P3)};
	}
	
	public void draw(Draw draw) {
		draw.line(P1.x,  P1.y,  P2.x,  P2.y);
		draw.line(P2.x,  P2.y,  P3.x,  P3.y);
		draw.line(P3.x,  P3.y,  P1.x,  P1.y);
	}
	
	public boolean hasCommonSummit(Triangle triangle) {
		// Renvoie true si this et triangle ont un sommet commun
		return ((this.P1.equals(triangle.P1)) | (this.P1.equals(triangle.P2)) | (this.P1.equals(triangle.P3)) |
				(this.P2.equals(triangle.P1)) | (this.P2.equals(triangle.P2)) | (this.P2.equals(triangle.P3)) |
				(this.P3.equals(triangle.P1)) | (this.P3.equals(triangle.P2)) | (this.P3.equals(triangle.P3)));
	}
	
	public boolean contains(Point point) {
		Edge triangleEdge1 = new Edge(P1, P2);
		Edge triangleEdge2 = new Edge(P2, P3);
		Edge triangleEdge3 = new Edge(P3, P1);
		Edge edge1 = new Edge(P1, point);
		Edge edge2 = new Edge(P2, point);
		Edge edge3 = new Edge(P3, point);
		boolean isTrigonometricOriented = triangleEdge1.makesPositiveAngleWith(triangleEdge2);
		if (isTrigonometricOriented) {
			if (triangleEdge1.makesPositiveAngleWith(edge1) & triangleEdge2.makesPositiveAngleWith(edge2) & triangleEdge3.makesPositiveAngleWith(edge3)) return true;
			return false;
		}
		else {
			if (!triangleEdge1.makesPositiveAngleWith(edge1) & !triangleEdge2.makesPositiveAngleWith(edge2) & !triangleEdge3.makesPositiveAngleWith(edge3)) return true;
			return false;
		}
	}
	
	@Override
	public boolean equals(Object triangle) {
		return (this.P1.equals(((Triangle)triangle).P1) & this.P2.equals(((Triangle)triangle).P2) & this.P3.equals(((Triangle)triangle).P3));
	}
}
