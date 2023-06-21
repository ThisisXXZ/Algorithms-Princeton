import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

    private int n;
    private ArrayList<LineSegment> seg;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point p : points) {
            if (p == null)
                throw new IllegalArgumentException();
        }

        n = 0;
        seg = new ArrayList<LineSegment>();        
        int len = points.length;
        Point[] sortedPoints = new Point[len];
        sortedPoints = Arrays.copyOf(points, len);
        Arrays.sort(sortedPoints);

        for (int i = 1; i < len; ++i) {
            if (sortedPoints[i].compareTo(sortedPoints[i - 1]) == 0)
                throw new IllegalArgumentException();
        }

        for (int p = 0; p < len; ++p) {
            for (int q = p + 1; q < len; ++q) {
                for (int r = q + 1; r < len; ++r) {
                    for (int s = r + 1; s < len; ++s) {
                        double k1 = sortedPoints[p].slopeTo(sortedPoints[q]);
                        double k2 = sortedPoints[q].slopeTo(sortedPoints[r]);
                        double k3 = sortedPoints[r].slopeTo(sortedPoints[s]);
                        if (k1 == k2 && k2 == k3) {
                            seg.add(new LineSegment(sortedPoints[p], sortedPoints[s]));
                            ++n;
                        }
                    }
                }
            }
        }
    }
    public int numberOfSegments() {
        return n;
    }      
    public LineSegment[] segments() {
        LineSegment[] res = new LineSegment[seg.size()];
        seg.toArray(res);
        return res;
    }
    public static void main(String[] args) {
        String fileName = StdIn.readString();
        // read the n points from a file
        In in = new In(fileName);
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