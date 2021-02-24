public class Controller {

    public static void main(String[] args) {

        int numberOfCheckpoints = 30;
        int numberOfClusters = 5;
        int numberOfBlocks = 1;
        double variance = 1;

        Checkpoint[] checkpointsBlocks = CheckpointGenerator.generateCheckpointsFromBlocks(numberOfCheckpoints,
                numberOfBlocks, variance);
        Checkpoint[] checkpointsUniform = CheckpointGenerator.generateCheckpoints(numberOfCheckpoints);
        ClusteringAlgorithm clusteringAlgorithm = new ClusteringTree(checkpointsUniform, numberOfClusters);
        Cluster[] clusters = clusteringAlgorithm.getClusters();
        TSPSolver solverGenetique = new Genetique(numberOfCheckpoints, 50, numberOfCheckpoints / 10);
        TSPSolver solverRecuit = new Recuit(50, Decroissance.N, 1);
        System.out.println("Prêt à executer le solveur");
        for (Cluster cluster : clusters)
            cluster.setPath(solverRecuit);
        System.out.println("Prêt à executer le graphique");
        Graphics.showResult(clusters);
    }

}
