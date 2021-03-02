package structure;

public class Drone {

    public static double speed = 0.01;

    private Vector position;
    private Cluster cluster;

    public Drone() {
        this.position = new Vector();
        this.cluster = new Cluster();
    }

    public Drone(Vector position) {
        this.position = position;
        this.cluster = new Cluster();
    }

    public Drone(Vector position, Cluster cluster) {
        this.position = position;
        this.cluster = cluster;
    }

    public Vector getPosition() {
        return position;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void move() {
        cluster.moveTowardsTarget(position, speed);
    }
}
