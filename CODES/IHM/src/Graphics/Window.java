package Graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import Global.Grid;
import Global.MouseState;
import Global.PathFinder;
import Global.Tile;

// Objet fenêtre pour l'affichage graphique, réagit aux actions du lavier et de la souris
public class Window extends JPanel implements KeyListener, MouseListener{

	private static final long serialVersionUID = 1L;
	
	// Grille grid
	Grid grid;
	
	// Objet graphique appelant
	LocalGraphics localGraphics;
	
	// Constructeur à partir d'une grille
	Window(Grid grid) {
		this.grid = grid;
	}
	
	// Méthode paintComponent qui dessine les drones, obstacles, checkpoints, ennemis et le chemin
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		float tileWidth = PathFinder.frameWidth / PathFinder.n;
		float tileHeight = PathFinder.frameHeight / PathFinder.m;
		int tileWidthInt = (int)tileWidth;
		int tileHeightInt = (int)tileHeight;
		
		g.setColor(Color.GREEN);
		for (Tile drone : grid.drones) g.fillOval((int)(drone.x * tileWidth), (int)(drone.y * tileHeight), tileWidthInt, tileHeightInt);
		
		g.setColor(Color.BLACK);
		for (Tile obstacle : grid.obstacles) g.fillRect((int)(obstacle.x * tileWidth), (int)(obstacle.y * tileHeight), tileWidthInt, tileHeightInt);
		
		g.setColor(Color.BLACK);
		for (Tile checkpoint : grid.checkpoints) g.drawOval((int)(checkpoint.x * tileWidth), (int)(checkpoint.y * tileHeight), tileWidthInt, tileHeightInt);
		
		g.setColor(Color.RED);
		for (Tile ennemy : grid.ennemies) g.fillOval((int)(ennemy.x * tileWidth), (int)(ennemy.y * tileHeight), tileWidthInt, tileHeightInt);
		
		g.setColor(Color.BLACK);
		int l = grid.path.size();
		for (int i = 0 ; i < l - 1 ; i++) drawLine(grid.path.get(i).x, grid.path.get(i).y, grid.path.get(i + 1).x, grid.path.get(i + 1).y, g, tileWidth, tileHeight);
		drawLine(grid.path.get(l - 1).x, grid.path.get(l - 1).y, grid.path.get(0).x, grid.path.get(0).y, g, tileWidth, tileHeight);
	}
	
	// Fonction auxiliaire qui dessine une ligne entre le centre des cass (i1, j1) et (i2, j2) sur le graphique g sachant la longueur et la largeur d'une tuile
	private void drawLine(int i1, int j1, int i2, int j2, Graphics g, float tileWidth, float tileHeight) {
		int x1 = (int)((i1 + 0.5) * tileWidth);
		int y1 = (int)((j1 + 0.5) * tileHeight);
		int x2 = (int)((i2 + 0.5) * tileWidth);
		int y2 = (int)((j2 + 0.5) * tileHeight);
		g.drawLine(x1, y1, x2, y2);
	}
	
	// Règle le type de dessin de localGraphics : e->EMPTY/EMPTY, d->EMPTY/DRONE, o->OBSTACLE/EMPTY, x->EMPTY/ENNEMY, c->CHECKPOINT/EMPTY
	@Override
	public void keyTyped(KeyEvent ev) {    
		char key = ev.getKeyChar();
		
		// Quitter
		if (key == 'q') System.exit(0);
		// Empty : gomme
		else if (key == 'e') localGraphics.mouseState = MouseState.EMPTY;
		// Drone
		else if (key == 'd') localGraphics.mouseState = MouseState.DRONE;
		// Obstacle
		else if (key == 'o') localGraphics.mouseState = MouseState.OBSTACLE;
		// Checkpoint
		else if (key == 'c') localGraphics.mouseState = MouseState.CHECKPOINT;
		// Ennemi
		else if (key == 'x') localGraphics.mouseState = MouseState.ENNEMY;
	}

	// Méthode de l'interface KeyListener
	@Override
	public void keyPressed(KeyEvent ev) {
	}

	// Méthode de l'interface KeyListener
	@Override
	public void keyReleased(KeyEvent ev) {    
	}

	// Méthode de l'interface MouseListener
	@Override
	public void mouseClicked(MouseEvent me) {
	}

	// Appelle la fonction onMouseClicked de l'objet graphique pour effectuer l'action sur la tule cliquée en fonction de l'état séléctionné de la souris
	@Override
	public void mousePressed(MouseEvent me) {
		localGraphics.onMouseClicked(me);
	}
	
	// Méthode de l'interface MouseListener
	@Override
	public void mouseReleased(MouseEvent me) {
	}
	
	// Méthode de l'interface MouseListener
	@Override
	public void mouseEntered(MouseEvent me) {
	}

	// Méthode de l'interface MouseListener
	@Override
	public void mouseExited(MouseEvent me) {
	}
	
}
