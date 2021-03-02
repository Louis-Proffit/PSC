package structure;

public class Checkpoint {

    private final Vector position;

    public Checkpoint() {
        this.position = new Vector();
    }

    public Checkpoint(Vector position) {
        this.position = position;
    }

    public Vector getPosition() {
        return position;
    }

    public Checkpoint copy() {
        return new Checkpoint(this.getPosition().copy());
    }

    public Checkpoint(Vector position, Checkpoint nextCheckpoint, Checkpoint previousCheckpoint) {
        this.position = position;
    }

    public double distance(Vector vector) {
        return vector.distance(this.position);
    }

    public double distance(Checkpoint checkpoint) {
        return checkpoint.position.distance(this.position);
    }
}
