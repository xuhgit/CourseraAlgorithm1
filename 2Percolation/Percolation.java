/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] opened;
    private int opennum = 0;
    private int top = 0; // virtual top site
    private int bottom; // virtual bottom site
    private int n;
    private WeightedQuickUnionUF wquf; // includes virtual top and bottom
    private WeightedQuickUnionUF wqufaux; // includes virtual top

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1)
            throw new IllegalArgumentException("dimensions of grid should be larger than 0.");
        opened = new boolean[n][n];
        bottom = n * n + 1;
        this.n = n;
        wquf = new WeightedQuickUnionUF(n * n + 2);
        wqufaux = new WeightedQuickUnionUF(n * n + 1);
    }

    // two dimensions row, col to array index
    private int getQuickUnionFindInd(int row, int col) {
        return n * (row - 1) + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > n)
            throw new IllegalArgumentException("row should within the range [0, n).");
        if (col < 1 || col > n)
            throw new IllegalArgumentException("col should within the range [0, n)");
        if (isOpen(row, col)) return;

        opened[row - 1][col - 1] = true;
        opennum += 1;
        if (row == 1) {
            wquf.union(getQuickUnionFindInd(row, col), top);
            wqufaux.union(getQuickUnionFindInd(row, col), top);
        }
        if (row == n)
            wquf.union(getQuickUnionFindInd(row, col), bottom);
        if (col > 1 && isOpen(row, col - 1)) {
            wquf.union(getQuickUnionFindInd(row, col), getQuickUnionFindInd(row, col - 1));
            wqufaux.union(getQuickUnionFindInd(row, col), getQuickUnionFindInd(row, col - 1));
        }
        if (col < n && isOpen(row, col + 1)) {
            wquf.union(getQuickUnionFindInd(row, col), getQuickUnionFindInd(row, col + 1));
            wqufaux.union(getQuickUnionFindInd(row, col), getQuickUnionFindInd(row, col + 1));
        }
        if (row > 1 && isOpen(row - 1, col)) {
            wquf.union(getQuickUnionFindInd(row, col), getQuickUnionFindInd(row - 1, col));
            wqufaux.union(getQuickUnionFindInd(row, col), getQuickUnionFindInd(row - 1, col));
        }
        if (row < n && isOpen(row + 1, col)) {
            wquf.union(getQuickUnionFindInd(row, col), getQuickUnionFindInd(row + 1, col));
            wqufaux.union(getQuickUnionFindInd(row, col), getQuickUnionFindInd(row + 1, col));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n)
            throw new IllegalArgumentException("row should within the range [0, n).");
        if (col < 1 || col > n)
            throw new IllegalArgumentException("col should within the range [0, n)");
        return opened[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > n)
            throw new IllegalArgumentException("row should within the range [0, n).");
        if (col < 1 || col > n)
            throw new IllegalArgumentException("col should within the range [0, n)");
        return wquf.find(top) == wquf.find(getQuickUnionFindInd(row, col))
                && wqufaux.find(top) == wqufaux.find(getQuickUnionFindInd(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opennum;
    }

    // does the system percolate?
    public boolean percolates() {
        return wquf.find(top) == wquf.find(bottom);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
