package pointgenerating;

import general.Vector;

/**
 * Classe de génération de points selon un distribution uniforme dans le cadre.
 * 
 * @author Louis Proffit
 */
public class PointGeneratorUniform implements PointGenerator {

    /**
     * Méthode de génération de points
     * 
     * @param numberOfPoints : Le nombre de points à générer
     * @return Le tableau des points générés
     */
    public Vector[] getPoints(int numberOfPoints) {
        long startTime = System.currentTimeMillis();
        Vector[] checkpoints = new Vector[numberOfPoints];
        for (int i = 0; i < numberOfPoints; i++)
            checkpoints[i] = generateRandomPoint();
        long deltaTimeInMillis = System.currentTimeMillis() - startTime;
        System.out.println("Points générés en " + deltaTimeInMillis + "ms");
        return checkpoints;
    }

    /**
     * Génère un point aléatoire selon une distribution uniforme dans le cadre
     * 
     * @return : Le point généré
     */
    private static Vector generateRandomPoint() {
        return new Vector(Math.random(), Math.random());
    }
}
