package Graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.JPanel;

import Global.Drone;
import Global.Enemy;
import Global.Grid;
import Global.Obstacle;
import Global.PathFinder;
import Global.Tile;

// Objet fenêtre pour l'affichage graphique, réagit aux actions du lavier et de la souris
public class Window extends JPanel implements KeyListener, MouseListener{

	private static final long serialVersionUID = 1L;
	
	// Objet graphique appelant
	public LocalGraphics localGraphics;
	
	// Dimensions
	public float tileWidth;
	public float tileHeight;
	public int tileWidthInt;
	public int tileHeightInt;
	public int windowWidth;
	public int windowHeight;
	
	// Grille grid
	public Grid grid;
	
	// Constructeur à partir d'une grille
	public Window(Grid grid, int windowWidth, int windowHeight) {
		super();
		this.setOpaque(false);
		this.setBounds(0, 0, windowWidth, windowHeight);
		this.setBackground(Color.WHITE);
		this.grid = grid;
		this.tileWidth = PathFinder.frameWidth / PathFinder.n;
		this.tileHeight = PathFinder.frameHeight / PathFinder.m;
		this.tileWidthInt = (int)tileWidth;
		this.tileHeightInt = (int)tileHeight;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
	}
	
	public void reset(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, windowWidth, windowHeight);
	}
	
	public void paintDrone(Drone drone, Graphics g) {
		g.setColor(Color.GREEN);
		g.fillOval((int)(drone.x * tileWidth), (int)(drone.y * tileHeight), tileWidthInt, tileHeightInt);
	}
	
	public void paintObstacle(Obstacle obstacle, Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect((int)(obstacle.x * tileWidth), (int)(obstacle.y * tileHeight), tileWidthInt, tileHeightInt);
	}
	
	public void paintEnemy(Enemy enemy, Graphics g) {
		g.setColor(Color.RED);
		g.fillOval((int)(enemy.x * tileWidth), (int)(enemy.y * tileHeight), tileWidthInt, tileHeightInt);
	}
	
	public void paintEmpty(Tile tile, Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect((int)(tile.x * tileWidth), (int)(tile.y * tileHeight), tileWidthInt, tileHeightInt);
	}
	
	public void paintCheckpoint(Tile tile, Graphics g) {
		g.setColor(Color.BLACK);
		g.drawOval((int)(tile.x * tileWidth + 0.25 * tileWidthInt), (int)(tile.y * tileHeight + 0.25 * tileWidthInt), (int)(0.5 * tileWidthInt), (int)(0.5 * tileWidthInt));
	}
	
	public void paintPath(LinkedList<Tile> path, Graphics g) {
		if (path == null ) return ;
		int size = path.size();
		if (size <= 2) return;
		g.setColor(Color.BLACK);
		for (int i = 0 ; i < size - 1 ; i++) {
			g.drawLine((int)(path.get(i).x * tileWidth), (int)(path.get(i).y * tileHeight), (int)(path.get(i + 1).x * tileWidth), (int)(path.get(i + 1).y * tileHeight));
		}
		g.drawLine((int)(path.get(size - 1).x * tileWidth), (int)(path.get(size - 1).y * tileHeight), (int)(path.get(0).x * tileWidth), (int)(path.get(0).y * tileHeight));
	}
	
	public void paintPath(Graphics g) {
		if (grid.path == null ) return ;
		int size = grid.path.size();
		if (size <= 2) return;
		g.setColor(Color.BLACK);
		int n = grid.path.size();
		if (n <= 1) return;
		for (int i = 0 ; i < n - 1 ; i++) {
			paintLine(grid.path.get(i), grid.path.get(i + 1), g);
		}
		paintLine(grid.path.get(0), grid.path.getLast(), g);
	}

	public void paintLine(Tile tile1, Tile tile2, Graphics g) {
		int x1 = (int)((tile1.x + 0.5)* tileWidth);
		int y1 = (int)((tile1.y + 0.5) * tileHeight);
		int x2 = (int)((tile2.x + 0.5) * tileWidth);
		int y2 = (int)((tile2.y + 0.5) * tileHeight);
		g.drawLine(x1, y1, x2, y2);
	}
	
	// Apelle la fonction de localGraphics qui règle le type de dessin
	@Override
	public void keyTyped(KeyEvent ev) {    
		localGraphics.keyTyped(ev);
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
