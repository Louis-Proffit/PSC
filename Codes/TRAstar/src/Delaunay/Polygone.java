package Delaunay;

import java.awt.Color;
import java.util.LinkedList;

import Graphics.Draw;

public class Polygone {
	
	public LinkedList<Edge> edges;
	
	public Polygone() {
		edges = new LinkedList<Edge>();
	}
	
	public void addTriangle(Triangle triangle) {
		Edge[] triangleEdges = triangle.getEdges();
		for (Edge edge : triangleEdges) {
			if (edges.contains(edge))edges.remove(edge);
			else edges.add(edge);
		}
	}
	
	public String toString() {
		String S = "Polygone : ";
		for (Edge edge : edges) {
			S += "(" + edge.P1.x + ";" + edge.P1.y + ") , ("+ edge.P2.x + ";" + edge.P2.y + ")";
		}
		return S;
	}
	
	public void draw(Draw draw) {
		draw.setPenColor(Color.BLACK);
		for (Edge edge : edges) {
			edge.draw(draw);
		}
	}
}
