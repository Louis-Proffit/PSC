package structure;

import java.util.HashMap;
import java.util.LinkedList;

public class Path {

    private int size;
    private HashMap<Integer, Checkpoint> order;
    private HashMap<Checkpoint, Integer> indices;

    public Path() {
        this.size = 0;
        this.indices = new HashMap<>();
        this.order = new HashMap<>();
    }

    public Checkpoint getCheckpointAfter(Checkpoint checkpoint) {
        if (size == 1)
            return null;
        else {
            int currentIndex = indices.get(checkpoint);
            if (currentIndex < size - 1)
                return order.get(currentIndex + 1);
            else
                return order.get(0);
        }
    }

    public Checkpoint getCheckpointBefore(Checkpoint checkpoint) {
        if (size == 1)
            return null;
        else {
            int currentIndex = indices.get(checkpoint);
            if (currentIndex > 0)
                return order.get(currentIndex - 1);
            else
                return order.get(size - 1);
        }
    }

    public Checkpoint getCheckpointAtIndex(int index) {
        return order.get(index);
    }

    public int getCheckpointIndex(Checkpoint checkpoint) {
        return indices.get(checkpoint);
    }

    public LinkedList<Checkpoint> getCheckpointsOrdered() {
        LinkedList<Checkpoint> result = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            result.add(order.get(i));
        }
        return result;
    }

    public void addCheckpoint(Checkpoint checkpoint) {
        order.put(size, checkpoint);
        indices.put(checkpoint, size);
        size++;

    }

    public void removeCheckpoint(Checkpoint checkpoint) {
        int index = indices.get(checkpoint);
        indices.remove(checkpoint);
        order.remove(index);
        size--;
        Checkpoint otherCheckpoint;
        for (int otherIndex = index + 1; otherIndex < size; otherIndex++) {
            otherCheckpoint = order.get(otherIndex);
            order.put(otherIndex - 1, otherCheckpoint);
            indices.put(otherCheckpoint, otherIndex - 1);
        }
        indices.replaceAll((c, oldIndex) -> {
            if (oldIndex > index)
                return oldIndex - 1;
            return oldIndex;
        });
    }

    /**
     * Exchanges the two indices
     * 
     * @param index1
     * @param index2
     */
    public void swapOrder(int index1, int index2) {
        Checkpoint checkpoint1 = order.get(index1);
        Checkpoint checkpoint2 = order.put(index2, checkpoint1);
        order.put(index1, checkpoint2);
        indices.put(checkpoint1, index2);
        indices.put(checkpoint2, index1);
    }

    public int getSize() {
        return size;
    }
}
