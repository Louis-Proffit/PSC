package Astar;

import Graphics.Draw;

public class AstarEdge {
	
	public AstarPoint P1;
	public AstarPoint P2;
	
	public AstarEdge(AstarPoint P1, AstarPoint P2) {
		this.P1 = P1;
		this.P2 = P2;
	}
	
	public boolean equals(Object edge) {
		return ( (this.P1.equals(((AstarEdge)(edge)).P1) & this.P2.equals(((AstarEdge)(edge)).P2) | (this.P1.equals(((AstarEdge)(edge)).P2) & this.P2.equals(((AstarEdge)(edge)).P1))) );
	}
	
	public void draw(Draw draw) {
		draw.line(P1.x, P1.y, P2.x, P2.y);
	}
	
	public AstarPoint getOtherEnd(AstarPoint point) {
		if (this.P1.equals(point)) return this.P2;
		if (this.P2.equals(point)) return this.P1;
		return null;
	}
}