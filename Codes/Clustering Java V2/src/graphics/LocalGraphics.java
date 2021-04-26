package graphics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import algorithms.ClusterAttribution;
import algorithms.ImproveType;
import structure.Checkpoint;
import structure.Cluster;
import structure.Drone;
import structure.Vector;

/**
 * Objet graphique pour un affichage personnalisé et plus rapide que Draw, @see
 * {@link Draw}
 * 
 * @LouisProffitX
 * @author Louis Proffit
 * @version 1.0
 */
public class LocalGraphics implements MouseListener, KeyListener, GraphicsInterface {

	/**
	 * Le cadre
	 */
	private final Frame frame;

	/**
	 * La fenêtre
	 */
	private final Window window;

	/**
	 * L'objet graphique de la fenêtre
	 */
	private final Graphics graphics;

	/**
	 * L'état de la souris. Conditionne l'action à effectuer en cas de clic
	 */
	public MouseState mouseState;

	/**
	 * La largeur de la fenêtre
	 */
	private final int width;

	/**
	 * La hauteur de la fenêtre
	 */
	private final int height;

	/**
	 * Constructeur simple. Par défaut, la souris est sur le mode EMPTY
	 * 
	 * @param width  : la largeur de la fenêtre
	 * @param height : la hauteur de la fenêtre
	 */
	public LocalGraphics(int width, int height) {
		this.width = width;
		this.height = height;
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

	@Override
	public void updateGraphics(ClusterAttribution clusterAttribution) {
		window.reset(graphics);
		LinkedList<Drone> drones = clusterAttribution.getDrones();
		int numberOfDrones = drones.size();
		int i = 0;
		for (Drone drone : drones) {
			window.setColor(i, numberOfDrones, graphics);
			updateGraphicsForDrone(drone, clusterAttribution.getDroneCluster(drone));
			i++;
		}
	}

	/**
	 * Met à jour les graphiques pour un drone et son cluster
	 * 
	 * @param drone   : le drone
	 * @param cluster : le cluster
	 */
	private void updateGraphicsForDrone(Drone drone, Cluster cluster) {
		window.paintDrone(drone, graphics);
		window.paintCluster(cluster, graphics);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'c')
			mouseState = MouseState.CHECKPOINT;
		else if (e.getKeyChar() == 'd')
			mouseState = MouseState.DRONE;
		else if (e.getKeyChar() == 'e')
			mouseState = MouseState.EMPTY;

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point point = e.getPoint();
		Vector vector = new Vector(point.getX() / width, point.getY() / height);
		if (mouseState == MouseState.CHECKPOINT) {
			Controller.addCheckpoint(new Checkpoint(vector), ImproveType.COMPLETE);
		} else if (mouseState == MouseState.DRONE) {
			Controller.addDrone(new Drone(vector), ImproveType.COMPLETE);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * Enumération des actions possibles avec la souris
	 * 
	 * @LouisProffitX
	 * @author Louis Proffit
	 * @version 1.0
	 */
	private static enum MouseState {
		/**
		 * Supprimer
		 */
		EMPTY,

		/**
		 * Ajouter un checkpoint
		 */
		CHECKPOINT,

		/**
		 * Ajouter un drone
		 */
		DRONE
	}
}
