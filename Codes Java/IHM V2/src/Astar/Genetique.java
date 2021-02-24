package Astar;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Comparator;

import Global.PathFinder;
import Global.Tile;

public class Genetique implements Comparator<ArrayList<Integer>> {
	
	int pop; //la taille des populations de solutions utilisées
	int etapes; //le nombre d'étapes d'évolution effectué
	int nombreElite; //"l'élite" de la population qu'on va conserver telle quelle à la génération suivante
	
	public Genetique(int p, int e, int elite) {
		pop = p;
		etapes = e;
		nombreElite = elite;
	}
	
	//Les points par lesquels il faut passer
	LinkedList<? extends Tile> points = PathFinder.grid.checkpoints;
	int numberOfCheckpoints = points.size();
	
	//La matrice des chemins entre points
	ArrayList<ArrayList<LinkedList<AstarTile>>> pathMatrix = new ArrayList<ArrayList<LinkedList<AstarTile>>>();
	
	//La matrice des coûts réels de ces chemins 
	ArrayList<ArrayList<Double>> costMatrix = new ArrayList<ArrayList<Double>>();

	//Le coût d'un chemin entre deux points
	public double cost(LinkedList<AstarTile> path) {
		double c = 0;
		for(int i=0; i<path.size()-1; i++) {
			c += path.get(i).distanceToCase(path.get(i+1));
		}
		return c;
	}
	
	
	@Override
	public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
		return (int) (this.score(o1) - this.score(o2));
	}
	
	//Le 'score' d'une solution au problème, c'est à dire la somme des coûts des chemins qui composent la solution
	public double score(ArrayList<Integer> solution) {
		double s = 0;
		for(int i=0; i < numberOfCheckpoints - 1; i++) {
			s += this.costMatrix.get(solution.get(i)).get(solution.get(i+1));
		}
		s += this.costMatrix.get(solution.get(numberOfCheckpoints - 1)).get(solution.get(0));
		return(s);
	}
	
	//Génération aléatoire d'une solution
	public ArrayList<Integer> createRoute() {
		ArrayList<Integer> route = new ArrayList<Integer>();
		for (int i=0; i<numberOfCheckpoints; i++) {
			route.add(i);
		}
		for (int i=numberOfCheckpoints - 1; i>0; i--) {
			int j = (int) (i*Math.random());
			Integer a = route.get(i);
			route.set(i, route.get(j));
			route.set(j, a);
		}
		return(route);
	}
	
	//Algorithme de croisement de deux solutions pour faire évoluer la population
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
		return(new_route);
	}
	
	//L'algorithme principal
	public LinkedList<Tile> geneticPathFinder(){
		
		//Initialisation des matrices de chemins et de coûts
		for (int i = 0 ; i < numberOfCheckpoints; i++) {
			costMatrix.add(new ArrayList<Double>());
			pathMatrix.add(new ArrayList<LinkedList<AstarTile>>());
			for (int j = 0 ; j < numberOfCheckpoints; j++) {
				
				LinkedList<AstarTile> chemin = AstarBis.aStar(PathFinder.grid, new AstarTile(points.get(i)), new AstarTile(points.get(j)));
				pathMatrix.get(i).add(chemin);
				costMatrix.get(i).add(this.cost(chemin));
			}
		}
		
		
		//Création de la population initiale de solutions
		PriorityQueue<ArrayList<Integer>> population = new PriorityQueue<ArrayList<Integer>>(1, (o1,o2) -> Double.compare(score(o1), score(o2)));
		for (int i = 0; i < pop; i++) {
			population.add(createRoute());
		}
		
		//La liste dans laquelle on mettra notre 
		PriorityQueue<ArrayList<Integer>> next_gen = new PriorityQueue<ArrayList<Integer>>(1, (o1,o2) -> Double.compare(score(o1), score(o2)));
		
		ArrayList<ArrayList<Integer>> mating_pool = new ArrayList<ArrayList<Integer>>();
		
		for (int e = 0; e < etapes; e++) {
			for (int j = 0; j < nombreElite ; j++) {
				ArrayList<Integer> solution = population.poll();				
				next_gen.add(solution);
				mating_pool.add(solution);
			}
			
			while ( !(population.isEmpty())) {
				mating_pool.add(population.poll());
			}
			
			for (int j = 0; j < mating_pool.size() / 2; j++) {
				ArrayList<Integer> solution = this.croiser(mating_pool.get(j), mating_pool.get(mating_pool.size() - 1 - j));
				next_gen.add(solution);				
			}
			
			population.clear();
			for (ArrayList<Integer > element : next_gen) {
				population.add(element);
			}
			next_gen.clear();
			mating_pool.clear();
		}
		
		ArrayList<Integer> optimum = population.peek();
		
		LinkedList<Tile> chemin = new LinkedList<Tile>();
		for (int i = 0; i < numberOfCheckpoints - 1; i++) {
			for(AstarTile e : pathMatrix.get(optimum.get(i)).get(optimum.get(i+1))) {
				chemin.addLast(e);
			}
		}
		for(AstarTile e : pathMatrix.get(optimum.get(numberOfCheckpoints - 1)).get(optimum.get(0))) {
			chemin.addLast(e);
		}
		
		return(chemin);	
	}
}


