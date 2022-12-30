import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private final Stack<Board> solutionBoards;
    private boolean isSolvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        isSolvable = false;
        solutionBoards = new Stack<>();
        MinPQ<SearchNode> searchNodes = new MinPQ<>();
        MinPQ<SearchNode> searchNodesTwin = new MinPQ<>();
        Board twin = initial.twin();

        searchNodes.insert(new SearchNode(initial, null));
        searchNodesTwin.insert(new SearchNode(twin, null));
        while (true) {
            SearchNode searchNode = searchNodes.delMin();
            SearchNode searchNodeTwin = searchNodesTwin.delMin();

            if (searchNode.board.isGoal()) {
                isSolvable = true;
                SearchNode current = searchNode;
                while (current != null) {
                    solutionBoards.push(current.board);
                    current = current.prevNode;
                }
                break;
            }

            if (searchNodeTwin.board.isGoal()) {
                isSolvable = false;
                break;
            }

            for (Board board: searchNode.board.neighbors()) {
                if (searchNode.prevNode == null || !searchNode.prevNode.board.equals(board)) {
                    searchNodes.insert(new SearchNode(board, searchNode));
                }
            }

            for (Board boardTwin: searchNodeTwin.board.neighbors()) {
                if (searchNodeTwin.prevNode == null || !searchNodeTwin.prevNode.board.equals(boardTwin)) {
                    searchNodesTwin.insert(new SearchNode(boardTwin, searchNodeTwin));
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return solutionBoards.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        return solutionBoards;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode prevNode;
        private int moves;
        private int manhattan;

        private SearchNode(Board board, SearchNode prevNode) {
            this.board = board;
            this.prevNode = prevNode;
            this.manhattan = board.manhattan();
            if (prevNode == null) moves = 0;
            else moves = prevNode.moves + 1;
        }

        public int compareTo(SearchNode that) {
            int priorityDiff = (this.manhattan + this.moves) - (that.manhattan + that.moves);
            return priorityDiff == 0 ? this.manhattan - that.manhattan : priorityDiff;
        }
    }

}