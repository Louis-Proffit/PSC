import java.awt.Color;
import java.util.LinkedList;

import javax.swing.JFrame;

public class Controller {
	
	public static double tolerance = Math.pow(10, -7);
	public static double width = 1;
	public static double height = 1;
	public static int n = 50;
	public static int m = 50;
	public static Grid grid;
	public static Draw draw = new Draw();
	public static LinkedList<Drone> drones = new LinkedList<Drone>();
	public static LinkedList<Obstacle> obstacles = new LinkedList<Obstacle>();;
	public static LinkedList<Zone> zones = new LinkedList<Zone>();
	
	public static void init() {
		draw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		draw.setXscale(0, width);
		draw.setYscale(0, height);
		Drone drone1 = new Drone(0.9, 0.1, new Point(1, 1));
		Obstacle obstacle1 = new Obstacle(new Point[] {new Point(0, 0), new Point(0.5, 0.5), new Point(0.3, 0.7)});
		Obstacle obstacle2 = new Obstacle(new Point[] {new Point(0.8, 0.4), new Point(0.95, 0.9), new Point(0.6, 0.85), new Point(0.55, 0.53)});
		Zone zone1 = new Zone(new Point[]{new Point(0.1, 0.8), new Point(0.2, 0.8), new Point(0.2, 0.85), new Point(0.15, 0.9)}, 1);
		Zone zone2 = new Zone(new Point[]{new Point(0.8, 0.1), new Point(0.7, 0.2), new Point(0.95, 0.18)}, 1);
		/*Zone zone2 = new Zone(new Point[]{new Point(0.3, 0.02), new Point(0.4, 0.32), new Point(0.6, 0.41), new Point(0.92, 0.28)}, 2);*/
		drones.add(drone1);
		obstacles.add(obstacle1);
		obstacles.add(obstacle2);
		zones.add(zone1);
		zones.add(zone2);
		/*zones.add(zone2);*/
		grid = new Grid(width, height, n, m, obstacles, zones);
		grid.draw(draw);
	}
	
	public static void update() {
		draw.setPenColor(Color.BLUE);
		draw.setPenRadius(0.01);
		for (Drone drone : drones) {
			drone.moveDrone(grid, draw);
		}
		grid.updatePotential(drones);
		grid.draw(draw);
	}
	
	public static void main(String[] args) throws InterruptedException {
		init();
		/*for(int i = 0 ; i < 200 ; i++) {
			update();
		}*/
	}
}
	