package structure;

/**
 * Classe représentant un {@link Drone}
 * 
 * @LouisProffitX
 * @author Louis Proffit
 * @version 1.0
 */
public class Drone extends MutableVector {

    private Checkpoint target;

    /**
     * La vitesse du drone en unité de cadre par mise à jour graphique
     */
    public static double speed = 0.01;

    /**
     * Constructeur simple
     * 
     * @param position
     */
    public Drone(Vector position) {
        super(position.getX(), position.getY());
        this.target = null;
    }

    public Checkpoint getTarget() {
        return target;
    }

    public void setTarget(Checkpoint target) {
        this.target = target;
    }

    /**
     * Fait avancer un drone vers une cible sur la distance speed
     * 
     * @param target : la cible
     */
    public boolean move() {
        if (target == null)
            return false;
        if (this.distance(target) < speed) {
            this.x = target.x;
            this.y = target.y;
            return true;
        }
        MutableVector movement = new MutableVector(target.getX() - this.getX(), target.getY() - this.getY());
        movement.normalize(speed);
        this.add(movement);
        return false;
    }
}
