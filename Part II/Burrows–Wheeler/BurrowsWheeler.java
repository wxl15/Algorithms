import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String input = BinaryStdIn.readString();
        int n = input.length();
        CircularSuffixArray csa = new CircularSuffixArray(input);

        for (int i = 0; i < n; i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }

        for (int i = 0; i < n; i++) {
            if (csa.index(i) == 0) BinaryStdOut.write(input.charAt(n - 1));
            else BinaryStdOut.write(input.charAt(csa.index(i) - 1));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        char[] t = BinaryStdIn.readString().toCharArray();
        int n = t.length, R = 256;
        char[] h = new char[n];
        int[] count = new int[R + 1], next = new int[n];

        // key-indexed sort t[] -> h[]
        for (int i = 0; i < n; i++) count[t[i] + 1]++;
        for (int r = 0; r < R; r++) count[r + 1] += count[r];
        for (int i = 0; i < n; i++) {
            int j = count[t[i]]++;
            h[j] = t[i];
            next[j] = i;        // simultaneously construct next[]
        }

        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(h[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        String sign = args[0];
        if (sign.equals("-")) transform();
        else if (sign.equals("+")) inverseTransform();
        else throw new IllegalArgumentException();
    }
}
