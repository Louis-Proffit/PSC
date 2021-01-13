package Global;

public class Obstacle extends Tile{

	// Constructeur à partir d'une position (x, y)
	public Obstacle(int x, int y) {
		super(x, y);
		super.isCheckpoint = false;
	}
	
	// Constructeur à partir d'une case
	public Obstacle(Tile tile) {
		super(tile.x, tile.y);
	}
	
	@Override
	public void repaint() {
		PathFinder.localGraphics.paintObstacle(this);
		//PathFinder.drawInterface.paintObstacle(this);
	}
}
