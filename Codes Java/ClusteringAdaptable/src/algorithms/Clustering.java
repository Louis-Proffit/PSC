package algorithms;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import structure.Checkpoint;
import structure.Cluster;
import structure.Pair;

public class Clustering {

    private LinkedList<Cluster> availableClusters;
    private HashMap<Checkpoint, Cluster> association;

    public Clustering() {
        this.availableClusters = new LinkedList<>();
        this.association = new HashMap<>();
    }

    public void addCheckpoint(Checkpoint checkpoint, ImproveType improve) {
        if (availableClusters.isEmpty())
            return;
        double distanceMin = 2;
        double distance;
        Cluster minCluster = null;
        for (Cluster cluster : availableClusters) {
            if (cluster.getSize() == 0) {
                // On ajoute systématiquement à un cluster vide
                cluster.addCheckpoint(checkpoint);
                if (improve == ImproveType.COMPLETE)
                    improveComplete();
                return;
            }
            distance = checkpoint.distance(cluster.getMean());
            if (distance < distanceMin) {
                distanceMin = distance;
                minCluster = cluster;
            }
        }
        assert minCluster != null;
        minCluster.addCheckpoint(checkpoint);
        switch (improve) {
            case NULL:
                return;
            case SIMPLE:
                minCluster.improvePath();
            case COMPLETE:
                improveComplete();
        }
    }

    public void addCluster(Cluster cluster) {
        availableClusters.add(cluster);
    }

    public Collection<Cluster> getClusters() {
        return association.values();
    }

    public void improveComplete() {
        boolean modified = true;
        while (modified) {
            modified = improveOneStep();
        }
        for (Cluster cluster : availableClusters)
            cluster.improvePath();
    }

    public void improveSteps(int numberOfSteps) {
        for (int i = 0; i < numberOfSteps; i++) {
            improveOneStep();
        }
    }

    private boolean improveOneStep() {
        HashMap<Checkpoint, Pair<Cluster>> modifications = new HashMap<>(); // Le premier cluster est l'ancien, le
                                                                            // deuxième est le nouveau
        boolean modified = false;
        boolean modifiedLocal = false;
        double distanceLocal;
        double currentDistance;
        Cluster cluster;
        Cluster minCluster = null;
        for (Checkpoint checkpoint : association.keySet()) {
            modifiedLocal = false;
            cluster = association.get(checkpoint);
            currentDistance = checkpoint.distance(cluster.getMean());
            minCluster = null;
            for (Cluster otherCluster : availableClusters) {
                if (otherCluster.getSize() == 0) {
                    association.put(checkpoint, otherCluster); // On ajoute la nouvelle association
                    cluster.removeCheckpoint(checkpoint); // On enlève le checkpoint de son ancien cluster
                    otherCluster.addCheckpoint(checkpoint);
                    return true;
                }
                distanceLocal = checkpoint.distance(otherCluster.getMean());
                if (distanceLocal < currentDistance) {
                    currentDistance = distanceLocal;
                    minCluster = otherCluster;
                    modifiedLocal = true;
                    modified = true;
                }
            }
            if (modifiedLocal) {
                modifications.put(checkpoint, new Pair<Cluster>(cluster, minCluster));
            }
        }
        if (modified) {
            for (Entry<Checkpoint, Pair<Cluster>> entry : modifications.entrySet()) {
                association.put(entry.getKey(), entry.getValue().getSecond()); // On ajoute la nouvelle association
                entry.getValue().getFirst().removeCheckpoint(entry.getKey()); // On enlève le checkpoint de son ancien
                                                                              // cluster
                entry.getValue().getSecond().addCheckpoint(entry.getKey()); // On ajoute le checkpoint dans son nouveau
                                                                            // cluster
            }
        }
        return modified;
    }
}
