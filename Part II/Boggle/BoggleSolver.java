import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver {
    private BoggleBoard B;
    private Trie dict;
    private int[] adjs = new int[]{-1, 0, 1};

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        char[] fullList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        dict = new Trie(fullList);
        for (String s : dictionary) dict.put(s);

    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        B = board;
        HashSet<String> validWords = new HashSet<>();
        for (int i = 0; i < B.rows(); i++) {
            for (int j = 0; j < B.cols(); j++) {
                dfs(validWords, "", new boolean[B.rows()][B.cols()], i, j);
            }
        }
        return validWords;
    }

    private void dfs(HashSet<String> validWords, String current, boolean[][] visited, int i, int j) {
        if (i < 0 || j < 0 || i >= B.rows() || j >= B.cols() || visited[i][j]) return;

        char c = B.getLetter(i, j);
        if (c == 'Q') current += "QU";
        else current += c;

        if (!dict.hasPrefix(current)) return;
        if (dict.contains(current) && current.length() >= 3)
            validWords.add(current);

        visited[i][j] = true;
        for (int i_ : adjs) {
            for (int j_ : adjs) {
                dfs(validWords, current, visited, i + i_, j + j_);
            }
        }
        visited[i][j] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dict.contains(word)) return 0;
        int len = word.length();
        if (len < 3) return 0;
        if (len <= 4) return 1;
        if (len == 5) return 2;
        if (len == 6) return 3;
        if (len == 7) return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0, count = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
            count++;
        }
        StdOut.println("Score = " + score + "  Count = " + count);
    }
}

