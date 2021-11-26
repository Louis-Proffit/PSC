package algorithms;

import structure.Modification;

/**
 * Interface permettant l'application d'un recuit simulé
 * 
 * @LouisProffitX
 * @author Louis Proffit
 * @version 1.0
 */
public interface RecuitInterface {

    /**
     * Renvoie la taille du problème
     * 
     * @return la taille du problème
     */
    int getSize();

    /**
     * Renvoie une proposition de modification
     * 
     * @return : la proposition
     */
    Modification modificationFunction();

    /**
     * Renvoie l'amélioration engendrée par une modification. Une amélioration
     * positive au sens littéral sera positive au sens strict.
     * 
     * @param modification : la modification à évaluer
     * @return la valeur de l'amélioration
     */
    Double improvementFunction(Modification modification);

    /**
     * Applique la modification
     * 
     * @param modification
     */
    void commitFunction(Modification modification);

}
