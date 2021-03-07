package graphics;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Objet graphique cadre pour LocalGraphics
 * 
 * @LouisProffitX
 * @author Louis Proffit
 * @version 1.0
 */
public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * La fenêtre contenue par le cadre
	 */
	public Window window;

	/**
	 * Constructeur simple
	 * 
	 * @param width         : la largeur en pixels de la fenêtre
	 * @param height        : la hauteur en pixels de la fenêtre
	 * @param localGraphics : l'objet graphique (également listener)
	 */
	public Frame(int width, int height, LocalGraphics localGraphics) {
		this.setTitle("Simulation");
		this.window = new Window(width, height);
		this.setSize(new Dimension(width, height));
		this.getContentPane().add(window);
		this.addKeyListener(localGraphics);
		this.addMouseListener(localGraphics);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setVisible(true);
	}
}
