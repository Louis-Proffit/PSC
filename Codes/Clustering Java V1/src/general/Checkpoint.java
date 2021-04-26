package general;

/**
 * Classe décrivant un checkpoint, caractérisé par sa position
 * 
 * @author Louis Proffit
 */
public class Checkpoint extends Vector {

    /**
     * Constructeur simple
     * 
     * @param x : La coordonnée x du checkpoint.
     * @param y : La coordonnée y du checkpoint.
     */
    public Checkpoint(double x, double y) {
        super(x, y);
    }

    /**
     * Constructeur simple
     * 
     * @param vector : La position du checkpoint
     */
    public Checkpoint(Vector vector) {
        super(vector.getX(), vector.getY());
    }

}
