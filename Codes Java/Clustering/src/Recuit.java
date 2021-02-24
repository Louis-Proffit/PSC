import java.util.ArrayList;
import java.util.LinkedList;

public class Recuit {

	// Les param�tres de la fonction de temp�rature
	private final Decroissance decroissance;
	private final double valeur_initiale;

	// Le nombre d'�tapes sans changement qui provoquent l'arret du recuit simul�
	public static int temps_arret;

	public Recuit(int e, Decroissance d, double init) {
		temps_arret = e;
		valeur_initiale = init;
		decroissance = d;
	}

	// La matrice des co�ts r�els de ces chemins
	ArrayList<ArrayList<Double>> costMatrix = new ArrayList<ArrayList<Double>>();

	public double temperature(int i) {
		double result = 0;
		switch (decroissance) {
			case LOG:
				result = valeur_initiale / Math.log(i);
			case N:
				result = valeur_initiale / i;
			case N2:
				result = valeur_initiale / (i * i);
		}
		return (result);
	}

	// Le 'score' d'une solution au probl�me, c'est � dire la somme des co�ts des
	// chemins qui composent la solution
	public double score(ArrayList<Integer> solution) {
		double s = 0;
		for (int i = 0; i < n - 1; i++) {
			s += this.costMatrix.get(solution.get(i)).get(solution.get(i + 1));
		}
		s += this.costMatrix.get(solution.get(n - 1)).get(solution.get(0));
		return (s);
	}

	public double cost(LinkedList<AstarTile> path) {
		double c = 0;
		for (int i = 0; i < path.size() - 1; i++) {
			c += path.get(i).distanceToCase(path.get(i + 1));
		}
		return c;
	}

	private static ArrayList<Integer> copy(ArrayList<Integer> a) {
		ArrayList<Integer> out = new ArrayList<Integer>();
		for (int i = 0; i < a.size(); i++) {
			out.add(a.get(i));
		}
		return (out);
	}

	public ArrayList<Integer> recuitSimplifie(LinkedList<Vector> points) {
		int numberOfPoints = points.size();
		// Initialisation des matrices de chemins et de co�ts
		for (int i = 0; i < numberOfPoints; i++) {
			costMatrix.add(new ArrayList<Double>());
			for (int j = 0; j < numberOfPoints; j++) {
				costMatrix.get(i).add(points.get(i).distance(points.get(j)));
			}
		}

		// Le nombre d'�tapes
		int e = 0;

		// La temp�rature
		double t = 0;

		// Le nombre d'�tapes sans changement
		int j = 0;

		// Les parcours qu'on va modifier en it�rant le recuit simul�
		ArrayList<Integer> solution1 = new ArrayList<Integer>();
		ArrayList<Integer> solution2 = new ArrayList<Integer>();
		for (int i = 0; i < numberOfPoints; i++) {
			solution1.add(i);
			solution2.add(i);
		}

		// Des entiers correspondant aux positions qu'on va tirer au hasard dans l'array
		int a = 0;
		int b = 0;

		// Une variable temporaire
		int temporaire = 0;

		// Puis on effectue des �tapes de recuit simul� jusqu'� atteindre un �tat
		// relativement stationnaire
		while (j < temps_arret) {
			e++;
			t = temperature(e);

			a = (int) (Math.random() * numberOfPoints);
			while (b == a) {
				b = (int) (Math.random() * numberOfPoints);
			}
			temporaire = solution2.get(a);
			solution2.set(a, solution2.get(b));
			solution2.set(b, temporaire);

			if (score(solution2) <= score(solution1)) {
				solution1 = copy(solution2);
				j = 0;
			} else {
				if (Math.exp(-(score(solution2) - score(solution1)) / t) > Math.random()) {
					solution1 = copy(solution2);
					j = 0;
				} else {
					solution2 = copy(solution1);
				}
			}
		}
		return (solution1);
	}

}