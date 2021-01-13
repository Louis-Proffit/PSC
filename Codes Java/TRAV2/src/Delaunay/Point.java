package Delaunay;


import Graphics.Controller;
import Graphics.Draw;

public class Point {
	
	public double x;
	public double y;
	
	public Point() {
		// Random point
		this.x = Math.random() * Controller.width;
		this.y = Math.random() * Controller.height;
	}
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	
	public boolean equals(Object point) {
		return ((x == ((Point)point).x) & (y == ((Point)point).y));
	}
	
	public double distance (Point point) {
		return Math.pow(Math.pow(point.x - this.x, 2) + Math.pow(point.y - this.y, 2), 0.5);
	}
	
	public String toString() {
		return "Point : (" + x + ";" + y + ")";
	}
	
	public void draw(Draw draw) {
		draw.point(x,  y);
	}
	
	public double scalar(Point point) {
		return point.x * this.x + point.y * this.y;
	}
}
