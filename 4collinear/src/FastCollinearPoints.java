import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {
    private final Point[] inputPoints;
    private LineSegment[] foundSegments;
    private int segmentsCount;

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points
        if (points == null) throw new IllegalArgumentException("The entry is null");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("null in entry");
        }
        this.inputPoints = points.clone();
        Arrays.sort(inputPoints);
        for (int i = 1; i < inputPoints.length; i++) {
            if (inputPoints[i - 1].equals(inputPoints[i]))
                throw new IllegalArgumentException("Duplicate entries in given points.");
        }

        this.foundSegments = new LineSegment[2];
        this.segmentsCount = 0;

        for (int i = 0; i < inputPoints.length; i++) {
            Arrays.sort(inputPoints);
            Arrays.sort(inputPoints, inputPoints[i].slopeOrder());

            for (int p = 0, first = 1, last = 2; last < inputPoints.length; last++) {
                // find last collinear to p point
                while (last < inputPoints.length
                        && Double.compare(inputPoints[p].slopeTo(inputPoints[first]),
                            inputPoints[p].slopeTo(inputPoints[last])) == 0) {
                    last++;
                }
                // if found at least 3 elements, make segment if it's unique
                if (last - first >= 3 && inputPoints[p].compareTo(inputPoints[first]) < 0) {
                    addToArray(new LineSegment(inputPoints[p], inputPoints[last - 1]));
                }
                // try to find nex
                first = last;   
            }
        }

    }

    public int numberOfSegments() {
        // the number of line segments
        return this.segmentsCount;
    }

    public LineSegment[] segments() {
        // the line segments
        LineSegment[] res = new LineSegment[this.segmentsCount];
        for (int i = 0; i < this.segmentsCount; i++) {
            res[i] = this.foundSegments[i];
        }
        return res;
    }

    private Point[] addLine(Point p, int start, int diffindex) {
        Point[] res = new Point[diffindex - start + 1];
        res[0] = p;
        int ind = 1;
        for (int i = start; i < diffindex; i++) {
            res[ind++] = this.inputPoints[i];
        }

        Arrays.sort(res);
        return new Point[] {res[0], res[res.length - 1]};
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
