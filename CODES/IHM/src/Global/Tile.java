package Global;

// Objet case
public class Tile {
	
	// Coordonnées
	public int x;
	public int y;
	
	// Type de case
	public TileType tileType = TileType.EMPTY;
	public TileContainer tileContainer = TileContainer.EMPTY;
	
	// Construit une case à partir de ses coordonnées
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	// rend la distance avec la case en argument c
	public double distanceToCase(Tile c) {
		return Math.pow((c.x - x),2) + Math.pow((c.y - y), 2);
	}
	
	// Test l'égalité de position avec un objet. L'égalité en position implique l'égalité structurelle
	@Override
	public boolean equals(Object c) {
		if (c.getClass() == Tile.class) return false;
		return (this.x == ((Tile)c).x & this.y == ((Tile)c).y);
	}
	
	// Convertit une case en chaîne de caractère contenant uniquement ses coordonnées
	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}
}