package Global;

// Objet drone
public class Drone extends Tile{
	
	// Constructeur à partir d'une position (x, y)
	public Drone(int x, int y){
		super(x, y);
	}
	
	// Constructeur à partir d'une case
	public Drone(Tile tile) {
		super(tile.x, tile.y, tile.isCheckpoint);
	}
	
	@Override
	public void repaint() {
		PathFinder.localGraphics.paintDrone(this);
		//PathFinder.drawInterface.paintDrone(this);
	}
}