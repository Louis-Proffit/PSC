import java.util.Arrays;

/**
 * La classe est une version de l'algorithme de clustering par arbre. Elle met
 * en place un système de union find pour identifier les classes des clusters Le
 * tableau classes = int[] correspond à cela. Tous les checkpoints pointent vers
 * leur parent, qui peut ne pas être un représentant de la classe. Le tableau
 * distances = double[] comporte les distances entre clusters, il n'est valide
 * que pour tous les représentants de checkpoints. A chaque étape, on fusionne
 * les deux clusters les plus proches (cette distance est conservée en mémoire
 * au cours des itérations). Lors de la fusion de deux clusters, on choisit
 * comme représentant un des deux représentant des clsuters au hasard (à
 * perfectionner) On parcours la liste et la colonne de ce représentant, et on y
 * met à jour la distance pour chaque autre représentant de cluster. On renvoie
 * la liste des clusters en la déduisant du tableau classe
 * 
 * @author Louis Proffit
 * @version 1.0
 */
public class ClusteringTree implements ClusteringAlgorithm {

    private final Checkpoint[] checkpoints;
    private final int numberOfCheckpoints;
    private final int numberOfClusters;
    private final Cluster[] clusters;
    private final int[] classes;
    private final double[][] distances;
    private final double[] weights;

    public ClusteringTree(Checkpoint[] checkpoints, int numberOfClusters) {
        this.checkpoints = checkpoints;
        this.numberOfCheckpoints = checkpoints.length;
        this.numberOfClusters = numberOfClusters;
        this.clusters = new Cluster[numberOfClusters];
        Arrays.setAll(clusters, i -> new Cluster());
        this.classes = getClasses(numberOfCheckpoints);
        this.distances = getDistances(checkpoints);
        this.weights = new double[numberOfCheckpoints];
        Arrays.setAll(weights, i -> 1);
        getClusteringTree();
    }

    /**
     * 
     * @param checkpoints      liste des checkpoints
     * @param finalUnionFind   // Le résultat de la dernière étape de l'union-find
     *                         de getClustering()
     * @param numberOfClusters // Le nombre de clusters souhaité
     * @return La liste des clusters telle que déterminée par l'algorithme @see
     *         getClustering()
     */
    public Cluster[] getClusters() {
        int[] representantToCluster = new int[numberOfCheckpoints]; // Le tableau est rempli de zéros sauf aux positions
                                                                    // des
        // représentants de clusters, qui indique alors son rang
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
        return clusters;
    }

    /**
     * Cette méthode trouve récursivement le parent d'un checkpoint dans un tableau
     * union-find
     * 
     * @param classes le tableau union-find, ou un enfant pointe vers son parent
     * @param child   l'indice pour lequel on veut trouver le représentant
     * @return le représentant de child
     */
    private int getRepresentant(int child) {
        int parent = classes[child];
        if (parent == child)
            return parent;
        return getRepresentant(parent);
    }

    /**
     * Méthode principale pour effectuer le calcul de l'arbre de clusters
     * 
     * @param checkpoints      le tableau des checkpoints
     * @param numberOfClusters le nombre de clusters à la fin, pour arrêter les
     *                         fusions aubon moment
     * @return Le tableau d'union-find à la dernière itération
     */
    private int[] getClusteringTree() {
        for (int i = 0; i < numberOfCheckpoints - numberOfClusters; i++) { // Boucle sur les n étapes pour créer l'arbre
                                                                           // complet
            int[] distanceMinCoordinates = getMinimumDistance();
            int clusterIndex1 = distanceMinCoordinates[0];
            int clusterIndex2 = distanceMinCoordinates[1];
            classes[clusterIndex2] = clusterIndex1; // On envoie le cluster du deuxième indice vers celui du premier
                                                    // indice
            weights[clusterIndex1] += weights[clusterIndex2];
            for (int j = 0; j < numberOfCheckpoints; j++) { // Parcours de la ligne et de la colonne pour mettre à jour
                                                            // les distances
                if (classes[j] == j & j != i) { // On ignore les non-représentants et le nouveua cluster
                    distances[clusterIndex1][j] = Math.min(distances[clusterIndex1][j], distances[clusterIndex2][j]); // Ligne
                    distances[j][clusterIndex1] = Math.min(distances[j][clusterIndex1], distances[j][clusterIndex2]); // Colonne
                }
            }
        }
        return classes;
    }

    /**
     * Méthode pour trouver la distance minimum entre deux checkpoints
     * 
     * @param distances le tableau des distances
     * @param classes   le tableau union-find
     * @return tableau de longueur 2 contenant les coordonnées de la distance
     *         minimum. Le résultat est garanti de désigner deux représentants de
     *         cluster
     */
    private int[] getMinimumDistance() {
        double min = 2 * Math.pow((1 + numberOfCheckpoints), 2);
        int xmin = -1;
        int ymin = -1;
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < i; j++) {
                if (distances[i][j] * (1 + weights[i]) * (1 + weights[j]) < min & classes[i] == i & classes[j] == j) {
                    min = distances[i][j] * weights[i] * weights[j];
                    xmin = i;
                    ymin = j;
                }
            }
        }
        return new int[] { xmin, ymin };
    }

    /**
     * Initialise le tableau classes
     * 
     * @param numberOfCheckpoints le nombre de checkpoints
     * @return Tableau de longueur numberOfCheckpoints qui contient l valeur i en
     *         position i
     */
    private static int[] getClasses(int numberOfCheckpoints) {
        int[] classes = new int[numberOfCheckpoints];
        for (int i = 0; i < numberOfCheckpoints; i++) {
            classes[i] = i;
        }
        return classes;
    }

    /**
     * Méthode à executer au début de l'algorithme pour obte,ir le tableau des
     * distance
     * 
     * @param checkpoints La liste des checkpoints
     * @return double[][] distances : le tableau des distances entre chaque
     *         checkpoints, en respectant l'ordre du tableau
     **/
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