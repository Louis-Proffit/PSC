package Global;

public class Checkpoint extends Tile{

	public Checkpoint(int x, int y) {
		super(x, y);
	}
	
	@Override
	public boolean equals(Object o) {
		return (this.x == ((Tile)o).x & this.y == ((Tile)o).y);
	}
}
