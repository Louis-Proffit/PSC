package graphics;

import java.awt.Color;

import general.Cluster;
import general.Drone;
import general.Vector;

/**
 * Classe contenant les fonctions d'affichage
 * 
 * @author Louis Proffit
 */
public class Graphics {

    /**
     * Objet graphique
     */
    private static Draw draw = new Draw();

    /**
     * Affiche les checkpoints, dans le carré unité, avec une couleur suivant un
     * dégradé pour les différencier, ainsi que le chemin parcourant chacun des
     * clusters
     * 
     * @param clusters la liste des clusters contenant les checkpoints
     */
    public static void showResult(Drone[] drones) {
        draw.setCanvasSize(2000, 2000);
        int numberOfDrones = drones.length;
        Cluster cluster;
        Drone drone;
        for (int i = 0; i < numberOfDrones; i++) {
            Color color = Color.getHSBColor(((float) i) / ((float) numberOfDrones), 1f, 1f);
            drone = drones[i];
            cluster = drone.getAttributedCluster();
            draw.setPenRadius(0.01);
            draw.setPenColor(color);
            draw.square(drone.getX(), drone.getY(), 0.01); // On dessine le drone
            for (Vector checkpoint : cluster.getInnerCheckpoints()) {
                draw.point(checkpoint.getX(), checkpoint.getY()); // On dessine les checkpoints
            }
            draw.setPenRadius(0.02);
            Vector[] path = cluster.getPath();
            int numberOfCheckpoints = path.length;
            for (int j = 0; j < numberOfCheckpoints - 1; j++) {
                draw.line(path[j].getX(), path[j].getY(), path[j + 1].getX(), path[j + 1].getY()); // On dessine le
                                                                                                   // chemin
            }
            draw.line(path[numberOfCheckpoints - 1].getX(), path[numberOfCheckpoints - 1].getY(), path[0].getX(),
                    path[0].getY());
        }
    }

}
