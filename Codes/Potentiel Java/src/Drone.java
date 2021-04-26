import java.util.LinkedList;

public class Drone {

	public Point position;
	public Point speedVector;
	public double maxSpeedValue = 0.05;
	public LinkedList<Point> trajectory;
	public double proportionOfInertia = 0.3; // Le vecteur d'évoluion est une proportion du vecteur vitesse initial et du gradient
	public double decreasingSpaceInfluence = 0.1;
	public double potentialIncreasePerStep = 0.1;
	
	public Drone(double x, double y, double maxSpeedValue, double proportionOfInertia, Point initialDirection) {
		this.position = new Point(x, y);
		this.maxSpeedValue = maxSpeedValue;
		this.proportionOfInertia = proportionOfInertia;
		this.trajectory = new LinkedList<Point>();
		this.speedVector = initialDirection.normalize().mult(maxSpeedValue);
	}
	
	public Drone(double x, double y, Point initialDirection) {
		this.position = new Point(x, y);
		this.trajectory = new LinkedList<Point>();
		this.speedVector =  initialDirection.normalize().mult(maxSpeedValue);
	}
	
	public void moveDrone(Grid grid, Draw draw) {
		trajectory.add(position.copy());
		draw.point(position.x,  position.y);
		int i = (int)(position.x * grid.n / grid.width);
		int j = (int)(position.y * grid.m / grid.height);
		boolean lower = (position.x - i * grid.width / grid.n >= position.y - j * grid.height / grid.m);
		Point gradient;
		if (lower) gradient = new Point(grid.potential[i][j] - grid.potential[i + 1][j], grid.potential[i][j] - grid.potential[i][j + 1]);
		else gradient = new Point(grid.potential[i][j + 1] - grid.potential[i + 1][j + 1], grid.potential[i + 1][j] - grid.potential[i + 1][j + 1]);
		Point evolution = gradient.normalize().mult(1 - proportionOfInertia).add(speedVector.normalize().mult(proportionOfInertia));
		evolution.mult(maxSpeedValue);
		if (position.x + evolution.x > 0 & position.x + evolution.x < Controller.width) {
			this.position.x = position.x + evolution.x;
			this.speedVector.x = evolution.x;
		}
		else this.speedVector.x = 0;
		if (position.y + evolution.y > 0 & position.y + evolution.y < Controller.height) {
			this.position.y = position.y + evolution.y;
			this.speedVector.y = evolution.y;
		}
		else this.speedVector.y = 0;
	}
}
