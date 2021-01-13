package Global;

// Objet case
public class Tile {
	
	// Coordonn�es
	public int x;
	public int y;
	
	// Type de case
	public TileType tileType = TileType.EMPTY;
	public TileContainer tileContainer = TileContainer.EMPTY;
	
	// Construit une case � partir de ses coordonn�es
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	// rend la distance avec la case en argument c
	public double distanceToCase(Tile c) {
		return Math.pow((c.x - x),2) + Math.pow((c.y - y), 2);
	}
	
	// Test l'�galit� de position avec un objet. L'�galit� en position implique l'�galit� structurelle
	@Override
	public boolean equals(Object c) {
		if (c.getClass() == Tile.class) return false;
		return (this.x == ((Tile)c).x & this.y == ((Tile)c).y);
	}
	
	// Convertit une case en cha�ne de caract�re contenant uniquement ses coordonn�es
	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}
}