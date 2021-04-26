package Delaunay;


import Graphics.Draw;

public class ChainList {
	
	public Point head;
	public ChainList tail;
	
	public ChainList(Point head, ChainList tail) {
		this.head = head;
		this.tail = tail;
	}
	
	public ChainList(Point[] points) {
		int len = points.length;
		ChainList chainList = new ChainList(points[0], null);
		ChainList last = chainList;
		for (int i = 1 ; i < len ; i++) {
			chainList = new ChainList(points[i], chainList);
		}
		last.tail = chainList;
		this.head = chainList.head;
		this.tail = chainList.tail;
	}
	
	public void draw(Draw draw, int steps) {
		if (steps == 0) return;
		for (int i = 0 ; i < steps ; i++) {
			draw.line(head.x, head.y, tail.head.x, tail.head.y);
		}
		tail.draw(draw, steps - 1);
	}
	
	public boolean edgeCrossesChain(Edge edge, int steps) {
		// Return true if and only if edge crosses the chainList bound
		if (steps == 0) return false;
		if (edge.intersects(new Edge(head, tail.head))) return true;
		return tail.edgeCrossesChain(edge, steps - 1);
	}

	public boolean contains(Point P, int steps) {
		// Return true if and only if the chainList contains P
		if (steps == 0) return false;
		if (head.equals(P)) return true;
		return tail.contains(P,  steps - 1);
	}
	
	public boolean isAnEdge(Point P, Point Q, int steps) {
		// Return true if and only if PQ is an edge of the bound
		if (steps == 0) return false;
		if (head.equals(P) & tail.head.equals(Q)) return true;
		if (head.equals(Q) & tail.head.equals(P)) return true;
		return tail.isAnEdge(P, Q, steps - 1);
	}
}
