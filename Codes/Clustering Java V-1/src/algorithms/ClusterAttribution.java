package algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import graphics.Controller;
import structure.Checkpoint;
import structure.Drone;
import structure.Modification;

/**
 * Classe gérant l'attribution des drones aux clusters, grâce à un algorithme de
 * Recuit
 * 
 * @author Louis Proffit
 * @version 1.0
 */
public class ClusterAttribution implements RecuitInterface {

    /**
     * Liste des checkpoints, dans l'ordre d'ajout (l'ordre est indifférent)
     */
    private ArrayList<Checkpoint> checkpoints;

    /**
     * Liste des drones, dans l'ordre d'ajout (l'ordre est indifférent)
     */
    private ArrayList<Drone> drones;

    /**
     * Map d'association entre un drone et la liste des checkpoints de son cluster
     */
    private HashMap<Drone, ArrayList<Checkpoint>> droneAttribution;

    /**
     * Construit une attribution vide
     */
    public ClusterAttribution() {
        drones = new ArrayList<>(Controller.numberOfDrones);
        checkpoints = new ArrayList<>(Controller.numberOfCheckpoints);
        droneAttribution = new HashMap<>(Controller.numberOfDrones);
    }

    /**
     * Ajoute des drones à l'attribution avec un cluster vide
     * 
     * @param improveType : indique si on doit effectuer une amélioration de
     *                    l'attribution
     * @param dronesToAdd : les drones à ajouter
     */
    public void addDrone(ImproveType improveType, Drone... dronesToAdd) {
        for (Drone drone : dronesToAdd) {
            drones.add(drone);
            droneAttribution.put(drone, new ArrayList<>());
        }

        improve(improveType);
    }

    /**
     * Ajoute des checkpoints à l'attribution à un drone aléatoire
     * 
     * @param improveType      : indique si on doit effectuer une amélioration de
     *                         l'attribution
     * @param checkpointsToAdd : les checkpoints à ajouter
     */
    public void addCheckpoint(ImproveType improveType, Checkpoint... checkpointsToAdd) {
        for (Checkpoint checkpoint : checkpointsToAdd) {
            checkpoints.add(checkpoint);
            int randomIndex = (int) (Math.random() * drones.size());
            droneAttribution.get(drones.get(randomIndex)).add(checkpoint);
        }

        improve(improveType);
    }

    /**
     * Effectue un passage de TSP pour améliorer l'attribution
     */
    public void improve(ImproveType improveType) {
        if (improveType == ImproveType.COMPLETE) {
            RecuitAlgorithm.improve(this);
        }
        // Sets the drone target to the closest chekpoint in its cluster, which might be
        // null if the cluster is empty
        for (Drone drone : drones) {
            drone.setTarget(getClosestCheckpointInCluster(drone));
        }
    }

    /**
     * Returns the closest checkpoint in a drone's cluster, eventually null if the
     * cluster is empty
     * 
     * @param drone : the cluster
     * @return The closest checkpoint in the cluster or null if the cluster is empty
     */
    private Checkpoint getClosestCheckpointInCluster(Drone drone) {
        double distance;
        double minDistance = 2;
        Checkpoint minCheckpoint = null;
        for (Checkpoint checkpoint : droneAttribution.get(drone)) {
            distance = checkpoint.distance(drone);
            if (distance < minDistance) {
                minDistance = distance;
                minCheckpoint = checkpoint;
            }
        }
        return minCheckpoint;
    }

    /**
     * getter
     * 
     * @return La liste des checkpoints, sans ordre particulier
     */
    public ArrayList<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    /**
     * getter
     * 
     * @return la liste des drones, sans ordre particulier
     */
    public ArrayList<Drone> getDrones() {
        return drones;
    }

    /**
     * Returns the cluster of the drone via an ordered list of its checkpoints
     * 
     * @param drone : the drone
     * @return The cluster of the drone
     */
    public ArrayList<Checkpoint> getDroneCluster(Drone drone) {
        return droneAttribution.get(drone);
    }

    /**
     * Renvoie le checkpoint qui précède actuellement un checkpoint, dans un cluster
     * fixé
     * 
     * @param drone      : le cluster choisi
     * @param checkpoint : le checkpoint
     * @return le checkpoint avant checkpoint
     */
    public Checkpoint getCheckpointBefore(Drone drone, Checkpoint checkpoint) {
        ArrayList<Checkpoint> checkpointsInCluster = droneAttribution.get(drone);

        // Si le cluster ne contient qu'un checkpoint, il fait office de précécent
        if (checkpointsInCluster.size() == 1)
            return checkpoint;

        int checkpointIndexInCluster = checkpointsInCluster.indexOf(checkpoint);

        assert (checkpointIndexInCluster != -1);

        if (checkpointIndexInCluster == 0)
            return checkpointsInCluster.get(checkpointsInCluster.size() - 1);
        else
            return checkpointsInCluster.get(checkpointIndexInCluster - 1);
    }

    /**
     * Renvoie le checkpoint qui suit actuellement un checkpoint, dans un cluster
     * fixé
     * 
     * @param drone      : le cluster choisi
     * @param checkpoint : le checkpoint
     * @return le checkpoint après checkpoint
     */
    public Checkpoint getCheckpointAfter(Drone drone, Checkpoint checkpoint) {
        ArrayList<Checkpoint> checkpointsInCluster = droneAttribution.get(drone);

        // Si le cluster ne contient qu'un checkpoint, il fait office de suivant
        if (checkpointsInCluster.size() == 1)
            return checkpoint;

        int checkpointIndexInCluster = checkpointsInCluster.indexOf(checkpoint);
        if (checkpointIndexInCluster == checkpointsInCluster.size() - 1)
            return checkpointsInCluster.get(0);
        else
            return checkpointsInCluster.get(checkpointIndexInCluster + 1);
    }

