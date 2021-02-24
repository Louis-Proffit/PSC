public class Controller {

    public static void main(String[] args) {

        int numberOfCheckpoints = 200;
        int numberOfClusters = 5;
        int numberOfBlocks = 5;
        double variance = 0.1;

        Checkpoint[] checkpoints = RandomGenerator.generateCheckpointsFromBlocks(numberOfCheckpoints, numberOfBlocks,
                variance);
        ClusteringAlgorithm clusteringAlgorithm = new ClusteringTree(checkpoints, numberOfClusters);
        Cluster[] clusters = clusteringAlgorithm.getClusters();
        ClusteringAlgorithm.showResult(clusters);
    }

}
