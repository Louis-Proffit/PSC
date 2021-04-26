package Delaunay;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedList;

import Graphics.Draw;

public class Graph {
	
	public LinkedList<Point> pointsList;
	public boolean[][] edges;
	public int len;
	
	public Graph(LinkedList<Point> points) {
		this.pointsList = points;
		this.len = points.size();
		this.edges = new boolean[len][len];
	}
	
	public Graph() {
		this.pointsList = DelaunayGenerator.points;
		this.len = pointsList.size();
		this.edges = new boolean[len][len];
		for (Triangle triangle : DelaunayGenerator.triangles) {
			for (Edge edge : triangle.getEdges()) {
				edges[findIndex(edge.P1)][findIndex(edge.P2)] = true;
				edges[findIndex(edge.P2)][findIndex(edge.P1)] = true;
			}
		}
	}
	
	public void draw(Draw draw) {
		draw.setPenRadius(0.05);
		draw.setPenColor(Color.RED);
		for (Point point : pointsList) {
			point.draw(draw);
		}

		draw.setPenRadius(0.005);
		draw.setPenColor(Color.BLACK);
		for (int i = 0 ; i < len ; i++) {
			for (int j = 0 ; j < i ; j++) {
				if (edges[i][j]) {
					draw.line(pointsList.get(i).x, pointsList.get(i).y, pointsList.get(j).x, pointsList.get(j).y);
				}
			}
		}
	}
	
	public HashSet<Point> getConnectedPointsFrom(Point point) {
		HashSet<Point> connectedPoints = new HashSet<Point>();
		int i = findIndex(point);
		for (int j = 0 ; i < len ; i++) {
			if (edges[i][j]) {
				connectedPoints.add(pointsList.get(j));
			}
		}
		return connectedPoints;
	}
	
	public int findIndex(Point point) {
		int i = 0;
		while (!pointsList.get(i).equals(point)) {
			i += 1;
		}
		return i;	
	}
	
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
}
