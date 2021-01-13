package Graphics;

import java.awt.Color;
import java.util.LinkedList;

import Astar.AstarPathFinder;
import Delaunay.DelaunayGenerator;
import Delaunay.Graph;
import Delaunay.Point;
import Delaunay.Triangle;

public class Controller {
	
	public static double tolerance = Math.pow(10, -8);
	public static double width = 1;
	public static double height = 1;
	public static Graph graph;
	public static LinkedList<Point> points = new LinkedList<Point>();
	public static Draw draw = new Draw();
	public static Obstacles obstacles = new Obstacles();
	public static int startPointIndex;
	public static int endPointIndex;
	public static LinkedList<Point> path;
	public static Point startPoint;
	public static Point endPoint;
	public static Triangle startTriangle;
	public static Triangle endTriangle;
	
	
	public static void init() {
		points.add(new Point(0.2, 0.2));
		points.add(new Point(0.4, 0.2));
		points.add(new Point(0.7, 0.6));
		points.add(new Point(0.8, 0.9));
		obstacles.add(points.get(0), points.get(1));
		obstacles.add(points.get(0), points.get(2));
		draw.setXscale(0 - 0.05 * width, 1.05 * width);
		draw.setYscale(0 - 0.05 * width, 1.05 * height);
		draw.setPenRadius(0.005);
	}

	public static void setRandomPoints(int len) {
		for (int i = 0 ; i < len ; i++) {
			points.add(new Point());
		}
	}
	
	public static void drawGraph() {
		graph.draw(draw);
		obstacles.draw(draw);
	}
	
	public static void setGraph() {
		graph = DelaunayGenerator.delaunayGenerator();
		points = DelaunayGenerator.points;
	}
	
	public static int getRandomIndex() {
		return (int)(Math.random() * graph.len);
	}
	
	public static void drawPath() {
		draw.setPenColor(Color.GREEN);
		draw.setPenRadius(0.005);
		int len = path.size();
		for (int i = 0 ; i < len - 1 ; i++) {
			draw.line(path.get(i).x, path.get(i).y, path.get(i + 1).x, path.get(i + 1).y);
		}
	}
	
	public static void setLimitPoints() {
		startPoint = new Point(0.7, 0.5);
		endPoint = new Point(0.2, 0.4);
	}
	
	public static void setRandomLimitPoints() {
		/*startPointIndex = getRandomIndex();
		endPointIndex = getRandomIndex();*/
		double xStart = Math.random() * width;
		double yStart = Math.random() * height;
		double xEnd = Math.random() * width;
		double yEnd = Math.random() * height;
		startPoint = new Point(xStart, yStart);
		endPoint = new Point(xEnd, yEnd);
	}
	
	public static void findLimitTriangles() {
		for (Triangle triangle : DelaunayGenerator.triangles) {
			if (triangle.contains(startPoint)) startTriangle = triangle;
			if (triangle.contains(endPoint)) endTriangle = triangle;
		}
		System.out.println(startTriangle.toString());
		System.out.println(endTriangle.toString());
	}
	
	public static void drawLimitPoints() {
		draw.setPenColor(Color.GREEN);
		draw.setPenRadius(0.05);
		draw.point(startPoint.x, startPoint.y);
		draw.point(endPoint.x, endPoint.y);
	}
	
	public static void setPath() {
		/*path = AstarPathFinder.pathFinder(startPointIndex, endPointIndex, graph);*/
		path = AstarPathFinder.pathFinder(startPoint, endPoint, graph);
	}
	
	public static void main(String[] args) {
		init();
		setGraph();
		drawGraph();
		/*setRandomLimitPoints();*/
		setLimitPoints();
		drawLimitPoints();
		setPath();
		drawPath();
	}
}
