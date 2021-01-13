package Global;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Path {
	Tile element;
	Path prev;
	Path next;

	public Path (Tile tile) {
		this.element = tile;
		this.prev = null;
		this.next = null;
	}

	public Path () {
		this.element = null;
		this.prev = null;
		this.next = null;
	}

	// rajouter une tile au parcours
	public void insertAfter (Tile tile) {
		if(this.element == null) {
			this.element = tile;
			return;
		}
		Path p = new Path(tile);
		p.prev = this;
		// si jamais il y avait un sucesseur à this, il faut faire attention
		if(this.next != null) {
			p.next = this.next;
			p.next.prev = p;
		}
		this.next = p;
	}

	public void insertEnd(Tile tile) {
		if(!this.hasNext()) 
			this.insertAfter(tile);
		else 
			this.next.insertEnd(tile);
	}

	// constructeur à partir d'une LinkedList<Tile>
	public Path (LinkedList<Tile> tileList) {
		Path p = new Path();
		System.out.println("début de construction");
		for(Tile tile : tileList) {
			System.out.println("ajout de " + tile);
			p.insertEnd(tile);
		}
		System.out.println("fin de construction");
		this.element = p.element;
		this.next = p.next;
	}

	public int size() {
		if(this.element == null) return 0;
		
		if(this.hasNext()) 
			return 1 + this.next.size();
		else return 1;
	}

	public Tile getFirst() {
		return this.element;
	}
	
	public Tile getLast () {
		if(!this.hasNext()) 
			return this.element;
		else 
			return this.next.getLast();
	}

	public boolean hasNext() {
		return this.next!=null;
	}

	public String toString() {
		if(this.element == null) return "vide";
		Path p = this;
		String s = p.getFirst().toString();
		while(p.hasNext()) {
			p = p.next;
			s = s + p.element.toString();
		}
		s += "fin";
		return s;
	}

	public boolean contains (Tile tile) {
		if(this.element.equals(tile)) return true;
		if(this.hasNext()) {
			return this.next.contains(tile);
		}
		else {
			return false;
		}
	}
	
	public Tile get (int i) {
		if(i == 0) return this.element;
		else {
			if(this.hasNext()) 
				return this.next.get(i-1);
			else
				return null;
		}
	}
}
