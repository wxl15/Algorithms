import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;


public class PointSET {
    private SET<Point2D> pts;

    public PointSET() {
        pts = new SET<>();
    }

    public boolean isEmpty() {
        return pts.isEmpty();
    }

    public int size() {
        return pts.size();
    }

    public void insert(Point2D p) {
        check(p);
        if (contains(p)) return;
        pts.add(p);
    }

    public boolean contains(Point2D p) {
        check(p);
        return pts.contains(p);
    }

    public void draw() {
        for (Point2D p : pts) p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        check(rect);
        Queue<Point2D> q = new Queue<>();
        for (Point2D p : pts) if (rect.contains(p)) q.enqueue(p);
        return q;
    }

    public Point2D nearest(Point2D p) {
        check(p);
        Point2D nn = null;
        double nDist2 = Double.POSITIVE_INFINITY;
        for (Point2D q : pts) {
            double dist2 = p.distanceSquaredTo(q);
            if (dist2 < nDist2) {
                nn = q;
                nDist2 = dist2;
            }
        }
        return nn;
    }

    private void check(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
    }

    public static void main(String[] args) {

    }
}
