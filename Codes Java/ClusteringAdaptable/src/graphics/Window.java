package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.JPanel;
import structure.Checkpoint;
import structure.Cluster;
import structure.Drone;
import structure.Vector;

public class Window extends JPanel implements KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;

	public static final double droneSize = 0.015;
	public static final double checkpointSize = 0.01;

	private final int width;
	private final int height;
	private final LocalGraphics localGraphics;

	public Window(int width, int height, LocalGraphics localGraphics) {
		super();
		this.setOpaque(false);
		this.setBounds(0, 0, width, height);
		this.setBackground(Color.WHITE);
		this.width = width;
		this.height = height;
		this.localGraphics = localGraphics;
	}

	public void setColor(int index, int size, Graphics graphics) {
		graphics.setColor(Color.getHSBColor(((float) index) / ((float) size), 1f, 1f));
	}

	public void reset(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
	}

	public void paintDrone(Drone drone, Graphics g) {
		int size = getRoundedX(droneSize);
		int x = getRoundedX(drone.getPosition().getX() - droneSize / 2);
		int y = getRoundedX(drone.getPosition().getY() - droneSize / 2);
		g.fillOval(x, y, size, size);
	}

	private void paintCheckpoint(Checkpoint checkpoint, Graphics g) {
		int size = getRoundedX(checkpointSize);
		int x = getRoundedX(checkpoint.getPosition().getX() - checkpointSize / 2);
		int y = getRoundedX(checkpoint.getPosition().getY() - checkpointSize / 2);
		g.fillOval(x, y, size, size);
	}

	public void paintCluster(Cluster cluster, Graphics g) {
		LinkedList<Checkpoint> checkpoints = cluster.getCheckpointsOrdered();
		if (checkpoints.size() == 0)
			return;
		if (checkpoints.size() == 1) {
			paintCheckpoint(checkpoints.getFirst(), g);
			return;
		}
		for (int i = 0; i < checkpoints.size() - 1; i++) {
			paintCheckpoint(checkpoints.get(i), g);
			paintLine(checkpoints.get(i).getPosition(), checkpoints.get(i + 1).getPosition(), g);
		}
		paintCheckpoint(checkpoints.getLast(), g);
		paintLine(checkpoints.getLast().getPosition(), checkpoints.getFirst().getPosition(), g);
	}

	private void paintLine(Vector firstPosition, Vector secondPosition, Graphics g) {
		g.drawLine(getRoundedX(firstPosition.getX()), getRoundedX(firstPosition.getY()),
				getRoundedX(secondPosition.getX()), getRoundedX(secondPosition.getY()));
	}

	public int getRoundedX(double x) {
		return (int) (x * width);
	}

	public int getRoundedY(double y) {
		return (int) (y * height);
	}

	@Override
	public void keyTyped(KeyEvent ev) {

	}

	@Override
	public void keyPressed(KeyEvent ev) {
	}

	@Override
	public void keyReleased(KeyEvent ev) {
	}

	@Override
	public void mouseClicked(MouseEvent me) {
	}

	@Override
	public void mousePressed(MouseEvent me) {
		localGraphics.onMouseClicked(me);
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	}

	@Override
	public void mouseEntered(MouseEvent me) {
	}

	@Override
	public void mouseExited(MouseEvent me) {
	}

}
