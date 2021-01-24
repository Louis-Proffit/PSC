import java.awt.Color;
import java.util.LinkedList;

public class Grid {
	
	public int n;
	public int m;
	public double width;
	public double height;
	public Map map; // Zones d'intérêt et obstacles (-1)
	public double[][] potential; // Potentiel en cours d'action
	public double minPotential;
	public double maxPotential;
	public double potentialGap;
	public double totalPotential;
	
	public Grid(double width, double height, int n, int m, LinkedList<Obstacle> obstacles, LinkedList<Zone> zones){
		this.n = n;
		this.m = m;
		this.width = width;
		this.height = height;
		this.potential = new double[n + 1][m + 1];
		this.map = new Map(n, m, width, height);
		for (Obstacle obstacle : obstacles) this.map.addObstacle(obstacle);
		for (Zone zone : zones) this.map.addInterestZone(zone);
		double minPotentialIntermediate = map.priorityMap[0][0];
		double maxPotentialIntermediate = map.priorityMap[0][0];
		double totalPotentialIntermediate = 0;
		double localPotential;
		for (int i = 0 ; i <= n ; i++) {
			for (int j = 0 ; j <= m ; j++) {
				if (!map.obstacleMap[i][j]) {
					localPotential = map.priorityMap[i][j];
					this.potential[i][j] = localPotential;
					if (localPotential < minPotentialIntermediate) minPotentialIntermediate = localPotential;
					if (localPotential > maxPotentialIntermediate) maxPotentialIntermediate = localPotential;
					totalPotentialIntermediate += localPotential;
				}
			}
		}
		this.totalPotential = totalPotentialIntermediate;
		this.minPotential = minPotentialIntermediate;
		this.maxPotential = maxPotentialIntermediate;
		this.potentialGap = maxPotentialIntermediate - minPotentialIntermediate;
	}
	
	public void updatePotential(LinkedList<Drone> drones) {
		// Met à jour le potentiel :
		// Augmente le potentiel dans le sillage des drones
		double potentialCurrentVariation = 0;
		double distanceSquare;
		double increase;
		for (Drone drone : drones) {
			for (int i = 0 ; i <= n ; i++) {
				for (int j = 0 ; j <= m ; j++) {
					if (!map.obstacleMap[i][j]) { // Evite les obstacles
						if ((i / n * width - drone.position.x) * drone.speedVector.x + (j / m * height - drone.position.y) * drone.speedVector.y < 0) { // Evite les points qui sont devant le drone
							distanceSquare = Math.pow((float)(i) / n * width - drone.position.x, 2) + Math.pow((float)(j) / m * height - drone.position.y, 2);
							if (distanceSquare > 0.071) { // Evite les points situés sur la ligne du drone (pour ne pas qu'il surfe
								increase = drone.potentialIncreasePerStep * Math.exp(- distanceSquare / Math.pow(drone.decreasingSpaceInfluence, 2));
								potentialCurrentVariation += increase;
								potential[i][j] += increase;
							}
						}
					}
				}
			}
		}
		
		// Le potentiel de la carte a été augmenté de potentialCurrentVariation
		// Le potentiel total de la carte est map.totalPotential
		// On diminue le potentiel de tous les points non obstacles proportionnelement à leur importance
		for (int i = 0 ; i <= n ; i++) {
			for (int j = 0 ; j <= m ; j++) {
				if (!map.obstacleMap[i][j]) {
					potential[i][j] -= (map.maximumPotential - map.priorityMap[i][j]) / totalPotential * potentialCurrentVariation;
				} 
			}
		}
		// Mise à jour des données sur le potentiel
		double minPotentialIntermediate = map.priorityMap[0][0];
		double maxPotentialIntermediate = map.priorityMap[0][0];
		double totalPotentialIntermediate = 0;
		double localPotential;
		for (int i = 0 ; i <= n ; i++) {
			for (int j = 0 ; j <= m ; j++) {
				if (!map.obstacleMap[i][j]) {
					localPotential = potential[i][j];
					if (localPotential < minPotentialIntermediate) minPotentialIntermediate = localPotential;
					if (localPotential > maxPotentialIntermediate) maxPotentialIntermediate = localPotential;
					totalPotentialIntermediate += localPotential;
				}
			}
		}
		this.totalPotential = totalPotentialIntermediate;
		this.minPotential = minPotentialIntermediate;
		this.maxPotential = maxPotentialIntermediate;
		this.potentialGap = maxPotentialIntermediate - minPotentialIntermediate;
		System.out.println(this.totalPotential);
		System.out.println(potentialCurrentVariation);
	}
	
	public void draw(Draw draw) {
		// Affiche la grille
		for (int i = 0 ; i < n ; i++) {
			for (int j = 0 ; j < m ; j++) {
				if (map.obstacleMap[i][j] & map.obstacleMap[i + 1][j] & map.obstacleMap[i + 1][j + 1]) draw.setPenColor(Color.BLACK);
				else draw.setPenColor(getColor(potential[i][j], potential[i + 1][j], potential[i + 1][j + 1]));
				draw.filledPolygon(new double[]{i * width / n, (i + 1) * width / n, (i + 1) * width / n}, new double[]{j * height / m, j * height / m, (j + 1) * height / m});
				if (map.obstacleMap[i][j] & map.obstacleMap[i][j + 1] & map.obstacleMap[i + 1][j + 1]) draw.setPenColor(Color.BLACK);
				else draw.setPenColor(getColor(potential[i][j], potential[i][j + 1], potential[i + 1][j + 1]));
				draw.filledPolygon(new double[]{i * width / n, i * width / n, (i + 1) * width / n}, new double[]{j * height / m, (j + 1) * height / m, (j + 1) * height / m});
			}
		}
	}
	
	public Color getColor(double potential1, double potential2, double potential3) {
		// Renvoie une couleur proportionelle à la hauteur du triangle
		if (potentialGap == 0) return Color.GREEN;
		double proportion = (potential1 + potential2 + potential3 - 3 * minPotential) / (3 * potentialGap);
		if (proportion < 0) proportion = 0;
		if (proportion > 1) proportion = 1;
		return new Color((int)(255 *(1 - proportion)), (int)(255 * proportion), 0);
	}
	
	public String toString() {
		String result = "";
		for (int i = 0 ; i < n ; i++) {
			for (int j = 0 ; j < m ; j++) {
				result += potential[i][j] + " ";
			}
			result += "\n";
		}
		return result;
	}
}
