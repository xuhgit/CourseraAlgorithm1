import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!points.contains(p))
            points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        TreeSet<Point2D> rectPoints = new TreeSet<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                rectPoints.add(p);
            }
        }
        return rectPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (points.isEmpty()) return null;
        double dist = Double.POSITIVE_INFINITY;
        Point2D res = null;
        for (Point2D q : points) {
            double tmpdis = p.distanceSquaredTo(q);
            if (Double.compare(dist, tmpdis) > 0) {
                res = q;
                dist = tmpdis;
            }
        }
        return res;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET pointSET = new PointSET();

        pointSET.insert(new Point2D(0.7, 0.2)); // A
        pointSET.insert(new Point2D(0.5, 0.4)); // B
        pointSET.insert(new Point2D(0.2, 0.3)); // C
        pointSET.insert(new Point2D(0.4, 0.7)); // D
        pointSET.insert(new Point2D(0.9, 0.6)); // E


        System.out.println(pointSET.nearest(new Point2D(0.078, 0.552)));
        System.out.println(pointSET.nearest(new Point2D(0.684, 0.73)));
    }
}