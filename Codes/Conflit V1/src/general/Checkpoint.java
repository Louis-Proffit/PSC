package general;

public class Checkpoint {
	
	private Vector position;
	
	private double satisfaction; // Satisfaction du checkpoint, égale au temps écoulé depuis le dernier passage d'un drone
	
	public Checkpoint(double x, double y) {
		this.position = new Vector(x, y);
		this.satisfaction = 1;
	}

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	public double getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(double satisfaction) {
		this.satisfaction = satisfaction;
	}
	
	public void evolveSatisfaction() {
		this.satisfaction = Math.pow(Math.pow(satisfaction, 0.5) + 1, 2);
		/*this.satisfaction += 1;*/
	}
}
