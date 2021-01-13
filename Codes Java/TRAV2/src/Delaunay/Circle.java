package Delaunay;


import Graphics.Draw;

public class Circle {

	public Point center;
	public double radius;

	public Circle(Point center, double radius) {
		this.center = center;
		this.radius = radius;
	}

	public static Circle circunscribedCircle(Triangle triangle) {
		//droite 1 : ax+by+c=0
		//droite 2 : dx+ey+f=0
		//solution : intersection des deux droites
		double a, b, c, d, e, f;
		Point vector1 = new Point(triangle.P1.x - triangle.P2.x, triangle.P1.y - triangle.P2.y);
		Point vector2 = new Point(triangle.P1.x - triangle.P3.x, triangle.P1.y - triangle.P3.y);
		Point middle1 = new Point((triangle.P1.x + triangle.P2.x) / 2, (triangle.P1.y + triangle.P2.y) / 2);
		Point middle2 = new Point((triangle.P1.x + triangle.P3.x) / 2, (triangle.P1.y + triangle.P3.y) / 2);
		a = vector1.x;
		b = vector1.y;
		c = - a * middle1.x - b * middle1.y;
		d = vector2.x;
		e = vector2.y;
		f = - d * middle2.x - e * middle2.y;
		double x = (f * b - c * e) / (a * e - d * b);
		double y = (d * c - a * f) / (a * e - d * b);
		Point center = new Point(x, y);
		double radius = center.distance(triangle.P1);
		return new Circle(center, radius);
	}

	public boolean contains(Point point) {
		return (center.distance(point) < radius);
	}

	public void draw(Draw draw) {
		draw.circle(center.x, center.y, radius);
	}

	/*public static void main(String[] args) {
		Triangle T = new Triangle(new Point(0, 0), new Point(10, 0), new Point(5, 5));
		Circle C = circunscribedCircle(T);
		Draw draw = new Draw();
		draw.setXscale(-5, 15);
		draw.setYscale(-10, 10);
		draw.setPenColor(Color.BLACK);
		T.draw(draw);
		C.draw(draw);

	}*/
}
