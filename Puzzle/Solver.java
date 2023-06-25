import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Solver {

    private class SearchNode implements Comparable<SearchNode> {
        public Board board;
        public SearchNode prev;
        public int mht;
        public int step;
        public int priority;
        
        public SearchNode(Board initial, SearchNode previous) {
            board = initial;
            prev = previous;
            mht = initial.manhattan();
            if (previous == null) {
                step = 0;
            } else {
                step = previous.step + 1;
            }
            priority = mht + step;
        }

        public boolean isGoal() {
            return board.isGoal();
        }

        public int compareTo(SearchNode that) {
            if (priority < that.priority)    return -1;
            if (priority > that.priority)    return 1; 
            if (mht < that.mht)              return -1;
            if (mht > that.mht)              return 1;
            return 0;
        }
    }

    private List<Board> ans;

    private boolean solvable;
    private boolean solvableTwin;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        MinPQ<SearchNode> pqFir = new MinPQ<SearchNode>();
        MinPQ<SearchNode> pqSec = new MinPQ<SearchNode>();
        ans = new ArrayList<>();
        solvable = false;
        solvableTwin = false;

        pqFir.insert(new SearchNode(initial, null));
        pqSec.insert(new SearchNode(initial.twin(), null));

        while (!solvable && !solvableTwin) {
            SearchNode topFir = pqFir.delMin();
            if (topFir.isGoal()) {
                solvable = true;
                SearchNode seq = topFir;
                while (seq != null) {
                    ans.add(seq.board);
                    seq = seq.prev;
                }
                Collections.reverse(ans);
                break;
            }
            for (Board nb : topFir.board.neighbors()) {
                if (topFir.prev != null && nb.equals(topFir.prev.board))   continue;
                pqFir.insert(new SearchNode(nb, topFir));
            }

            SearchNode topSec = pqSec.delMin();
            if (topSec.isGoal()) {
                solvableTwin = true;
                break;
            }
            for (Board nb : topSec.board.neighbors()) {
                if (topSec.prev != null && nb.equals(topSec.prev.board))   continue;
                pqSec.insert(new SearchNode(nb, topSec));
            }
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solvable) 
            return ans.size() - 1;
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solvable)
            return ans;
        return null;
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
}