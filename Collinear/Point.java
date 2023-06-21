/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
 
public class Point implements Comparable<Point> {
 
    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point
 
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }
 
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }
 
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(x, y, that.x, that.y);
    }
 
    public double slopeTo(Point that) {
        if (x == that.x) {
            if (y == that.y)
                return Double.NEGATIVE_INFINITY;
            return Double.POSITIVE_INFINITY;
        }
        if (y == that.y) {
            return 0;
        } 
        return (double) (that.y - y) / (that.x - x);
    }
 
    public int compareTo(Point that) {
        if (y < that.y)    return -1;
        if (y > that.y)    return 1;
        if (x < that.x)    return -1;
        if (x > that.x)    return 1;
        return 0;
    }
 
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new SlopeOrder();
    }

    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point p1, Point p2) {
            double k1 = slopeTo(p1);
            double k2 = slopeTo(p2); 
            if (k1 < k2)
                return -1;
            if (k1 > k2)
                return 1;
            return p1.compareTo(p2);
        }
    }

 
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }
}