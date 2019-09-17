import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        checkCorners(points);
        int N = points.length;
        Bag<LineSegment> bagOfSegments = new Bag<>();

        // loop 1: get slopes[]
        for (int i = 0; i < N - 3; i++) {
            double[] slopes = new double[N - i - 1];
            Point p = points[i];
            for (int j = i + 1; j < N; j++) {
                Point q = points[j];
                slopes[j - i - 1] = p.slopeTo(q);
            }

            // loop 2: check collinearity
            for (int k = 0; k < N - i - 3; k++) {
                boolean collinearity = false;
                double slopeK = slopes[k];
                int numOfEqualSlopes = 1;
                for (int l = k + 1; l < N - i - 1; l++) {
                    if (slopeK == slopes[l]) numOfEqualSlopes++;
                    if (numOfEqualSlopes >= 3) {
                        collinearity = true;
                        break;
                    }
                }

                // loop 3
                if (collinearity) {
                    Point btm = p, top = p;
                    for (int m = 0; m < N - i - 1; m++) {
                        Point r = points[i + m + 1];
                        if (slopes[m] == slopeK) {
                            if (r.compareTo(btm) < 0) btm = r;
                            else if (r.compareTo(top) > 0) top = r;
                        }
                    }
                    bagOfSegments.add(new LineSegment(btm, top));
                }
            }
        }
        segments = new LineSegment[bagOfSegments.size()];
        int n = 0;
        for (LineSegment s : bagOfSegments) {
            segments[n] = s;
            n++;
        }
    }

    private void checkCorners(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        int N = points.length;
        for (Point p : points) if (p == null) throw new IllegalArgumentException();
        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.clone();
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}
