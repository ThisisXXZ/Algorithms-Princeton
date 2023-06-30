import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {

    private Set<Point2D> s;

    public PointSET() {
        s = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return s.isEmpty();
    }

    public int size() {
        return s.size();
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (s.contains(p))
            return;
        s.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return s.contains(p);
    }

    public void draw() {
        for (Point2D p : s) p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        List<Point2D> res = new ArrayList<Point2D>();
        for (Point2D p : s) {
            if (rect.contains(p))
                res.add(p);
        }
        return res;
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (s.isEmpty())    return null;
        double min = Double.POSITIVE_INFINITY;
        Point2D res = null;
        for (Point2D points : s) {
            double dis = p.distanceSquaredTo(points);
            if (dis < min) {
                min = dis;
                res = points;
            }
        }
        return res;
    }

    public static void main(String[] args) {

    }
}
