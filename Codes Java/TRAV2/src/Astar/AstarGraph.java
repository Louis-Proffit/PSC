package Astar;

import Delaunay.Triangle;
import Graphics.Controller;
import Graphics.Draw;

import java.util.LinkedList;

import Delaunay.Point;
import Delaunay.DelaunayGenerator;
import Delaunay.Edge;
import Delaunay.Obstacle;

public class AstarGraph {
	
	public AstarPoint[] points;
	public AstarPoint startPoint;
	public AstarPoint endPoint;
	public Obstacle obstacles;
	public boolean[][] edges;
	public int len;
	
	public AstarGraph(Point startPoint, Point endPoint) {
		this.len = Controller.points.size() + 2;
		points = new AstarPoint[len];
		for (int i = 0 ; i < len - 2 ; i++) {
			points[i] = new AstarPoint(Controller.points.get(i));
		}
		this.startPoint = new AstarPoint(startPoint);
		this.endPoint = new AstarPoint(endPoint);
		points[len - 2] = this.startPoint;
		points[len - 1] = this.endPoint;
		this.edges = new boolean[len][len];
		for (Triangle triangle : DelaunayGenerator.triangles) {
			
			int i1 = getIndex(triangle.P1);
			int i2 = getIndex(triangle.P2);
			int i3 = getIndex(triangle.P3);
			if (connectedInTriangulation(i1, i2)) {
				edges[i1][i2] = true;
				edges[i2][i1] = true;
			}
			if (connectedInTriangulation(i1, i3)) {
				edges[i1][i3] = true;
				edges[i3][i1] = true;
			}
			if (connectedInTriangulation(i3, i2)) {
				edges[i3][i2] = true;
				edges[i2][i3] = true;
			}
		}
		for (int i = 0 ; i < len - 2 ; i++) {
			if (connectedStart(i)) {
				edges[i][len - 2] = true;
				edges[len - 2][i] = true;
			}
			if (connectedEnd(i)) {
				edges[i][len - 1] = true;
				edges[len - 1][i] = true;
			}
		}
		if (startAndEndConnected()) {
			edges[len - 1][len - 2] = true;
			edges[len - 2][len - 1] = true;
		}
	}
	
	public int getIndex(Point point) {
		// Returns the index of point
		for (int i = 0 ; i < len ; i++) {
			if (points[i].equals(point)) return i;
		}
		System.out.println("Debug : le point demandé n'est pas dans la liste");
		return -1;
	}
	
	public boolean connectedInTriangulation(int i, int j) {
		// Returns true if and only if two points form the triangulation of indexes i and j < len - 2 are connected
		for (Obstacle obstacle : Controller.obstacles) {
			if (!obstacle.triangulationLinkIsCorrect(points[i], points[j])) return false;
		}
		return true;
	}
	
	public boolean connectedEnd(int index){
		// Returns true if and only if the end point is connected to pointsList[index]
		for (Obstacle obstacle : Controller.obstacles) {
			if (!obstacle.linkIsCorrect(points[index], endPoint)) return false;
		}
		return true;
	}
	
	public boolean connectedStart(int index){
		// Returns true if and only if the start point is connected to pointsList[index]
		for (Obstacle obstacle : Controller.obstacles) {
			if (!obstacle.linkIsCorrect(points[index], startPoint)) return false;
		}
		return true;
	}
	
	public boolean startAndEndConnected() {
		// Return true if and only if the start point and the end point are connected
		for (Obstacle obstacle : Controller.obstacles) {
			if (!obstacle.linkIsCorrect(startPoint, endPoint)) return false;
		}
		return true;
	}
	
	public LinkedList<AstarPoint> getConnectedPoints(AstarPoint point){
		// Returns the list of points connected to point
		LinkedList<AstarPoint> result = new LinkedList<AstarPoint>();
		for (int i = 0 ; i < len ; i++) {
			if (points[i].equals(point)) {
				for (int j = 0 ; j < len ; j++) {
					if (edges[i][j] & j != i) {
						result.add(points[j]);
					}
				}
				return result;
			}
		}
		System.out.println("Debug : le point choisi n'est pas dans la liste");
		return null;
	}
	
	@Override
	public String toString() {
		String S = "";
		for (int i = 0 ; i < len ; i++) {
			for (int j = 0 ; j < len ; j++) {
				S += ";" + edges[i][j];
			}
			S += "\n";
		}
		return S;
	}
	
	public void draw(Draw draw) {
		for (int i =  0; i < len ; i++) {
			for (int j = i + 1 ; j < len ; j++) {
				if (edges[i][j]) {
					(new Edge(points[i], points[j])).draw(draw);
				}
			}
		}
	}
}

