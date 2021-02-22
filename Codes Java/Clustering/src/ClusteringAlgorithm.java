import java.awt.Color;

public interface ClusteringAlgorithm {

    /**
     * Génère un set de checkpoints aléatoire dans le carré unité
     * 
     * @param numberOfCheckpoints le nombre de checkpoints à générer
     * @return le tableau des checkpoints générés
     */
    public static Checkpoint[] generateRandomCheckpoints(int numberOfCheckpoints) {
        Checkpoint[] checkpoints = new Checkpoint[numberOfCheckpoints];
        double x, y;
        for (int i = 0; i < numberOfCheckpoints; i++) {
            x = Math.random();
            y = Math.random();
            checkpoints[i] = new Checkpoint(x, y);
        }
        return checkpoints;
    }

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
            Color color = Color.getHSBColor(((float) i) / ((float) clusters.length), 1f, 1f);
            draw.setPenColor(color);
            for (Checkpoint checkpoint : clusters[i].getInnerCheckpoints()) {
                draw.point(checkpoint.getX(), checkpoint.getY());
            }
        }
    }

    Cluster[] getClusters();

}
