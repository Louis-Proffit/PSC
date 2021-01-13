package Astar;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedList;

import Delaunay.Graph;
import Delaunay.Point;
import Graphics.Controller;
import Graphics.Draw;
import Graphics.Obstacles;

public class AstarGraph {
	
	public LinkedList<AstarPoint> pointsList = new LinkedList<AstarPoint>();
	public Obstacles obstacles;
	public boolean[][] edges;
	public int len;
	
	public AstarGraph(Graph graph, Point startPoint, Point endPoint) {
		obstacles = Controller.obstacles;
		this.len = graph.len + 2;
		for (Point point : graph.pointsList) {
			this.pointsList.add(new AstarPoint(point));
		}
		this.pointsList.add(new AstarPoint(startPoint));
		this.pointsList.add(new AstarPoint(endPoint));
		this.edges = new boolean[len][len];
		for (int i = 0 ; i < graph.len ; i++) {
			for (int j = 0 ; j < graph.len ; j++) {
				if (obstacles.isValidLink(pointsList.get(i), pointsList.get(j)) & graph.edges[i][j]) {
					this.edges[i][j] = true;
					this.edges[j][i] = true;
				}
				else {
					this.edges[i][j] = false;
					this.edges[j][i] = false;
				}
			}
		}
		for (int i = 0 ; i < graph.len ; i++) {
			if (obstacles.isValidLink(pointsList.get(i), pointsList.get(len - 2))) {
				edges[i][len - 2] = true;
				edges[len - 2][i] = true;
			}
		}
		for (int i = 0 ; i < graph.len ; i++) {
			if (obstacles.isValidLink(pointsList.get(i), pointsList.get(len - 1))) {
				edges[len - 1][i] = true;
				edges[i][len - 1] = true;
			}
		}
		if (obstacles.isValidLink(pointsList.get(len - 2), pointsList.get(len - 1))) {
			edges[len - 1][len - 2] = true;
			edges[len - 2][len - 1] = true;
		}
		else {
			edges[len - 1][len - 2] = false;
			edges[len - 2][len - 1] = false;
		}
		edges[len - 1][len - 1] = false;
		edges[len - 2][len - 2] = false;		
	}
	
	public void draw(Draw draw) {
		draw.setPenColor(Color.RED);
		for (AstarPoint point : pointsList) {
			point.draw(draw);
		}
		draw.setPenColor(Color.BLACK);
		for (int i = 0 ; i < len ; i++) {
			for (int j = 0 ; j < i ; j++) {
				if (edges[i][j]) {
					draw.line(pointsList.get(i).x, pointsList.get(i).y, pointsList.get(j).x, pointsList.get(j).y);
				}
			}
		}
	}
	
	public HashSet<AstarPoint> getConnectedPointsFrom(AstarPoint point) {
		HashSet<AstarPoint> connectedPoints = new HashSet<AstarPoint>();
		int i = findIndex(point);
		for (int j = 0 ; j < len ; j++) {
			if (edges[i][j]) {
				connectedPoints.add(pointsList.get(j));
			}
		}
		
		return connectedPoints;
	}
	
	public int findIndex(AstarPoint point) {
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
