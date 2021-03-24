package graphics;

import java.util.concurrent.TimeUnit;

import algorithms.ClusterAttribution;
import algorithms.ImproveType;
import structure.Checkpoint;
import structure.Drone;
import structure.MutableVector;

/**
 * Classe principale, à executer pour faire fonctionner les algorithmes.
 * 
 * @LouisProffitX
 * @author Louis Proffit
 * @version 1.0
 */
public class Controller {

    /**
     * Nombre d'étapes de la simulation
     */
    private static int numberOfSteps = 1000;

    /**
     * Nombre de drones à générer au départ
     */
    public static final int numberOfDrones = 5;

    /**
     * Nombre de checkpoints à générer au départ
     */
    public static final int numberOfCheckpoints = 20;

    /**
     * Objet de calcul de l'attribuion des drones aux clusters
     */
    private static final ClusterAttribution clusterAttribution = new ClusterAttribution();

    /**
     * Objet graphique
     */
    private static final GraphicsInterface graphics = new LocalGraphics(1000, 1000);

    /**
     * Crée une configuration initiale aléatoire avec un nombre de drones et de
     * clusters fixé. Améliore immédiatement la solution, et effectue un affichage
     */
    private static void init() {
        for (int i = 0; i < numberOfDrones; i++) {
            addDrone(ImproveType.NULL, new Drone(new MutableVector()));
        }
        for (int i = 0; i < numberOfCheckpoints; i++) {
            addCheckpoint(ImproveType.NULL, new Checkpoint(new MutableVector()));
        }
        clusterAttribution.improve(ImproveType.COMPLETE);
        draw();
    }

    /**
     * Met à jour la situation : les drones bougent et le graphique évolue
     */
    private static void update() {
        clusterAttribution.move();
        draw();
    }

    /**
     * Met à jour le graphique
     */
    private static void draw() {
        graphics.updateGraphics(clusterAttribution);
    }

    /**
     * Ajoute un drone
     * 
     * @param drone   : le drone à ajouter
     * @param improve : le travail à effectuer à l'issue
     */
    public static void addDrone(ImproveType improve, Drone... drones) {
        clusterAttribution.addDrone(improve, drones);
    }

    /**
     * Ajoute un checkpoint
     * 
     * @param checkpoint : le checkpoint à ajouter
     * @param improve    : le travail à effectuer à la fin
     */
    public static void addCheckpoint(ImproveType improve, Checkpoint... checkpoints) {
        clusterAttribution.addCheckpoint(improve, checkpoints);
    }

    /**
     * Fait tourner la simulation
     */
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

    /**
     * Initialise et fait tourner la simulation
     */
    public static void main(String[] args) {
        init();
        run();
    }

}
