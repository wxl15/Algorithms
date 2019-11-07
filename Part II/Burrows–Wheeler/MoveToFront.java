import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private final static int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        String input = BinaryStdIn.readString();
        char[] code = new char[R];
        for (int i = 0; i < R; i++) {
            code[i] = (char) i;
        }

        for (int i = 0; i < input.length(); i++) {
            int j = getIndex(code, input.charAt(i));
            BinaryStdOut.write((char) j);
            moveToFront(code, j);
        }
        BinaryStdOut.close();
    }

    private static int getIndex(char[] a, char c) {
        for (int i = 0; i < a.length; i++) if (a[i] == c) return i;
        return -1;
    }

    private static void moveToFront(char[] a, int j) {
        char temp = a[j];
        for (int i = j; i >= 1; i--) {
            a[i] = a[i - 1];
        }
        a[0] = temp;
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] code = new char[R];
        for (int i = 0; i < R; i++) {
            code[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            char j = BinaryStdIn.readChar();
            BinaryStdOut.write(code[j]);
            moveToFront(code, j);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        String sign = args[0];
        if (sign.equals("-")) encode();
        else if (sign.equals("+")) decode();
        else throw new IllegalArgumentException();
    }
}
