package structure;

/**
 * Classe représentant un checkpoint
 * 
 * @LouisProffitX
 * @author Louis Proffit
 * @version 1.0
 */
public class Checkpoint extends Vector {

    /**
     * Constructeur simple
     * 
     * @param position
     */
    public Checkpoint(Vector position) {
        super(position.getX(), position.getY());
    }
}
