import edu.princeton.cs.algs4.Stack;

public class Board {
    private int[][] tiles;
    private int N, iZero, jZero, hamCount = 0, manhCount = 0;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        N = tiles.length;
        this.tiles = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                // copy
                this.tiles[i][j] = tiles[i][j];

                // position of blank
                if (tiles[i][j] == 0) {
                    iZero = i;
                    jZero = j;
                }

                // hamming and manhattan method
                else if (tiles[i][j] != i * N + j + 1) {
                    hamCount++;
                    manhCount += dManh(i, j);
                }
            }
        }

    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (j == N - 1) s.append(tiles[i][j]).append("\n");
                else s.append(tiles[i][j]).append(" ");
            }
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return N;
    }

    // number of tiles out of place
    public int hamming() {
        return hamCount;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhCount;
    }

    private int dManh(int i, int j) {
        int value = tiles[i][j] - 1, iTarget = value / N, jTarget = value % N;
        return Math.abs(iTarget - i) + Math.abs(jTarget - j);
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamCount == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (getClass() != y.getClass()) return false;
        return toString().equals(y.toString());
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<>();
        if (iZero > 0) {
            int[][] neighbor = copy(tiles);
            exch(neighbor, iZero - 1, jZero, iZero, jZero);
            neighbors.push(new Board(neighbor));
        }
        if (iZero < N - 1) {
            int[][] neighbor = copy(tiles);
            exch(neighbor, iZero + 1, jZero, iZero, jZero);
            neighbors.push(new Board(neighbor));
        }
        if (jZero > 0) {
            int[][] neighbor = copy(tiles);
            exch(neighbor, iZero, jZero - 1, iZero, jZero);
            neighbors.push(new Board(neighbor));
        }
        if (jZero < N - 1) {
            int[][] neighbor = copy(tiles);
            exch(neighbor, iZero, jZero + 1, iZero, jZero);
            neighbors.push(new Board(neighbor));
        }
        return neighbors;
    }

    private void exch(int[][] a, int i, int j, int k, int l) {
        int temp = a[i][j];
        a[i][j] = a[k][l];
        a[k][l] = temp;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twin = copy(tiles);
        if (iZero == 0) exch(twin, 1, 0, 1, 1);
        else exch(twin, 0, 0, 0, 1);
        return new Board(twin);
    }

    // brute algorithm to determine solvability
//    private boolean isSolvable() {
//        int[][] temp = new int[N][N], goal = new int[N][N];
//        int input = 1;
//
//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < N; j++) {
//                temp[i][j] = tiles[i][j];
//                goal[i][j] = input++;
//            }
//        }
//
//        // an equivalent goal form by moving blank with a L-shaped path
//        int k = N - 1, l = N - 1;
//        while (l > jZero) goal[k][l] = goal[k][--l];
//        while (k > iZero) goal[k][l] = goal[--k][l];
//        goal[iZero][jZero] = 0;
//
//        int swaps = 0;
//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < N; j++) {
//                int g = goal[i][j];
//                if (temp[i][j] != g) {
//                    int[] id = indexOf(temp, g);
//                    exch(temp, i, j, id[0], id[1]);
//                    swaps++;
//                }
//            }
//        }
//        return swaps % 2 == 0;
//    }
//
//    private int[] indexOf(int[][] a, int x) {
//        int[] index = new int[2];
//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < N; j++) {
//                if (a[i][j] == x) {
//                    index[0] = i;
//                    index[1] = j;
//                    return index;
//                }
//            }
//        }
//        return index;
//    }

    private int[][] copy(int[][] a) {
        int n = a.length;
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = a[i][j];
            }
        }
        return copy;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] a = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board = new Board(a);
        for (Board b : board.neighbors()) System.out.println(b.toString());
//        System.out.println(board.twin().toString());
//        System.out.println(board.toString());
//        System.out.println(board.hamming() + " " + board.manhattan());
        System.out.println(board.toString().equals(board.toString()));
    }
}
