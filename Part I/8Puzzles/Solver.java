import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private boolean isSolvable = true;
    private SearchNode solnNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        MinPQ<SearchNode> pq = new MinPQ<>(), pqTwin = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));
        pqTwin.insert(new SearchNode(initial.twin(), 0, null));

        while (true) {
            SearchNode current = pq.delMin(), currentTwin = pqTwin.delMin();
            System.out.println(current.item.toString());
            if (current.item.isGoal()) {
                solnNode = current;
                break;
            }
            if (currentTwin.item.isGoal()) {
                isSolvable = false;
                break;
            }

            int moves = current.moves + 1, movesTwin = currentTwin.moves + 1;
            for (Board neighbor : current.item.neighbors()) {
                if (moves > 1 && neighbor.equals(current.prev.item)) continue;
                pq.insert(new SearchNode(neighbor, moves, current));
            }
            for (Board neighbor : currentTwin.item.neighbors()) {
                if (moves > 1 && neighbor.equals(currentTwin.prev.item)) continue;
                pqTwin.insert(new SearchNode(neighbor, movesTwin, currentTwin));
            }
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board item;
        private final int moves, priority;
        private SearchNode prev;

        public SearchNode(Board item, int moves, SearchNode prev) {
            this.item = item;
            this.moves = moves;
            this.prev = prev;
            this.priority = this.item.manhattan() + this.moves;
        }

        public int compareTo(SearchNode that) {
            return this.priority - that.priority;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return isSolvable ? solnNode.moves : -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!isSolvable) return null;

        Stack<Board> solution = new Stack<>();
        SearchNode current = solnNode;
        do {
            solution.push(current.item);
            current = current.prev;
        } while (current != null);
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {
//        int[][] a = {{6, 8, 7}, {2, 5, 4}, {3, 0, 1}};
//        int[][] a = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        int[][] a = {{1, 2, 4}, {3, 5, 6}, {7, 8, 0}};
        Board initial = new Board(a);
        Solver solver = new Solver(initial);
        System.out.println(solver.isSolvable);
    }
}
