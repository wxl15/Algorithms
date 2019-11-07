import java.util.HashMap;

public class Trie {
    private int R;
    private HashMap<Character, Integer> charToId;
    private Node root;

    private class Node {
        boolean isWord = false;
        Node[] next = new Node[R];
    }

    public Trie(char[] fullList) {
        R = fullList.length;
        root = new Node();
        charToId = new HashMap<>();
        for (int i = 0; i < R; i++) {
            charToId.put(fullList[i], i);
        }
    }

    public void put(String key) {
        root = put(root, key, 0);
    }

    private Node put(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.isWord = true;
            return x;
        }
        int i = charToId.get(key.charAt(d));
        x.next[i] = put(x.next[i], key, d + 1);
        return x;
    }

    public boolean contains(String key) {
        Node last = getNode(root, key, 0);
        if (last == null) return false;
        return last.isWord;
    }

    private Node getNode(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int i = charToId.get(key.charAt(d));
        return getNode(x.next[i], key, d + 1);
    }

    public boolean hasPrefix(String prefix) {
        Node last = getNode(root, prefix, 0);
        return last != null;
    }

    public static void main(String[] args) {
        Trie T = new Trie("ABCDEFGHIJKLMNOPQRSTVWXYZ".toCharArray());
        T.put("ABDEE");
        System.out.println(T.hasPrefix("ABD"));
        System.out.println(T.contains("ABDEE"));
    }
}
