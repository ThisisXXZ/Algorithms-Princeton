import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

    private ArrayList<LineSegment> seg;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point p : points) {
            if (p == null)
                throw new IllegalArgumentException();
        }
        seg = new ArrayList<LineSegment>();
        int len = points.length;
        Point[] sortedPoints = new Point[len];
        sortedPoints = Arrays.copyOf(points, len);
        Arrays.sort(sortedPoints);

        for (int i = 1; i < len; ++i) {
            if (sortedPoints[i].compareTo(sortedPoints[i - 1]) == 0)
                throw new IllegalArgumentException();
        }
        
        Point[] tmp = new Point[len];
        for (int i = 0; i < len; ++i) {
            tmp = Arrays.copyOf(sortedPoints, len);
            Arrays.sort(tmp, sortedPoints[i].slopeOrder());
            int p = 0;
            while (p < len) {
                double k = tmp[p].slopeTo(sortedPoints[i]);
                int q = p + 1;
                while (q < len && k == tmp[q].slopeTo(sortedPoints[i])) 
                    ++q;
                if (sortedPoints[i].compareTo(tmp[p]) < 0 && q - p >= 3) {
                    seg.add(new LineSegment(sortedPoints[i], tmp[q - 1]));
                }
                p = q;
            }
        }

        
    } 
    public int numberOfSegments() {
        return seg.size();
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }           
}