import java.awt.Color;
import java.util.Arrays;

/**
 * La classe est une version de l'algorithme de clustering par arbre.
 * Elle met en place un système de union find pour identifier les classes des clusters
 * Le tableau classes = int[] correspond à cela. Tous les checkpoints pointent vers leur parent, qui peut ne pas être un représentant de la classe.
 * Le tableau distances = double[] comporte les distances entre clusters, il n'est valide que pour tous les représentants de checkpoints.
 * A chaque étape, on fusionne les deux clusters les plus proches (cette distance est conservée en mémoire au cours des itérations).
 * Lors de la fusion de deux clusters, on choisit comme représentant un des deux représentant des clsuters au hasard (à perfectionner)
 * On parcours la liste et la colonne de ce représentant, et on y met à jour la distance pour chaque autre représentant de cluster.
 * On renvoie la liste des clusters en la déduisant du tableau classe
 * @author Louis Proffit
 * @version 1.0
 */
public class ClusteringTree {

    /**
     * 
     * @param checkpoints liste des checkpoints
     * @param finalUnionFind // Le résultat de la dernière étape de l'union-find de getClustering()
     * @param numberOfClusters // Le nombre de clusters souhaité
     * @return La liste des clusters telle que déterminée par l'algorithme @see getClustering()
     */
    public static Cluster[] getClusters(Checkpoint[] checkpoints, int numberOfClusters){
        int[] classes = getClusteringTree(checkpoints, numberOfClusters);
        Cluster[] clusters = new Cluster[numberOfClusters];
        for (int i = 0 ; i < numberOfClusters ; i++){
            clusters[i] = new Cluster();
        }
        int[] representantToCluster = new int[classes.length]; // Le tableau est rempli de zéros sauf aux positions des représentants de clusters, qui indique alors son rang
        int rank = 0;// Augmente au fur et à mesure des itérations
        for (int i = 0 ; i < classes.length ; i++){
            if (classes[i] == i){
                representantToCluster[i] = rank;
                rank++;
            }
        }
        for (int child = 0 ; child < classes.length ; child++){
            int representant = getRepresentant(classes, child);
            clusters[representantToCluster[representant]].addCheckpoint(checkpoints[child]);
        }
        return clusters;
    }

    /**
     * Cette méthode trouve récursivement le parent d'un checkpoint dans un tableau union-find
     * @param classes le tableau union-find, ou un enfant pointe vers son parent
     * @param child l'indice pour lequel on veut trouver le représentant
     * @return le représentant de child
     */
    private static int getRepresentant(int[] classes, int child){
        int parent = classes[child];
        if (parent == child) return child;
        return getRepresentant(classes, parent);
    }

    /**
     * Méthode principale pour effectuer le calcul de l'arbre de clusters
     * @param checkpoints le tableau des checkpoints
     * @param numberOfClusters le nombre de clusters à la fin, pour arrêter les fusions aubon moment
     * @return Le tableau d'union-find à la dernière itération
     */
    private static int[] getClusteringTree(Checkpoint[] checkpoints, int numberOfClusters){
        int numberOfCheckpoints = checkpoints.length;
        int[] classes = getClasses(numberOfCheckpoints);
        double[][] distances = getDistances(checkpoints);
        
        for (int i = 0 ; i < numberOfCheckpoints - numberOfClusters ; i++){ // Boucle sur les n étapes pour créer l'arbre complet
            int[] distanceMinCoordinates = getMinimumDistance(distances, classes);
            int clusterIndex1 = distanceMinCoordinates[0];
            int clusterIndex2 = distanceMinCoordinates[1];
            classes[clusterIndex2] = clusterIndex1; // On envoie le cluster du deuxième indice vers celui du premier indice
            for (int j = 0 ; j < numberOfCheckpoints ; j++){ // Parcours de la ligne et de la colonne pour mettre à jour les distances
                if (classes[j] == j & j != i){ // On ignore les non-représentants et le nouveua cluster 
                    distances[clusterIndex1][j] = Math.min(distances[clusterIndex1][j], distances[clusterIndex2][j]); // Ligne
                    distances[j][clusterIndex1] = Math.min(distances[j][clusterIndex1], distances[j][clusterIndex2]); // Colonne
                }
            }
            System.out.println(Arrays.toString(classes));
        }
        return classes;
    }

