package structure;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Classe décrivant un chemin ordonné de {@link Checkpoint}
 * 
 * @LouisProffitX
 * @author Louis Proffit
 * @version 1.0
 */
public class Path {

    /**
     * Le nombre de {@link Checkpoint} du chemin
     */
    private int size = 0;

    /**
     * La matrice d'association indice -> checkpoint. Contient les entrées inversées
     * de indices
     */
    private HashMap<Integer, Checkpoint> order = new HashMap<>();

    /**
     * La matrice d'association checkpoint -> indice. Contient les entrées inversées
     * de order
     */
    private HashMap<Checkpoint, Integer> indices = new HashMap<>();

    /**
     * Le vecteur moyen des positions des éléments du chemin
     */
    private Vector mean = new Vector();

    /**
     * Renvoie la distance entre le chemin (sa moyenne) et une position
     * 
     * @param vector : la position
     * @return la distance
     */
    public double distance(Vector vector) {
        return vector.distance(mean);
    }

    /**
     * Calcule la moyenne et la stocke dans mean. Si le chemin est vide, il attribue
     * un vecteur aléatoire
     */
    private void computeMean() {
        if (size == 0)
            this.mean = new MutableVector();
        else {
            MutableVector result = new MutableVector(0, 0);
            for (Checkpoint checkpoint : indices.keySet()) {
                result.add(checkpoint);
            }
            this.mean = result.getMult(1f / size);
        }
    }

    /**
     * Calcule le checkpoint qui suit un autre checkpoint dans le chemin
     * 
     * @param checkpoint : le checkpoint
     * @return le checkpoint qui suit
     */
    public Checkpoint getCheckpointAfter(Checkpoint checkpoint) {
        if (size <= 1)
            return null;
        else {
            int currentIndex = indices.get(checkpoint);
            if (currentIndex < size - 1)
                return order.get(currentIndex + 1);
            else
                return order.get(0);
        }
    }

    /**
     * Calcule le checkpoint qui précède un autre checkpoint dans le chemin
     * 
     * @param checkpoint : le checkpoint
     * @return le checkpoint qui précède
     */
    public Checkpoint getCheckpointBefore(Checkpoint checkpoint) {
        if (size == 1)
            return null;
        else {
            int currentIndex = indices.get(checkpoint);
            if (currentIndex > 0)
                return order.get(currentIndex - 1);
            else
                return order.get(size - 1);
        }
    }

    /**
     * Récupère le checkpoint situé à un indice précis
     * 
     * @param index : l'indice
     * @return le checkpoint à cet indice
     */
    public Checkpoint getCheckpointAtIndex(int index) {
        return order.get(index);
    }

    /**
     * Récupère l'indice d'un checkpoint précis
     * 
     * @param checkpoint : le checkpoint
     * @return l'indice de ce checkpoint
     */
    public int getCheckpointIndex(Checkpoint checkpoint) {
        return indices.get(checkpoint);
    }

    /**
     * Récupère la liste des checkpoints du chemin dans l'ordre
     * 
     * @return : la liste des éléments du chemin
     */
    public LinkedList<Checkpoint> getCheckpointsOrdered() {
        LinkedList<Checkpoint> result = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            result.add(order.get(i));
        }
        return result;
    }

    /**
     * Ajoute un checkpoint dans le chemin (à la derinère position)
     * 
     * @param checkpoint : le checkpoint à ajouter
     */
    public void addCheckpoint(Checkpoint checkpoint) {
        order.put(size, checkpoint);
        indices.put(checkpoint, size);
        size++;
        computeMean();
    }

    /**
     * Enlève un checkpoin du chemin
     * 
     * @param checkpoint : le checkpoint à ajouter
     */
    public void removeCheckpoint(Checkpoint checkpoint) {
        int index = indices.get(checkpoint);
        swapOrder(index, size - 1);
        removeLastCheckpoint();
    }

    /**
     * Enlève le dernier checkpoint du chemin
     */
    private void removeLastCheckpoint() {
        Checkpoint previousLastCheckpoint = order.remove(size - 1);
        indices.remove(previousLastCheckpoint);
        size--;
        computeMean();
    }

    /**
     * Echange les deux indices dans le parcours
     * 
     * @param firstIndex  : le premier indice
     * @param secondIndex : le deuxième indice
     */
    public void swapOrder(int firstIndex, int secondIndex) {
        Checkpoint checkpoint1 = order.get(firstIndex);
        Checkpoint checkpoint2 = order.put(secondIndex, checkpoint1);
        order.put(firstIndex, checkpoint2);
        indices.put(checkpoint1, secondIndex);
        indices.put(checkpoint2, firstIndex);
    }

    /**
     * Renvoie la taille du chemin
     * 
     * @return la taille du chemin
     */
    public int getSize() {
        return size;
    }
}
