package pointgenerating;

import java.util.Random;

import general.Vector;

/**
 * Classe de génération de points selon une distribution par blocs,
 * {@link PointGenerator}.
 * <p>
 * La distibution se fait de la manière suivante :
 * <p>
 * - Un numbre <b>numberOfBlocks</b> de points sont générés dans le cadre
 * <p>
 * - Un nombre <b>numberOfPoints</b> de points est généré, tirés aléatoirement
 * dans le cadre (avec le paramètre <b>rescalingType</b>) selon une distribution
 * gaussienne, centrée en un bloc, et de variance <b>variance</b>
 * 
 * @author Louis Proffit
 */
public class PointGeneratorFromBlocks implements PointGenerator {

    /**
     * La variance de la distribution gaussienne
     */
    public static double variance = 0.1f;

    /**
     * Le type de rescaling à effectuer quand un checkpoint est tiré hors du cadre
     */
    public static RescalingType rescalingType = RescalingType.RETRY;

    /**
     * Le nombre de blocs à générer
     */
    public static int numberOfBlocks = 5;

    /**
     * Le générateur de nombre aléatoires
     */
    private static Random random = new Random();

    /**
     * @param numberOfPoints : Nombre de points à générer
     * @return Le tableau de points généré selon la distribution gaussienne :
     *         {@link PointGeneratorFromBlocks}.
     */
    public Vector[] getPoints(int numberOfPoints) {
        long startTime = System.currentTimeMillis();
        Vector[] blocks = new Vector[numberOfBlocks];
        Vector[] result = new Vector[numberOfPoints];
        for (int i = 0; i < numberOfBlocks; i++)
            blocks[i] = generateRandomPoint();
        for (int i = 0; i < numberOfPoints; i++) {
            int blockIndex = i % numberOfBlocks;
            result[i] = generateRandomPointFromBlockGaussian(blocks[blockIndex]);
        }
        long deltaTimeInMillis = System.currentTimeMillis() - startTime;
        System.out.println("Points générés en " + deltaTimeInMillis + "ms");
        return result;
    }

    /**
     * Génère un point aléatoirement suivant une distribution gaussienne centrée en
     * <b>blockCenter</b>, et de variance <b>variance</b>.
     * 
     * @param blockCenter : Le centre de la distribution gaussienne
     * @return Le point généré
     */
    private Vector generateRandomPointFromBlockGaussian(Vector blockCenter) {
        double x = blockCenter.getX() + random.nextGaussian() * variance;
        double y = blockCenter.getY() + random.nextGaussian() * variance;
        switch (rescalingType) {
            case RESCALE:
                // On fait entrer le point dans le cadre
                if (x < 0f)
                    x = 0f;
                else if (x > 1f)
                    x = 1f;
                if (y < 0f)
                    x = 0f;
                else if (y > 1f)
                    x = 1f;
                return new Vector(x, y);
            case RETRY:
                // On recommence un tirage de point
                if (x < 0 | x > 1 | y < 0 | y > 1)
                    return generateRandomPointFromBlockGaussian(blockCenter);
                return new Vector(x, y);
        }
        return null; // Jamais atteint
    }

    /**
     * Génère un point uniformément dans le cadre
     * 
     * @return Le point généré
     */
    private static Vector generateRandomPoint() {
        return new Vector(random.nextDouble(), random.nextDouble());
    }

    /**
     * Enumération des types de rescaling lorsqu'un point est en dehors du cadre :
     * 
     * @author Louis Proffit
     */
    public enum RescalingType {
        /**
         * On pioche un nouveau point, plus lent mais plus pertinent
         */
        RETRY,
        /**
         * On replace les coordonnées dans le cadre, crée des accumulations de
         * checkpoints sur les bordures mais plus rapide
         */
        RESCALE
    }
}
