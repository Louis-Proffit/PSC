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
	int tailleSurvie = pop/5; //la partie de la population à partir de laquelle on va produire la génération suivante
	int tailleElite = tailleSurvie/4; //"l'élite" de la population qu'on va conserver telle quelle à la génération suivante
	
	public Genetique(int p, int e) {
		pop = p;
		etapes = e;
	}
	
	//Les points par lesquels il faut passer
	static LinkedList<Tile> points = PathFinder.grid.checkpoints;
	static int numberOfCheckpoints = points.size();
	
	//La matrice des chemins entre points, qu'on calcule au fur et à mesure de l'algorithme
	ArrayList<ArrayList<LinkedList<AstarTile>>> pathMatrix = new ArrayList<ArrayList<LinkedList<AstarTile>>>();
	
	//La matrice des coûts, estimés ou réels, de ces chemins 
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
		return (int) (score(o1) - score(o2));
	}
	
	//Le 'score' d'une solution au problème, c'est à dire la somme des coûts des chemins qui composent la solution
	public double score(ArrayList<Integer> solution) {
		double s = 0;
		for(int i=0; i < numberOfCheckpoints - 1; i++) {
			s += costMatrix.get(solution.get(i)).get(solution.get(i+1));
		}
		s += costMatrix.get(solution.get(numberOfCheckpoints - 1)).get(solution.get(0));
		return(s);
	}
	
	//Génération aléatoire d'une solution
	public static ArrayList<Integer> createRoute() {
		ArrayList<Integer> route = new ArrayList<Integer>(numberOfCheckpoints);
		for (int i=0; i<numberOfCheckpoints; i++) {
			route.set(i,i);
		}
		for (int i=numberOfCheckpoints; i>0; i--) {
			int j = (int) (i*Math.random());
			route.set(i, j);
			route.set(j, i);
		}
		return(route);
	}
	
	//Algorithme de croisement de deux solutions pour faire évoluer la population
	public ArrayList<Integer> croiser(ArrayList<Integer> route1, ArrayList<Integer> route2) {
		int split = (int) (numberOfCheckpoints * Math.random());
		int ajouts = 0;
		for (int i = 0; i < numberOfCheckpoints; i++) {
			if (route1.indexOf(route2.get(i)) >= split) {
				route1.set(split+ajouts, route2.get(i));
				ajouts++;
			}
		}
		return(route1);
	}
	
	//L'algorithme principal
	public LinkedList<Tile> geneticPathFinder(){
		
		//Initialisation des matrices de chemins et de coûts
		for (int i = 0 ; i < numberOfCheckpoints - 1; i++) {
			costMatrix.add(new ArrayList<Double>());
			pathMatrix.add(new ArrayList<LinkedList<AstarTile>>());
			for (int j = 0 ; j < numberOfCheckpoints - 1; j++) {
				//Pour l'instant aucun chemin n'a été calculé donc on initialise à null
				pathMatrix.get(i).add(null); 
				
				//On initialise les coûts avec la distance à vol d'oiseau entre les points, qui est une sous-estimation du coût réel du chemin.
				double cost = points.get(i).distanceToCase(points.get(j));
				costMatrix.get(i).add(cost);
			}
		}
		
		//Création de la population initiale de solutions
		PriorityQueue<ArrayList<Integer>> population = new PriorityQueue<ArrayList<Integer>>();
		for (int i = 0; i < pop; i++) {
			population.add(createRoute());
		}
		
		//La liste dans laquelle on mettra notre 
		PriorityQueue<ArrayList<Integer>> next_gen = new PriorityQueue<ArrayList<Integer>>();
		
		for (int e = 0; e < etapes; e++) {
			for (int j = 0; j < tailleElite; j++) {
				ArrayList<Integer> solution = population.poll();
				next_gen.add(solution);
				
				for (int i = 0; i < numberOfCheckpoints - 1; i++) {
					if (pathMatrix.get(solution.get(i)).get(solution.get(i+1)) == null) {
						LinkedList<AstarTile> chemin = Astar.aStar(PathFinder.grid, new AstarTile(points.get(solution.get(i))), new AstarTile(points.get(solution.get(i+1))));
						costMatrix.get(solution.get(i)).set(solution.get(i+1), this.cost(chemin));
						costMatrix.get(solution.get(i+1)).set(solution.get(i), this.cost(chemin));
						pathMatrix.get(solution.get(i)).set(solution.get(i+1), chemin);
						pathMatrix.get(solution.get(i+1)).set(solution.get(i), Astar.aStar(PathFinder.grid, new AstarTile(points.get(solution.get(i+1))), new AstarTile(points.get(solution.get(i)))));
					}
				}
				if (pathMatrix.get(solution.get(0)).get(solution.get(numberOfCheckpoints - 1)) == null) {
					LinkedList<AstarTile> chemin = Astar.aStar(PathFinder.grid, new AstarTile(points.get(solution.get(0))), new AstarTile(points.get(solution.get(numberOfCheckpoints - 1))));
					costMatrix.get(solution.get(0)).set(solution.get(numberOfCheckpoints - 1), this.cost(chemin));
					costMatrix.get(solution.get(numberOfCheckpoints - 1)).set(solution.get(0), this.cost(chemin));
					pathMatrix.get(solution.get(0)).set(solution.get(numberOfCheckpoints - 1), chemin);
					pathMatrix.get(solution.get(numberOfCheckpoints - 1)).set(solution.get(0), Astar.aStar(PathFinder.grid, new AstarTile(points.get(solution.get(numberOfCheckpoints - 1))), new AstarTile(points.get(solution.get(0)))));
				}
			}
			
			ArrayList<ArrayList<Integer>> mating_pool = new ArrayList<ArrayList<Integer>>();
			for (int i = 0; i < tailleSurvie - tailleElite; i++) {
				mating_pool.add(population.poll());
			}
			
			for (int j = 0; j < mating_pool.size() / 2; j++) {
				ArrayList<Integer> solution = this.croiser(mating_pool.get(j), mating_pool.get(mating_pool.size() - j));
				next_gen.add(solution);
				
				for (int i = 0; i < numberOfCheckpoints - 1; i++) {
					if (pathMatrix.get(solution.get(i)).get(solution.get(i+1)) == null) {
						LinkedList<AstarTile> chemin = Astar.aStar(PathFinder.grid, new AstarTile(points.get(solution.get(i))), new AstarTile(points.get(solution.get(i+1))));
						costMatrix.get(solution.get(i)).set(solution.get(i+1), this.cost(chemin));
						costMatrix.get(solution.get(i+1)).set(solution.get(i), this.cost(chemin));
						pathMatrix.get(solution.get(i)).set(solution.get(i+1), chemin);
						pathMatrix.get(solution.get(i+1)).set(solution.get(i), Astar.aStar(PathFinder.grid, new AstarTile(points.get(solution.get(i+1))), new AstarTile(points.get(solution.get(i)))));
					}
				}
				if (pathMatrix.get(solution.get(0)).get(solution.get(numberOfCheckpoints - 1)) == null) {
					LinkedList<AstarTile> chemin = Astar.aStar(PathFinder.grid, new AstarTile(points.get(solution.get(0))), new AstarTile(points.get(solution.get(numberOfCheckpoints - 1))));
					costMatrix.get(solution.get(0)).set(solution.get(numberOfCheckpoints - 1), this.cost(chemin));
					costMatrix.get(solution.get(numberOfCheckpoints - 1)).set(solution.get(0), this.cost(chemin));
					pathMatrix.get(solution.get(0)).set(solution.get(numberOfCheckpoints - 1), chemin);
					pathMatrix.get(solution.get(numberOfCheckpoints - 1)).set(solution.get(0), Astar.aStar(PathFinder.grid, new AstarTile(points.get(solution.get(numberOfCheckpoints - 1))), new AstarTile(points.get(solution.get(0)))));
				}
				
			}
			
			population = next_gen;

			next_gen.clear();
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


