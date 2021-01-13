package Global;

// Objet Ennemis
public class Enemy extends Tile{
	
	int xTarget;
	int yTarget;
	
	// Constructeur à partir d'une position (x, y)
	public Enemy(int x, int y) {
		super(x, y);
		isCheckpoint = false;
		xTarget = (int)(Math.random() * PathFinder.n);
		yTarget = (int)(Math.random() * PathFinder.m);
		System.out.println(xTarget + ";" + yTarget);
	}
	
	// Constructeur à partir d'une case
	public Enemy(Tile tile) {
		super(tile.x, tile.y, tile.isCheckpoint);
		xTarget = (int)(Math.random() * PathFinder.n);
		yTarget = (int)(Math.random() * PathFinder.m);
		System.out.println(xTarget + ";" + yTarget);
	}
	
	@Override
	public void repaint() {
		PathFinder.localGraphics.paintEnemy(this);
		//PathFinder.drawInterface.paintEnemy(this);
	}
	
	public void refresh() {
		PathFinder.grid.tilesGrid[this.x][this.y] = new Tile(this);
		PathFinder.grid.toRepaint.add(PathFinder.grid.tilesGrid[this.x][this.y]);
		int xPath = this.x - xTarget;
		int yPath = this.y - yTarget;
		int dx = 0;
		int dy = 0;
		if ((xPath > 0) & x < PathFinder.n  - 1) {
			dx = -1;
		}
		if ((xPath < 0) & x > 0) {
			dx = +1;
		}
		if ((yPath > 0) & y < PathFinder.m  - 1) {
			dy = -1;
		}
		if ((yPath < 0) & y > 0) {
			dy = +1;
		}
		Enemy newEnemy = new Enemy(this.x + dx, this.y + dy);
		if (PathFinder.grid.tilesGrid[this.x + dx][this.y + dy].isCheckpoint) newEnemy.isCheckpoint = true;
		PathFinder.grid.tilesGrid[this.x][this.y] = newEnemy;
		PathFinder.grid.toRepaint.add(newEnemy);
	}
}
