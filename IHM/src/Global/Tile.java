package Global;

// Objet case
public class Tile {
	
	// Coordonnées (en cases)
	public int x;
	public int y;
	
	// Est un checkpoint
	public boolean isCheckpoint;
	
	// Construit une case à partir de ses coordonnées
	public Tile(int x, int y) {
			this.x = x;
			this.y = y;
		}
	
	//Construit une case à partir d'une case (eventuellement drone, 
	public Tile(Tile tile) {
		this.x = tile.x;
		this.y = tile.y;
		this.isCheckpoint = tile.isCheckpoint;
	}
	
	// Construit une case à partir de ses coordonnées
	public Tile(int x, int y, boolean isCheckpoint) {
		this.x = x;
		this.y = y;
		this.isCheckpoint = isCheckpoint;
	}
	
	// rend la distance avec la case en argument c
	public double distanceToCase(Tile c) {
		return Math.sqrt(Math.pow((c.x - x),2) + Math.pow((c.y - y), 2));
	}
	
	// Test l'égalité de position avec un objet. L'égalité en position implique l'égalité structurelle
	@Override
	public boolean equals(Object c) {
		return (this.x == ((Tile)c).x & this.y == ((Tile)c).y);
	}
	
	// Convertit une case en chaîne de caractère contenant uniquement ses coordonnées
	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}
	
	// Peint une case vide
	public void repaint() {
		PathFinder.localGraphics.paintEmpty(this);
		//PathFinder.drawInterface.paintEmpty(this);
	}
}