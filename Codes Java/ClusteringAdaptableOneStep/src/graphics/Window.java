package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import structure.Checkpoint;
import structure.Drone;
import structure.Vector;

/**
 * Fenêtre graphique pour un {@link LocalGraphics}
 * 
 * @LouisProffitX
 * @author Louis Proffit
 * @version 1.0
 */
public class Window extends JPanel {

	private static final long serialVersionUID = 1L;

	public static final double droneSize = 0.015;
	public static final double checkpointSize = 0.01;

	/**
	 * La largeur
	 */
	private final int width;

	/**
	 * La hauteur
	 */
	private final int height;

	/**
	 * 95% de la largeur, pour garder une marge
	 */
	private final int resizedWidth;

	/**
	 * 95% de la hauteur, pour garder une marge
	 */
	private final int resizedHeight;

	/**
	 * Constructeur simple
	 * 
	 * @param width  : la largeur de la fenêtre
	 * @param height : la hauteur de la fenêtre
	 */
	public Window(int width, int height) {
		super();
		this.setOpaque(false);
		this.setBounds(0, 0, width, height);
		this.setBackground(Color.WHITE);
		this.width = width;
		this.height = height;
		this.resizedWidth = (int) (width * 0.95);
		this.resizedHeight = (int) (width * 0.95);
	}

	/**
	 * Configure une couleur dans une palette
	 * 
	 * @param index    : l'indice de la couleur dans la palette
	 * @param size     : la taille de la palette
	 * @param graphics : l'objet auquel appliquer la nouvelle couleur
	 */
	public void setColor(int index, int size, Graphics graphics) {
		graphics.setColor(Color.getHSBColor(((float) index) / ((float) size), 1f, 1f));
	}

	/**
	 * Remet à jour le graphisme de la fenêtre
	 * 
	 * @param g
	 */
	public void reset(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
	}

	/**
	 * Dessine un drone
	 * 
	 * @param drone : le drone
	 */
	public void paintDrone(Drone drone, Graphics g) {
		int size = getRoundedX(droneSize);
		int x = getRoundedX(drone.getX() - droneSize / 2);
		int y = getRoundedX(drone.getY() - droneSize / 2);
		g.fillOval(x, y, size, size);
	}

	/**
	 * Dessine un checkpoint
	 * 
	 * @param checkpoint : le checkpoint
	 */
	private void paintCheckpoint(Checkpoint checkpoint, Graphics g) {
		int size = getRoundedX(checkpointSize);
		int x = getRoundedX(checkpoint.getX() - checkpointSize / 2);
		int y = getRoundedX(checkpoint.getY() - checkpointSize / 2);
		g.fillOval(x, y, size, size);
	}

	/**
	 * Dessine un cluster
	 * 
	 * @param cluster : le cluster
	 */
	public void paintCluster(List<Checkpoint> checkpoints, Graphics g) {
		int size = checkpoints.size();
		if (size == 0)
			return;
		if (size == 1) {
			paintCheckpoint(checkpoints.get(0), g);
			return;
		}
		for (int i = 0; i < size - 1; i++) {
			paintCheckpoint(checkpoints.get(i), g);
			paintLine(checkpoints.get(i), checkpoints.get(i + 1), g);
		}
		paintCheckpoint(checkpoints.get(size - 1), g);
		paintLine(checkpoints.get(size - 1), checkpoints.get(0), g);
	}

	/**
	 * Fonction auxiliaire pour dessiner une ligne
	 * 
	 * @param firstPosition  : la première extrémité de la ligne
	 * @param secondPosition : la seconde extrémité de la ligne
	 */
	private void paintLine(Vector firstPosition, Vector secondPosition, Graphics g) {
		g.drawLine(getRoundedX(firstPosition.getX()), getRoundedX(firstPosition.getY()),
				getRoundedX(secondPosition.getX()), getRoundedX(secondPosition.getY()));
	}

	/** Récupère un x resized en pixel à partir d'un x dans le cadre */
	public int getRoundedX(double x) {
		return (int) (x * resizedWidth);
	}

	/** Récupère un y resized en pixel à partir d'un y dans le cadre */
	public int getRoundedY(double y) {
		return (int) (y * resizedHeight);
	}
}
