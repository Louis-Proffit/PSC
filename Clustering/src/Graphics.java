import java.awt.Color;
import java.util.List;

public class Graphics {

    /**
     * Affiche les checkpoints, dans le carré unité, avec une couleur suivant un
     * dégradé pour les différencier
     * 
     * @param clusters la liste des clusters contenant les checkpoints
     */
    public static void showResult(Cluster[] clusters) {
        Draw draw = new Draw();
        draw.setPenRadius(0.02);
        for (int i = 0; i < clusters.length; i++) {
            int numberOfCheckpoints = clusters[i].getInnerCheckpoints().size();
            Color color = Color.getHSBColor(((float) i) / ((float) clusters.length), 1f, 1f);
            draw.setPenColor(color);
            for (Checkpoint checkpoint : clusters[i].getInnerCheckpoints()) {
                draw.point(checkpoint.getX(), checkpoint.getY());
            }
            draw.setPenRadius(0.01);
            List<Vector> path = clusters[i].getPath();
            for (int j = 0; j < numberOfCheckpoints - 1; j++) {
                draw.line(path.get(j).getX(), path.get(j).getY(), path.get(j + 1).getX(), path.get(j + 1).getY());
            }
            draw.line(path.get(numberOfCheckpoints - 1).getX(), path.get(numberOfCheckpoints - 1).getY(),
                    path.get(0).getX(), path.get(0).getY());
        }
    }

}
