import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    final private ArrayList<String> synsetsId;     // id to synset
    final private HashMap<String, Bag<Integer>> synsetsNoun;      // noun to synset id
    final private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        synsetsId = new ArrayList<>();
        synsetsNoun = new HashMap<>();

        // parse synsets/vertices
        In S = new In(synsets);
        int N = 0;
        while (!S.isEmpty()) {
            N++;
            String[] curr = S.readLine().split(",");
            int id = Integer.parseInt(curr[0]);
            String synset = curr[1];
            synsetsId.add(synset);
            for (String word : synset.split(" ")) {
                if (synsetsNoun.containsKey(word)) {
                    synsetsNoun.get(word).add(id);
                } else {
                    Bag<Integer> bag = new Bag<>();
                    bag.add(id);
                    synsetsNoun.put(word, bag);
                }
            }
        }

        // parse hypernyms/edges and create graph
        Digraph G = new Digraph(N);
        In H = new In(hypernyms);
        int v = 0, root = -1;
        while (!H.isEmpty()) {
            String[] curr = H.readLine().split(",");
            if (curr.length == 1) {
                if (root == -1) root = v;
                else throw new IllegalArgumentException();      // has more than 1 root
            } else {
                for (int i = 1; i < curr.length; i++) {
                    int w = Integer.parseInt(curr[i]);
                    G.addEdge(v, w);
                }
            }
            v++;
        }

        // if has only 1 root and no directed cycle then the graph is a rooted DAG
        if (root == -1 || new DirectedCycle(G).hasCycle()) throw new IllegalArgumentException();
        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetsNoun.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return synsetsNoun.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return sap.length(synsetsNoun.get(nounA), synsetsNoun.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        int a = sap.ancestor(synsetsNoun.get(nounA), synsetsNoun.get(nounB));
        return synsetsId.get(a);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
    }
}
