package Astar;

public class Recuit {
	
	//Les param�tres de la fonction de temp�rature
	public static Decroissance decroissance;
	public static double valeur_initiale;
	
	//Le nombre d'�tapes
	public static int etapes;
	
	Recuit(int e, Decroissance d, double init){
		etapes = e;
		valeur_initiale = init;
		decroissance = d;
	}
	
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
	

}