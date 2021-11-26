package general;

import java.util.LinkedList;
import java.util.Random;

import graphics.LocalGraphics;

public class Controller {

	public static int width = 1000; // Largeur en pixels
	public static int height = 1000; // Hauteur en pixels
	public static LocalGraphics graphics;
	public static int numberOfSteps = 100;

	public static Map map;
	public static LinkedList<Drone> drones;
	public static boolean[][] availableCheckpoints;
	public static int[] droneTargets;
	public static double droneSpeed = 5;
	public static double p = 0.9; // Probabilit� de faire le choix optimal

	public static int numberOfDrones = 4;
	public static int numberOfCheckpoints = 1000;

	public static void init() {
		map = new Map(width, height);
		drones = new LinkedList<Drone>();

		// Ajout de trois checkpoints

		for (int i = 0; i < 50; i++) {
			map.addCheckpoint(new Checkpoint(Math.random() * width * 0.3, Math.random() * height * 0.3));
		}
		for (int i = 0; i < 50; i++) {
			map.addCheckpoint(new Checkpoint(Math.random() * width * 0.3 + width * 0.7, Math.random() * height * 0.3));
		}
		for (int i = 0; i < 50; i++) {
			map.addCheckpoint(new Checkpoint(Math.random() * width * 0.3, Math.random() * height * 0.3 + height * 0.7));
		}
		for (int i = 0; i < 50; i++) {
			map.addCheckpoint(new Checkpoint(Math.random() * width * 0.3 + width * 0.7,
					Math.random() * height * 0.3 + height * 0.7));
		}
		/*
		 * map.addCheckpoint(new Checkpoint(100, 100)); map.addCheckpoint(new
		 * Checkpoint(200, 800)); map.addCheckpoint(new Checkpoint(200, 700));
		 * map.addCheckpoint(new Checkpoint(300, 720)); map.addCheckpoint(new
		 * Checkpoint(800, 400)); map.addCheckpoint(new Checkpoint(700, 500));
		 * map.addCheckpoint(new Checkpoint(730, 580)); map.addCheckpoint(new
		 * Checkpoint(400, 20)); map.addCheckpoint(new Checkpoint(500, 200));
		 * map.addCheckpoint(new Checkpoint(430, 110)); map.addCheckpoint(new
		 * Checkpoint(900, 900)); map.addCheckpoint(new Checkpoint(950, 830));
		 */
		// for (int i = 0; i < numberOfCheckpoints; i++)
		// map.addCheckpoint(new Checkpoint(width * Math.random(), height *
		// Math.random()));

		for (int i = 0; i < numberOfDrones; i++) {
			drones.add(new Drone(Math.random() * width, Math.random() * height));
		}

		availableCheckpoints = new boolean[drones.size()][map.getCheckpoints().size()];
		for (int i = 0; i < drones.size(); i++) {
			for (int j = 0; j < map.getCheckpoints().size(); j++) {
				availableCheckpoints[i][j] = true;
			}
		}

		droneTargets = new int[drones.size()];
		for (int i = 0; i < drones.size(); i++)
			droneTargets[i] = -1; // Aucun drone n'a de cible

		for (int i = 0; i < drones.size(); i++)
			makeDecision(i); // Intialisation de la d�cision pour le drone

		graphics = new LocalGraphics(width, height);
		graphics.draw(drones, map);
	}

	public static void makeDecision(int droneIndex) {
		// Fait prendre au drone une d�cision
		// Le drone � une probabilit� p de choisir le checkpoint le plus int�ressant, et
		// 1-p d'en choisir un au hasard
		int indexMaxInterest = -1;
		double maxInterest = 0;
		double interest;
		for (int j = 0; j < map.getCheckpoints().size(); j++) { // On r�cup�re le checkpoint le plus int�ressant
			if (availableCheckpoints[droneIndex][j]) {
				interest = map.getCheckpoints().get(j).getSatisfaction()
						/ (drones.get(droneIndex).getPosition().distance(map.getCheckpoints().get(j).getPosition())
								+ 1);
				if (interest > maxInterest) {
					indexMaxInterest = j;
					maxInterest = interest;
				}
			}
		}
		Checkpoint checkpointMaxInterest;
		double random = Math.random();
		if (random < p) { // On choisit le checkpoint le plus int�ressant
			checkpointMaxInterest = map.getCheckpoints().get(indexMaxInterest);
		} else { // On choisit un checkpoint au hasard
			int index = (int) (Math.random() * map.getCheckpoints().size());
			checkpointMaxInterest = map.getCheckpoints().get(index);
		}
		for (int i = 0; i < drones.size(); i++) {
			if (droneTargets[i] == indexMaxInterest & i != droneIndex) { // On est dans le cas d'un conflit
				if (drones.get(droneIndex).getPosition().distance(checkpointMaxInterest.getPosition()) > drones.get(i)
						.getPosition().distance(checkpointMaxInterest.getPosition())) {
					// Le conflit est perdu, on �limine ce checkpoint des checkpoints autoris�s et
					// on retente
					availableCheckpoints[droneIndex][indexMaxInterest] = false;
					makeDecision(droneIndex);
					return;
				} else { // Le conflit est gagn�, on fait plut�t choisir le drone d'indice i
					droneTargets[droneIndex] = indexMaxInterest;
					drones.get(droneIndex).setTarget(checkpointMaxInterest);
					availableCheckpoints[i][indexMaxInterest] = false;
					makeDecision(i);
					return;
				}
			}
		}
		drones.get(droneIndex).setTarget(checkpointMaxInterest);
		droneTargets[droneIndex] = indexMaxInterest;
		return;
	}

	public static void evolve() {
		// La fonction evolve augmente toutes les satisfactions des checkpoints, fait
		// bouger les drones vers leur cible, et met � jour cette cible si ils
		// l'atteignent
		// La sp�cificit� de cette fonction est de fairte prendre une d�cision au drone
		// uniquement quand il est en conflit ou quand il atteint un checkpoint
		for (Checkpoint checkpoint : map.getCheckpoints()) {
			checkpoint.evolveSatisfaction();
		}
		for (int i = 0; i < drones.size(); i++) {
			int checkpointIndex = droneTargets[i];
			boolean isOnTarget = drones.get(i).moveToTarget();
			if (isOnTarget) {
				for (int j = 0; j < drones.size(); j++)
					availableCheckpoints[j][checkpointIndex] = true;
				map.getCheckpoints().get(droneTargets[i]).setSatisfaction(1);
				makeDecision(i);
			}
		}
		for (int i = 0; i < drones.size(); i++) {
			makeDecision(i);
		}
	}

	public static double getTotalSatisfaction() {
		double result = 0;
		for (Checkpoint checkpoint : map.getCheckpoints()) {
			result += checkpoint.getSatisfaction();
		}
		return result / map.getCheckpoints().size();
	}

	public static void printTargets() {
		for (int i = 0; i < drones.size(); i++) {
			System.out.print(droneTargets[i] + "-");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		init();
		while (graphics.frame.isActive()) {
			graphics.draw(drones, map);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
			evolve();
		}
	}
}
