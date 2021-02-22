public class Controller {

    public static void main(String[] args) {

        int numberOfCheckpoints = 1000;
        int numberOfClusters = 5;

        Checkpoint[] checkpoints = ClusteringAlgorithm.generateRandomCheckpoints(numberOfCheckpoints);
        ClusteringAlgorithm clusteringAlgorithm = new ClusteringTree(checkpoints, numberOfClusters);
        Cluster[] clusters = clusteringAlgorithm.getClusters();
        ClusteringAlgorithm.showResult(clusters);
    }

}
