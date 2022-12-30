/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private double[] thresholds;
    private int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1)
            throw new IllegalArgumentException("n should be larger than 0");
        if (trials < 1)
            throw new IllegalArgumentException("trials should be larger than 0");
        thresholds = new double[trials];
        this.trials = trials;
        double size = n * n;

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int tmpr = StdRandom.uniformInt(1, n + 1);
                int tmpc = StdRandom.uniformInt(1, n + 1);
                if (perc.isOpen(tmpr, tmpc))
                    continue;
                perc.open(tmpr, tmpc);
            }
            thresholds[i] = perc.numberOfOpenSites() / size;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (trials == 1)
            return Double.NaN;
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev()) / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev()) / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats perstats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        StdOut.printf("mean\t= %f\n", perstats.mean());
        StdOut.printf("stddev\t= %f\n", perstats.stddev());
        StdOut.printf("95%% confidence interval\t= [%f, %f]\n", perstats.confidenceLo(), perstats.confidenceHi());
    }
}
