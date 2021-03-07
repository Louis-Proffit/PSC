package structure;

/**
 * Classe décrivant un point/vecteur modifiable par ses coordonnées cartésiennes
 * 
 * @author Louis Proffit
 */
public class MutableVector extends Vector {

    /**
     * Constructeur générant un point aléatoire dans le cadre
     */
    public MutableVector() {
        super(Math.random(), Math.random());
    }

    /**
     * Constructeur simple
     * 
     * @param x : L'abscisse du vecteur
     * @param y : L'ordonnée du vecteur
     */
    public MutableVector(double x, double y) {
        super(x, y);
    }

    /**
     * settter
     * 
     * @param x : La coordonnée x du vecteur
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * settter
     * 
     * @param x : La coordonnée y du vecteur
     */
    public void setY(double y) {
        this.y = y;
    }

    public void set(Vector vector) {
        setX(vector.x);
        setY(vector.y);
    }

    /**
     * Méthode pour ajouter un vecteur (avec modification)
     * 
     * @param vector : Le vecteur à ajouter
     */
    public void add(Vector vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    /**
     * Méthode pour retirer un vecteur (avec modification)
     * 
     * @param vector : Le vecteur à retirer
     */
    public void substract(MutableVector vector) {
        this.x -= vector.x;
        this.y -= vector.y;
    }

    /**
     * Normalise le vecteur
     * 
     * @param newNorm : la future norme (non nulle)
     */
    public void normalize(double newNorm) {
        double norm = getNorm();
        this.x = x / norm * newNorm;
        this.y = y / norm * newNorm;
    }
}
