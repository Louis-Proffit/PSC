package graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

// Objet fenêtre pour l'affichage graphique, réagit aux actions du lavier et de la souris
public class Window extends JPanel implements KeyListener, MouseListener{

	private static final long serialVersionUID = 1L;
	
	// Objet graphique appelant
	public LocalGraphics localGraphics;
	
	// Constructeur à partir d'une grille
	public Window(int windowWidth, int windowHeight) {
		super();
		this.setOpaque(false);
		this.setBounds(0, 0, windowWidth, windowHeight);
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
