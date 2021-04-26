package general;
import java.util.LinkedList;

public class Map {
	
	private double width;
	private double height;
	
	private LinkedList<Checkpoint> checkpoints;
	
	public Map(double width, double height) {
		this.width = width;
		this.height = height;
		this.checkpoints = new LinkedList<>();
	}
	
	public void addCheckpoint(Checkpoint checkpoint) {
		checkpoints.add(checkpoint);
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public LinkedList<Checkpoint> getCheckpoints() {
		return checkpoints;
	}

	public void setCheckpoints(LinkedList<Checkpoint> checkpoints) {
		this.checkpoints = checkpoints;
	}

	
}
