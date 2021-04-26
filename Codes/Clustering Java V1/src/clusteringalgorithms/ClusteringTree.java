package clusteringalgorithms;

import java.util.Arrays;

import general.Checkpoint;
import general.Cluster;

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
public class ClusteringTree implements ClusteringSolver {

    /**
     * Nombre de checkpoints.
     */
    private int numberOfCheckpoints;

    /**
     * Nombre de clusters à générer.
     */
    private int numberOfClusters;

    /**
     * Tableau des clusters générés.
     */
    private Cluster[] clusters;

    /**
     * tableau utilitaire de l'union-find, contenant en indice i le parent du ième
     * cluster, et i si il est son propre représentant.
     */
    private int[] classes;

    /**
     * Matrice des distances entre les checkpoints, de dimension
     * <b>numberOfCheckpoints x numberOfCheckpoints</b>
     */
    private double[][] distances;

    /**
     * Tableau utilitaire de l'union-find, contenant en indice i le nombre de
     * checkpoints dans le ième cluster.
     */
    private double[] weights;

    /**
     * Méthode de synthèse renvoyant le tableau des clusters
     * 
     * @param checkpoints      : Les checkpoitns à répartir dans les clusters
     * @param numberOfClusters : Le nombre de clusters à créer
     * @return Le tableau des clusters de longueur <b>numberOfClusters</b>
     */
    @Override
    public Cluster[] getClusters(Checkpoint[] checkpoints, int numberOfClusters) {
        long startTime = System.currentTimeMillis();
        this.numberOfCheckpoints = checkpoints.length;
        this.numberOfClusters = numberOfClusters;
        this.clusters = new Cluster[numberOfClusters];
        Arrays.setAll(clusters, i -> new Cluster());
        this.classes = getClasses(numberOfCheckpoints);
        this.distances = getDistances(checkpoints);
        this.weights = new double[numberOfCheckpoints];
        Arrays.setAll(weights, i -> 1);
        getClusteringTree(); // Crée l'arbre par effet de bord dans les variables classes et weights

        int[] representantToCluster = new int[numberOfCheckpoints]; // Le tableau est rempli de zéros sauf aux positions
                                                                    // des représentants de clusters, qui indique alors
                                                                    // son rang
        int rank = 0;// Augmente au fur et à mesure des itérations
        for (int i = 0; i < numberOfCheckpoints; i++) {
            if (classes[i] == i) {
                representantToCluster[i] = rank;
                rank++;
            }
        }
        for (int child = 0; child < numberOfCheckpoints; child++) {
            int representant = getRepresentant(child);
            clusters[representantToCluster[representant]].addCheckpoint(checkpoints[child]);
        }
        long deltaTimeInMillis = System.currentTimeMillis() - startTime;
        System.out.println("Clustering calculé en " + deltaTimeInMillis + "ms");
        return clusters;
    }

    /**
     * Renvoie l'index du représentant de l'argument <b>child</b>. Avec le tableau
     * classes, ce représentant est soit lui même soit le représentant du parent
     * 
     * @param child : L'index d checkpoint enfant
     * @return L'index du représentant de <b>child</b>
     */
    private int getRepresentant(int child) {
        int parent = classes[child];
        if (parent == child)
            return parent;
        return getRepresentant(parent);
    }

    /**
     * Crée l'arbre de clustering par effet de bord sur les tableaux classes et
     * weights (structure union-find)
     */
    private void getClusteringTree() {
        for (int i = 0; i < numberOfCheckpoints - numberOfClusters; i++) { // Boucle sur les n étapes pour créer l'arbre
                                                                           // complet
            int[] distanceMinCoordinates = getMinimumDistance();
            int clusterIndex1 = distanceMinCoordinates[0];
            int clusterIndex2 = distanceMinCoordinates[1];
            // On branche un cluster sur l'autre en fonction de leur poids
            if (weights[clusterIndex1] <= weights[clusterIndex2]) {
                classes[clusterIndex1] = clusterIndex2; // On envoie le cluster du deuxième indice vers celui du premier
                                                        // indice
                weights[clusterIndex2] += weights[clusterIndex1];
                for (int j = 0; j < numberOfCheckpoints; j++) { // Parcours de la ligne et de la colonne pour mettre à
                                                                // jour
                    // les distances
                    if (classes[j] == j & j != i) { // On ignore les non-représentants et le nouveau cluster
                        distances[clusterIndex2][j] = Math.min(distances[clusterIndex2][j],
                                distances[clusterIndex1][j]); // Ligne
                        distances[j][clusterIndex2] = Math.min(distances[j][clusterIndex2],
                                distances[j][clusterIndex1]); // Colonne
                    }
                }
            } else {
                classes[clusterIndex2] = clusterIndex1;
                weights[clusterIndex1] += weights[clusterIndex2];
                for (int j = 0; j < numberOfCheckpoints; j++) {
                    if (classes[j] == j & j != i) {
                        distances[clusterIndex1][j] = Math.min(distances[clusterIndex1][j],
                                distances[clusterIndex2][j]);
                        distances[j][clusterIndex1] = Math.min(distances[j][clusterIndex1],
                                distances[j][clusterIndex2]);
                    }
                }
            }
        }
    }

    /**
     * Cherche et renvoie la plus petite distance entre deux clusters dans le
     * tableau distance. Cette distace est pondérée du nombre de checkpoints dans le
     * cluster.
     * 
     * @return Un tableau d'indices sous la forme [i, j] ou i et j sont les indices
     *         des deux clusters les plus proches
     */
    private int[] getMinimumDistance() {
        double min = 2 * Math.pow((1 + numberOfCheckpoints), 2);
        int xmin = -1;
        int ymin = -1;
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < i; j++) {
                if (distances[i][j] * weights[i] * weights[j] < min & classes[i] == i & classes[j] == j) {
                    min = distances[i][j] * weights[i] * weights[j];
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
        for (int i = 0; i < numberOfCheckpoints; i++) {
            classes[i] = i;
        }
        return classes;
    }

    /**
     * Méthode pour initialiser la matrice des distances
     * 
     * @param checkpoints : La liste des checkpoints
     * @return Une matrice carrée de côté le nombre des checkpoints telle que
     *         M_{i,j} = d(C_i, C_j)
     */
    private static double[][] getDistances(Checkpoint[] checkpoints) {
        int numberOfCheckpoints = checkpoints.length;
        double[][] distances = new double[numberOfCheckpoints][numberOfCheckpoints];
        double distance;
        for (int i = 0; i < numberOfCheckpoints; i++) {
            for (int j = 0; j < i; j++) {
                distance = checkpoints[i].distance(checkpoints[j]);
                distances[i][j] = distance;
                distances[j][i] = distance;
            }
        }
        return distances;
    }
}