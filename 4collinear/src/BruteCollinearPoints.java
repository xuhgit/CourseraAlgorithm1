import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {
    private final Point[] inputPoints;
    private LineSegment[] foundSegments;
    private int segmentsCount;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        checkInputs(points);
        this.inputPoints = points.clone();
        this.foundSegments = new LineSegment[2];
        this.segmentsCount = 0;

        Arrays.sort(this.inputPoints);
        Arrays.sort(this.inputPoints, new Point(0, 0).slopeOrder());

        for (int i = 0; i < this.inputPoints.length-3; i++) {
            for (int j = i+1; j < this.inputPoints.length; j++) {
                for (int k = j+1; k < this.inputPoints.length; k++) {
                    for (int h = k+1; h < this.inputPoints.length; h++) {
                        double slope1 = this.inputPoints[i].slopeTo(this.inputPoints[j]);
                        double slope2 = this.inputPoints[i].slopeTo(this.inputPoints[k]);
                        double slope3 = this.inputPoints[i].slopeTo(this.inputPoints[h]);
                        if (Double.compare(slope1, slope2) == 0 && Double.compare(slope2, slope3) == 0) {
                            addToArray(new LineSegment(this.inputPoints[i], this.inputPoints[h]));
                        }
                    }
                }
            }
        }
    }

    private void checkInputs(Point[] points) {
        if (points == null) throw new IllegalArgumentException("The entry is null");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("null in entry");
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) throw new IllegalArgumentException("null in entry");
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Duplicate entries in given points.");
                }
            }
        }
    }

    private void addToArray(LineSegment line) {
        if (line == null) throw new IllegalArgumentException();
        if (this.segmentsCount == this.foundSegments.length) resize(2 * this.foundSegments.length);
        this.foundSegments[segmentsCount++] = line;
    }

    private void resize(int capacity) {
        LineSegment[] temp = new LineSegment[capacity];
        for (int i = 0; i < this.segmentsCount; i++) {
            temp[i] = this.foundSegments[i];
        }
        this.foundSegments = temp;
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.segmentsCount;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] res = new LineSegment[this.segmentsCount];
        for (int i = 0; i < this.segmentsCount; i++) {
            res[i] = this.foundSegments[i];
        }
        return res;
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
