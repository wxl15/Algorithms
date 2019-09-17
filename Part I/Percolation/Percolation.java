import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int n;
    private int sites;
    private int numOpenSites;
    private boolean[] grid;
    private WeightedQuickUnionUF id, backwash;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("input out of range");
        }

        this.n = n;
        sites = n * n;
        numOpenSites = 0;

        // first and last indexes are for virtual top and bottom
        id = new WeightedQuickUnionUF(sites + 2);
        // remove virtual bottom to deal with backwash
        backwash = new WeightedQuickUnionUF(sites + 1);
        // all sites are blocked, first index is for convinience when converting
        grid = new boolean[sites + 1];
    }

    // 2-d index to 1-d
    private int coordToIndex(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IllegalArgumentException("input out of range");
        }

        return n * (row - 1) + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IllegalArgumentException("input out of range");
        }

        int index = coordToIndex(row, col);
        if (!isOpen(row, col)) {
            grid[index] = true;
            numOpenSites++;
        }

        // union with adjacent open sites
        if (row > 1 && isOpen(row - 1, col)) {
            id.union(index, coordToIndex(row - 1, col));
            backwash.union(index, coordToIndex(row - 1, col));
        }
        if (row < n && isOpen(row + 1, col)) {
            id.union(index, coordToIndex(row + 1, col));
            backwash.union(index, coordToIndex(row + 1, col));
        }
        if (col > 1 && isOpen(row, col - 1)) {
            id.union(index, coordToIndex(row, col - 1));
            backwash.union(index, coordToIndex(row, col - 1));
        }
        if (col < n && isOpen(row, col + 1)) {
            id.union(index, coordToIndex(row, col + 1));
            backwash.union(index, coordToIndex(row, col + 1));
        }

        // union with top/bottom
        if (row == 1) {
            id.union(index, 0);
            backwash.union(index, 0);
        }
        if (row == n) id.union(index, sites + 1);
    }


    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int index = coordToIndex(row, col);
        return grid[index];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int index = coordToIndex(row, col);
        return backwash.connected(index, 0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return id.connected(0, sites + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
