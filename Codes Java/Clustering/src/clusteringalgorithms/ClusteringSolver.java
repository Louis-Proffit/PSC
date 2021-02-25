package clusteringalgorithms;

import general.Checkpoint;
import general.Cluster;

/**
 * Interface que doit implémenter un solveur de clustering
 * <p>
 * Implémentée notamment par : {@link ClusteringTree} et {@link Kmeans}
 */
public interface ClusteringSolver {

    /**
     * Méthode de synthèse du calcul du clustering
     * 
     * @param checkpoints      : Le tableau des checkpoints à traiter.
     * @param numberOfClusters : Le nombre de clusters à générer.
     * @return Le tableau des clusters, de longueur <b>numberOfClusters</b>.
     */
    Cluster[] getClusters(Checkpoint[] checkpoints, int numberOfClusters);

}
