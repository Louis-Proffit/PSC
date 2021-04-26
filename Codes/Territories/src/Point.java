
public class Point {
	
	double x,y;
	
	Point(double a, double b){
		x=a;
		y=b;
	}

	public boolean equals(Point p) {
		return (x==p.x && y==p.y);
	}
	
	public String toString() {
		return "[ "+x+" ; "+y+" ]";
	}
}