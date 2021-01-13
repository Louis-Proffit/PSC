public class Triangle extends Delaunay{
	
	Point[] s=new Point[3];
	
	Triangle(Point p1, Point p2, Point p3){
		s[0]=p1;
		s[1]=p2;
		s[2]=p3;
	}
	
	static double aire(Triangle t) {
		double[] aretes = new double[3];
		double p=0;
		for(int i=0; i<3; i++) {
			aretes[i]=Math.sqrt(Math.pow((t.s[i].x-t.s[(i+1)%3].x),2)+Math.pow((t.s[i].y-t.s[(i+1)%3].y),2));
			p+=aretes[i]/2;
		}
		return Math.sqrt(p*(p-aretes[0])*(p-aretes[1])*(p-aretes[2]));
	}
	
	double[] CC() {
		double x_c=( (Math.pow(s[2].x,2)-Math.pow(s[1].x,2)+Math.pow(s[2].y,2)-Math.pow(s[1].y,2))/(2*(s[2].y-s[1].y)) - (Math.pow(s[1].x,2)-Math.pow(s[0].x,2)+Math.pow(s[1].y,2)-Math.pow(s[0].y,2))/(2*(s[1].y-s[0].y)) )/( (s[1].x-s[0].x)/(s[1].y-s[0].y) - (s[2].x-s[1].x)/(s[2].y-s[1].y) );
		double y_c=(Math.pow(s[1].x,2)-Math.pow(s[0].x,2)+Math.pow(s[1].y,2)-Math.pow(s[0].y,2))/(2*(s[1].y-s[0].y))-x_c*(s[1].x-s[0].x)/(s[1].y-s[0].y);
		double R = Math.sqrt(Math.pow((s[0].x-x_c),2)+Math.pow((s[0].y-y_c),2));
		return new double[] {x_c, y_c, R};
	}
	
	boolean isIn(double[] p) {
		double[] cc = this.CC();
		return (Math.sqrt(Math.pow(p[0]-cc[0], 2)+Math.pow(p[1]-cc[1], 2))<=cc[2]);
	}
	
}