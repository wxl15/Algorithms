import java.util.Arrays;

public class CircularSuffixArray {
    private int n;
    private int[] index;
    private String s;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        this.s = s;
        n = this.s.length();
        index = new int[n];
        for (int i = 0; i < n; i++) index[i] = i;
        // modified Quick3string
        sort(index);
    }

    private void sort(int[] a) {
        sort(a, 0, a.length - 1, 0);
    }

    private void sort(int[] a, int lo, int hi, int d) {
        if (lo >= hi) return;
        int lt = lo, gt = hi, v = charAt(s, a[lo], d), i = lo + 1;
        while (i <= gt) {
            int t = charAt(s, a[i], d);
            if (t < v) exch(a, lt++, i++);
            else if (t > v) exch(a, i, gt--);
            else i++;
        }
        sort(a, lo, lt - 1, d);
        if (v >= 0) sort(a, lt, gt, d + 1);
        sort(a, gt + 1, hi, d);
    }

    private int charAt(String s, int j, int d) {
        int sum = j + d;
        if (sum < n) return s.charAt(sum);
        else if (d < n) return s.charAt(sum - n);
        else return -1;     // d cannot exceed n
    }

    private void exch(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // length of s
    public int length() {
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException();
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ZLXTVOAWRT");
        System.out.println(Arrays.toString(csa.index));
        System.out.println(csa.length());
    }
}
