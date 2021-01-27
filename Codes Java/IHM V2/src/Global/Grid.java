package Global;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

// Objet grille
public class Grid{

	// Dimensions
	public int n;
	public int m;

	// Listes des éléments présents sur la grille
	public LinkedList<Drone> drones = new LinkedList<Drone>();
	public LinkedList<Obstacle> obstacles = new LinkedList<Obstacle>();	
	public LinkedList<Checkpoint> checkpoints = new LinkedList<Checkpoint>();
	public LinkedList<Enemy> enemies = new LinkedList<Enemy>();	

	// Chemin de cases contigües qui relie les checkpoints
	public LinkedList<Tile> path;
	
	public Grid(int n, int m){
		// Constructeur à partir d'une taille (n, m)
		this.n = n;
		this.m = m;
	}
	
	public boolean isDrone(int x, int y) {
		for(Drone drone : drones) {
			if (drone.x == x & drone.y == y) return true;
		}
		return false;
	}
	
	public boolean isEnemy(int x, int y) {
		for (Enemy enemy : enemies) {
			if (enemy.x == x & enemy.y == y) return true;
		}
		return false;
	}
	
	public boolean isObstacle(int x, int y) {
		for(Obstacle obstacle : obstacles) {
			if (obstacle.x == x & obstacle.y == y) return true;
		}
		return false;
	}
	
	public boolean isCheckpoint(int x, int y) {
		for(Checkpoint checkpoint : checkpoints) {
			if (checkpoint.x == x & checkpoint.y == y) return true;
		}
		return false;
	}
}
