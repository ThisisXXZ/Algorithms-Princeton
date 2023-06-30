import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.List;
import java.util.ArrayList;

public class KdTree {
    
    private Node root;
    private int size;

    private Point2D nearestPoint;
    private double minDis;
    
    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;

        public Node(Point2D p, RectHV rect, Node lb, Node rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }

        public double x() { return p.x(); }
        public double y() { return p.y(); }
    }

    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() { return size == 0; }

    public int size() { return size; }

    private Node put(Node u, Point2D p, double xmin, double ymin, double xmax, double ymax, boolean k) {
        if (u == null)
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax), null, null);
        if (k) {
            if (p.x() < u.x())
                u.lb = put(u.lb, p, xmin, ymin, u.x(), ymax, !k);
            else
                u.rt = put(u.rt, p, u.x(), ymin, xmax, ymax, !k);
        } else {
            if (p.y() < u.y())
                u.lb = put(u.lb, p, xmin, ymin, xmax, u.y(), !k);
            else
                u.rt = put(u.rt, p, xmin, u.y(), xmax, ymax, !k);
        }
        return u;
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (contains(p))    return;
        ++size;
        root = put(root, p, 0.0, 0.0, 1.0, 1.0, true);
    }

    private boolean get(Node u, Point2D p, boolean k) {
        if (u == null)  return false;
        if (p.equals(u.p))  return true;
        if (k) {
            if (p.x() < u.x())
                return get(u.lb, p, !k);
            else
                return get(u.rt, p, !k);
        } else {
            if (p.y() < u.y())
                return get(u.lb, p, !k);
            else 
                return get(u.rt, p, !k);
        }
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return get(root, p, true);
    }

    private void draw(Node u, boolean k) {
        if (u == null)  return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        u.p.draw();
        if (k) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(u.x(), u.rect.ymin(), u.x(), u.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(u.rect.xmin(), u.y(), u.rect.xmax(), u.y());
        }
        draw(u.lb, !k);
        draw(u.rt, !k);
    }

    public void draw() {
        draw(root, true);
    }
    
    private void range(Node u, RectHV rect, List<Point2D> pts) {
        if (u == null)  return;
        if (!rect.intersects(u.rect))   return;
        if (rect.contains(u.p))
            pts.add(u.p);
        range(u.lb, rect, pts);
        range(u.rt, rect, pts);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        List<Point2D> points = new ArrayList<Point2D>();
        range(root, rect, points);
        return points;
    }

    private void nearest(Node u, Point2D p, boolean k) {
        if (u == null)  return;
        if (u.rect.distanceSquaredTo(p) >= minDis)  return;
        double squaredDis = p.distanceSquaredTo(u.p);
        if (squaredDis < minDis) {
            minDis = squaredDis;
            nearestPoint = u.p;
        }
        if (k) {
            if (p.x() < u.x()) {
                nearest(u.lb, p, !k);
                nearest(u.rt, p, !k);
            } else {
                nearest(u.rt, p, !k);
                nearest(u.lb, p, !k);
            }
        } else {
            if (p.y() < u.y()) {
                nearest(u.lb, p, !k);
                nearest(u.rt, p, !k);
            } else {
                nearest(u.rt, p, !k);
                nearest(u.lb, p, !k);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        nearestPoint = null;
        minDis = Double.POSITIVE_INFINITY;
        nearest(root, p, true);
        return nearestPoint;
    } 

    public static void main(String[] args) {
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        KdTree kdtree = new KdTree();
        while (true) {
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                if (rect.contains(p)) {
                    StdOut.printf("%8.6f %8.6f\n", x, y);
                    kdtree.insert(p);
                    StdDraw.clear();
                    kdtree.draw();
                    StdDraw.show();
                }
            }
            StdDraw.pause(70);
        }

    }
}
