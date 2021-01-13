package Global;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Test {
	public static void main(String[] args) {
		Path p = new Path();
		
		p.insertEnd(new Tile (1,1));
		p.insertEnd(new Tile (3,3));

		System.out.println(p.toString());
		
		LinkedList<Tile> t = new LinkedList<Tile>();
		t.add(new Tile(0,0));
		t.add(new Tile(1,1));
		t.add(new Tile(2,2));
		t.add(new Tile(3,3));
		
		Path q = new Path(t);
		
		System.out.println(q.toString());
		

	}

}
