package Graphics;

import java.awt.Color;
import java.util.LinkedList;

import Delaunay.Point;
import Astar.AstarPathFinder;
import Delaunay.DelaunayGenerator;
import Delaunay.Obstacle;
import Delaunay.Triangle;

public class Controller {
	
	public static double tolerance = Math.pow(10, -7);
	public static double width = 10;
	public static double height = 10;
	public static Draw draw = new Draw();
	public static LinkedList<Point> points = new LinkedList<Point>();
	public static LinkedList<Point> path;
	public static Point startPoint;
	public static Point endPoint;
	public static LinkedList<Obstacle> obstacles = new LinkedList<Obstacle>();
	
	public static void initDelaunayTriangulation() {
		points.add(new Point(1, 1));
		points.add(new Point(2, 0.5));
		points.add(new Point(2, 2));
		points.add(new Point(3, 1));
		points.add(new Point(3, 4));
		points.add(new Point(6, 4));
		points.add(new Point(6, 1));
		points.add(new Point(7, 4));
		points.add(new Point(8, 4));
		points.add(new Point(7, 1));
		points.add(new Point(2, 5));
		points.add(new Point(8, 5));
		points.add(new Point(6, 8));
		obstacles.add(new Obstacle(new Point[] {points.get(0), points.get(1), points.get(2)}));
		obstacles.add(new Obstacle(new Point[] {points.get(3), points.get(4), points.get(5), points.get(6)}));
		obstacles.add(new Obstacle(new Point[] {points.get(7), points.get(8), points.get(9)}));
		obstacles.add(new Obstacle(new Point[] {points.get(10), points.get(11), points.get(12)}));
		draw.setXscale(0 - 0.05 * width, 1.05 * width);
		draw.setYscale(0 - 0.05 * width, 1.05 * height);
		DelaunayGenerator.delaunayGenerator();
	}
	
	public static void drawDelaunayTriangulation() {
		draw.setPenColor(Color.BLACK);
		draw.setPenRadius(0.006);
		for (Triangle triangle : DelaunayGenerator.triangles) triangle.draw(draw);
		draw.setPenColor(Color.RED);
		draw.setPenRadius(0.01);
		for (Obstacle obstacle : obstacles) {
			obstacle.draw(draw);
		}
	}
	
	public static void setPath() {
		AstarPathFinder.pathFinder(startPoint, endPoint);
		path = AstarPathFinder.path;
	}
	
	public static void drawPath() {
		draw.setPenColor(Color.GREEN);
		draw.setPenRadius(0.005);
		int len = path.size();
		for (int i = 0 ; i < len - 1 ; i++) {
			draw.line(path.get(i).x, path.get(i).y, path.get(i + 1).x, path.get(i + 1).y);
		}
	}
	
	public static void drawGraph() {
		// Can only be used once limit points have been defined and astarGraph has been calculated
		draw.setPenColor(Color.GREEN);
		draw.setPenRadius(0.005);
		AstarPathFinder.astarGraph.draw(draw);
	}
	
	public static void setLimitPoints() {
		startPoint = new Point(1, 7);
		endPoint = new Point(9, 1);
	}
	
	public static void drawLimitPoints() {
		draw.setPenColor(Color.GREEN);
		draw.setPenRadius(0.05);
		draw.point(startPoint.x, startPoint.y);
		draw.point(endPoint.x, endPoint.y);
	}
	
	public static void main(String[] args) {
		initDelaunayTriangulation();
		drawDelaunayTriangulation();
		setLimitPoints();
		drawLimitPoints();
		setPath();
		drawPath();
	}
}
