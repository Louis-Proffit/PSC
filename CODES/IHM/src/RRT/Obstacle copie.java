import java.awt.Color;

public class Obstacle extends RTT{
	Point center;
	double radius;
	TypeObstacle type;
	
	static final double epsilon = 0.02;
	
	Obstacle(TypeObstacle type, Point p, double r){
		center=p;
		radius=r;
		this.type=type;
	}
	
	void draw() {
		draw.setPenColor(Color.BLACK);
		if(type==TypeObstacle.CIRCLE) {
			draw.filledCircle(center.x, center.y, radius);
		}
		else if(type==TypeObstacle.SQUARE) {
			draw.filledSquare(center.x, center.y, radius);
		}
	}
	
	boolean isIn(Point p) {
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