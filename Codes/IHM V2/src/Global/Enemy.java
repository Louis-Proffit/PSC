package Global;

// Objet Ennemis
public class Enemy extends Tile{
	
	int xTarget;
	int yTarget;
	
	public Enemy(int x, int y) {
		super(x, y);
		xTarget = (int)(Math.random() * PathFinder.n);
		yTarget = (int)(Math.random() * PathFinder.m);
	}
	
	public void refresh() {
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
		x += dx;
		y += dy;
	}
}
