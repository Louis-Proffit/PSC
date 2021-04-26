package general;

public class DistanceToCluster {

    private final double distance;
    private final Checkpoint checkpoint;

    public DistanceToCluster(double distance, Checkpoint checkpoint) {
        this.distance = distance;
        this.checkpoint = checkpoint;
    }

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    public double getDistance() {
        return distance;
    }
}
