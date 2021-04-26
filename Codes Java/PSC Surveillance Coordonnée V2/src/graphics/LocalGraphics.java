package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import general.Checkpoint;
import general.Drone;
import general.Map;

// Objet à construire pour obtenir un rendu graphique
public class LocalGraphics {
	
	// Largeur et hauteur en pixels de la fênetre
	public int frameWidth;
	public int frameHeight;
	
	// Champs graphiques
	public Frame frame;
	public Window window;
	public Graphics graphics;

	public int checkpointSize = 11;
	public int droneSize = 10;
	
	// Construit un affichage de n*m cases, tileWidth*tileHeight pixels, à partir d'une grille grid
	public LocalGraphics(int width, int height) {		
		this.frameWidth = (int)(width + 50);
		this.frameHeight = (int)(height + 50);
		
		this.frame = new Frame(frameWidth, frameHeight);
		this.window = frame.window;
		this.graphics = window.getGraphics();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("Interruption impromptue");
		}
		window.localGraphics = this;
	}
	
	public void draw(LinkedList<Drone> drones, Map map) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0,  0,  frameWidth,  frameHeight);
		// Checkpoints
		graphics.setColor(Color.BLACK);
		for (Checkpoint checkpoint : map.getCheckpoints()) graphics.drawOval((int)checkpoint.getPosition().getX(), (int)checkpoint.getPosition().getY(), checkpointSize, checkpointSize);
		// Drones
		graphics.setColor(Color.GREEN);
		for (Drone drone : drones) graphics.fillOval((int)drone.getPosition().getX(), (int)drone.getPosition().getY(), droneSize, droneSize);
		
		
	}
	
	// Change la grille en fonction des deux paramètres writingType et writingContainer
	public void onMouseClicked(MouseEvent ev) {
		
	}
	
	// Règle le type de dessin de mouseState : e->EMPTY/EMPTY, d->EMPTY/DRONE, o->OBSTACLE/EMPTY, x->EMPTY/ENNEMY, c->CHECKPOINT/EMPTY
	public void keyTyped(KeyEvent ev) {
	}
	
	// Met à jour l'affichage
	public void refreshGraphics() {
	
	}
}
