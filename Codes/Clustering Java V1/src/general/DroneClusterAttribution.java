package general;

import java.util.Arrays;

import tspsolving.TSPRecuit.DecroissanceType;

public class DroneClusterAttribution {

    /**
     * Type de décroissance de la température
     */
    public static DecroissanceType temperatureDecroissanceType = DecroissanceType.N;

    /**
     * Valeur initiale de la température
     */
    public static double temperatureInitialValue = 1;

    /**
     * Nombre d'étapes de simulation
     */
    public static int numberOfSteps = 10000;

    /**
     * Nombre d'étapes sans changement au bout desquelles on accepte la solution
     */
    public static int numberOfStepsWithoutChanges = 100;

    /**
     * Matrice des distances entre drones et clusters. M_{i,j} est la distance entre
     * le ième drone et le jième cluster
     */
    private DistanceToCluster[][] distances;

    /**
     * Matrice décrivant le matching entre les drones et les clusters. X_i est
     * l'indice du cluster du ième drone
     */
    private int length;

    public void attribute(Drone[] drones, Cluster[] clusters) {
        long startTime = System.currentTimeMillis();
        this.length = drones.length;
        this.distances = getDistances(drones, clusters);
        int[] matching = getFirstMatching(length);

        int currentSteps = 0;
        int currentStepsWithoutChanges = 0;
        numberOfStepsWithoutChanges = 4 * length;
        numberOfSteps = 100 * length;
        double currentTemperature;
        int[] proposition;
        double solutionScore = getMatchingScore(matching);
        double propositionScore;

        while (currentSteps < numberOfSteps & currentStepsWithoutChanges < numberOfStepsWithoutChanges) {
            currentTemperature = temperature(currentSteps);
            proposition = proposition(matching);
            propositionScore = getMatchingScore(proposition);

            if (h1(Math.exp((solutionScore - propositionScore) / currentTemperature)) > Math.random()) {
                matching = proposition;
                solutionScore = propositionScore;
                if (solutionScore == propositionScore)
                    currentStepsWithoutChanges = 0;
                else
                    currentStepsWithoutChanges += 1;
            } else
                currentStepsWithoutChanges += 1;
            currentSteps++;
        }
        for (int i = 0; i < length; i++) {
            drones[i].setAttributedCluster(clusters[matching[i]]);
        }
        long deltaTimeInMillis = System.currentTimeMillis() - startTime;
        System.out.println("TSP Calculé en " + deltaTimeInMillis + "ms");
        if (currentStepsWithoutChanges == numberOfStepsWithoutChanges)
            System.out.println("--TSP shortcuted--");
    }

    /**
     * Fonction de température, auxiliaire pour le recuit simulé
     * 
     * @param time : Le temps auquel on calcule la température
     * @return La température au temps <b>time</b>
     */
    private double temperature(int time) {
        switch (temperatureDecroissanceType) {
            case LOG:
                return temperatureInitialValue / Math.log(time);
            case N:
                return temperatureInitialValue / time;
            case N2:
                return temperatureInitialValue / (time * time);
        }
        return 0;
    }

    /**
     * Une fonction auxiliaire h pour le recuit simulé, vérifie l'équation h(x) =
     * xh(1/x)
     */
    @SuppressWarnings("unused")
    private static double h1(double x) {
        return Math.min(1, x);
    }

    /**
     * Une fonction auxiliaire h pour le recuit simulé, vérifie l'équation h(x) =
     * xh(1/x)
     */
    @SuppressWarnings("unused")
    private static double h2(double x) {
        return x / (1 + x);
    }

    private int[] proposition(int[] currentMatching) {
        int a = (int) (Math.random() * length);
        int b = (int) (Math.random() * length);
        while (a == b)
            b = (int) (Math.random() * length);
        int[] proposition = new int[length];
        for (int i = 0; i < length; i++) {
            if (i == a)
                proposition[i] = currentMatching[b];
            else if (i == b)
                proposition[i] = currentMatching[a];
            else
                proposition[i] = currentMatching[i];
        }
        return proposition;
    }

    private double getMatchingScore(int[] currentMatching) {
        double score = 0;
        for (int i = 0; i < length; i++) {
            score += distances[i][currentMatching[i]].getDistance();
        }
        return score;
    }

    /**
     * Méthode pour initialiser un tableau d'attribution
     * 
     * @param length : La longueur du tableau
     * @return un tableau de longueur <b>length</b> contenant i à l'index i.
     */
    private static int[] getFirstMatching(int length) {
        int[] matching = new int[length];
        for (int i = 0; i < length; i++) {
            matching[i] = i;
        }
        return matching;
    }

    /**
     * Méthode pour initialiser la matrice des distances
     * 
     * @param checkpoints : La liste des checkpoints
     * @return Une matrice carrée de côté le nombre des checkpoints telle que
     *         M_{i,j} = d(C_i, C_j)
     */
    private static DistanceToCluster[][] getDistances(Drone[] drones, Cluster[] clusters) {
        int length = drones.length;
        DistanceToCluster[][] distances = new DistanceToCluster[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                distances[i][j] = clusters[j].distanceToCluster(drones[i]);
            }
        }
        return distances;
    }
}