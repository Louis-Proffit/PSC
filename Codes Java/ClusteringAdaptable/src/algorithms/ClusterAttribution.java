package algorithms;

import java.util.HashMap;
import java.util.LinkedList;

import structure.Checkpoint;
import structure.Cluster;
import structure.Drone;
import structure.Modification;
import structure.Pair;
import structure.Vector;

/**
 * Classe gérant l'attribution des drones aux clusters, grâce à un algorithme de
 * Recuit
 * 
 * @author Louis Proffit
 * @version 1.0
 */
public class ClusterAttribution implements RecuitInterface {

    /**
     * Liste des drones à attribuer
     */
    private LinkedList<Drone> drones = new LinkedList<>();

    /**
     * Matrice d'association drone <-> cluster. Chaque drone de la liste drones est
     * dans la matrice. Toutes les valeurs de la matrice sont distinctes.
     */
    private HashMap<Drone, Cluster> association = new HashMap<>();

    /**
     * Objet d'association des checkpoints aux clusters
     */
    private Clustering clustering = new Clustering();

    /**
     * Méthode pour ajouter un drone
     * 
     * @param drone   : Le drone à ajouter
     * @param improve : Le travail de mise à jour à effectuer à la fin
     */
    public void addDrone(Drone drone, ImproveType improve) {
        drones.add(drone);
        Cluster cluster = new Cluster();
        association.put(drone, cluster);
        clustering.addCluster(cluster, improve);
        TSPRecuit.improvePath(this);
    }

    /**
     * Renvoie le cluster associé à un drone. Essentiellement utilisé par les
     * méthodes graphiques
     * 
     * @param drone
     * @return
     */
    public Cluster getDroneCluster(Drone drone) {
        return association.get(drone);
    }

    /**
     * Ajoute un checkpoint
     * 
     * @param checkpoint : Le checkpoint à ajouter
     * @param improve    : Le travail à effectuer à la fin
     */
    public void addCheckpoint(Checkpoint checkpoint, ImproveType improve) {
        clustering.addCheckpoint(checkpoint, improve);
    }

    /**
     * Améliore la répartition des drones et des clusters. Utilise un algorithme de
     * recuit simulé.
     * 
     * @param improve : Le type d'amélioration à effectuer
     */
    public void improve(ImproveType improve) {
        clustering.improve(improve);
        if (improve == ImproveType.COMPLETE)
            TSPRecuit.improvePath(this);
    }

    /**
     * Renvoie la liste des drones. Essentiellement utilisé par les méthodes
     * graphiques
     * 
     * @return La liste des drones
     */
    public LinkedList<Drone> getDrones() {
        return drones;
    }

    /**
     * Fait effectuer à tous les drones un mouvement. Si ils atteignent leur cible,
     * ils se placent dessus et celle-ci est mise à jour. Si il n'y a pas de cible,
     * le drone reste immobile.
     */
    public void move() {
        Vector target;
        Cluster cluster;
        for (Drone drone : drones) {
            cluster = association.get(drone);
            target = cluster.getCurrentTarget();
            if (target == null)
                continue;
            if (target.distance(drone) < Drone.speed) {
                drone.set(target);
                cluster.moveTargetForward();
            } else
                drone.move(target);
        }
    }

    @Override
    public int getSize() {
        return drones.size();
    }

    @Override
    public Modification modificationFunction() {
        return new Pair<Integer>((int) (drones.size() * Math.random()), (int) (drones.size() * Math.random()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Double improvementFunction(Modification modification) {
        Pair<Integer> swap = (Pair<Integer>) modification;
        Drone firstDrone = drones.get(swap.getFirst());
        Drone secondDrone = drones.get(swap.getSecond());
        double result = 0;
        result += association.get(firstDrone).distance(secondDrone);
        result += association.get(secondDrone).distance(firstDrone);
        result -= association.get(firstDrone).distance(firstDrone);
        result -= association.get(secondDrone).distance(secondDrone);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void commitFunction(Modification modification) {
        Pair<Integer> swap = (Pair<Integer>) modification;
        Drone firstDrone = drones.get(swap.getFirst());
        Drone secondDrone = drones.get(swap.getSecond());
        Cluster firstCluster = association.get(firstDrone);
        Cluster secondCluster = association.get(secondDrone);
        association.put(firstDrone, secondCluster);
        association.put(secondDrone, firstCluster);
    }
}
