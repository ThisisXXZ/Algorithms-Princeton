import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    public final static double C = 1.96;

    private double[] res;
    private int t;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        res = new double[trials];
        t = trials;
        for (int i = 0; i < trials; ++i)    res[i] = simulate(n);
    }

    private double simulate(int n) {
        Percolation sim = new Percolation(n);
        while (!sim.percolates()) {
            int row = StdRandom.uniformInt(1, n + 1);
            int col = StdRandom.uniformInt(1, n + 1);
            while (sim.isOpen(row, col)) {
                row = StdRandom.uniformInt(1, n + 1);
                col = StdRandom.uniformInt(1, n + 1);
            }
            sim.open(row, col);
        }
        return (double) sim.numberOfOpenSites() / n / n;
    }


    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(res);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(res);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double lo = mean() - C * stddev() / Math.sqrt(t);
        return lo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double hi = mean() + C * stddev() / Math.sqrt(t);
        return hi;
    }

   // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException();
        }
        PercolationStats perc = new PercolationStats(n, t);
        StdOut.printf("mean                    = %f\n", perc.mean());
        StdOut.printf("stddev                  = %f\n", perc.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", perc.confidenceLo(), perc.confidenceHi());
    }

}