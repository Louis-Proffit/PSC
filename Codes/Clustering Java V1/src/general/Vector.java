package general;

/**
 * Classe décrivant un point/vecteur par ses coordonnées cartésiennes
 * 
 * @author Louis Proffit
 */
public class Vector {

    /**
     * Abscisse du point
     */
    private double x;

    /**
     * Coordonnées du point
     */
    private double y;

    /**
     * Constructeur générant un point aléatoire dans le cadre
     */
    public Vector() {
        this.x = Math.random();
        this.y = Math.random();
    }

    /**
     * Constructeur simple
     * 
     * @param x : L'abscisse du vecteur
     * @param y : L'ordonnée du vecteur
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calcule la distance euclidienne entre this et <b>vector</b>
     * 
     * @param vector : Le vecteur donc on veut calculer la distance avec this
     * @return La distance entre this et <b>vector</b>
     */
    public double distance(Vector vector) {
        return Math.pow(Math.pow(vector.x - this.x, 2) + Math.pow(vector.y - this.y, 2), 0.5);
    }

    /**
     * getter
     * 
     * @return La coordonnée x du vecteur
     */
    public double getX() {
        return x;
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
     * getter
     * 
     * @return La coordonnée y du vecteur
     */
    public double getY() {
        return y;
    }

    /**
     * settter
     * 
     * @param x : La coordonnée y du vecteur
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Méthode pour ajouter un vecteur (avec modification)
     * 
     * @param vector le vecteur à ajouter
     */
    public void add(Vector vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    /**
     * Méthode pour multiplier this par un scalaire (avec modification)
     * 
     * @param mult : Le coefficient de multiplication
     * @return Le vecteur modifié
     */
    public Vector getMult(double mult) {
        return new Vector(this.x * mult, this.y * mult);
    }

    /**
     * Renvoie un copie du vecteur
     * 
     * @param vector
     * @return Une copie de <b>vector</b>
     */
    public Vector copy(Vector vector) {
        return new Vector(vector.y, vector.x);
    }
}