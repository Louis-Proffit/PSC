package Graphics;

import java.awt.Point;
import java.awt.event.MouseEvent;

import Global.Grid;
import Global.MouseState;
import Global.Tile;
import Global.TileContainer;
import Global.TileType;

// Objet à construire pour obtenir un rendu graphique
public class LocalGraphics {
	
	// Nombre de cases en abscisses et en ordonnées
	public int n;
	public int m;
	
	// Largeur et hauteur en pixels de la fênetre
	public int frameWidth;
	public int frameHeight;
	
	// Largeur et hauteur en pixels d'une case
	public float tileWidth;
	public float tileHeight;
	
	// Champs graphiques
	public Frame frame;
	public Window window;
	
	//Grille
	public Grid grid;
	
	// Etat de la souris, conditionne l'action à effectuer si on clique
	public MouseState mouseState;
	
	// Construit un affichage de n*m cases, tileWidth*tileHeight pixels, à partir d'une grille grid
	public LocalGraphics(int n, int m, int frameWidth, int frameHeight, Grid grid) {
		this.n = n;
		this.m = m;
		
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		
		this.tileWidth = frameWidth / n;
		this.tileHeight = frameHeight / m;
		
		this.grid = grid;
		this.frame = new Frame(frameWidth, frameHeight, grid);
		this.window = frame.window;
		window.localGraphics = this;
		mouseState = MouseState.EMPTY;
	}
	
	// Renvoie les coordonnées en nombre de cases d'un point. Fonction auxilliaire pour la méthode onMouseClicked
	public int[] getTileFromPoint(Point P) {
		int x = P.x - 8;
		int y = P.y - 31;
		
		int i = (int)(x / tileWidth);
		int j = (int)(y / tileHeight);
		
		i = Math.min(Math.abs(i), n);
		j = Math.min(Math.abs(j), m);
		
		return new int[] {i, j};
	}
	
	// Change la grille en fonction des deux paramètres writingType et writingContainer
	public void onMouseClicked(MouseEvent me) {
		
		int[] tileCoordinate = getTileFromPoint(me.getPoint());
		
		int i = tileCoordinate[0];
		int j = tileCoordinate[1];
		
		Tile tile = grid.grid[i][j];
		
		switch (mouseState) {
		
		// Dans tous les cas, on reset la case
		case EMPTY :
			if (tile.tileContainer == TileContainer.DRONE) grid.drones.remove(tile);
			else if (tile.tileContainer == TileContainer.ENNEMY) grid.ennemies.remove(tile);
			if (tile.tileType == TileType.OBSTACLE) grid.obstacles.remove(tile);
			else if (tile.tileType == TileType.CHECKPOINT) grid.checkpoints.remove(tile);
			tile.tileType = TileType.EMPTY;
			tile.tileContainer = TileContainer.EMPTY;
			break;
			
		// On ne peut ajouter un drone que sur une case de type différent de obstacle et de contenu vide
		case DRONE :
			if ((tile.tileContainer == TileContainer.EMPTY) & (tile.tileType != TileType.OBSTACLE)) {
				tile.tileContainer = TileContainer.DRONE;
				grid.drones.add(tile);
			}
			break;
		
		// On ne peut ajouter un obstacle que sur une case de type vide et de contenu vide
		case OBSTACLE :
			if ((tile.tileContainer == TileContainer.EMPTY) & (tile.tileType == TileType.EMPTY)) {
				tile.tileType = TileType.OBSTACLE;
				grid.obstacles.add(tile);
			}
			break;
		
		// On ne peut ajouter un checkpoint que sur une case de type vide
		case CHECKPOINT :
			if (tile.tileType == TileType.EMPTY) {
				tile.tileType = TileType.CHECKPOINT;
				grid.checkpoints.add(tile);
			}
			break;
		
		// On ne peut ajouter un ennemi que sur une case de type vide et de contenu vide
		case ENNEMY :
			if ((tile.tileContainer == TileContainer.EMPTY) & (tile.tileType == TileType.EMPTY)) {
				tile.tileContainer = TileContainer.ENNEMY;
				grid.ennemies.add(tile);
			}
			break;
		}
	}

	
	// Met à jour l'affichage
	public void refreshGraphics() {
		window.repaint();
	}
	
	// Met à jour la position des ennemis
	public void refreshEnemy() {
	}
	
	
	// Met à jour la position des drones
	public void refreshDrones() {
	}
}
