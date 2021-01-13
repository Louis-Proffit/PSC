package Graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import Global.Grid;

// Cadre de l'objet graphique
public class Frame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	// Champ fen�tre
	Window window;
	
	// Constructeur d'un cadre � partir de sa largeur, sa hauteur et de la grille grid. D�l�gue au constructeur de Window
	Frame(int frameWidth, int frameHeight, Grid grid) {
		Window window = new Window(grid);
		window.setPreferredSize(new Dimension(frameWidth, frameHeight));
		this.window = window;
		this.setTitle("Simulation");
		this.add(window, BorderLayout.CENTER);
		this.pack();
		this.add(window);
		this.addKeyListener(window);
		this.addMouseListener(window);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
