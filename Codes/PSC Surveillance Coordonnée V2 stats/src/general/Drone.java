package general;

public class Drone {
	
	private Vector position;
	private Checkpoint target;
	
	public Drone(double x, double y) {
		this.position = new Vector(x, y);
	}

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	public Checkpoint getTarget() {
		return target;
	}

	public void setTarget(Checkpoint target) {
		this.target = target;
	}

	public boolean moveToTarget() {
		if (target.getPosition().distance(position) < Controller.droneSpeed) {
			this.position = target.getPosition().copy();
			return true;
		}
		Vector speedVector = target.getPosition().remove(position).normalize(Controller.droneSpeed);
		position = position.add(speedVector);
		return false;
	}
}
