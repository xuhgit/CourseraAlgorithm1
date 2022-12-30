/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.LinkedList;
import java.util.Queue;

public class PercolationBFS {
    static int block = 0;
    static int open = 1;
    int[][] grid;
    int n;
    WeightedQuickUnionUF wquf;
    static class pair {
        int row, col;
        public pair(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    static int dRow[] = { -1, 0, 1, 0 };
    static int dCol[] = { 0, 1, 0, -1 };

    boolean isValid(boolean visited[][], int row, int col) {
        if (!((0 < row && row < this.n + 1) && (0 < col && col < this.n + 1))) {
            return false;
        }
        if (visited[row][col])
            return false;
        return isOpen(row, col);
    }

    // creates n-by-n grid, with all sites initially blocked
    public PercolationBFS(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n should be larger than 0");
        this.grid = new int[n + 1][n + 1];
        this.n = n;
        this.wquf = new WeightedQuickUnionUF(n);
    }

    public boolean isValid(int row, int col) {
        return 0 < row && row < n + 1 && 0 < col && col < n + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isValid(row, col))
            throw new IllegalArgumentException("row and col should be valid");
        this.grid[row][col] = PercolationBFS.open;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isValid(row, col))
            throw new IllegalArgumentException("row and col should be valid");
        return this.grid[row][col] == PercolationBFS.open;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isValid(row, col))
            throw new IllegalArgumentException("row and col should be valid");
        boolean[][] visited = new boolean[this.n + 1][this.n + 1];
        Queue<pair> q = new LinkedList<pair>();
        q.add(new pair(row, col));
        visited[row][col] = true;

        while (!q.isEmpty()) {
            pair cell = q.peek();
            int x = cell.row;
            int y = cell.col;
            if (x == 1) {
                return true;
            }
            visited[x][y] = true;

            q.remove();

            for (int i = 0; i < 4; i++) {
                int nx = x + PercolationBFS.dRow[i];
                int ny = y + PercolationBFS.dCol[i];
                if (!isValid(visited, nx, ny))
                    continue;

                q.add(new pair(nx, ny));
                visited[nx][ny] = true;
            }
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int opennums = 0;
        for (int i = 1; i < this.n + 1; i++) {
            for (int j = 1; j < this.n + 1; j++) {
                if (this.grid[i][j] == PercolationBFS.open) {
                    opennums += 1;
                }
            }
        }
        return opennums;
    }

    // does the system percolate?
    public boolean percolates() {
        for (int j = 1; j < this.n + 1; j++) {
            if (!isOpen(this.n, j))
                continue;
            if (isFull(this.n, j))
                return true;
        }
        return false;
    }

    // test client (optional)
    public static void main(String[] args) {
        int grid[][] = { { 0, 0, 0, 0, 0, 0, 0, 0, 0},
                         { 0, block, block, open, open, open, block, block, block},
                         { 0, open, block, block, open, open, open, open, open },
                         { 0, open, open, open, block, block, open, open, block },
                         { 0, block, block, open, open, block, open, open, open },
                         { 0, block, open, open, open, block, open, open, block },
                         { 0, block, open, block, block, block, block, open, open},
                         { 0, open, block, open, block, open, open, open, open },
                         { 0, open, open, open, open, block, block, open, block },
                         };
        PercolationBFS perc = new PercolationBFS(8);
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (grid[i][j] == PercolationBFS.open) {
                    perc.open(i, j);
                }
            }
        }
        StdOut.println(perc.percolates());
    }
}
