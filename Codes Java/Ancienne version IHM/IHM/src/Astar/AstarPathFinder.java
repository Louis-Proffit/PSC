package Astar;

import java.util.LinkedList;

import Global.Grid;
import Global.Tile;

public class AstarPathFinder {
	
	// Trouve le chemin entre le checkpoint i et i+1. Si i le dernier indice de checkpoints, entre i et 0
		public static LinkedList<Tile> intermediatePathFinder(Tile startTile, Tile endTile, Grid grid) {
			AstarTile astarStartTile = new AstarTile(startTile);
			AstarTile astarEndTile = new AstarTile(endTile);
			LinkedList<AstarTile> astarResult = AstarBis.aStar(grid, astarStartTile, astarEndTile);
			LinkedList<Tile> result = new LinkedList<Tile>();
			if (astarResult == null) {
				grid.pathReady = false;
				System.out.println("Pas de chemin trouvé");
				return null;
			}
			for (AstarTile e : astarResult) {
				result.add((Tile)e);
			}
			grid.pathReady = true;
			return result;
		}
		
		// Trouve le chemin qui relie tous les checkpoints
		public static LinkedList<Tile> pathFinder(Grid grid) {
			
			int numberOfCheckpoints = grid.checkpoints.size();
			
			if (numberOfCheckpoints < 2) {
				grid.pathReady = false;
				return new LinkedList<Tile>();
			}
			
			LinkedList<Tile> result = new LinkedList<Tile>();
			LinkedList<Tile> intermediateResult;
			for (int i = 0 ; i < numberOfCheckpoints - 1; i++) {
				intermediateResult = intermediatePathFinder(grid.checkpoints.get(i), grid.checkpoints.get(i + 1), grid);
				if (intermediateResult == null) return new LinkedList<Tile>();
				result.addAll(intermediateResult);

			}
			intermediateResult = intermediatePathFinder(grid.checkpoints.get(numberOfCheckpoints - 1), grid.checkpoints.get(0), grid);
			if (intermediateResult == null) return new LinkedList<Tile>();
			result.addAll(intermediateResult);
			return result;
		}
	
}
