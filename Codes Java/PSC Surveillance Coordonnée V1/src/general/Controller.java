package general;
import java.util.LinkedList;

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
	public static double droneSpeed = 10;
	

	public static void init() {
		map = new Map(width, height);
		drones = new LinkedList<Drone>();
		
		// Ajout de trois checkpoints
		map.addCheckpoint(new Checkpoint(Math.random() * width, Math.random() * height));
		map.addCheckpoint(new Checkpoint(Math.random() * width, Math.random() * height));
		map.addCheckpoint(new Checkpoint(Math.random() * width, Math.random() * height));
		map.addCheckpoint(new Checkpoint(Math.random() * width, Math.random() * height));
		map.addCheckpoint(new Checkpoint(Math.random() * width, Math.random() * height));
		map.addCheckpoint(new Checkpoint(Math.random() * width, Math.random() * height));
		map.addCheckpoint(new Checkpoint(Math.random() * width, Math.random() * height));
		map.addCheckpoint(new Checkpoint(Math.random() * width, Math.random() * height));
		map.addCheckpoint(new Checkpoint(Math.random() * width, Math.random() * height));
		map.addCheckpoint(new Checkpoint(Math.random() * width, Math.random() * height));
		map.addCheckpoint(new Checkpoint(Math.random() * width, Math.random() * height));
		map.addCheckpoint(new Checkpoint(Math.random() * width, Math.random() * height));
		/*map.addCheckpoint(new Checkpoint(100, 100));
		map.addCheckpoint(new Checkpoint(200, 800));
		map.addCheckpoint(new Checkpoint(200, 700));
		map.addCheckpoint(new Checkpoint(300, 720));
		map.addCheckpoint(new Checkpoint(800, 400));
		map.addCheckpoint(new Checkpoint(700, 500));
		map.addCheckpoint(new Checkpoint(730, 580));
		map.addCheckpoint(new Checkpoint(400, 20));
		map.addCheckpoint(new Checkpoint(500, 200));
		map.addCheckpoint(new Checkpoint(430, 110));
		map.addCheckpoint(new Checkpoint(900, 900));
		map.addCheckpoint(new Checkpoint(950, 830));*/
		
		// Ajout de trois drones
		drones.add(new Drone(Math.random() * width, Math.random() * height));
		drones.add(new Drone(Math.random() * width, Math.random() * height));
		drones.add(new Drone(Math.random() * width, Math.random() * height));
		drones.add(new Drone(Math.random() * width, Math.random() * height));
		drones.add(new Drone(Math.random() * width, Math.random() * height));
				
		// 
		availableCheckpoints = new boolean[drones.size()][map.getCheckpoints().size()];
		for (int i = 0 ; i < drones.size() ; i++) {
			for (int j = 0 ; j < map.getCheckpoints().size() ; j++) {
				availableCheckpoints[i][j] = true;
			}
		}
		
		droneTargets = new int[drones.size()];
		for (int i = 0 ; i < drones.size() ; i++) droneTargets[i] = -1; // Aucun drone n'a de cible
		
		for (int i = 0 ; i < drones.size() ; i++) makeDecision(i); // Intialisation de la décision pour le drone
		
		graphics = new LocalGraphics(width, height);
		graphics.draw(drones, map);
	}
	
	public static void makeDecision(int droneIndex) {
		// Fait prendre au drone une décision
		int indexMaxInterest = -1;
		double maxInterest = 0;
		double interest;
		for (int j = 0 ; j < map.getCheckpoints().size() ; j++) {
			if (availableCheckpoints[droneIndex][j]) {
				interest = map.getCheckpoints().get(j).getSatisfaction() / (drones.get(droneIndex).getPosition().distance(map.getCheckpoints().get(j).getPosition()) + 1);
				if (interest > maxInterest) {
					indexMaxInterest = j;
					maxInterest = interest;
				}
			}
		}
		for (int i = 0 ; i < drones.size() ; i++) {
			if (droneTargets[i] == indexMaxInterest) {
				if (drones.get(droneIndex).getPosition().distance(map.getCheckpoints().get(indexMaxInterest).getPosition()) > drones.get(i).getPosition().distance(map.getCheckpoints().get(indexMaxInterest).getPosition())){
					availableCheckpoints[droneIndex][indexMaxInterest] = false;
					makeDecision(droneIndex);
					return;
				}
				else {
					droneTargets[droneIndex] = indexMaxInterest;
					availableCheckpoints[i][indexMaxInterest] = false;
					makeDecision(i);
					return;
				}
			}
		}
		droneTargets[droneIndex] = indexMaxInterest;
		return;
	}
	
	public static void evolve() {
		// La fonction evolve augmente toutes les satisfactions des checkpoints, fait bouger les drones vers leur cible, et met à jour cette cible si ils l'atteignent
		// La spécificité de cette fonction est de fairte prendre une décision au drone uniquement quand il est en conflit ou quand il atteint un checkpoint
		for (Checkpoint checkpoint : map.getCheckpoints()) {
			checkpoint.evolveSatisfaction();
		}
		for (int i = 0 ; i < drones.size() ; i++) {
			int checkpointIndex = droneTargets[i];
			boolean isOnTarget = drones.get(i).moveToTarget(map.getCheckpoints().get(checkpointIndex));
			if (isOnTarget) {
				for (int j = 0 ; j < drones.size() ; j++) availableCheckpoints[j][checkpointIndex] = true;
				map.getCheckpoints().get(droneTargets[i]).setSatisfaction(1);
				makeDecision(i);
			}
		}
	}
	
	public static double getTotalSatisfaction() {
		double result = 0;
		for (Checkpoint checkpoint: map.getCheckpoints()) {
			result += checkpoint.getSatisfaction();
		}
		return result / map.getCheckpoints().size();
	}
	
	public static void main(String[] args) {
		init();
		for (int i = 0 ; i < 1000 ; i++) {
			graphics.draw(drones,  map);
			try {Thread.sleep(30);} catch (InterruptedException e) {}
			evolve();
			System.out.println(getTotalSatisfaction());
		}
	}
}
