package general;

/**
 * Classe décrivant un drone, caractérisé par sa position
 * 
 * @author Louis Proffit
 */
public class Drone extends Vector {

    private Cluster attributedCluster;

    public Drone() {
        super(Math.random(), Math.random());
    }

    public Drone(double x, double y) {
        super(x, y);
    }

    public Cluster getAttributedCluster() {
        return attributedCluster;
    }

    public void setAttributedCluster(Cluster attributedCluster) {
        this.attributedCluster = attributedCluster;
    }
}
