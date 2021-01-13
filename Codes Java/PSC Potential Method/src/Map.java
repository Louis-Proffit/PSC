import java.util.LinkedList;

public class Map {
	
	public int n;
	public int m;
	public double width;
	public double height;
	public double[][] priorityMap; // La carte du potentiel. Le obstacles sont à 0. 
	public boolean[][] obstacleMap;
	public int numberOfNonObstaclePoints;
	public double totalPotential; // Potential total sur la carte hors des obstacles;
	public double maximumPotential; // Potentiel maximal sur la carte

	public Map(int n, int m, double width, double height) {
		this.n = n;
		this.m = m;
		this.width = width;
		this.height = height;
		this.priorityMap = new double[n + 1][m + 1];
		this.obstacleMap = new boolean[n + 1][m + 1];
		this.numberOfNonObstaclePoints = n * m;
		this.totalPotential = 0;
		this.maximumPotential = 0;
	}
	
	public void addObstacle(Obstacle obstacle) {
		// Ajoute un obstacle en augmentant le potentiel de toute la carte proportionnelement à la distance à l'obstacle
		LinkedList<Integer[]> interiorPoints = obstacle.getInteriorPoints(width, height, n, m);
		int numberOfNewObstaclePoints = 0;
		int numberOfInteriorPoints = interiorPoints.size();
		for (Integer[] coordinates : interiorPoints) obstacleMap[coordinates[0]][coordinates[1]] = true;
		double distanceSquare;
		for (int i = 0 ; i <= n ; i++) {
			for (int j = 0 ; j <= m ; j++) {
				if (!obstacleMap[i][j]) {
					numberOfNewObstaclePoints += 1;
					for (int k = 0 ; k < numberOfInteriorPoints ; k++) {
						distanceSquare = Math.pow((float)(i - interiorPoints.get(k)[0]) / n * width, 2) + Math.pow((float)(j - interiorPoints.get(k)[1]) / m * height, 2);
						priorityMap[i][j] += obstacle.obstaclePotential / numberOfInteriorPoints * Math.exp(- distanceSquare / Math.pow(obstacle.decreasingSpaceInfluence, 2));
					}
				}
			}
		}
		numberOfNonObstaclePoints -= numberOfNewObstaclePoints;
		double maximumPotential = 0;
		double totalPotential = 0;
		for (int i = 0 ; i <= n ; i++) {
			for (int j = 0 ; j <= m ; j++) {
				if (!obstacleMap[i][j]) {
					totalPotential += priorityMap[i][j];
					if (priorityMap[i][j] < maximumPotential) maximumPotential = priorityMap[i][j];
				}
			}
		}
		this.totalPotential = totalPotential;
		this.maximumPotential = maximumPotential;
	}
	
	public void addInterestZone(Zone zone) {
		// Ajoute une zone d'intérêt en augmentant le potentiel de tous les points proportionnelement à leuyr distance à la zone
		LinkedList<Integer[]> interiorPoints = zone.getInteriorPoints(width, height, n, m);
		int numberOfInteriorPoints = interiorPoints.size();
		double distanceSquare;
		for (int i = 0 ; i <= n ; i++) {
			for (int j = 0 ; j <= m ; j++) {
				for (int k = 0 ; k < numberOfInteriorPoints ; k++) {
					distanceSquare = Math.pow((float)(i - interiorPoints.get(k)[0]) / n * width, 2) + Math.pow((float)(j - interiorPoints.get(k)[1]) / m * height, 2);
					priorityMap[i][j] -= zone.priority / numberOfInteriorPoints * Math.exp(- distanceSquare / Math.pow(zone.decreasingSpaceInfluence, 2));
				}
			}
		}
		double totalPotential = 0;
		for (int i = 0 ; i <= n ; i++) {
			for (int j = 0 ; j <= m ; j++) {
				if (!obstacleMap[i][j]) totalPotential += priorityMap[i][j];
			}
		}
		this.totalPotential = totalPotential;
	}
	
	public String toString() {
		String S = "";
		for (int i = 0 ; i <= n ; i++) {
			for (int j = 0 ; j <= m ; j++) {
				S += priorityMap[i][j] + " ; ";
			}
			S += "\n";
		}
		return S;
	}
}
