package graphics;

import algorithms.ClusterAttribution;

/**
 * Interface pour les objets graphiques
 * 
 * @LouisProffitX
 * @author Louis Proffit
 * @version 1.0
 */
public interface GraphicsInterface {

    /**
     * Dessine une configuration
     * 
     * @param configuration : la configuration
     */
    public void updateGraphics(ClusterAttribution configuration);

}
