package Global;

// Objet case
public class Tile {
	
	public int x;
	public int y;
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public double distanceToCase(Tile c) {
		return Math.sqrt(Math.pow((c.x - x),2) + Math.pow((c.y - y), 2));
	}
	
	@Override
	public boolean equals(Object c) {
		return ((this.x == ((Tile)c).x) & (this.y == ((Tile)c).y));
	}
}