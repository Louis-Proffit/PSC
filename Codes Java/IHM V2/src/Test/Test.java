package Test;

import java.util.LinkedList;

import Astar.AstarTile;
import Global.Checkpoint;
import Global.Grid;
import Global.Obstacle;
import Global.Path;
import Global.Tile;
import javafx.application.Application;
import javafx.stage.Stage;   
import javafx.scene.*;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class Test extends Application{
	
	public static int n = 100;
	public static int m = 100;
	
	public static Grid grid;
	
	public static void addCheckpoint(int x, int y) {
		// Ajoute un checkpoint à la simulation 
		Checkpoint tile = new Checkpoint(x, y);
		grid.checkpoints.add(tile);
	}
	
	public static void addObstacle(int x, int y) {
		// Ajoute un obstacle à la simulation
		Obstacle obstacle = new Obstacle(x, y);
		grid.obstacles.add(obstacle);
	}
	
	@Override
	public void start(Stage stage) {
		
	    	NumberAxis xAxis= new NumberAxis(0, 1000000, 1000);
	    	NumberAxis yAxis= new NumberAxis(1, 3, 0.05);

		    xAxis.setLabel("Temps de calcul");
		    yAxis.setLabel("Longueur du chemin (% de l'optimal)");

		    ScatterChart<Number, Number> graphe = new ScatterChart<>(xAxis, yAxis);
		    graphe.setTitle("Rapport entre performance et temps de calcul");
		    
		    ScatterChart.Series data = new ScatterChart.Series();
		    data.setName("data");
		    
		    
		    grid = new Grid(n, m);
		    
		    //On choisit 10 checkpoints au hasard
		    
		    int i = 0;
		    while (i < 10) {
		    	int x = (int) (Math.random() * 100);
		    	int y = (int) (Math.random() * 100);
		    	if (!grid.isCheckpoint(x, y)) {
		    		addCheckpoint(x,y);
		    		i++;
		    	}
		    }
		    
		    //On construit les obstacles avec une chaîne de Markov : a chaque etape on a 1 chance sur 10 d'aller 
		    //a une case au hasard et de la transformer en obstacle, et 9 chances sur 10 de transformer en obstacle
		    //un des voisins qui n'est pas encore un obstacle
		    //On répète cette opération 500 fois (de cette manière 5% de la surface est composée d'obstacles)
		    
		    int a = (int) (Math.random() * 100);
	    	int b = (int) (Math.random() * 100);
	    	
		    for (int j = 0; j < 500; j++) {
		    	if (Math.random() < 0.1) {
		    		a = (int) (Math.random() * 100);
			    	b = (int) (Math.random() * 100);
			    	addObstacle(a,b);
		    	}
		    	else {
		    		LinkedList<Tile> voisins = new LinkedList<Tile>();
		    		for (int x = -1 ; x < 2; x++) {
						for (int y = -1 ; y < 2 ; y++) {
							int new_x = a + x;
							int new_y = b + y;
							if (new_x > -1 & new_x < n & new_y > -1 & new_y < m) {
								if (!grid.isObstacle(x, y) && !grid.isCheckpoint(x, y)) {
									Tile new_case = new Tile(new_x, new_y);
									voisins.add(new_case);
								}
							}
						}
					}
		    		if (voisins.isEmpty()) {
		    			a = (int) Math.random() * 100;
				    	b = (int) Math.random() * 100;
				    	addObstacle(a,b);
		    		}
		    		else {
			    		int c = (int) Math.random() * voisins.size();
			    		addObstacle(voisins.get(c).x, voisins.get(c).y);
		    		}
		    	}
		    }
		    
		    Genetique genOpt = new Genetique(20, 50, 5);
		    Path optimum = new Path(genOpt.geneticPathFinder());
		    double tailleOpt = optimum.length();
		    
		    for (int p = 5; p<6; p = p+10 ) {
		    	for (int e = 1; e<2; e++) {
		    		
				    long startTime = System.currentTimeMillis();
				    Genetique gen = new Genetique(p, e, 5);
				    Path chemin = new Path(gen.geneticPathFinder());
				    long endTime = System.currentTimeMillis();
				    long totalTime = endTime - startTime;
				    System.out.println(totalTime);

				    data.getData().add(new ScatterChart.Data(chemin.length()/optimum.length(), totalTime));
				    
		    	}
		    }
		    
		    data.getData().add(new ScatterChart.Data(10000, 0.8));

		    graphe.getData().add(data);

		    Scene view = new Scene(graphe, 640, 480);
		    stage.setScene(view);
		    stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
