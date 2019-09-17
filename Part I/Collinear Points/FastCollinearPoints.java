import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] segments;

    // finds all line segments containing 4 or more sortedPoints
    public FastCollinearPoints(Point[] points) {
        checkNull(points);
        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        checkDuplicates(sortedPoints);

        int N = sortedPoints.length;
        ArrayList<LineSegment> lines = new ArrayList<>();

        for (Point p : sortedPoints) {
            Point[] pointsBySlope = sortedPoints.clone();
            Arrays.sort(pointsBySlope, p.slopeOrder());

            int j = 1;
            while (j < N) {
                Point min = pointsBySlope[j];
                double SLOPE_REF = p.slopeTo(pointsBySlope[j]);
                int Initial_j = j;      // #points in potential segment: only point p now
                while (j < N && p.slopeTo(pointsBySlope[j]) == SLOPE_REF) {
                    j++;
                    if (j == N) break;
                }
                if (j - Initial_j >= 3 && p.compareTo(min) < 0) {
                    Point max = pointsBySlope[j - 1];
                    lines.add(new LineSegment(p, max));
                }
            }
            segments = lines.toArray(new LineSegment[0]);
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

    private void checkNull(Point[] sortedPoints) {
        if (sortedPoints == null) throw new IllegalArgumentException();
        for (Point p : sortedPoints) if (p == null) throw new IllegalArgumentException();
    }

    private void checkDuplicates(Point[] sortedPoints) {
        int N = sortedPoints.length;
        for (int i = 0; i < N - 1; i++) {
            if (sortedPoints[i].equals(sortedPoints[i + 1])) throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {

        // read the n sortedPoints from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

//        int n = 4000;
//        int index = 0;
//        Point[] points = new Point[n];
//        for (int i = 0; i < n / 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                points[index] = new Point(i, j);
//                index++;
//            }
//        }

        FastCollinearPoints collinear = new FastCollinearPoints(points);
        System.out.println(Arrays.toString(collinear.segments()));
    }
}
