package RRT;

import java.util.LinkedList;

public class Point{
	
	public double x;
	public double y;
	LinkedList<Point> links;
	
	public Point(double x, double y){
		this.x = x;
		this.y = y;
		links = new LinkedList<Point>();
	}
	
	public void addLink(Point newPoint, double length){
		double dist = distance(newPoint);
		if(dist > length) {
			newPoint.x = this.x + (newPoint.x - this.x) * length / dist;
			newPoint.y = this.y + (newPoint.y - this.y) * length / dist;
		}
		this.links.add(newPoint);
		newPoint.links.addFirst(this);
		RRT.draw.line(newPoint.x, newPoint.y, this.x, this.y);
	}
	
	public boolean isInBounds() {
		if ((this.x < RRT.taille) & (0 < this.x) & (this.y < RRT.taille) & (0 <= this.y)) return true;
		return false;
	}
		
	public boolean isValid() {
		for(Obstacle obstacle: RRT.obstacles) {
			if(obstacle.pointIsIn(this)) {
				return false;
			}
		}
		return true;
	}
	
	public double distance(Point P) {
		return Math.sqrt(Math.pow((this.x-P.x),2) + Math.pow((this.y-P.y),2));
	}
	
	public boolean isLinked() {
		return links==null;
	}
	
	public Point middle(Point P) {
		return new Point((this.x + P.x) / 2, (this.y + P.y) / 2); 
	}
	
	public double[] toDouble() {
		return new double[] {x, y};
	}
	
	public Point toPoint(double[] d) {
		return new Point(d[0], d[1]);
	}
	
	@Override
	public String toString() {
		return "( "+x+" ; "+y+" )";
	}
	
	@Override
	public boolean equals(Object obj) {
		Point P = (Point)obj;
		if ((P.x == this.x) & (P.y == this.y)) return true;
		return false;
	}
}