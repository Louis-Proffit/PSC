package Delaunay;

import java.util.HashSet;
import java.util.LinkedList;


import Graphics.Controller;

public class DelaunayGenerator {
	
	public static LinkedList<Point> points = new LinkedList<Point>();
	public static HashSet<Triangle> triangles = new HashSet<Triangle>();
	
	public static void delaunayGenerator() {
		// Generates the Delaunay triangulation of Controller.points in the outer square
		points = Controller.points;
		init();
		for (Point point : points) addPoint(point);
	}
	
	public static void init() {
		// Intialises the triangulation with the outer square points
		Point P1 = new Point(0, 0);
		Point P2 = new Point(0, Controller.height);
		Point P3 = new Point(Controller.width, 0);
		Point P4 = new Point(Controller.width, Controller.height);
		Controller.points.add(P1);
		Controller.points.add(P2);
		Controller.points.add(P3);
		Controller.points.add(P4);
		triangles.add(new Triangle(P1, P2, P3));
		triangles.add(new Triangle(P2, P4, P3));
	}
	
	public static void addPoint(Point point) {
		/// Adds a point to the triangulation
		HashSet<Triangle> toDelete = new HashSet<Triangle>();
		toDelete = new HashSet<Triangle>();
		Polygone superPolygone = new Polygone();
		for (Triangle triangle : triangles) {
			if (Circle.circunscribedCircle(triangle).contains(point)) {
				superPolygone.addTriangle(triangle);
				toDelete.add(triangle);
			}
		}
		for (Triangle triangleToDelete : toDelete) {
			triangles.remove(triangleToDelete);
		}
		for (Edge edge : superPolygone.edges) {
			Triangle triangleToAdd = new Triangle(edge.P1, edge.P2, point);
			triangles.add(triangleToAdd);
		}
	}
}
