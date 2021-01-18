import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

public class ConvexHull {
	
	Collection<Point> points;
	LinkedList<Point> sortedList;
	Point pivot;
	
	ConvexHull(Collection<Point> col){
		points = col;
		sortedList = new LinkedList<Point>(points);
		sortedList.sort(sortByY);
		pivot=sortedList.getFirst();
		sortedList.sort(compareAngleWithPivot);
	}

	private static Comparator<Point> sortByY = new Comparator<Point>() {

		@Override
		public int compare(Point p1, Point p2) {

			if(p1==null) return -1;
			if(p2==null) return 1;

			if( p1.y > p2.y) return 1;
			if( p1.y==p2.y ) return 0;
			else return -1;

		}
	};
	
	Comparator<Point> compareAngleWithPivot= new Comparator<Point>() {

		@Override
		public int compare(Point p1, Point p2) {

			if(p1==null) return -1;
			if(p2==null) return 1;

			double prodVect =  (p1.x-pivot.x)*(p2.y-pivot.y)-(p1.y-pivot.y)*(p2.x-pivot.x);
			if(prodVect>0) return -1;
			else if(prodVect==0) return 0;
			else return 1;
		}
	};
	
	static double prodVect(Point p1, Point p2, Point p3) {
		return (p2.x-p1.x)*(p3.y-p1.y)-(p2.y-p1.y)*(p3.x-p1.x);
	}
	
	LinkedList<Point> process() {
		
		if(sortedList.size()<2) return null;
		else {
			LinkedList<Point> hull = new LinkedList<Point>();
		
			hull.addFirst(sortedList.pop());
			hull.addFirst(sortedList.pop());
			
			for(Point p : sortedList) {
				
				while(hull.size()>=2 && prodVect(hull.get(1), hull.getFirst(), p)<=0) {
					hull.pop();
				}
				hull.addFirst(p);
				
			}
			hull.addFirst(pivot);
			return hull;
		}
	}
	
	public static void main(String args[]) {
		
		Walker w = new Walker(0,1);
		w.territory.put(0, new Point(0,1));
		w.territory.put(1, new Point(1,2));
		w.territory.put(2, new Point(1.5,1.8));
		w.territory.put(4, new Point(2,0));
		w.territory.put(5, new Point(3,1.2));
		w.territory.put(6, new Point(3.5,4));
		ConvexHull test = new ConvexHull(w.territory.values());
		System.out.println(test.process());
	}
}
