package graphics;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.List;

import structure.Drone;

public class LocalGraphics {

	private Frame frame;
	private Window window;
	private Graphics graphics;

	public MouseState mouseState;

	public LocalGraphics(int width, int height) {

		this.frame = new Frame(width, height, this);
		this.window = frame.window;
		this.graphics = window.getGraphics();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("Interruption impromptue");
		}
		mouseState = MouseState.EMPTY;
	}

	public void onMouseClicked(MouseEvent ev) {
		System.out.println("Implementer la méthode");
	}

	public void updateGraphics(List<Drone> drones) {
		window.reset(graphics);
		int numberOfDrones = drones.size();
		for (int i = 0; i < numberOfDrones; i++) {
			window.setColor(i, numberOfDrones, graphics);
			updateGraphicsForDrone(drones.get(i));
		}
	}

	private void updateGraphicsForDrone(Drone drone) {
		window.paintCluster(drone.getCluster(), graphics);
		window.paintDrone(drone, graphics);
	}

	private static enum MouseState {
		EMPTY, CHECKPOINT, DRONE
	}
}
