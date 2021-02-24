import java.util.ArrayList;
import java.util.List;

public class Recuit implements TSPSolver {

	// Les param�tres de la fonction de temp�rature
	private final Decroissance decroissance;
	private final double initialValue;
	private final int finishingTime;

	/**
	 * Paramètres pour la fonction de température
	 * 
	 * @param finishingTime : temps d'execution
	 * @param decroissance  : type de décroissance
	 * @param initialValue  : valeur initiale de la température
	 */
	public Recuit(int finishingTime, Decroissance decroissance, double initialValue) {
		this.initialValue = initialValue;
		this.decroissance = decroissance;
		this.finishingTime = finishingTime;
	}

	// La matrice des co�ts r�els de ces chemins
	ArrayList<ArrayList<Double>> costMatrix = new ArrayList<ArrayList<Double>>();

	public double temperature(int i) {
		switch (decroissance) {
			case LOG:
				return initialValue / Math.log(i);
			case N:
				return initialValue / i;
			case N2:
				return initialValue / (i * i);
		}
		return 0;
	}

	// Le 'score' d'une solution au probl�me, c'est � dire la somme des co�ts des
	// chemins qui composent la solution
	public double score(ArrayList<Integer> solution) {
		double s = 0;
		int numberOfPoints = solution.size();
		for (int i = 0; i < numberOfPoints - 1; i++) {
			s += this.costMatrix.get(solution.get(i)).get(solution.get(i + 1));
		}
		s += this.costMatrix.get(solution.get(numberOfPoints - 1)).get(solution.get(0));
		return s;
	}

	public static double h(double x) {
		return Math.min(1, x);
	}

	public static ArrayList<Integer> proposition1(ArrayList<Integer> currentPath) {
		ArrayList<Integer> proposition = new ArrayList<>();
		for (Integer integer : currentPath)
			proposition.add(integer);
		int a = (int) (Math.random() * currentPath.size());
		int b = (int) (Math.random() * currentPath.size());
		while (b == a) {
			b = (int) (Math.random() * currentPath.size());
		}
		int temporaire = proposition.get(a);
		proposition.set(a, proposition.get(b));
		proposition.set(b, temporaire);
		return proposition;
	}

	public static ArrayList<Integer> proposition2(ArrayList<Integer> currentPath) {
		ArrayList<Integer> proposition = new ArrayList<>();
		int a = (int) (Math.random() * currentPath.size());
		int b = (int) (Math.random() * currentPath.size());
		while (b == a) {
			b = (int) (Math.random() * currentPath.size());
		}
		if (a > b) {
			int temporaire = a;
			a = b;
			b = temporaire;
		}
		for (int i = 0; i < a; i++)
			proposition.add(currentPath.get(i));
		for (int i = b - 1; i >= a; i--)
			proposition.add(currentPath.get(i));
		for (int i = b; i < currentPath.size(); i++)
			proposition.add(currentPath.get(i));
		int temporaire = proposition.get(a);
		proposition.set(a, proposition.get(b));
		proposition.set(b, temporaire);
		return proposition;
	}

	public List<Integer> getPath(List<? extends Vector> points) {
		int numberOfPoints = points.size();
		// Initialisation des matrices de chemins et de co�ts
		for (int i = 0; i < numberOfPoints; i++) {
			costMatrix.add(new ArrayList<Double>());
			for (int j = 0; j < numberOfPoints; j++) {
				costMatrix.get(i).add(points.get(i).distance(points.get(j)));
			}
		}

		// Le nombre d'�tapes effectuées
		int e = 0;

		// La temp�rature
		double t;

		// Le nombre d'�tapes sans changement
		int j = 0;

		// Les parcours qu'on va modifier en it�rant le recuit simul�
		ArrayList<Integer> solution = new ArrayList<Integer>();
		ArrayList<Integer> proposition;

		for (int i = 0; i < numberOfPoints; i++) {
			solution.add(i);
		}

		// Puis on effectue des �tapes de recuit simul� jusqu'� atteindre un �tat
		// stationnaire
		double solutionScore = score(solution);
		double propositionScore;
		while (j < finishingTime) {
			System.out.println(solutionScore);
			e++;
			t = temperature(e);
			proposition = proposition2(solution);
			propositionScore = score(proposition);

			if (propositionScore == solutionScore) {
				j++;
			} else if (h(Math.exp((score(solution) - score(proposition)) / t)) > Math.random()) {
				solution = proposition;
				solutionScore = propositionScore;
				j = 0;
			} else
				j++;
		}
		System.out.println("Path found");
		return solution;
	}
}