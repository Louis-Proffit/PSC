package clusteringalgorithms;

import java.util.ArrayList;

import general.Checkpoint;
import general.Cluster;

/**
 * Classe contenant un agorithme de clustering sur la méthode des k-means.
 * Techniquement, on commence par tirer aléatoirement k moyennes dans le cadre.
 * On connecte ensuite chaque checkpoint au vector (la moyenne) le plus proche.
 * On déduit pour chacun de ces clusters une nouvelle moyenne. On itère ensuite
 * ce processus.
 * 
 * @author Louis Proffit
 */
public class Kmeans implements ClusteringSolver {

    /**
     * Méthode qui synthétise le calcul du clustering.
     * 
     * @param checkpoints      : Le tableau des checkpoints à répartir dans les
     *                         clusters.
     * @param numberOfClusters : Le nombre de clusters à générer.
     * @return Le tableau des clusters contenant les checkpoints.
     */
    @Override
    public Cluster[] getClusters(Checkpoint[] checkpoints, int numberOfClusters) {
        long startTime = System.currentTimeMillis();
        int numberOfCheckpoints = checkpoints.length;
        boolean hasChanged = true;
        Cluster[] clusters = new Cluster[numberOfClusters];
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
                double dist = checkpoints[j].distance(clusters[k].getMean());
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
                    double dist = checkpoints[j].distance(clusters[k].getMean());
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
        long deltaTimeInMillis = System.currentTimeMillis() - startTime;
        System.out.println("Clustering calculé en " + deltaTimeInMillis + "ms");
        return clusters;
    }

    /*
     * public static void showResultsPolygons(Cluster[] clusters) { Draw draw = new
     * Draw(); draw.setPenRadius(0.02); for (int i = 0; i < clusters.length; i++) {
     * Color color = Color.getHSBColor(((float) i) / ((float) clusters.length), 1f,
     * 1f); draw.setPenColor(color); double[] xList = clusters[i].getXList();
     * double[] yList = clusters[i].getYList(); draw.polygon(xList, yList); } }
     */
}
