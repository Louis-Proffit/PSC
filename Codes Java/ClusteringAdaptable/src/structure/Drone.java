package structure;

/**
 * Classe représentant un {@link Drone}
 * 
 * @LouisProffitX
 * @author Louis Proffit
 * @version 1.0
 */
public class Drone extends MutableVector {

    /**
     * La vitesse du drone en unité de cadre par mise à jour graphique
     */
    public static double speed = 0.001;

    /**
     * Constructeur simple
     * 
     * @param position
     */
    public Drone(Vector position) {
        super(position.getX(), position.getY());
    }

    /**
     * Fait avancer un drone vers une cible sur la distance speed
     * 
     * @param target : la cible
     */
    public void move(Vector target) {
        MutableVector movement = new MutableVector(target.getX() - this.getX(), target.getY() - this.getY());
        movement.normalize(speed);
        this.add(movement);
    }
}
