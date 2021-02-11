import java.awt.Color;

public class Kmeans {

    public static double[] getBounds(Checkpoint[] checkpoints) {
        // Format : [xmin, xmax, ymin, ymax]
        double xmin = 0;
        double ymin = 0;
        double xmax = 0;
        double ymax = 0;
        for (Checkpoint checkpoint : checkpoints) {
            if (checkpoint.getX() < xmin)
                xmin = checkpoint.getX();
            if (checkpoint.getY() < ymin)
                ymin = checkpoint.getY();
            if (checkpoint.getX() > xmax)
                xmax = checkpoint.getX();
            if (checkpoint.getY() > xmax)
                ymax = checkpoint.getY();
        }
        return new double[] { xmin, xmax, ymin, ymax };
    }

    public static Cluster[] generateRandomRepartition(Checkpoint[] checkpoints, int numberOfClusters) {
        /* La méthode rend une liste de clusters. */
        int size = checkpoints.length;
        Cluster[] clusters = new Cluster[numberOfClusters];
        for (int i = 0; i < numberOfClusters; i++)
            clusters[i] = new Cluster();
        int index;
        for (int i = 0; i < size; i++) {
            index = (int) (Math.random() * numberOfClusters);
            clusters[index].addCheckpoint(checkpoints[i]);
        }
        return clusters;
    }

    public static double getRepartitionScore(Cluster[] clusters) {
        double score = 0;
        for (Cluster cluster : clusters) {
            score += cluster.score();
        }
        return score;
    }

    public static Cluster[] KMean(Checkpoint[] checkpoints, int numberOfClusters){
        int numberOfPasses = 1000;
        Cluster[] solutionRepartition = null;
        Cluster[] currentRepartition;
        double solutionScore = 0;
        double currentScore;
        for (int i = 0 ; i < numberOfPasses ; i++){
            currentRepartition = generateRandomRepartition(checkpoints, numberOfClusters);
            currentScore = getRepartitionScore(currentRepartition);
            if (currentScore < solutionScore | i == 0){
                solutionScore = currentScore;
                solutionRepartition = currentRepartition;
            }
        }
        return solutionRepartition;
    }

    public static void showResultsPolygons(Cluster[] clusters){
        Draw draw = new Draw();
        draw.setPenRadius(0.02);
        for (int i = 0 ; i < clusters.length ; i++){
            Color color = Color.getHSBColor(((float)i) / ((float)clusters.length), 1f, 1f);
            draw.setPenColor(color);
            double[] xList = clusters[i].getXList();
            double[] yList = clusters[i].getYList();
            draw.polygon(xList, yList);
        }
    }

    public static Checkpoint[] generateRandomCheckpoints(int numberOfCheckpoints){
        Checkpoint[] checkpoints = new Checkpoint[numberOfCheckpoints];
        double x,y;
        for (int i = 0 ; i < numberOfCheckpoints ; i++){
            x = Math.random();
            y = Math.random();
            checkpoints[i] = new Checkpoint(x, y);
        }
        return checkpoints;
    }

    public static void main(String[] args){
        int numberOfCheckpoints = 20;
        int numberOfClusters = 5;
        Checkpoint[] checkpoints = generateRandomCheckpoints(numberOfCheckpoints);

        Cluster[] clusters = KMean(checkpoints, numberOfClusters);
        /*showResultsPoints(clusters);*/
        showResultsPolygons(clusters);
    }

}
