import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination {
    private int n, matches, V;
    private int[] w, l, r;
    private int[][] against;
    private boolean[] E;
    private ArrayList<String> teams = new ArrayList<>();
    private HashMap<String, Integer> teamToIndex = new HashMap<>();
    private Bag<String>[] R;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();
        matches = (n - 1) * (n - 2) / 2;
        V = (n - 1) + matches + 2;
        w = new int[n];
        l = new int[n];
        r = new int[n];
        against = new int[n][n];
        E = new boolean[n];
        R = (Bag<String>[]) new Bag[n];

        for (int i = 0; i < n; i++) {
            R[i] = new Bag<String>();
            String team = in.readString();
            teams.add(team);
            teamToIndex.put(team, i);
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                against[i][j] = in.readInt();
            }
        }

        for (int i = 0; i < n; i++) {
            boolean naiveEliminate = false;
            for (int j = 0; j < n; j++) {
                if (w[i] + r[i] - w[j] < 0) {
                    E[i] = true;
                    R[i].add(teams.get(j));
                    naiveEliminate = true;
                    break;
                }
            }
            if (!naiveEliminate) FF(i);
        }
    }

    private void FF(int i) {
        // s = 0, t = V-1, game vertices: 1 to matches, team vertices: matches+1 to V-2
        FlowNetwork G = new FlowNetwork(V);
        int x = 1, top = matches + 1, totalGamesLeft = 0;
        for (int j = 0; j < n; j++) {
            if (j == i) continue;
            int bot = top + 1;
            for (int k = j + 1; k < n; k++) {
                if (k == i) continue;
                int gamesLeft = against[j][k];
                totalGamesLeft += gamesLeft;
                G.addEdge(new FlowEdge(0, x, gamesLeft));   // s -> game, game: top-bot
                G.addEdge(new FlowEdge(x, top, Double.MAX_VALUE));  // game -> top team
                G.addEdge(new FlowEdge(x, bot, Double.MAX_VALUE));  // game -> bot team
                bot++;
                x++;
            }
            top++;
        }
        for (int j = 0; j < n; j++) {
            if (j == i) continue;
            G.addEdge(new FlowEdge(x++, V - 1, w[i] + r[i] - w[j]));    // team -> t
        }

        FordFulkerson ff = new FordFulkerson(G, 0, V - 1);
        if (ff.value() != totalGamesLeft) {
            E[i] = true;
            int y = matches + 1;    // first team vertex
            for (int j = 0; j < n; j++) {
                if (j == i) continue;
                if (ff.inCut(y++)) R[i].add(teams.get(j));
            }
        }
    }

    public int numberOfTeams() {
        return n;
    }

    public Iterable<String> teams() {
        return teams;
    }

    public int wins(String team) {
        check(team);
        return w[teamToIndex.get(team)];
    }

    public int losses(String team) {
        check(team);
        return l[teamToIndex.get(team)];
    }

    public int remaining(String team) {
        check(team);
        return r[teamToIndex.get(team)];
    }

    public int against(String team1, String team2) {
        check(team1);
        check(team2);
        int i = teamToIndex.get(team1), j = teamToIndex.get(team2);
        return against[i][j];
    }

    public boolean isEliminated(String team) {
        check(team);
        return E[teamToIndex.get(team)];
    }

    public Iterable<String> certificateOfElimination(String team) {
        check(team);
        if (!isEliminated(team)) return null;
        return R[teamToIndex.get(team)];
    }

    private void check(String team) {
        if (!teamToIndex.containsKey(team)) throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
