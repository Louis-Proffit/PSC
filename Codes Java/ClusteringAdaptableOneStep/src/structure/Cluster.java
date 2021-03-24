package structure;

import java.util.LinkedList;

import algorithms.RecuitAlgorithm;
import algorithms.RecuitInterface;

/**
 * Classe représentant un {@link Cluster}. Contient la possibilité d'être
 * améliorée par un algorithme {@link TSPRecuit}.
 * 
 * @LouisProffitX
 * @author Louis Proffit
 * @version 1.0
 */
public class Cluster implements RecuitInterface {

    /**
     * La cible en cours dans le cluster
     */
    private Checkpoint currentTarget = null;

    /**
     * Le chemin du cluster
     */
    private Path path = new Path();

    /**
     * Effectue un passage de {@link TSPRecuit} pour améliorer le chemin
     */
    public void improvePath() {
        RecuitAlgorithm.improve(this);
    }

    /**
     * Renvoie la distance entre le cluster (sa moyenne) et une position
     * 
     * @param vector : la position
     * @return la distance
     */
    public double distance(Vector vector) {
        return path.distance(vector);
    }

    /**
     * Renvoie la cible courante
     * 
     * @return : la cible courante
     */
    public Checkpoint getCurrentTarget() {
        return currentTarget;
    }

    /**
     * Avance d'un cran la cible. Si il n'y a pas de successeur, la cible devient
     * nulle
     */
    public void moveTargetForward() {
        if (currentTarget == null)
            return;
        this.currentTarget = path.getCheckpointAfter(currentTarget);
    }

    /**
     * Renvoie la liste des checkpoints du chemin dans l'ordre de parcours (le
     * premier élément est arbitraire)
     * 
     * @return : la liste ordonnée des checkpoints
     */
    public LinkedList<Checkpoint> getCheckpointsOrdered() {
        return path.getCheckpointsOrdered();
    }

    /**
     * Ajoute un checkpoint au chemin
     * 
     * @param checkpoint : le checkpoint à ajouter
     */
    public void addCheckpoint(Checkpoint checkpoint) {
        if (currentTarget == null)
            currentTarget = checkpoint;
        path.addCheckpoint(checkpoint);
    }

    /**
     * Retire un checkpoint du chemin. Si ce checkpoint est la cible, on passe à la
     * suivante
     * 
     * @param checkpoint : le checkpoint à retirer
     */
    public void removeCheckpoint(Checkpoint checkpoint) {
        if (currentTarget == checkpoint)
            currentTarget = path.getCheckpointAfter(checkpoint);
        path.removeCheckpoint(checkpoint);
    }

    @Override
    public int getSize() {
        return path.getSize();
    }

    @Override
    public Modification modificationFunction() {
        int size = path.getSize();
        return new Pair<Integer>((int) (Math.random() * size), (int) (Math.random() * size));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Double improvementFunction(Modification modification) {
        double result = 0;
        Pair<Integer> swap = (Pair<Integer>) modification;
        Checkpoint firstCheckpoint = path.getCheckpointAtIndex(swap.getFirst());
        Checkpoint secondCheckpoint = path.getCheckpointAtIndex(swap.getSecond());
        Checkpoint checkpointBeforeFirst = path.getCheckpointBefore(firstCheckpoint);
        Checkpoint checkpointAfterSecond = path.getCheckpointAfter(secondCheckpoint);
        result += checkpointBeforeFirst.distance(secondCheckpoint);
        result += checkpointAfterSecond.distance(firstCheckpoint);
        result -= firstCheckpoint.distance(checkpointBeforeFirst);
        result -= secondCheckpoint.distance(checkpointAfterSecond);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void commitFunction(Modification modification) {
        Pair<Integer> swap = (Pair<Integer>) modification;
        int size = path.getSize();
        int increasingIndex = swap.getFirst();
        int decreasingIndex = swap.getSecond();
        if (increasingIndex == decreasingIndex)
            return;
        if (increasingIndex < decreasingIndex) {
            while (increasingIndex < decreasingIndex) {
                path.swapOrder(increasingIndex, decreasingIndex);
                increasingIndex++;
                decreasingIndex--;
            }
        } else {
            boolean changed = false;
            while (increasingIndex < decreasingIndex & !changed) {
                path.swapOrder(increasingIndex, decreasingIndex);
                increasingIndex++;
                decreasingIndex--;
                if (increasingIndex == size) {
                    changed = true;
                    increasingIndex = 0;
                }
                if (increasingIndex == -1) {
                    changed = true;
                    decreasingIndex = size - 1;
                }
            }
        }
    }
}
