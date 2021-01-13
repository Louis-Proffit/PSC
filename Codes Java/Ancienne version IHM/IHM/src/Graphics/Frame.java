package Graphics;

import java.awt.Dimension;

import javax.swing.JFrame;

import Global.Grid;

// Cadre de l'objet graphique
public class Frame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	// Champ fenêtre
	public Window window;
	
	// Constructeur d'un cadre à partir de sa largeur, sa hauteur et de la grille grid. Délègue au constructeur de Window
	public Frame(int frameWidth, int frameHeight, Grid grid) {
		this.setTitle("Simulation");
		this.window = new Window(grid, frameWidth, frameHeight);
		this.setSize(new Dimension(frameWidth, frameHeight));
		this.getContentPane().add(window);
		this.addKeyListener(window);
		this.addMouseListener(window);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setVisible(true);
	}
}
