import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

public class ConvexHull {

    Collection<? extends Vector> points;
    LinkedList<Vector> sortedList;
    Vector pivot;

    public ConvexHull(Collection<? extends Vector> col) {
        points = col;
        sortedList = new LinkedList<Vector>(points);
        sortedList.sort(sortByY);
        pivot = sortedList.getFirst();
        sortedList.sort(compareAngleWithPivot);
    }

    private static Comparator<Vector> sortByY = new Comparator<Vector>() {

        @Override
        public int compare(Vector p1, Vector p2) {

            if (p1 == null)
                return -1;
            if (p2 == null)
                return 1;

            if (p1.getY() > p2.getY())
                return 1;
            if (p1.getY() == p2.getY())
                return 0;
            else
                return -1;

        }
    };

    Comparator<Vector> compareAngleWithPivot = new Comparator<Vector>() {

        @Override
        public int compare(Vector p1, Vector p2) {

            if (p1 == null)
                return -1;
            if (p2 == null)
                return 1;

            double prodVect = (p1.getX() - pivot.getX()) * (p2.getY() - pivot.getY())
                    - (p1.getY() - pivot.getY()) * (p2.getX() - pivot.getX());
            if (prodVect > 0)
                return -1;
            else if (prodVect == 0)
                return 0;
            else
                return 1;
        }
    };

    static double prodVect(Vector p1, Vector p2, Vector p3) {
        return (p2.getX() - p1.getX()) * (p3.getY() - p1.getY()) - (p2.getY() - p1.getY()) * (p3.getX() - p1.getX());
    }

    LinkedList<Vector> process() {

        if (sortedList.size() < 2)
            return null;
        else {
            LinkedList<Vector> hull = new LinkedList<Vector>();

            hull.addFirst(sortedList.pop());
            hull.addFirst(sortedList.pop());

            for (Vector p : sortedList) {

                while (hull.size() >= 2 && prodVect(hull.get(1), hull.getFirst(), p) <= 0) {
                    hull.pop();
                }
                hull.addFirst(p);

            }
            hull.addFirst(pivot);
            return hull;
        }
    }

    public static void main(String args[]) {

        LinkedList<Vector> points = new LinkedList<>();
        points.add(new Vector());
        points.add(new Vector());
        points.add(new Vector());
        points.add(new Vector());
        points.add(new Vector());
        points.add(new Vector());

        ConvexHull convexHull = new ConvexHull(points);

        System.out.println(convexHull.process().size());
    }
}