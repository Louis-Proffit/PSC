package RRT;

import java.awt.Color;

public class Obstacle{
	Point center;
	double radius;
	TypeObstacle type;
	
	static final double epsilon = 5;
	
	Obstacle(TypeObstacle type, Point p, double r){
		center=p;
		radius=r;
		this.type=type;
	}
	
	public void draw() {
		RRT.draw.setPenColor(Color.BLACK);
		
		if(type==TypeObstacle.CIRCLE) {
			RRT.draw.filledCircle(center.x, center.y, radius);
		}
		
		else if(type==TypeObstacle.SQUARE) {
			RRT.draw.filledSquare(center.x, center.y, radius);
		}
	}
	
	boolean pointIsIn(Point p) {
		if(type==TypeObstacle.CIRCLE) {
			return (Math.sqrt(Math.pow(p.x-center.x, 2)+Math.pow(p.y-center.y, 2))<radius+epsilon);
		}
		else if(type==TypeObstacle.SQUARE) {
			return (Math.max(Math.abs(p.x-center.x), Math.abs(p.y-center.y))< radius+epsilon);
		}
		else {
			return false;
		}
	}
	
	public boolean linkIsIn(Point P, Point Q) {
		double normPC = Math.sqrt(Math.pow(center.x - P.x, 2) + Math.pow(center.y - P.y, 2));
		double normPQ = Math.sqrt(Math.pow(Q.x - P.x, 2) + Math.pow(Q.y - P.y, 2));
		double angle = Math.acos((  (Q.x - P.x) * (center.x - P.x) +  (Q.y - P.y) * (center.y - P.y)  ) / (normPC * normPQ));
		if (normPC * Math.sin(angle) < radius) {
			return true;
		}
		return false;
	}
}

enum TypeObstacle {
	NULL, CIRCLE, SQUARE;

	public static TypeObstacle typeOf(String word) {
		try {
			return TypeObstacle.valueOf(word);
		} catch (RuntimeException e) {
			return NULL;
		}
	}
}