    /**
     * Méthode pour trouver la distance minimum entre deux checkpoints
     * @param distances le tableau des distances
     * @param classes le tableau union-find
     * @return tableau de longueur 2 contenant les coordonnées de la distance minimum. 
     * Le résultat est garanti de désigner deux représentants de cluster
     */
    private static int[] getMinimumDistance(double[][] distances, int[] classes){
        double min = distances[0][1];
        int xmin = 0;
        int ymin = 0;
        for (int i = 0 ; i < distances.length ; i++){
            for (int j = 0 ; j < i ; j++){
                if (distances[i][j] < min & classes[i] == i & classes[j] == j){
                    min = distances[i][j];
                    xmin = i;
                    ymin = j;
                }
            }
        }
        return new int[]{xmin, ymin};
    }

    /**
     * Initialise le tableau classes
     * @param numberOfCheckpoints le nombre de checkpoints
     * @return Tableau de longueur numberOfCheckpoints qui contient l valeur i en position i
     */
    private static int[] getClasses(int numberOfCheckpoints){
        int[] classes = new int[numberOfCheckpoints];
        for (int i = 0 ; i < numberOfCheckpoints ; i++){
            classes[i] = i;
        }
        return classes;
    }

    /** 
    * Méthode à executer au début de l'algorithme pour obte,ir le tableau des distance
    * @param checkpoints La liste des checkpoints
    * @return double[][] distances : le tableau des distances entre chaque checkpoints, en respectant l'ordre du tableau 
    **/
    private static double[][] getDistances(Checkpoint[] checkpoints){
        int numberOfCheckpoints = checkpoints.length;
        double[][] distances = new double[numberOfCheckpoints][numberOfCheckpoints];
        double distance;
        for (int i = 0 ; i < numberOfCheckpoints ; i++){
            for (int j = 0 ; j < i ; j++){
                distance = checkpoints[i].distance(checkpoints[j]);
                distances[i][j] = distance;
                distances[j][i] = distance;
            }
        }
        return distances;
    }

    /**
     * Génère un set de checkpoints aléatoire dans le carré unité
     * @param numberOfCheckpoints le nombre de checkpoints à générer
     * @return le tableau des checkpoints générés
     */
    private static Checkpoint[] generateRandomCheckpoints(int numberOfCheckpoints){
        Checkpoint[] checkpoints = new Checkpoint[numberOfCheckpoints];
        double x,y;
        for (int i = 0 ; i < numberOfCheckpoints ; i++){
            x = Math.random();
            y = Math.random();
            checkpoints[i] = new Checkpoint(x, y);
        }
        return checkpoints;
    }

    /**
     * Affiche les checkpoints, dans le carré unité, avec une couleur suivant un dégradé pour les différencier
     * @param clusters la liste des clusters contenant les checkpoints
     */
    private static void showResultsPoints(Cluster[] clusters){
        Draw draw = new Draw();
        draw.setPenRadius(0.02);
        for (int i = 0 ; i < clusters.length ; i++){
            Color color = Color.getHSBColor(((float)i) / ((float)clusters.length), 1f, 1f);
            draw.setPenColor(color);
            for (Checkpoint checkpoint : clusters[i].getInnerCheckpoints()){
                draw.point(checkpoint.getX(), checkpoint.getY());
            }
        }
    }

    public static void main(String[] args){
        int numberOfCheckpoints = 20;
        int numberOfClusters = 5;
        Checkpoint[] checkpoints = generateRandomCheckpoints(numberOfCheckpoints);

        Cluster[] clusters = getClusters(checkpoints, numberOfClusters);
        showResultsPoints(clusters);
    }
    
}
