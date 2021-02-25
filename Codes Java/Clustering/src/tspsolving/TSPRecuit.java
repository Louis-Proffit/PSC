package tspsolving;

import java.util.Arrays;

import general.Checkpoint;

/**
 * Classe implémentant un solveur de TSP par une méthode de recuit simulé.
 * 
 * @author Louis Proffit
 */
public class TSPRecuit implements TSPSolver {

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
	public static int numberOfSteps = 100000;

	/**
	 * Nombre d'étapes sans changement au bout desquelles on accepte la solution
	 */
	public static int numberOfStepsWithoutChanges = 100;

	/**
	 * Méthode de synthèse du calcul du chemin optimal
	 * 
	 * @param points : la liste des checkpoints
	 * @return La liste des checkpoints dans l'ordre optimal trouvé
	 */
	public Checkpoint[] getPath(Checkpoint[] points) {
		long startTime = System.currentTimeMillis();
		if (points.length <= 3)
			return copy(points); // Rien à changer
		int numberOfPoints = points.length;
		double[][] distances = new double[numberOfPoints][numberOfPoints];
		double distance;
		for (int i = 0; i < numberOfPoints; i++) {
			for (int j = 0; j < numberOfPoints; j++) {
				distance = points[i].distance(points[j]);
				distances[i][j] = distance;
				distances[j][i] = distance;
			}
		}
		int currentSteps = 0;
		int currentStepsWithoutChanges = 0;
		numberOfStepsWithoutChanges = 4 * numberOfPoints;
		double currentTemperature;
		Checkpoint[] solution = copy(points);
		Checkpoint[] proposition;
		double solutionScore = getPathScore(solution);
		double propositionScore;

		while (currentSteps < numberOfSteps & currentStepsWithoutChanges < numberOfStepsWithoutChanges) {
			currentTemperature = temperature(currentSteps);
			proposition = proposition2(solution);
			propositionScore = getPathScore(proposition);

			if (h1(Math.exp((solutionScore - propositionScore) / currentTemperature)) > Math.random()) {
				solution = proposition;
				solutionScore = propositionScore;
				if (solutionScore == propositionScore)
					currentStepsWithoutChanges = 0;
				else
					currentStepsWithoutChanges += 1;
			} else
				currentStepsWithoutChanges += 1;
			currentSteps++;
		}
		long deltaTimeInMillis = System.currentTimeMillis() - startTime;
		System.out.println("TSP Calculé en " + deltaTimeInMillis + "ms");
		if (currentStepsWithoutChanges == numberOfStepsWithoutChanges)
			System.out.println("--TSP shortcuted--");
		return solution;
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
	 * Fonction pour calculer la longueur d'un chemin
	 * 
	 * @param path : Le chemin dont on veut calculer la longueur
	 * @return La longueur du chemin <b>path</b>
	 */
	private double getPathScore(Checkpoint[] path) {
		double s = 0;
		int numberOfPoints = path.length;
		for (int i = 0; i < numberOfPoints - 1; i++) {
			s += path[i].distance(path[i + 1]);
		}
		s += path[numberOfPoints - 1].distance(path[0]);
		return s;
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

	/**
	 * Fonction de proposition qui renvoie un chemin dérivé de
	 * <b>currentSolution</b> avec la permutation de deux éléments La fonction ne
	 * modifie pas son argument.
	 * 
	 * @param currentSolution : Le chemin actuel.
	 * @return La nouvelle proposition de chemin.
	 */
	@SuppressWarnings("unused")
	private Checkpoint[] proposition1(Checkpoint[] currentSolution) {
		int numberOfPoints = currentSolution.length;
		Checkpoint[] proposition = new Checkpoint[numberOfPoints];
		for (int i = 0; i < numberOfPoints; i++) {
			proposition[i] = currentSolution[i];
		}
		int firstIndex = (int) (Math.random() * numberOfPoints);
		int secondIndex = (int) (Math.random() * numberOfPoints);
		while (secondIndex == firstIndex) {
			secondIndex = (int) (Math.random() * numberOfPoints);
		}
		Checkpoint temporaryVariable = currentSolution[firstIndex];
		proposition[firstIndex] = proposition[secondIndex];
		proposition[secondIndex] = temporaryVariable;
		return proposition;
	}

	/**
	 * Fonction de proposition, qui inverse l'ordre de parcours d'un sous-tableau du
	 * chemin <b>currentSolution</b>. La fonction ne modifie pas son argument.
	 * 
	 * @param currentSolution : Le chemin actuel.
	 * @return Le nouveau chemin.
	 */
	@SuppressWarnings("unused")
	private Checkpoint[] proposition2(Checkpoint[] currentSolution) {
		int numberOfPoints = currentSolution.length;
		int firstIndex = (int) (Math.random() * numberOfPoints);
		int secondIndex = (int) (Math.random() * numberOfPoints);
		while (secondIndex == firstIndex) {
			secondIndex = (int) (Math.random() * numberOfPoints);
		}
		Checkpoint[] proposition = new Checkpoint[numberOfPoints];
		if (firstIndex < secondIndex) {
			for (int i = 0; i < firstIndex; i++)
				proposition[i] = currentSolution[i];
			for (int i = firstIndex; i < secondIndex; i++)
				proposition[i] = currentSolution[secondIndex - i + firstIndex - 1];
			for (int i = secondIndex; i < numberOfPoints; i++)
				proposition[i] = currentSolution[i];
			return proposition;
		} else {
			for (int i = secondIndex; i < firstIndex; i++)
				proposition[i] = currentSolution[i];
			for (int i = firstIndex; i < numberOfPoints + secondIndex; i++)
				if (i < numberOfPoints & secondIndex - i + firstIndex - 1 >= 0)
					proposition[i] = currentSolution[secondIndex - i + firstIndex - 1];
				else if (i < numberOfPoints)
					proposition[i] = currentSolution[secondIndex - i + firstIndex - 1 + numberOfPoints];
				else if (secondIndex - i + firstIndex - 1 >= 0)
					proposition[i - numberOfPoints] = currentSolution[secondIndex - i + firstIndex - 1];
				else
					proposition[i - numberOfPoints] = currentSolution[secondIndex - i + firstIndex - 1
							+ numberOfPoints];
			return proposition;
		}
	}

	/**
	 * Fonction auxiliaire pour copier un array. La fonction ne modifie pas son
	 * argument
	 * 
	 * @param points : L'array à copier
	 * @return L'array copié
	 */
	private Checkpoint[] copy(Checkpoint[] points) {
		Checkpoint[] result = new Checkpoint[points.length];
		Arrays.setAll(result, i -> points[i]);
		return result;
	}

	/**
	 * Enumération des types de décroissance possible de la température
	 */
	public static enum DecroissanceType {
		/**
		 * Décroissance logarithmique
		 */
		LOG,

		/**
		 * Décroissance linéaire
		 */
		N,

		/**
		 * Décroissance quadratique
		 */
		N2
	}
}