import java.util.LinkedList;

public class Map {
	
	public int n;
	public int m;
	public double width;
	public double height;
	public double[][] priorityMap; // La carte du potentiel. Le obstacles sont à 0. 

	public Map(int n, int m, double width, double height) {
		this.n = n;
		this.m = m;
		this.width = width;
		this.height = height;
		this.priorityMap = new double[n + 1][m + 1];
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
