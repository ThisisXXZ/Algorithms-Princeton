import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.Arrays;

public class BurrowsWheeler {

    private static final int R = 256;

    private static int charAt(String s, int start, int pos) {
        return (int) s.charAt((start + pos) % s.length());
    }
    
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); ++i) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < s.length(); ++i)
            BinaryStdOut.write(charAt(s, csa.index(i), s.length() - 1), 8);
        BinaryStdOut.close();
    }

    public static void inverseTransform() {
        int start = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        int len = t.length();
        
        char[] ch = t.toCharArray();
        Arrays.sort(ch);
        String h  = String.valueOf(ch);

        int[] count = new int[R + 1];
        int[] next = new int[len];
        for (int i = 0; i < len; ++i)
            count[h.charAt(i) + 1]++;
        for (int i = 1; i <= R; ++i)
            count[i] += count[i - 1];
        for (int i = 0; i < len; ++i)
            next[count[t.charAt(i)]++] = i;

        for (int i = 0; i < t.length(); ++i) {
            BinaryStdOut.write(h.charAt(start), 8);
            start = next[start];
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-"))
            transform();
        if (args[0].equals("+"))
            inverseTransform();
    }
}