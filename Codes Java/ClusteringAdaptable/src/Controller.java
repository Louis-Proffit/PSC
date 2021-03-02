import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import algorithms.Clustering;
import algorithms.ImproveType;
import graphics.LocalGraphics;
import structure.Checkpoint;
import structure.Drone;
import structure.Vector;

public class Controller {

    private static int numberOfSteps = 1000;

    private static final int numberOfDrones = 5;
    private static final int numberOfCheckpoints = 30;
    private static final LinkedList<Drone> drones = new LinkedList<>();
    private static final Clustering clustering = new Clustering();
    private static final LocalGraphics graphics = new LocalGraphics(1000, 1000);

    private static void init() {
        for (int i = 0; i < numberOfDrones; i++) {
            addDrone(new Drone(new Vector()), false);
        }
        for (int i = 0; i < numberOfCheckpoints; i++) {
            addCheckpoint(new Checkpoint(new Vector()), ImproveType.NULL);
        }
        clustering.improveComplete();
        graphics.updateGraphics(drones);
    }

    private static void update() {
        for (Drone drone : drones) {
            drone.move();
        }
        graphics.updateGraphics(drones);
    }

    private static void addDrone(Drone drone, boolean improve) {
        drones.add(drone);
        clustering.addCluster(drone.getCluster());
        if (improve)
            clustering.improveComplete();
    }

    private static void addCheckpoint(Checkpoint checkpoint, ImproveType improve) {
        clustering.addCheckpoint(checkpoint, improve);
    }

    private static void run() {
        try {
            for (int i = 0; i < numberOfSteps; i++) {
                TimeUnit.MILLISECONDS.sleep(200);
                update();
            }
        } catch (InterruptedException e) {
            System.out.println("Simulation interrompue");
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        init();
        run();
    }

}
