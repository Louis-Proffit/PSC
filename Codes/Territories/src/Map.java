
public class Map {
	
	int size;
	Draw draw = new Draw();
	
	Map(int s){
		size=s;
		draw.setXscale(0, size);
		draw.setYscale(0, size);
	}
	
	boolean contains(Point p) {
		return (p.x>0 && p.x<size && p.y>0 && p.y<size);
	}
}