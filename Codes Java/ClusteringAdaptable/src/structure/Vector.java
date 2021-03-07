package structure;

/**
 * Classe décrivant un vecteur (immodifiable)
 * 
 * @LouisProffitX
 * @author Louis Proffit
 * @version 1.0
 */
public class Vector {

    /**
     * La coordonnée x du vecteur
     */
    public double x;

    /**
     * La coordonnée y du vecteur
     */
    public double y;

    /**
     * Constructeur simple d'un point aléatoire dans le cadre
     */
    public Vector() {
        this.x = Math.random();
        this.y = Math.random();
    }

    /**
     * Constructeur simple
     * 
     * @param x : l'abscisse du vecteur
     * @param y : l'ordonnée du vecteur
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calcule la distance entre this et un vecteur (vus comme des positions)
     * 
     * @param vector : l'autre vecteur
     * @return la distance
     */
    public double distance(Vector vector) {
        return Math.pow(Math.pow(vector.x - this.x, 2) + Math.pow(vector.y - this.y, 2), 0.5);
    }

    /**
     * Renvoie la coordonnée x du vecteur
     * 
     * @return la coordonnée x du vecteur
     */
    public double getX() {
        return x;
    }

    /**
     * Renvoie la coordonnée y du vecteur
     * 
     * @return la coordonnée y du vecteur
     */
    public double getY() {
        return y;
    }

    /**
     * Renvoie une copie de this soustrait d'un vecteur vector. this n'est pas
     * modifié
     * 
     * @param vector : le vecteur à soustraire
     * @return la copie du vecteur soustrait
     */
    public Vector copyMinus(Vector vector) {
        return new Vector(this.x - vector.x, this.y - vector.y);
    }

    /**
     * Renvoie une copie de this modifié d'un coefficient mult. this n'est pas
     * modifié
     * 
     * @param mult : le coefficient
     * @return la copie du vecteur multiplié
     */
    public Vector getMult(double mult) {
        return new Vector(this.x * mult, this.y * mult);
    }

    /**
     * Renvoie la norme de this
     * 
     * @return la norme de this
     */
    public double getNorm() {
        return Math.pow(Math.pow(this.x, 2) + Math.pow(this.y, 2), 0.5);
    }

    @Override
    public String toString() {
        return "Vecteur : (" + x + ";" + y + ")";
    }
}