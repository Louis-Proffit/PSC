import java.util.Random;

public class RandomGenerator {

    public static Random random = new Random();

    public static Checkpoint[] generateCheckpointsFromBlocks(int numberOfClusters, int numberOfBlocks,
            double variance) {
        Checkpoint[] blocks = new Checkpoint[numberOfBlocks];
        Checkpoint[] result = new Checkpoint[numberOfClusters];
        for (int i = 0; i < numberOfBlocks; i++)
            blocks[i] = generateRandomCheckpoint();
        for (int i = 0; i < numberOfClusters; i++) {
            int blockIndex = i % numberOfBlocks;
            result[i] = generateRandomCheckpointFromBlockGaussian(blocks[blockIndex], variance);
        }
        return result;
    }

    public static Checkpoint generateRandomCheckpoint() {
        return new Checkpoint(Math.random(), Math.random());
    }

    public static Checkpoint generateRandomCheckpointFromBlockGaussian(Checkpoint blockCenter, double variance) {
        Random random = new Random();
        double x = blockCenter.getX() + random.nextGaussian() * variance;
        double y = blockCenter.getY() + random.nextGaussian() * variance;
        if (x < 0)
            x = 0;
        if (x > 1)
            x = 1;
        if (y < 0)
            x = 0;
        if (y > 1)
            x = 1;
        return new Checkpoint(x, y);
    }
}