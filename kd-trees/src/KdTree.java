import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class KdTree {
    private Node root;

    private int size = 0;

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return this.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) {
            root = Node.create(p, 0, null, null);
            size++;
        } else if (!contains(p)) {
            insert(root, p, true);
            size++;
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return false;
        return contains(root, p, true);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, new RectHV(0, 0, 1, 1));
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> rectPoints = new LinkedList<>();
        range(this.root, rect, rectPoints);
        return rectPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (isEmpty()) return null;
        if (p == null) throw new IllegalArgumentException();
        return nearest(p, root.data, root);
    }

    private void insert(Node node, Point2D p, boolean useX) {
        Comparator<Point2D> comp = useX ? Point2D.X_ORDER : Point2D.Y_ORDER;
        // if the point to be inserted is smaller, go left
        if (comp.compare(node.data, p) > 0) {
            if (node.left == null) {
                node.left = Node.create(p, node.nodeLevel + 1, null, null);
            } else {
                insert(node.left, p, !useX);
            }
        } else if (node.right == null) {
            node.right = Node.create(p, node.nodeLevel + 1, null, null);
        } else {
            insert(node.right, p, !useX);
        }
    }

    private boolean contains(Node n, Point2D p, boolean useX) {
        Comparator<Point2D> comp = useX ? Point2D.X_ORDER : Point2D.Y_ORDER;
        if (n.data.equals(p)) return true;

        if (comp.compare(n.data, p) > 0) {
            if (n.left == null) {
                return false;
            } else {
                return contains(n.left, p, !useX);
            }
        } else if (n.right == null) {
            return false;
        } else {
            return contains(n.right, p, !useX);
        }
    }

    private void range(Node n, RectHV rect, List<Point2D> acc) {
        if (n == null) {
            return;
        }

        if (rect.contains(n.data)) {
            acc.add(n.data);
        }

        if (n.nodeLevel % 2 == 0) { // Vertical segment
            // the vertical line intersects with query rectangle
            if (rect.xmin() <= n.data.x() && n.data.x() <= rect.xmax()) {
                range(n.left, rect, acc);
                range(n.right, rect, acc);
            } else if (rect.xmin() > n.data.x()) { // Search right
                range(n.right, rect, acc);
            } else { // Search left
                range(n.left, rect, acc);
            }
        } else { // Horizontal segment
            // the horizontal line intersects with query rectangle
            if (rect.ymin() <= n.data.y() && n.data.y() <= rect.ymax()) {
                range(n.left, rect, acc);
                range(n.right, rect, acc);
            } else if (rect.ymin() > n.data.y()) { // Search up
                range(n.right, rect, acc);
            } else { // Search down
                range(n.left, rect, acc);
            }
        }
    }

    // [0,0] <-> [1, 1]
    private void draw(Node n, RectHV rectHV) {
        if (n == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        n.data.draw();

        StdDraw.setPenRadius(0.001);
        if (n.nodeLevel % 2 == 0) {
            // vertical
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.data.x(), rectHV.ymin(), n.data.x(), rectHV.ymax());
            draw(n.left, new RectHV(rectHV.xmin(), rectHV.ymin(), n.data.x(), rectHV.ymax()));
            draw(n.right, new RectHV(n.data.x(), rectHV.ymin(), rectHV.xmax(), rectHV.ymax()));
        } else {
            // horizontal
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rectHV.xmin(), n.data.y(), rectHV.xmax(), n.data.y());
            draw(n.left, new RectHV(rectHV.xmin(), rectHV.ymin(), rectHV.xmax(), n.data.y()));
            draw(n.right, new RectHV(rectHV.xmin(), n.data.y(), rectHV.xmax(), rectHV.ymax()));
        }
    }

    private Point2D nearest(Point2D goal, Point2D best, Node n) {
        if (n == null) return best;
        if (n.data.distanceSquaredTo(goal) < best.distanceSquaredTo(goal)) best = n.data;
        Comparator<Point2D> comp = n.nodeLevel % 2 == 0 ? Point2D.X_ORDER : Point2D.Y_ORDER;
        Node goodSide, badSide;
        if (comp.compare(goal, n.data) < 0) {
            goodSide = n.left;
            badSide = n.right;
        } else {
            goodSide = n.right;
            badSide = n.left;
        }
        best = nearest(goal, best, goodSide);
        double treshhold = n.nodeLevel % 2 == 0 ? Math.abs(goal.x() - n.data.x()) : Math.abs(goal.y() - n.data.y());
        double threshholdSqure = treshhold * treshhold;
        if (Double.compare(best.distanceSquaredTo(goal), threshholdSqure) > 0) {
            best = nearest(goal, best, badSide);
        }
        return best;
    }

    // Represents a Node in a Kd tree
    private static class Node {
        private final Point2D data;
        private Node left;
        private Node right;
        private final int nodeLevel;

        private Node(Point2D data, int nodeLevel, Node left, Node right) {
            this.data = data;
            this.nodeLevel = nodeLevel;
            this.left = left;
            this.right = right;
        }

        public static Node create(Point2D data, int nodeLevel, Node left, Node right) {
            return new Node(data, nodeLevel, left, right);
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        String filename = args[0];
        In in = new In(filename);
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdTree.insert(p);
        }

        Point2D res = kdTree.nearest(new Point2D(0.46, 0.53));
        StdOut.println(res);
    }
}