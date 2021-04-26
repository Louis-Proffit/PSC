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
	
	public Grid(double width, double height, int n, int m, LinkedList<Zone> zones){
		this.n = n;
		this.m = m;
		this.width = width;
		this.height = height;
		this.potential = new double[n + 1][m + 1];
		this.map = new Map(n, m, width, height);
		for (Zone zone : zones) this.map.addInterestZone(zone);
		double minPotentialIntermediate = map.priorityMap[0][0];
		double maxPotentialIntermediate = map.priorityMap[0][0];
		double localPotential;
		for (int i = 0 ; i <= n ; i++) {
			for (int j = 0 ; j <= m ; j++) {
				localPotential = map.priorityMap[i][j];
				this.potential[i][j] = localPotential;
				if (localPotential < minPotentialIntermediate) minPotentialIntermediate = localPotential;
				if (localPotential > maxPotentialIntermediate) maxPotentialIntermediate = localPotential;
			}
		}
		this.minPotential = minPotentialIntermediate;
		this.maxPotential = maxPotentialIntermediate;
	}
	
	public void updatePotential(LinkedList<Drone> drones) {
		double distanceSquare;
		
		// Pour chaque drone , on modifie le potentiel exponentiellement autour de lui
		for (Drone drone : drones) {
			for (int i = 0 ; i <= n ; i++) {
				for (int j = 0 ; j <= m ; j++) {
					distanceSquare = Math.pow((float)(i) / n * width - drone.position.x, 2) + Math.pow((float)(j) / m * height - drone.position.y, 2);
					potential[i][j] += drone.decreasingSpaceInfluence / distanceSquare;
				}
			}
		}
		// On diminue le potentiel de tous les points non obstacles proportionnelement à leur importance
		for (int i = 0 ; i <= n ; i++) {
			for (int j = 0 ; j <= m ; j++) {
				potential[i][j] -= map.priorityMap[i][j];
			}
		}
		// Mise à jour des données sur le potentiel
		double minPotentialIntermediate = map.priorityMap[0][0];
		double maxPotentialIntermediate = map.priorityMap[0][0];
		double localPotential;
		for (int i = 0 ; i <= n ; i++) {
			for (int j = 0 ; j <= m ; j++) {
				localPotential = potential[i][j];
				if (localPotential < minPotentialIntermediate) minPotentialIntermediate = localPotential;
				if (localPotential > maxPotentialIntermediate) maxPotentialIntermediate = localPotential;
			}
		}
		this.minPotential = minPotentialIntermediate;
		this.maxPotential = maxPotentialIntermediate;
	}
	
	public void draw(Draw draw) {
		// Affiche la grille
		for (int i = 0 ; i < n ; i++) {
			for (int j = 0 ; j < m ; j++) {
				draw.setPenColor(getColor(potential[i][j], potential[i + 1][j], potential[i + 1][j + 1]));
				draw.filledPolygon(new double[]{i * width / n, (i + 1) * width / n, (i + 1) * width / n}, new double[]{j * height / m, j * height / m, (j + 1) * height / m});
				draw.setPenColor(getColor(potential[i][j], potential[i][j + 1], potential[i + 1][j + 1]));
				draw.filledPolygon(new double[]{i * width / n, i * width / n, (i + 1) * width / n}, new double[]{j * height / m, (j + 1) * height / m, (j + 1) * height / m});
			}
		}
	}
	
	public Color getColor(double potential1, double potential2, double potential3) {
		// Renvoie une couleur proportionelle à la hauteur du triangle
		double potentialGap = this.maxPotential - this.minPotential;
		if (potentialGap == 0) return Color.GREEN;
		double proportion = (potential1 + potential2 + potential3 - 3 * minPotential) / (3 * potentialGap);
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
