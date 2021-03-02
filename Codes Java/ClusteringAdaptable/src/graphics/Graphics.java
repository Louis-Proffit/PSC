package graphics;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import structure.Checkpoint;
import structure.Cluster;
import structure.Drone;
import structure.Vector;

/**
 * Classe contenant les fonctions d'affichage
 * 
 * @author Louis Proffit
 */
public class Graphics {

    /**
     * Objet graphique
     */
    private final Draw draw;

    /**
     * 
     */
    public Graphics() {
        draw = new Draw();
        draw.setCanvasSize(2000, 2000);
    }

    /**
     * 
     * @param drones
     */
    public void updateGraphics(List<Drone> drones) {
        draw.setPenColor(Draw.WHITE);
        draw.filledRectangle(0, 0, 1, 1);
        int numberOfDrones = drones.size();
        for (int i = 0; i < numberOfDrones; i++) {
            Color color = Color.getHSBColor(((float) i) / ((float) numberOfDrones), 1f, 1f);
            draw.setPenColor(color);
            updateGraphicsForDrone(drones.get(i));
        }
    }

    /**
     * 
     * @param drone
     */
    private void updateGraphicsForDrone(Drone drone) {
        paintDrone(drone);
        paintDronePath(drone.getCluster());
    }

    /**
     * 
     * @param drone
     */
    private void paintDrone(Drone drone) {
        draw.setPenRadius(0.01);
        draw.square(drone.getPosition().getX(), drone.getPosition().getY(), 0.01);
    }

    /**
     * 
     * @param drone
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
            paintLine(checkpoints.get(i).getPosition(), checkpoints.get(i + 1).getPosition());
        }
        paintCheckpoint(checkpoints.getLast());
        paintLine(checkpoints.getLast().getPosition(), checkpoints.getFirst().getPosition());
    }

    /**
     * 
     * @param firstPosition
     * @param secondPosition
     */
    private void paintLine(Vector firstPosition, Vector secondPosition) {
        draw.line(firstPosition.getX(), firstPosition.getY(), secondPosition.getX(), secondPosition.getY());
    }

    /**
     * 
     * @param checkpoint
     */
    private void paintCheckpoint(Checkpoint checkpoint) {
        draw.setPenRadius(0.02);
        draw.point(checkpoint.getPosition().getX(), checkpoint.getPosition().getY());
    }

}
