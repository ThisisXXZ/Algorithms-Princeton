// to handle the backwash problem, one could simply create another
// uf to check whether the node is connected to the virtual head.

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int N;
    private int count;

    private WeightedQuickUnionUF uf;
    private boolean[][] grid;
    private int[][] id;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        N = n;
        count = 0;
        uf = new WeightedQuickUnionUF(n * n + 2);   // nodes 0-n*n+1 are needed
        grid = new boolean[n + 1][n + 1];
        id = new int[n + 1][n + 1];

        for (int i = 1; i <= N; ++i) {
            for (int j = 1; j <= N; ++j) {
                grid[i][j] = false;
                id[i][j] = (i - 1) * N + j;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > N || col < 1 || col > N) {
            throw new IllegalArgumentException();
        }
        grid[row][col] = true;
        ++count;
        if (row == 1) {
            uf.union(0, id[row][col]);
        } else if (grid[row - 1][col]) {
            uf.union(id[row - 1][col], id[row][col]);
        }
        if (row == N) {
            uf.union(N * N + 1, id[row][col]);
        } else if (grid[row + 1][col]) {
            uf.union(id[row + 1][col], id[row][col]);
        }
        if (col > 1 && grid[row][col - 1]) {
            uf.union(id[row][col - 1], id[row][col]);
        }
        if (col < N && grid[row][col + 1]) {
            uf.union(id[row][col + 1], id[row][col]);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > N || col < 1 || col > N) {
            throw new IllegalArgumentException();
        }
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > N || col < 1 || col > N) {
            throw new IllegalArgumentException();
        }
        return uf.find(0) == uf.find(id[row][col]);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(0) == uf.find(N * N + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        
    }
}