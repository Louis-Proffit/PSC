import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Comparator;

public class Genetique implements Comparator<ArrayList<Integer>> {

	private final int pop; // la taille des populations de solutions utilis�es
	private final int etapes; // le nombre d'�tapes d'�volution effectu�
	private final int nombreElite; // "l'�lite" de la population qu'on va conserver telle quelle � la g�n�ration
									// suivante

	public Genetique(int p, int e, int elite) {
		pop = p;
		etapes = e;
		nombreElite = elite;
	}

	// Les points par lesquels il faut passer
	LinkedList<? extends Vector> points;
	int numberOfCheckpoints = points.size();

	// La matrice des co�ts r�els de ces chemins
	ArrayList<ArrayList<Double>> costMatrix = new ArrayList<ArrayList<Double>>();

	@Override
	public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
		return (int) (this.score(o1) - this.score(o2));
	}

	// Le 'score' d'une solution au probl�me, c'est � dire la somme des co�ts des
	// chemins qui composent la solution
	public double score(ArrayList<Integer> solution) {
		double s = 0;
		for (int i = 0; i < numberOfCheckpoints - 1; i++) {
			s += this.costMatrix.get(solution.get(i)).get(solution.get(i + 1));
		}
		s += this.costMatrix.get(solution.get(numberOfCheckpoints - 1)).get(solution.get(0));
		return (s);
	}

	// G�n�ration al�atoire d'une solution
	public ArrayList<Integer> createRoute() {
		ArrayList<Integer> route = new ArrayList<Integer>();
		for (int i = 0; i < numberOfCheckpoints; i++) {
			route.add(i);
		}
		for (int i = numberOfCheckpoints - 1; i > 0; i--) {
			int j = (int) (i * Math.random());
			Integer a = route.get(i);
			route.set(i, route.get(j));
			route.set(j, a);
		}
		return (route);
	}

	// Algorithme de croisement de deux solutions pour faire �voluer la population
	@SuppressWarnings("unchecked")
	public ArrayList<Integer> croiser(ArrayList<Integer> route1, ArrayList<Integer> route2) {
		ArrayList<Integer> new_route = (ArrayList<Integer>) route1.clone();
		int split = (int) (numberOfCheckpoints * Math.random());
		ArrayList<Integer> partToChange = new ArrayList<Integer>();
		for (int i = 0; i < numberOfCheckpoints; i++) {
			if (route1.indexOf(route2.get(i)) >= split) {
				partToChange.add(i);
			}
		}
		for (int k = 0; k < partToChange.size(); k++) {
			new_route.set(split + k, route2.get(partToChange.get(k)));
		}
		return (new_route);
	}

	public ArrayList<Integer> genetiqueSimplifie(LinkedList<Vector> points) {
		numberOfCheckpoints = points.size();

		// Initialisation des matrices de co�ts
		for (int i = 0; i < numberOfCheckpoints; i++) {
			costMatrix.add(new ArrayList<Double>());
			for (int j = 0; j < numberOfCheckpoints; j++) {
				costMatrix.get(i).add(points.get(i).distance(points.get(j)));
			}
		}

		// Cr�ation de la population initiale de solutions
		PriorityQueue<ArrayList<Integer>> population = new PriorityQueue<ArrayList<Integer>>(1,
				(o1, o2) -> Double.compare(score(o1), score(o2)));
		for (int i = 0; i < pop; i++) {
			population.add(createRoute());
		}

		// La liste dans laquelle on mettra notre
		PriorityQueue<ArrayList<Integer>> next_gen = new PriorityQueue<ArrayList<Integer>>(1,
				(o1, o2) -> Double.compare(score(o1), score(o2)));

		ArrayList<ArrayList<Integer>> mating_pool = new ArrayList<ArrayList<Integer>>();

		for (int e = 0; e < etapes; e++) {
			for (int j = 0; j < nombreElite; j++) {
				ArrayList<Integer> solution = population.poll();
				next_gen.add(solution);
				mating_pool.add(solution);
			}

			while (!(population.isEmpty())) {
				mating_pool.add(population.poll());
			}

			for (int j = 0; j < mating_pool.size() / 2; j++) {
				ArrayList<Integer> solution = this.croiser(mating_pool.get(j),
						mating_pool.get(mating_pool.size() - 1 - j));
				next_gen.add(solution);
			}

			population.clear();
			for (ArrayList<Integer> element : next_gen) {
				population.add(element);
			}

			next_gen.clear();
			mating_pool.clear();
		}

		ArrayList<Integer> optimum = population.peek();
		return (optimum);
	}
}
