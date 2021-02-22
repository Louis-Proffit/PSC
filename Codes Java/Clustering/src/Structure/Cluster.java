import java.util.LinkedList;

public class Cluster {

    private final LinkedList<Checkpoint> innerCheckpoints;
    private final Vector sum;

    public Cluster() {
        this.innerCheckpoints = new LinkedList<Checkpoint>();
        this.sum = new Vector(0, 0);
    }

    public LinkedList<Checkpoint> getInnerCheckpoints() {
        return innerCheckpoints;
    }

    public void addCheckpoint(Checkpoint checkpoint) {
        innerCheckpoints.add(checkpoint);
        sum.add(checkpoint);
    }

    public double getVariance() {
        double variance = 0;
        Vector mean = sum.getMult(1f / innerCheckpoints.size());
        for (Checkpoint checkpoint : innerCheckpoints) {
            variance += Math.pow(checkpoint.getX() - mean.getX(), 2) + Math.pow(checkpoint.getY() - mean.getY(), 2);
        }
        return variance;
    }

    public double[] getXList() {
        /* Array avec toutes les coordonnées x */
        ConvexHull convexHull = new ConvexHull(innerCheckpoints);
        LinkedList<Vector> convexHullPoints = convexHull.process();
        double[] xValues = new double[convexHullPoints.size()];
        int i = 0;
        for (Vector point : convexHullPoints) {
            xValues[i] = point.getX();
            i++;
        }
        return xValues;
    }

    public double[] getYList() {
        /* Array avec toutes les coordonnées y */
        ConvexHull convexHull = new ConvexHull(innerCheckpoints);
        LinkedList<Vector> convexHullPoints = convexHull.process();
        double[] yValues = new double[convexHullPoints.size()];
        int i = 0;
        for (Vector point : convexHullPoints) {
            yValues[i] = point.getY();
            i++;
        }
        return yValues;
    }

    public double score() {
        Vector mean = mean();
        double score = 0;
        for (Checkpoint checkpoint : innerCheckpoints) {
            score += checkpoint.distance(mean);
        }
        return score;
    }

    public Vector mean() {
        double x = 0;
        double y = 0;
        for (Checkpoint checkpoint : innerCheckpoints) {
            x += checkpoint.getX();
            y += checkpoint.getY();
        }
        x = x / innerCheckpoints.size();
        y = y / innerCheckpoints.size();
        return new Vector(x, y);
    }
}