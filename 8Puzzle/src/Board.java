import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public final class Board {
    private final int n;
    private final int[][] board;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException();
        this.n = tiles.length;
        this.board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder str = new StringBuilder();
        int dim = dimension();
        str.append(dim + "\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (j != dim - 1)
                    str.append(board[i][j] + "\t");
                else
                    str.append(board[i][j] + "\n");
            }
        }
        return str.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int dis = 0;
        int dim = dimension();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (!(i == dim - 1 && j == dim - 1)) {
                    if (board[i][j] != i * dim + j + 1) dis += 1;
                }
            }
        }
        return dis;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int dis = 0;
        int dim = dimension();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                int tr, tc;
                if (board[i][j] != 0) {
                    tr = (board[i][j] - 1) / dim;
                    tc = board[i][j] - 1 - tr * dim;
                    dis += Math.abs(tr - i) + Math.abs(tc - j);
                }
            }
        }
        return dis;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        int dim = that.dimension();
        if (dim != this.dimension()) return false;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (this.board[i][j] != that.board[i][j])
                    return false;
            }
        }
        return true;
    }

    private int blankPosition() {
        int dim = dimension();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (this.board[i][j] == 0)
                    return i * dim + j;
            }
        }
        return -1;
    }

    private int[][] copy() {
        int dim = dimension();
        int[][] newboard = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                newboard[i][j] = board[i][j];
            }
        }
        return newboard;
    }

    private int[][] swap(int or, int oc, int nr, int nc) {
        int[][] newboard = copy();
        int tmp = newboard[or][oc];
        newboard[or][oc] = newboard[nr][nc];
        newboard[nr][nc] = tmp;
        return newboard;
    }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<>();
        int dim = dimension();
        int blankPos = blankPosition();
        int bx = blankPos / dim;
        int by = blankPos - bx * dim;
        int[][] dxdys = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        for (int[] dxdy : dxdys) {
            int nx = bx + dxdy[0];
            int ny = by + dxdy[1];
            if (!(-1 < nx && nx < dim && -1 < ny && ny < dim))
                continue;
            else
                neighbors.push(new Board(swap(bx, by, nx, ny)));
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinTiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twinTiles[i][j] = board[i][j];
            }
        }

        int tmpTile = 0;
        int tmpi = 0;
        int tmpj = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (twinTiles[i][j] == 0)
                    continue;
                if (tmpTile == 0) {
                    tmpTile = twinTiles[i][j];
                    tmpi = i;
                    tmpj = j;
                } else {
                    twinTiles[tmpi][tmpj] = twinTiles[i][j];
                    twinTiles[i][j] = tmpTile;
                    return new Board(twinTiles);
                }
            }
        }
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        Board board = new Board(new int[][]{{1, 2, 3}, {4, 0, 6}, {7, 8, 5}});
        StdOut.println(board);
        for (Board board1 :
                board.neighbors()) {
            StdOut.println(board1);
        }
    }

}