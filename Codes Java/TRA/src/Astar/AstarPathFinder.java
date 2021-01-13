package Astar;

import java.util.LinkedList;

import Delaunay.Graph;
import Delaunay.Point;

public class AstarPathFinder {
	
	public static AstarGraph astarGraph;
	
	public static LinkedList<Point> pathFinder(Point startPoint, Point endPoint, Graph graph){
		astarGraph = new AstarGraph(graph, startPoint, endPoint);
		return pathFinderIndex(astarGraph.len - 2, astarGraph.len - 1);
	}
	
	public static LinkedList<Point> pathFinderIndex(int startPointIndex, int endPointIndex) {
		AstarPoint astarStartPoint = astarGraph.pointsList.get(startPointIndex);
		AstarPoint astarEndPoint = astarGraph.pointsList.get(endPointIndex);
		LinkedList<AstarPoint> pointPath = Astar.aStar(astarGraph, astarStartPoint, astarEndPoint);
		LinkedList<Point> points = new LinkedList<Point>();
		int len = pointPath.size();
		for (int i = 0 ; i < len  ; i++) points.add(new Point(pointPath.get(i)));
		return points;
	}
}