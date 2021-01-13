package Astar;

import java.util.LinkedList;

import Delaunay.Point;

public class AstarPathFinder {
	
	public static AstarGraph astarGraph;
	public static LinkedList<Point> path = new LinkedList<Point>();
	
	public static void pathFinder(Point startPoint, Point endPoint){
		// Adds the path between startPoint and endPoint in the local static variable path
		astarGraph = new AstarGraph(startPoint, endPoint);
		LinkedList<AstarPoint> astarPath = Astar.aStar(astarGraph,  new AstarPoint(startPoint),  new AstarPoint(endPoint));
		for (AstarPoint astarPoint : astarPath) {
			path.add(astarPoint);
		}
	}
}