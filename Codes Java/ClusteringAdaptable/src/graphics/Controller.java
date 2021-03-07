package graphics;

import java.util.concurrent.TimeUnit;

import algorithms.ClusterAttribution;
import algorithms.ImproveType;
import structure.Checkpoint;
import structure.Drone;
import structure.MutableVector;

public class Controller {

    private static int numberOfSteps = 1000;

    private static final int numberOfDrones = 5;
    private static final int numberOfCheckpoints = 20;
    private static final ClusterAttribution clusterAttribution = new ClusterAttribution();
    private static final LocalGraphics graphics = new LocalGraphics(1000, 1000);

    private static void init() {
        for (int i = 0; i < numberOfDrones; i++) {
            addDrone(new Drone(new MutableVector()), ImproveType.NULL);
        }
        for (int i = 0; i < numberOfCheckpoints; i++) {
            addCheckpoint(new Checkpoint(new MutableVector()), ImproveType.NULL);
        }
        clusterAttribution.improve(ImproveType.COMPLETE);
        graphics.updateGraphics(clusterAttribution);
    }

    private static void update() {
        clusterAttribution.move();
        draw();
    }

    private static void draw() {
        graphics.updateGraphics(clusterAttribution);
    }

    public static void addDrone(Drone drone, ImproveType improveType) {
        clusterAttribution.addDrone(drone, improveType);
    }

    public static void addCheckpoint(Checkpoint checkpoint, ImproveType improve) {
        clusterAttribution.addCheckpoint(checkpoint, improve);
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
