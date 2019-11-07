import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    final private WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        String outcast = null;
        int max = -1;
        for (String self : nouns) {
            int sum = sumDistances(self, nouns);
            if (sum > max) {
                outcast = self;
                max = sum;
            }
        }
        return outcast;
    }

    private int sumDistances(String self, String[] nouns) {
        int sum = 0;
        for (String other : nouns) sum += wordnet.distance(self, other);
        return sum;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
