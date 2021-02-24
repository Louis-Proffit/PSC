package Astar;

import java.util.ArrayList;
import java.util.LinkedList;

import Global.PathFinder;
import Global.Tile;

public class Recuit {
	
	//Les paramètres de la fonction de température
	public static Decroissance decroissance;
	public static double valeur_initiale;
	
	//Le nombre d'étapes sans changement qui provoquent l'arret du recuit simulé
	public static int temps_arret;
	
	Recuit(int e, Decroissance d, double init){
		temps_arret = e;
		valeur_initiale = init;
		decroissance = d;
	}
	
	//Les points par lesquels il faut passer
		LinkedList<? extends Tile> points = PathFinder.grid.checkpoints;
		int n = points.size();
		
		//La matrice des chemins entre points
		ArrayList<ArrayList<LinkedList<AstarTile>>> pathMatrix = new ArrayList<ArrayList<LinkedList<AstarTile>>>();
		
		//La matrice des coûts réels de ces chemins 
		ArrayList<ArrayList<Double>> costMatrix = new ArrayList<ArrayList<Double>>();
	
	public static double temperature(int i) {
		double result = 0;
		switch (decroissance) {
		case LOG:
			result = valeur_initiale/Math.log(i);
		case N:
			result = valeur_initiale/i;
		case N2:
			result = valeur_initiale/(i*i);
		}
		return(result);
	}
	
	//Le 'score' d'une solution au problème, c'est à dire la somme des coûts des chemins qui composent la solution
		public double score(ArrayList<Integer> solution) {
			double s = 0;
			for(int i=0; i < n - 1; i++) {
				s += this.costMatrix.get(solution.get(i)).get(solution.get(i+1));
			}
			s += this.costMatrix.get(solution.get(n - 1)).get(solution.get(0));
			return(s);
		}
		
		public double cost(LinkedList<AstarTile> path) {
			double c = 0;
			for(int i=0; i<path.size()-1; i++) {
				c += path.get(i).distanceToCase(path.get(i+1));
			}
			return c;
		}
	
	public ArrayList<Integer> recuitPathFinder(){
		//Initialisation des matrices de chemins et de coûts
				for (int i = 0 ; i < n; i++) {
					costMatrix.add(new ArrayList<Double>());
					pathMatrix.add(new ArrayList<LinkedList<AstarTile>>());
					for (int j = 0 ; j < n; j++) {
						
						LinkedList<AstarTile> chemin = AstarBis.aStar(PathFinder.grid, new AstarTile(points.get(i)), new AstarTile(points.get(j)));
						pathMatrix.get(i).add(chemin);
						costMatrix.get(i).add(this.cost(chemin));
					}
				}
				
			//Le nombre d'étapes
			int e = 0;
			
			//La température
			double t  = 0;
			
			//Le nombre d'étapes sans changement
			int j = 0;
			
			//Les parcours qu'on va modifier en itérant le recuit simulé
			ArrayList<Integer> solution1 = new ArrayList<Integer>();
			ArrayList<Integer> solution2 = new ArrayList<Integer>();
			for (int i = 0; i < n; i++) {
				solution1.add(i);		
				solution2.add(i);
			}
			
			//Des entiers correspondant aux positions qu'on va tirer au hasard dans l'array
			int a = 0;
			int b = 0;
			
			//Une variable temporaire
			int temporaire = 0;
		
			//Puis on effectue des étapes de recuit simulé jusqu'à atteindre un état relativement stationnaire
				while (j < temps_arret) {
					e++;
					t = temperature(e);
					
					a = (int) (Math.random()*n);
					while (b ==a) {
						b = (int) (Math.random()*n);
					}
					temporaire = solution2.get(a);
					solution2.set(a, solution2.get(b));
					solution2.set(b, temporaire);
					
					if (score(solution2) <= score(solution1)) {
						solution1 = copy(solution2);
						j = 0;
					}
					else {
						if (Math.exp( - (score(solution2) - score(solution1)) / t) > Math.random()) {
							solution1 = copy(solution2);
							j = 0;
						}
						else {
							solution2 = copy(solution1);
						}
					}
				}
				return(solution1);
	}

	private static ArrayList<Integer> copy(ArrayList<Integer> a) {
		ArrayList<Integer> out = new ArrayList<Integer>();
		for (int i = 0; i < a.size(); i++) {
			out.add(a.get(i));
		}
		return(out);
	}
	
	public ArrayList<Integer> recuitSimplifie(LinkedList<Tile> checkpoints){
		points = checkpoints;
		n = points.size();
		//Initialisation des matrices de chemins et de coûts
				for (int i = 0 ; i < n; i++) {
					costMatrix.add(new ArrayList<Double>());
					for (int j = 0 ; j < n; j++) {
						costMatrix.get(i).add(points.get(i).distanceToCase(points.get(j)));
					}
				}
				
			//Le nombre d'étapes
			int e = 0;
			
			//La température
			double t  = 0;
			
			//Le nombre d'étapes sans changement
			int j = 0;
			
			//Les parcours qu'on va modifier en itérant le recuit simulé
			ArrayList<Integer> solution1 = new ArrayList<Integer>();
			ArrayList<Integer> solution2 = new ArrayList<Integer>();
			for (int i = 0; i < n; i++) {
				solution1.add(i);		
				solution2.add(i);
			}
			
			//Des entiers correspondant aux positions qu'on va tirer au hasard dans l'array
			int a = 0;
			int b = 0;
			
			//Une variable temporaire
			int temporaire = 0;
		
			//Puis on effectue des étapes de recuit simulé jusqu'à atteindre un état relativement stationnaire
				while (j < temps_arret) {
					e++;
					t = temperature(e);
					
					a = (int) (Math.random()*n);
					while (b ==a) {
						b = (int) (Math.random()*n);
					}
					temporaire = solution2.get(a);
					solution2.set(a, solution2.get(b));
					solution2.set(b, temporaire);
					
					if (score(solution2) <= score(solution1)) {
						solution1 = copy(solution2);
						j = 0;
					}
					else {
						if (Math.exp( - (score(solution2) - score(solution1)) / t) > Math.random()) {
							solution1 = copy(solution2);
							j = 0;
						}
						else {
							solution2 = copy(solution1);
						}
					}
				}
				return(solution1);
	}

}