    /**
     * Moves every drone and updates their target if they rejoined it
     */
    public void move() {
        boolean toUpdate;
        for (Drone drone : drones) {
            toUpdate = drone.move();
            if (toUpdate)
                drone.setTarget(getCheckpointAfter(drone, drone.getTarget()));
        }
    }

    @Override
    public int getSize() {
        System.out.println(checkpoints.size() * drones.size());
        return checkpoints.size() * drones.size();
    }

    @Override
    public Modification modificationFunction() {

        // Récupération des deux drones, le premier doit avoir un cluster non vide
        int originDroneIndex = (int) (Math.random() * drones.size());
        while (droneAttribution.get(drones.get(originDroneIndex)).size() == 0)
            originDroneIndex = (int) (Math.random() * drones.size());
        int destinationDroneIndex = (int) (Math.random() * drones.size());

        assert (droneAttribution.get(drones.get(originDroneIndex)).size() > 0);

        Drone originDrone = drones.get(originDroneIndex);
        Drone destinationDrone = drones.get(destinationDroneIndex);

        int originDroneClusterSize = droneAttribution.get(originDrone).size();
        int destinationDroneClusterSize = droneAttribution.get(destinationDrone).size();

        // Récupération du sous-array du premier Drone
        int originCheckpointIndex = (int) (Math.random() * originDroneClusterSize);
        int destinationCheckpointIndex = (int) (Math.random() * destinationDroneClusterSize);

        Checkpoint originCheckpoint = droneAttribution.get(originDrone).get(originCheckpointIndex);

        // Checkpoint d'arrivée. Si le cluster d'arrivée est vide, il vaut null
        Checkpoint destinationCheckpoint;
        if (destinationDroneClusterSize == 0)
            destinationCheckpoint = null;
        else
            destinationCheckpoint = droneAttribution.get(destinationDrone).get(destinationCheckpointIndex);

        return new AttributionModification(originDrone, destinationDrone, originCheckpoint, destinationCheckpoint);
    }

    @Override
    public Double improvementFunction(Modification _modification) {

        AttributionModification modification = (AttributionModification) (_modification);
        double improvement = 0.0d;
        Checkpoint previousCheckpointOrigin = getCheckpointBefore(modification.getOriginDrone(),
                modification.getOriginCheckpoint());
        Checkpoint nextCheckpointOrigin = getCheckpointBefore(modification.getOriginDrone(),
                modification.getOriginCheckpoint());

        if (modification.getDestinationCheckpoint() == null) {
            improvement += previousCheckpointOrigin.distance(modification.getOriginCheckpoint());
            improvement += nextCheckpointOrigin.distance(modification.getOriginCheckpoint());
            return improvement;
        }

        Checkpoint previousCheckpointDestination = modification.getDestinationCheckpoint();
        Checkpoint nextCheckpointDestination = getCheckpointAfter(modification.getDestinationDrone(),
                previousCheckpointDestination);

        // In the origin checkpoint
        improvement -= previousCheckpointOrigin.distance(nextCheckpointOrigin);
        improvement += previousCheckpointOrigin.distance(modification.getOriginCheckpoint());
        improvement += nextCheckpointOrigin.distance(modification.getOriginCheckpoint());

        // In the destination checkpoint
        improvement += previousCheckpointDestination.distance(nextCheckpointDestination);
        improvement -= previousCheckpointDestination.distance(modification.getOriginCheckpoint());
        improvement -= nextCheckpointDestination.distance(modification.getOriginCheckpoint());

        return improvement;
    }

    @Override
    public void commitFunction(Modification _modification) {

        AttributionModification modification = (AttributionModification) (_modification);

        // Cluster d'origine
        Drone originDrone = modification.getOriginDrone();
        ArrayList<Checkpoint> originDroneClusterCheckpoints = droneAttribution.get(originDrone);
        int originCheckpointIndex = originDroneClusterCheckpoints.indexOf(modification.getOriginCheckpoint());

        // Cluster de destination
        Drone destinationDrone = modification.getDestinationDrone();
        ArrayList<Checkpoint> destinationDroneClusterCheckpoints = droneAttribution.get(destinationDrone);

        if (modification.getDestinationCheckpoint() == null) {
            originDroneClusterCheckpoints.remove(originCheckpointIndex);
            destinationDroneClusterCheckpoints.add(modification.getOriginCheckpoint());
        } else {
            int destinationIndex = destinationDroneClusterCheckpoints.indexOf(modification.getDestinationCheckpoint());
            originDroneClusterCheckpoints.remove(originCheckpointIndex);
            destinationDroneClusterCheckpoints.add(destinationIndex, modification.getOriginCheckpoint());
        }
    }

    /**
     * Une modification correspond au transfert d'un sous-array du parcours d'un
     * drone vers un autre drone, éventuellement avec un parcours inversé.
     */
    public class AttributionModification implements Modification {

        private Drone originDrone;
        private Drone destinationDrone;
        private Checkpoint originCheckpoint;
        private Checkpoint destinationCheckpoint;

        public AttributionModification(Drone originDrone, Drone destinationDrone, Checkpoint originCheckpoint,
                Checkpoint destinationCheckpoint) {
            this.originDrone = originDrone;
            this.destinationDrone = destinationDrone;
            this.originCheckpoint = originCheckpoint;
            this.destinationCheckpoint = destinationCheckpoint;
        }

        public Checkpoint getDestinationCheckpoint() {
            return destinationCheckpoint;
        }

        public Drone getDestinationDrone() {
            return destinationDrone;
        }

        public Checkpoint getOriginCheckpoint() {
            return originCheckpoint;
        }

        public Drone getOriginDrone() {
            return originDrone;
        }
    }
}