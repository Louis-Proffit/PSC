package general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import clusteringalgorithms.ClusteringSolver;
import clusteringalgorithms.ClusteringTree;
import clusteringalgorithms.Kmeans;
import graphics.Graphics;
import pointgenerating.PointGenerator;
import pointgenerating.PointGeneratorFromBlocks;
import pointgenerating.PointGeneratorUniform;
import tspsolving.Genetique;
import tspsolving.TSPRecuit;
import tspsolving.TSPSolver;

/**
 * Classe à executer pour visualiser les algorithmes de clustering et de TSP. Le
 * processus se déroule en trois phases :
 * <p>
 * - Génération aléatoire de checkpoints, avec notamment les distributions
 * {@link PointGeneratorUniform} et {@link PointGeneratorFromBlocks}
 * <p>
 * - Génération du clusering avec notamment les algorithmes {@link Kmeans} et
 * {@link ClusteringTree}
 * <p>
 * - Génération des chemins pour les drones, via un solveur de TSP, avec
 * notamment {@link TSPRecuit} et {@link Genetique} La classe Graphics permet
 * l'affichage des données
 * 
 * @author Louis Proffit
 */
public class Controller {

    /**
     * Nombre de checkpoints générés
     */
    private static int numberOfCheckpoints = 100;

    /**
     * Nombre de clusters générés
     */
    private static int numberOfClusters = 1;

    /**
     * Classe utilisée pour la génération des checkpoints
     */
    private static Class<? extends PointGenerator> checkpointGeneratorClass = PointGeneratorUniform.class;

    /**
     * Classe utilisée pour le clustering
     */
    private static Class<? extends ClusteringSolver> clusteringSolverClass = Kmeans.class;

    /**
     * Classe utilisée pour le solveur du problème TSP.
     */
    private static Class<? extends TSPSolver> tspSolverClass = TSPRecuit.class;

    public static void main(String[] args) {
        try {
            /* Création de la distribution */
            Constructor<? extends PointGenerator> checkpointGeneratorConstructor;
            checkpointGeneratorConstructor = checkpointGeneratorClass.getConstructor();
            PointGenerator checkpointGenerator = checkpointGeneratorConstructor.newInstance(); // Constructeur sans
                                                                                               // argument
            Vector[] checkpointVectors = checkpointGenerator.getPoints(numberOfCheckpoints);
            Checkpoint[] checkpoints = new Checkpoint[numberOfCheckpoints];
            Arrays.setAll(checkpoints, i -> new Checkpoint(checkpointVectors[i]));

            /* Création du clustering */
            Constructor<? extends ClusteringSolver> clusteringSolverConstructor = clusteringSolverClass
                    .getConstructor();
            ClusteringSolver clusteringAlgorithm = clusteringSolverConstructor.newInstance(); // Constructeur sans
                                                                                              // argument
            Cluster[] clusters = clusteringAlgorithm.getClusters(checkpoints, numberOfClusters);

            /* Création des chemins */
            Constructor<? extends TSPSolver> tspSolverConstructor = tspSolverClass.getConstructor();
            TSPSolver tspSolver = tspSolverConstructor.newInstance(); // Constructeur sans argument
            for (Cluster cluster : clusters)
                cluster.setPath(tspSolver);

            /* Affichage du résultat */
            Graphics.showResult(clusters);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            System.out.println("Le constructeur n'existe pas");
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.out.println("L'objet ne peut pas être crée");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
