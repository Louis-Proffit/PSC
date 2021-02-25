package pointgenerating;

import general.Vector;

/**
 * Interface à implementer pour les classes qui fournissent une distribution de
 * points
 */
public interface PointGenerator {

    /**
     * Méthode de synthèse pour générer les points
     * 
     * @param numberOfPoints : Le nombre de points à générer
     * @return Le tableau des points générés, de longueur <b>numberOfCheckpoints</b>
     */
    public Vector[] getPoints(int numberOfPoints);

}