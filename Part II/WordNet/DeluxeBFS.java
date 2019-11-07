import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

public class DeluxeBFS {
    final private Digraph G;

    public DeluxeBFS(Digraph G) {
        this.G = G;
    }

    public int[] bfs(int v) {
        check(v);

        int[] dist = new int[G.V()];
        for (int i = 0; i < G.V(); i++) dist[i] = -1;      // -1 if cannot reach
        dist[v] = 0;

        Queue<Integer> q = new Queue<>();
        q.enqueue(v);
        while (!q.isEmpty()) {
            int w = q.dequeue();
            for (int x : G.adj(w)) {
                int d = dist[w] + 1;
                if (dist[x] < 0) {
                    dist[x] = d;
                    q.enqueue(x);
                }
            }
        }
        return dist;
    }

    public int[] bfsSecond(int w, int[] dist_v) {
        check(w);
        int a = -1, min = Integer.MAX_VALUE;
        if (dist_v[w] >= 0) {               // case w is an ancestor of v
            a = w;
            min = dist_v[w];
        }

        int[] dist = new int[G.V()];
        for (int i = 0; i < G.V(); i++) dist[i] = -1;      // -1 if cannot reach
        dist[w] = 0;

        Queue<Integer> q = new Queue<>();
        q.enqueue(w);
        while (!q.isEmpty()) {
            int x = q.dequeue();
            for (int y : G.adj(x)) {
                int d = dist[x] + 1;
                if (dist[y] < 0) {
                    dist[y] = d;
                    q.enqueue(y);
                    int dv = dist_v[y], sum = d + dv;
                    if (dv >= 0 && sum < min) {
                        a = y;
                        min = sum;
                    }
                }
            }
        }
        return new int[]{a, min};
    }

    public int[] bfs(Iterable<Integer> v) {
        int[] dist = new int[G.V()];
        for (int i = 0; i < G.V(); i++) dist[i] = -1;      // -1 if cannot reach

        Queue<Integer> q = new Queue<>();
        for (Integer i : v) {
            check(i);
            q.enqueue(i);
            dist[i] = 0;
        }
        while (!q.isEmpty()) {
            int w = q.dequeue();
            for (int x : G.adj(w)) {
                int d = dist[w] + 1;
                if (dist[x] < 0) {
                    dist[x] = d;
                    q.enqueue(x);
                }
            }
        }
        return dist;
    }

    public int[] bfsSecond(Iterable<Integer> w, int[] dist_v) {
        int[] dist = new int[G.V()];
        for (int i = 0; i < G.V(); i++) dist[i] = -1;      // -1 if cannot reach

        int a = -1, min = Integer.MAX_VALUE;
        Queue<Integer> q = new Queue<>();
        for (Integer i : w) {
            check(i);
            // case some x in w is an ancestor of some y in v
            int dv = dist_v[i];
            if (dv >= 0 && dv < min) {
                a = i;
                min = dv;
            }
            q.enqueue(i);
            dist[i] = 0;
        }

        while (!q.isEmpty()) {
            int x = q.dequeue();
            for (int y : G.adj(x)) {
                int d = dist[x] + 1;
                if (dist[y] < 0) {
                    dist[y] = d;
                    q.enqueue(y);
                    int dv = dist_v[y], sum = d + dv;
                    if (dv >= 0 && sum < min) {
                        a = y;
                        min = sum;
                    }
                }
            }
        }
        return new int[]{a, min};
    }

    private void check(Integer v) {
        if (v == null || v < 0 || v >= G.V()) throw new IllegalArgumentException();
    }
}
