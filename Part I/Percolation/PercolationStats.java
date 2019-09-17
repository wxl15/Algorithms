import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int trials;
    private double mean;
    private double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("input out of range");
        }

        double[] fractions = new double[trials];
        double sites = n * n;
        this.trials = trials;

        for (int i = 0; i < trials; i++) {
            double fraction;
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                perc.open(row, col);
            }

            fraction = perc.numberOfOpenSites() / sites;
            fractions[i] = fraction;
        }

        mean = StdStats.mean(fractions);
        stddev = StdStats.stddev(fractions);
    }

    private double interval() {
        return (1.96 * stddev()) / Math.sqrt(trials);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - interval();
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + interval();
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);

        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = "
                                   + "[" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }

}
