import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;


public class KdTree {
    private int N;
    private Node root;

    public KdTree() {
    }

    private class Node {
        private Node left, right;
        private Point2D item;
        private boolean vertical;
        private RectHV rect;

        public Node(Point2D p, boolean vertical, double xmin, double ymin, double xmax, double ymax) {
            item = p;
            this.vertical = vertical;
            rect = new RectHV(xmin, ymin, xmax, ymax);
        }
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void insert(Point2D p) {
        check(p);
        root = insert(root, p, true, 0, 0, 1, 1);
    }

    private Node insert(Node t, Point2D p, boolean vertical, double xmin, double ymin, double xmax, double ymax) {
        if (t == null) {
            N++;
            return new Node(p, vertical, xmin, ymin, xmax, ymax);
        }

        Point2D q = t.item;
        if (q.equals(p)) return t;

        if (vertical) {
            double x = q.x();
            if (p.x() - x < 0) t.left = insert(t.left, p, false, xmin, ymin, x, ymax);
            else t.right = insert(t.right, p, false, x, ymin, xmax, ymax);
        } else {
            double y = q.y();
            if (p.y() - y < 0) t.left = insert(t.left, p, true, xmin, ymin, xmax, y);
            else t.right = insert(t.right, p, true, xmin, y, xmax, ymax);
        }
        return t;
    }

    public boolean contains(Point2D p) {
        check(p);
        return contains(root, p);
    }

    private boolean contains(Node t, Point2D p) {
        if (t == null || !t.rect.contains(p)) return false;

        Point2D q = t.item;
        if (q.equals(p)) return true;
        double cmp;
        if (t.vertical) cmp = p.x() - q.x();
        else cmp = p.y() - q.y();
        if (cmp < 0) return contains(t.left, p);
        else return contains(t.right, p);
    }

    public void draw() {
        draw(root);
    }

    private void draw(Node t) {
        if (t == null) return;
        t.item.draw();
        if (t.vertical) {
            double x = t.item.x();
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x, t.rect.ymin(), x, t.rect.ymax());

        } else {
            double y = t.item.y();
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(t.rect.xmin(), y, t.rect.xmax(), y);
        }
        draw(t.left);
        draw(t.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        check(rect);
        Queue<Point2D> q = new Queue<>();
        range(root, rect, q);
        return q;
    }

    private void range(Node t, RectHV rect, Queue<Point2D> q) {
        if (t == null || !t.rect.intersects(rect)) return;
        Point2D p = t.item;
        if (rect.contains(p)) q.enqueue(p);
        range(t.left, rect, q);
        range(t.right, rect, q);
    }

    public Point2D nearest(Point2D p) {
        check(p);
        if (root == null) return null;
        Point2D q = root.item;
        return nearest(root, p, q, q.distanceSquaredTo(p));
    }

    private Point2D nearest(Node t, Point2D p, Point2D nn, double nDist) {
        if (t == null) return nn;
        Point2D q = t.item;
        double dist = q.distanceSquaredTo(p);
        if (dist < nDist) {
            nn = q;
            nDist = dist;
        }

        double cmp;
        if (t.vertical) cmp = p.x() - q.x();
        else cmp = p.y() - q.y();

        if (cmp < 0) {
            nn = nearest(t.left, p, nn, nDist);
            nDist = nn.distanceSquaredTo(p);
            if (t.right != null && nDist > t.right.rect.distanceSquaredTo(p)) nn = nearest(t.right, p, nn, nDist);
            return nn;
        } else {
            nn = nearest(t.right, p, nn, nDist);
            nDist = nn.distanceSquaredTo(p);
            if (t.left != null && nDist > t.left.rect.distanceSquaredTo(p)) nn = nearest(t.left, p, nn, nDist);
            return nn;
        }
    }

    private void check(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        int c = 0, size = 0;
        while (c++ < 10000) {
            Point2D p = new Point2D(StdRandom.uniform(0.0, 1.0), StdRandom.uniform(0.0, 1.0));
            tree.insert(p);
            size++;
        }
        System.out.println(size + " " + tree.size());

        RectHV rect = new RectHV(0, 0, 1, 1);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        tree.nearest(new Point2D(.5, .5)).draw();
        StdDraw.setPenRadius();
        tree.draw();

    }
}
