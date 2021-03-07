package algorithms;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import structure.Checkpoint;
import structure.Cluster;

/**
 * Classe qui gère l'association entre checkpoints et clusters. Elle optimise
 * cette association grâce à un algorithme k-means.
 * 
 * @author Louis Proffit
 * @version 1.0
 */
public class Clustering {

    /**
     * Liste des clusters
     */
    private LinkedList<Cluster> availableClusters = new LinkedList<>();

    /**
     * Matrice d'association entre checkpoints et clusters
     */
    private HashMap<Checkpoint, Cluster> association = new HashMap<>();

    /**
     * Ajoute un checkpoint
     * 
     * @param checkpoint : Le checkpoint à ajouter
     * @param improve    : Le travail à effectuer à la fin
     */
    public void addCheckpoint(Checkpoint checkpoint, ImproveType improve) {
        Cluster cluster = availableClusters.getFirst();
        cluster.addCheckpoint(checkpoint);
        association.put(checkpoint, cluster);
        improve(improve);
    }

    /**
     * Ajoute un cluster
     * 
     * @param cluster : Le cluster à ajouter
     * @param improve : Le travail à effectuer à la fin
     */
    public void addCluster(Cluster cluster, ImproveType improve) {
        availableClusters.add(cluster);
        improve(improve);
    }

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
    public void improve(ImproveType improve) {
        if (improve == ImproveType.NULL)
            return;
        else if (improve == ImproveType.SIMPLE) {
            for (Cluster clusterLocal : availableClusters)
                clusterLocal.improvePath();
            return;
        } else if (improve == ImproveType.COMPLETE) {
            boolean modified = true;
            while (modified)
                modified = improveOneStep();
            for (Cluster cluster : availableClusters)
                cluster.improvePath();
            return;
        }
    }

    /**
     * Effectue une étape de k-means
     * 
     * @return
     */
    private boolean improveOneStep() {
        HashMap<Checkpoint, Cluster> modifications = new HashMap<>();
        boolean modified = false;
        double distance;
        double currentMinDistance;
        Cluster cluster;
        Cluster minCluster;
        for (Checkpoint checkpoint : association.keySet()) {
            cluster = association.get(checkpoint);
            currentMinDistance = cluster.distance(checkpoint);
            minCluster = null;
            for (Cluster otherCluster : availableClusters) {
                distance = otherCluster.distance(checkpoint);
                if (distance < currentMinDistance) {
                    currentMinDistance = distance;
                    minCluster = otherCluster;
                    modified = true;
                }
            }
            if (minCluster != null) {
                modifications.put(checkpoint, minCluster);
            }
        }
        for (Entry<Checkpoint, Cluster> entry : modifications.entrySet()) {
            Cluster oldCluster = association.put(entry.getKey(), entry.getValue()); // On ajoute la nouvelle association
            oldCluster.removeCheckpoint(entry.getKey()); // On enlève le checkpoint de son ancien cluster
            entry.getValue().addCheckpoint(entry.getKey()); // On ajoute le checkpoint dans son nouveau cluster
        }
        return modified;
    }
}
