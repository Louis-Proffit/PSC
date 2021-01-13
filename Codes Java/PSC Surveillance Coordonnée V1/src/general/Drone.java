package general;


public class Drone {
	
	private Vector position;
	
	public Drone(double x, double y) {
		this.position = new Vector(x, y);
	}

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	public boolean moveToTarget(Checkpoint checkpoint) {
		if (checkpoint.getPosition().distance(position) < Controller.droneSpeed) {
			this.position = checkpoint.getPosition().copy();
			return true;
		}
		Vector speedVector = checkpoint.getPosition().remove(position).normalize(Controller.droneSpeed);
		position = position.add(speedVector);
		return false;
	}
}
