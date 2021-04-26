package algorithms;

import structure.Modification;

/**
 * Classe implémentant une méthode de recuit simulé.
 * 
 * @LouisProffitX
 * @author Louis Proffit
 * @version 1.0
 */
public class TSPRecuit {

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
	public static int numberOfSteps = 100;

	/**
	 * Nombre d'étapes de simulation sans changement
	 */
	public static int numberOfStepsWithoutChange = 10;

	/**
	 * Méthode statique d'amélioration du chemin
	 * 
	 * @param toImprove : la structure à améliorer
	 */
	public static void improvePath(RecuitInterface toImprove) {
		int size = toImprove.getSize();
		if (size <= 3)
			return;
		int currentSteps = 0;
		int currentStepsWithoutChange = 0;
		double currentTemperature;
		double improvement;
		Modification modification;
		numberOfSteps = 1000 * size;
		numberOfStepsWithoutChange = 5 * size;

		while (currentSteps < numberOfSteps & currentStepsWithoutChange < numberOfStepsWithoutChange) {
			currentTemperature = temperature(currentSteps);
			modification = toImprove.modificationFunction();
			improvement = toImprove.improvementFunction(modification);
			if (h(Math.exp(-improvement / currentTemperature)) > Math.random()) {
				toImprove.commitFunction(modification);
				currentStepsWithoutChange = 0;
			} else
				currentStepsWithoutChange += 1;
			currentSteps++;
		}
	}

	/**
	 * Fonction de température, auxiliaire pour le recuit simulé
	 * 
	 * @param time : Le temps auquel on calcule la température
	 * @return La température au temps <b>time</b>
	 */
	private static double temperature(int time) {
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
	private static double h(double x) {
		return x / (1 + x);
	}

	/**
	 * Enumération des types de décroissance possible de la température
	 */
	public static enum DecroissanceType {
		/**
		 * Décroissance logarithmique
	s	 */
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