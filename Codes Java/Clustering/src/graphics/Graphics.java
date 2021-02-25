package graphics;

import java.awt.Color;

import general.Vector;
import general.Cluster;

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
    public static void showResult(Cluster[] clusters) {
        draw.setCanvasSize(2000, 2000);
        for (int i = 0; i < clusters.length; i++) {
            draw.setPenRadius(0.04);
            Color color = Color.getHSBColor(((float) i) / ((float) clusters.length), 1f, 1f);
            draw.setPenColor(color);
            for (Vector checkpoint : clusters[i].getInnerCheckpoints()) {
                draw.point(checkpoint.getX(), checkpoint.getY());
            }
            draw.setPenRadius(0.02);
            Vector[] path = clusters[i].getPath();
            int numberOfCheckpoints = path.length;
            for (int j = 0; j < numberOfCheckpoints - 1; j++) {
                draw.line(path[j].getX(), path[j].getY(), path[j + 1].getX(), path[j + 1].getY());
            }
            draw.line(path[numberOfCheckpoints - 1].getX(), path[numberOfCheckpoints - 1].getY(), path[0].getX(),
                    path[0].getY());
        }
    }

}
