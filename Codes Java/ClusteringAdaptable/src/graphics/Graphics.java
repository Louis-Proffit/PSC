package graphics;

import java.awt.Color;
import java.util.LinkedList;

import algorithms.ClusterAttribution;
import structure.Checkpoint;
import structure.Cluster;
import structure.Drone;
import structure.Vector;

/**
 * Classe contenant les fonctions d'affichage
 * 
 * @LouisProffitX
 * @author Louis Proffit
 * @version 1.0
 */
public class Graphics {

    /**
     * Objet graphique
     */
    private final Draw draw;

    /**
     * Constructeur simple sur 2000 * 2000 pixels
     */
    public Graphics() {
        draw = new Draw();
        draw.setCanvasSize(2000, 2000);
    }

    /**
     * Met à jour les graphique pour une configuration
     * 
     * @param clusterAttribution : la configuration
     */
    public void updateGraphics(ClusterAttribution clusterAttribution) {
        draw.setPenColor(Draw.WHITE);
        draw.filledRectangle(0, 0, 1, 1);
        LinkedList<Drone> drones = clusterAttribution.getDrones();
        int numberOfDrones = drones.size();
        int i = 0;
        for (Drone drone : drones) {
            Color color = Color.getHSBColor(((float) i) / ((float) numberOfDrones), 1f, 1f);
            draw.setPenColor(color);
            updateGraphicsForDrone(drone, clusterAttribution.getDroneCluster(drone));
            i++;
        }
    }

    /**
     * Dessine un drone et son chemin
     * 
     * @param drone   : le drone
     * @param cluster : le cluster du drone
     */
    private void updateGraphicsForDrone(Drone drone, Cluster cluster) {
        paintDrone(drone);
        paintDronePath(cluster);
    }

    /**
     * Dessine un drone
     * 
     * @param drone : le drone
     */
    private void paintDrone(Drone drone) {
        draw.setPenRadius(0.01);
        draw.square(drone.getX(), drone.getY(), 0.01);
    }

    /**
     * Dessine un cluster
     * 
     * @param cluster : le cluster
     */
    private void paintDronePath(Cluster cluster) {
        draw.setPenRadius(0.01);
        LinkedList<Checkpoint> checkpoints = cluster.getCheckpointsOrdered();
        if (checkpoints.size() == 0)
            return;
        if (checkpoints.size() == 1) {
            paintCheckpoint(checkpoints.getFirst());
            return;
        }
        for (int i = 0; i < checkpoints.size() - 1; i++) {
            paintCheckpoint(checkpoints.get(i));
            paintLine(checkpoints.get(i), checkpoints.get(i + 1));
        }
        paintCheckpoint(checkpoints.getLast());
        paintLine(checkpoints.getLast(), checkpoints.getFirst());
    }

    /**
     * Fonction auxiliaire pour dessiner une ligne
     * 
     * @param firstPosition  : la première extrémité de la ligne
     * @param secondPosition : la seconde extrémité de la ligne
     */

    private void paintLine(Vector firstPosition, Vector secondPosition) {
        draw.line(firstPosition.getX(), firstPosition.getY(), secondPosition.getX(), secondPosition.getY());
    }

    /**
     * Dessine un checkpoint
     * 
     * @param checkpoint
     */
    private void paintCheckpoint(Checkpoint checkpoint) {
        draw.setPenRadius(0.02);
        draw.point(checkpoint.getX(), checkpoint.getY());
    }
}
