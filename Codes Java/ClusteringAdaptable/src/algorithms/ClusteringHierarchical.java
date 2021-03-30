package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import structure.Checkpoint;
import structure.Cluster;

/**
 * La classe est une version de l'algorithme de clustering par arbre. Elle met
 * en place un système de union find pour identifier les classes des clusters Le
 * tableau classes = int[] correspond à cela. Tous les checkpoints pointent vers
 * leur parent, qui peut ne pas être un représentant de la classe. Le tableau
 * distances = double[][] comporte les distances entre clusters, il n'est valide
 * que pour tous les représentants de checkpoints. A chaque étape, on fusionne
 * les deux clusters les plus proches, avec une pondération liée au poids des
 * clusters (cette distance est conservée en mémoire au cours des itérations).
 * Lors de la fusion de deux clusters, on choisit comme représentant un des deux
 * représentant des clsuters au hasard (à perfectionner) On parcours la liste et
 * la colonne de ce représentant, et on y met à jour la distance pour chaque
 * autre représentant de cluster.
 * 
 * @author Louis Proffit
 * @version 1.0
 */
public class ClusteringHierarchical extends ClusteringSolver {

    private ArrayList<Checkpoint> checkpoints = new ArrayList<>();
    private ArrayList<Cluster> clusters = new ArrayList<>();

    public void addCluster(Cluster cluster, ImproveType improveType) {
        clusters.add(cluster);
        improve(improveType);
    }

    public void addCheckpoint(Checkpoint checkpoint, ImproveType improveType) {
        checkpoints.add(checkpoint);
        improve(improveType);
    }

    /**
     * Cherche et renvoie la plus petite distance entre deux clusters dans le
     * tableau distance. Cette distace est pondérée du nombre de checkpoints dans le
     * cluster.
     * 
     * @return Un tableau d'indices sous la forme [i, j] ou i et j sont les indices
     *         des deux clusters les plus proches
     */
    private int[] getMinimumDistance(double[][] distances, int[] classes) {
        double min = 2;
        int xmin = -1;
        int ymin = -1;
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < i; j++) {
                if (distances[i][j] < min & classes[i] == i & classes[j] == j) {
                    min = distances[i][j];
                    xmin = i;
                    ymin = j;
                }
            }
        }
        return new int[] { xmin, ymin };
    }

    /**
     * Méthode pour initialiser un tableau d'union find
     * 
     * @param numberOfCheckpoints : La longueur du tableau d'union-find
     * @return un tableau de longueur <b>numberOfCheckpoints</b> contenant i à
     *         l'index i.
     */
    private static int[] getClasses(int numberOfCheckpoints) {
        int[] classes = new int[numberOfCheckpoints];
        for (int i = 0; i < numberOfCheckpoints; i++)
            classes[i] = i;
        return classes;
    }

    /**
     * Méthode pour initialiser la matrice des distances
     * 
     * @param checkpoints : La liste des checkpoints
     * @return Une matrice carrée de côté le nombre des checkpoints telle que
     *         M_{i,j} = d(C_i, C_j)
     */
    private double[][] getDistances() {
        int numberOfCheckpoints = checkpoints.size();
        double[][] distances = new double[numberOfCheckpoints][numberOfCheckpoints];
        double distance;
        for (int i = 0; i < numberOfCheckpoints; i++) {
            for (int j = 0; j < i; j++) {
                distance = checkpoints.get(i).distance(checkpoints.get(j));
                distances[i][j] = distance;
                distances[j][i] = distance;
            }
        }
        return distances;
    }

    void mergeClusters(int firstClusterIndex, int secondClusterIndex, double[][] distances, int[] classes) {
        // Action on distances
        for (int i = 0; i < distances.length; i++) {
            distances[i][firstClusterIndex] = Math.max(distances[i][firstClusterIndex],
                    distances[i][secondClusterIndex]);
            distances[firstClusterIndex][i] = distances[i][firstClusterIndex];
        }
        // Action on classes
        assert (classes[firstClusterIndex] == firstClusterIndex);
        assert (classes[secondClusterIndex] == secondClusterIndex);
        classes[secondClusterIndex] = firstClusterIndex;
    }

    private void performClustering(int[] classes) {
        Cluster[] clustersAssociation = new Cluster[classes.length];
        int currentClusterIndex = 0;
        for (int i = 0; i < classes.length; i++) {
            if (classes[i] == i) {
                clustersAssociation[i] = clusters.get(currentClusterIndex);
                currentClusterIndex++;
            }
        }
        assert (currentClusterIndex == clusters.size());
        for (int i = 0; i < classes.length; i++)
            clustersAssociation[findRepresentant(i, classes)].addCheckpoint(checkpoints.get(i));
    }

    private int findRepresentant(int index, int[] classes) {
        if (classes[index] == index)
            return index;
        return findRepresentant(classes[index], classes);
    }

    private void clearClusters() {
        for (Cluster cluster : clusters)
            cluster.clear();
    }

    @Override
    void improve(ImproveType improve) {
        if (improve == ImproveType.NULL)
            return;
        else if (improve == ImproveType.SIMPLE)
            return;
        clearClusters();
        int numberOfClusters = clusters.size();
        int numberOfCheckpoints = checkpoints.size();
        int[] classes = getClasses(numberOfCheckpoints);
        double[][] distances = getDistances();
        int mergesToPerform = numberOfCheckpoints - numberOfClusters;
        for (int i = 0; i < mergesToPerform; i++) {
            int[] clustersToMerge = getMinimumDistance(distances, classes);
            assert (clustersToMerge.length == 2);
            mergeClusters(clustersToMerge[0], clustersToMerge[1], distances, classes);
        }
        performClustering(classes);
        for (Cluster cluster : clusters)
            cluster.improvePath();
    }
}