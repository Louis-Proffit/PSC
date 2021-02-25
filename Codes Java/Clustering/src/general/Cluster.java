package general;

import java.util.LinkedList;

import tspsolving.TSPSolver;

/**
 * Classe décrivant un cluster
 * 
 * @author Louis Proffit
 */
public class Cluster {

    /**
     * Liste des checkpoints contenu dans le cluster
     */
    private final LinkedList<Checkpoint> innerCheckpoints;

    /**
     * Liste des points dans l'ordre à suivre tel que fourni par le solveur TSP, en
     * correspondance avec la liste des checkpoints
     */
    private Checkpoint[] path;

    /**
     * Constructeur de cluster vide
     */
    public Cluster() {
        this.innerCheckpoints = new LinkedList<Checkpoint>();
    }

    /**
     * getter
     * 
     * @return Liste des checkpoints du cluster
     */
    public LinkedList<Checkpoint> getInnerCheckpoints() {
        return innerCheckpoints;
    }

    /**
     * Ajout d'un checkpoint au cluster
     * 
     * @param checkpoint : Checkpoint à ajouter
     */
    public void addCheckpoint(Checkpoint checkpoint) {
        innerCheckpoints.add(checkpoint);
    }

    /**
     * Création du chemin à parcourir pour optimiser la surveillance du cluster
     * 
     * @param solver : Le type de solveur à utiliser
     */
    public void setPath(TSPSolver solver) {
        Checkpoint[] innerCheckpointsArray = new Checkpoint[innerCheckpoints.size()];
        this.path = solver.getPath(innerCheckpoints.toArray(innerCheckpointsArray));
    }

    /**
     * getter
     * 
     * @return Le chemin de parcours des checkpoints
     */
    public Checkpoint[] getPath() {
        return path;
    }

    /**
     * Récupère le tableau des coordonnées X des checkpoints du cluster
     * 
     * @return Le tableau des coordonnées X des checkpoints du cluster
     */
    public double[] getXArray() {
        /* Array avec toutes les coordonnées x */
        ConvexHull convexHull = new ConvexHull(innerCheckpoints);
        LinkedList<Vector> convexHullPoints = convexHull.process();
        double[] xValues = new double[convexHullPoints.size()];
        int i = 0;
        for (Vector point : convexHullPoints) {
            xValues[i] = point.getX();
            i++;
        }
        return xValues;
    }

    /**
     * Récupère le tableau des coordonnées Y des checkpoints du cluster
     * 
     * @return Le tableau des coordonnées Y des checkpoints du cluster
     */
    public double[] getYArray() {
        /* Array avec toutes les coordonnées y */
        ConvexHull convexHull = new ConvexHull(innerCheckpoints);
        LinkedList<Vector> convexHullPoints = convexHull.process();
        double[] yValues = new double[convexHullPoints.size()];
        int i = 0;
        for (Vector point : convexHullPoints) {
            yValues[i] = point.getY();
            i++;
        }
        return yValues;
    }

    /*
     * public double score() { Vector mean = mean(); double score = 0; for (Vector
     * checkpoint : innerCheckpoints) { score += checkpoint.distance(mean); } return
     * score; }
     */

    /*
     * public double getVariance() { double variance = 0; Vector mean = getMean();
     * for (Vector checkpoint : innerCheckpoints) { variance +=
     * Math.pow(checkpoint.getX() - mean.getX(), 2) + Math.pow(checkpoint.getY() -
     * mean.getY(), 2); } return variance; }
     */

    /**
     * Computes the mean of positions of checkpoints in the cluster
     * 
     * @return The mean position of the checkpoints in the cluster
     */
    public Vector getMean() {
        double x = 0;
        double y = 0;
        for (Vector checkpoint : innerCheckpoints) {
            x += checkpoint.getX();
            y += checkpoint.getY();
        }
        x = x / innerCheckpoints.size();
        y = y / innerCheckpoints.size();
        return new Vector(x, y);
    }

    public DistanceToCluster distanceToCluster(Vector vector) {
        if (innerCheckpoints.isEmpty())
            return new DistanceToCluster(100, null); // TODO
        double minDistanceToCluster = 2;
        Checkpoint closestCheckpoint = null;
        double distanceToCheckpoint;
        for (Checkpoint checkpoint : innerCheckpoints) {
            distanceToCheckpoint = checkpoint.distance(vector);
            if (distanceToCheckpoint < minDistanceToCluster) {
                minDistanceToCluster = distanceToCheckpoint;
                closestCheckpoint = checkpoint;
            }
        }
        return new DistanceToCluster(minDistanceToCluster, closestCheckpoint);
    }
}