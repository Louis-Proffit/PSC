package algorithms;

import structure.Checkpoint;
import structure.Cluster;

public abstract class ClusteringSolver {

    abstract void addCluster(Cluster cluster, ImproveType improveType);

    abstract void addCheckpoint(Checkpoint checkpoint, ImproveType improveType);

    /**
     * Effectue un travail d'amélioration
     * 
     * @param improve : Le type de travail d'amélioration à effectuer. Si c'est NULL
     *                : on ne fait rien. Si c'est SIMPLE : on améliore le chemin à
     *                l'intérieur de chacun des clusters. Si c'est COMPLETE : on
     *                effectue un passage de k-means complet, puis on efectue le
     *                même travail que SIMPLE.
     * @see ImproveType
     */
    abstract void improve(ImproveType improve);
}
