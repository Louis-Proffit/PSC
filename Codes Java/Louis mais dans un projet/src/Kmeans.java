import java.awt.Color;
import java.util.ArrayList;

public class Kmeans {

	public static double getRepartitionScore(Cluster[] clusters) {
		double score = 0;
		for (Cluster cluster : clusters) {
			score += cluster.score();
		}
		return score;
	}

	/**
	 * 
	 * @param checkpoints
	 * @param numberOfClusters
	 * @return
	 */
	public static Cluster[] KMean(Checkpoint[] checkpoints, int numberOfClusters) {
		boolean hasChanged = true;
		Cluster[] clusters = new Cluster[numberOfClusters];
		int numberOfCheckpoints = checkpoints.length;
		ArrayList<Integer> whichCluster = new ArrayList<Integer>();
		for (int i = 0; i < numberOfCheckpoints; i++) {
			whichCluster.add(-1);
		}

		int i = 0;
		while (i < numberOfClusters) {
			clusters[i] = new Cluster();
			int j = (int) (Math.random() * numberOfCheckpoints);
			if (whichCluster.get(j) == -1) {
				clusters[i].addCheckpoint(checkpoints[j]);
				whichCluster.set(j, i);
				i++;
			}
		}

		for (int j = 0; j < numberOfCheckpoints; j++) {
			double min = 10000;
			int destination = -1;
			for (int k = 0; k < numberOfClusters; k++) {
				double dist = checkpoints[j].distance(clusters[k].mean());
				if (dist < min) {
					min = dist;
					destination = k;
				}
			}

			if (whichCluster.get(j) == -1) {
				clusters[destination].addCheckpoint(checkpoints[j]);
				whichCluster.set(j, destination);
			}
		}

		while (hasChanged) {
			hasChanged = false;

			for (int j = 0; j < numberOfCheckpoints; j++) {
				double min = 10000;
				int destination = -1;
				for (int k = 0; k < numberOfClusters; k++) {
					double dist = checkpoints[j].distance(clusters[k].mean());
					if (dist < min) {
						min = dist;
						destination = k;
					}
				}

				if (whichCluster.get(j) != destination) {
					hasChanged = true;
					clusters[destination].addCheckpoint(checkpoints[j]);
					clusters[whichCluster.get(j)].getInnerCheckpoints().remove(checkpoints[j]);
					whichCluster.set(j, destination);
				}
			}
		}

		return clusters;
	}

	public static void showResultsPolygons(Cluster[] clusters) {
		Draw draw = new Draw();
		draw.setPenRadius(0.02);
		for (int i = 0; i < clusters.length; i++) {
			Color color = Color.getHSBColor(((float) i) / ((float) clusters.length), 1f, 1f);
			draw.setPenColor(color);
			double[] xList = clusters[i].getXList();
			double[] yList = clusters[i].getYList();
			draw.polygon(xList, yList);
		}
	}

	public static Checkpoint[] generateRandomCheckpoints(int numberOfCheckpoints) {
		Checkpoint[] checkpoints = new Checkpoint[numberOfCheckpoints];
		double x, y;
		for (int i = 0; i < numberOfCheckpoints; i++) {
			x = Math.random();
			y = Math.random();
			checkpoints[i] = new Checkpoint(x, y);
		}
		return checkpoints;
	}

	public static void main(String[] args) {
		int numberOfCheckpoints = 1000;
		int numberOfClusters = 5;
		Checkpoint[] checkpoints = generateRandomCheckpoints(numberOfCheckpoints);

		Cluster[] clusters = KMean(checkpoints, numberOfClusters);
		/* showResultsPoints(clusters); */
		showResultsPolygons(clusters);
	}

}