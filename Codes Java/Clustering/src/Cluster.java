import java.util.LinkedList;

public class Cluster {

    private LinkedList<Checkpoint> innerCheckpoints;

    public Cluster(LinkedList<Checkpoint> innerCheckpoints) {
        this.innerCheckpoints = innerCheckpoints;
    }

    public Cluster(){
        this.innerCheckpoints = new LinkedList<Checkpoint>();
    }
    
    public LinkedList<Checkpoint> getInnerCheckpoints() {
        return innerCheckpoints;
    }

    public void setInnerCheckpoints(LinkedList<Checkpoint> innerCheckpoints) {
        this.innerCheckpoints = innerCheckpoints;
    }

    public void addCheckpoint(Checkpoint checkpoint){
        innerCheckpoints.add(checkpoint);
    }

    public double[] getXList(){
        /* Array avec toutes les coordonnées x */
        ConvexHull convexHull = new ConvexHull(innerCheckpoints);
        LinkedList<Vector> convexHullPoints = convexHull.process();
        double[] xValues = new double[convexHullPoints.size()];
        int i = 0;
        for (Vector point : convexHullPoints){
            xValues[i] = point.getX();
            i ++;
        }
        return xValues;
    }

    public double[] getYList(){
        /* Array avec toutes les coordonnées y */
        ConvexHull convexHull = new ConvexHull(innerCheckpoints);
        LinkedList<Vector> convexHullPoints = convexHull.process();
        double[] yValues = new double[convexHullPoints.size()];
        int i = 0;
        for (Vector point : convexHullPoints){
            yValues[i] = point.getY();
            i ++;
        }
        return yValues;
    }

    public double score(){
        Vector mean = mean();
        double score = 0;
        for (Checkpoint checkpoint : innerCheckpoints){
            score += checkpoint.distance(mean);
        }
        return score;
    }

    public Vector mean(){
        double x = 0;
        double y = 0;
        for (Checkpoint checkpoint : innerCheckpoints){
            x += checkpoint.getX();
            y += checkpoint.getY();
        }
        x = x / innerCheckpoints.size();
        y = y / innerCheckpoints.size();
        return new Vector(x, y);
    }
}
