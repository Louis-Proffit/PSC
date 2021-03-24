package structure;

/**
 * Classe décrivant une paire d'objets de même type
 * 
 * @LouisProffitX
 * @author Louis Proffit
 * @version 1.0
 */
public class Pair<T> implements Modification {

    /**
     * Le premier élément de la paire
     */
    private final T first;

    /**
     * Le second élément de la paire
     */
    private final T second;

    /**
     * Constructeur simple
     * 
     * @param first  : le premier élément
     * @param second : le second élément
     */
    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Renvoie le premier élément de la paire
     * 
     * @return : le premier élément
     */
    public T getFirst() {
        return first;
    }

    /**
     * Renvoie le second élément de la paire
     * 
     * @return : le second élément
     */
    public T getSecond() {
        return second;
    }

    /**
     * Méthode usuelle equals Deux paires sont égales si elles contiennent les mêmes
     * éléments, indépendament de l'ordre
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        Pair<T> o = (Pair<T>) obj;
        return ((first.equals(o.getFirst()) & second.equals(o.getSecond()))
                | (second.equals(o.getFirst()) & first.equals(o.getSecond())));
    }

}
