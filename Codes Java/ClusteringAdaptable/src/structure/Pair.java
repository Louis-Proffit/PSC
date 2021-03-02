package structure;

public class Pair<U> implements Modification {

    private final U first;
    private final U second;

    public Pair(U first, U second) {
        this.first = first;
        this.second = second;
    }

    public U getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        Pair<U> o = (Pair<U>) obj;
        return ((first.equals(o.getFirst()) & second.equals(o.getSecond()))
                | (second.equals(o.getFirst()) & first.equals(o.getSecond())));
    }

}
