package graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

// Objet fen�tre pour l'affichage graphique, r�agit aux actions du lavier et de la souris
public class Window extends JPanel implements KeyListener, MouseListener{

	private static final long serialVersionUID = 1L;
	
	// Objet graphique appelant
	public LocalGraphics localGraphics;
	
	// Constructeur � partir d'une grille
	public Window(int windowWidth, int windowHeight) {
		super();
		this.setOpaque(false);
		this.setBounds(0, 0, windowWidth, windowHeight);
	}
	
	// Apelle la fonction de localGraphics qui r�gle le type de dessin
	@Override
	public void keyTyped(KeyEvent ev) {    
		localGraphics.keyTyped(ev);
	}

	// M�thode de l'interface KeyListener
	@Override
	public void keyPressed(KeyEvent ev) {
	}

	// M�thode de l'interface KeyListener
	@Override
	public void keyReleased(KeyEvent ev) {    
	}

	// M�thode de l'interface MouseListener
	@Override
	public void mouseClicked(MouseEvent me) {
	}

	// Appelle la fonction onMouseClicked de l'objet graphique pour effectuer l'action sur la tule cliqu�e en fonction de l'�tat s�l�ctionn� de la souris
	@Override
	public void mousePressed(MouseEvent me) {
		localGraphics.onMouseClicked(me);
	}
	
	// M�thode de l'interface MouseListener
	@Override
	public void mouseReleased(MouseEvent me) {
	}
	
	// M�thode de l'interface MouseListener
	@Override
	public void mouseEntered(MouseEvent me) {
	}

	// M�thode de l'interface MouseListener
	@Override
	public void mouseExited(MouseEvent me) {
	}
	
}
