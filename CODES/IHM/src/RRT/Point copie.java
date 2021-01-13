import java.util.LinkedList;

public class Point extends RTT{
	
	double x,y;
	LinkedList<Point> links;
	
	Point(double x, double y){
		this.x=x;
		this.y=y;
		links=new LinkedList<Point>();
	}
	
	static LinkedList<Point> addLink(Point newPoint, Point link, double length){
		
		double dist = Espace.distance(newPoint,link);
		if(dist>length) {
			newPoint.x=link.x-(link.x-newPoint.x)*length/dist;
			newPoint.y=link.y-(link.y-newPoint.y)*length/dist;
			
		}
		link.links.add(newPoint);
		newPoint.links.addFirst(link);
		draw.line(newPoint.x, newPoint.y, link.x, link.y);
		return newPoint.links;
	}
	
	boolean isLinked() {
		return links==null;
	}
	
	double[] toDouble() {
		return new double[] {x, y};
	}
	
	Point toPoint(double[] d) {
		return new Point(d[0], d[1]);
	}
	@Override
	public String toString() {
		return "( "+x+" ; "+y+" )";
	}
}