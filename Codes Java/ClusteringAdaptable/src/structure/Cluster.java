package structure;

import java.util.LinkedList;

import algorithms.RecuitInterface;
import algorithms.TSPRecuit;

public class Cluster implements RecuitInterface {

    private Checkpoint currentTarget;
    private Path path;
    private final Vector sum;

    public Cluster() {
        this.currentTarget = null;
        this.path = new Path();
        this.sum = new Vector(0f, 0f);
    }

    public void improvePath() {
        TSPRecuit.improvePath(this);
    }

    public Vector getMean() {
        switch (path.getSize()) {
            case (0):
                return sum;
            default:
                return sum.getMult(1f / path.getSize());
        }
    }

    public void moveTowardsTarget(Vector position, double speed) {
        if (currentTarget == null)
            return;
        if (position.distance(currentTarget.getPosition()) < speed) {
            position.set(currentTarget.getPosition());
            moveTargetForward();
        } else {
            Vector movement = currentTarget.getPosition().copyMinus(position);
            movement.normalize(speed);
            position.add(movement);
        }
    }

    private void moveTargetForward() {
        if (currentTarget == null)
            return;
        this.currentTarget = path.getCheckpointAfter(currentTarget);
    }

    public LinkedList<Checkpoint> getCheckpointsOrdered() {
        return path.getCheckpointsOrdered();
    }

    @Override
    public int getSize() {
        return path.getSize();
    }

    public void addCheckpoint(Checkpoint checkpoint) {
        if (currentTarget == null) {
            currentTarget = checkpoint;
        }
        sum.add(checkpoint.getPosition());
        path.addCheckpoint(checkpoint);
    }

    public void removeCheckpoint(Checkpoint checkpoint) {
        if (currentTarget == checkpoint) {
            currentTarget = path.getCheckpointAfter(checkpoint);
        }
        sum.substract(checkpoint.getPosition());
        path.removeCheckpoint(checkpoint);
    }

    @Override
    public Modification modificationFunction() {
        int size = path.getSize();
        return new Pair<Integer>((int) (Math.random() * size), (int) (Math.random() * size));
    }

    /**
     * Renvoie la différence entre la nouvelle distance et l'ancienne distance
     */
    @Override
    @SuppressWarnings("unchecked")
    public Double improvementFunction(Modification modification) {
        double result = 0;
        Pair<Integer> swap = (Pair<Integer>) modification;
        Checkpoint firstCheckpoint = path.getCheckpointAtIndex(swap.getFirst());
        Checkpoint secondCheckpoint = path.getCheckpointAtIndex(swap.getSecond());
        Checkpoint checkpointBeforeFirst = path.getCheckpointBefore(firstCheckpoint);
        Checkpoint checkpointAfterSecond = path.getCheckpointAfter(secondCheckpoint);
        result += checkpointBeforeFirst.getPosition().distance(secondCheckpoint.getPosition());
        result += checkpointAfterSecond.getPosition().distance(firstCheckpoint.getPosition());
        result -= firstCheckpoint.getPosition().distance(checkpointBeforeFirst.getPosition());
        result -= secondCheckpoint.getPosition().distance(checkpointAfterSecond.getPosition());
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void commitFunction(Modification modification) {
        Pair<Integer> swap = (Pair<Integer>) modification;
        int size = path.getSize();
        int increasingIndex = swap.getFirst();
        int decreasingIndex = swap.getSecond();
        if (increasingIndex == decreasingIndex)
            return;
        if (increasingIndex < decreasingIndex) {
            while (increasingIndex < decreasingIndex) {
                path.swapOrder(increasingIndex, decreasingIndex);
                increasingIndex++;
                decreasingIndex--;
            }
        } else {
            boolean changed = false;
            while (increasingIndex < decreasingIndex & !changed) {
                path.swapOrder(increasingIndex, decreasingIndex);
                increasingIndex++;
                decreasingIndex--;
                if (increasingIndex == size) {
                    changed = true;
                    increasingIndex = 0;
                }
                if (increasingIndex == -1) {
                    changed = true;
                    decreasingIndex = size - 1;
                }
            }
        }
    }
}
