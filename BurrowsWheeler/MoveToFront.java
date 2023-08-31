import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256;

    public static void encode() {

        int[] ind = new int[R];
        for (int i = 0; i < R; ++i) ind[i] = i;

        while (!BinaryStdIn.isEmpty()) {
            char tc = BinaryStdIn.readChar();
            int c   = (int) tc;
            int pos = 0;

            for (int i = 0; i < R; ++i) {
                if (ind[i] == c) {
                    pos = i;
                    break;
                }
            }

            BinaryStdOut.write(pos, 8);
            
            for (int i = pos; i > 0; --i)
                ind[i] = ind[i - 1];
            ind[0] = c;
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        int[] ind = new int[R];
        for (int i = 0; i < R; ++i) ind[i] = i;

        while (!BinaryStdIn.isEmpty()) {
            char tc = BinaryStdIn.readChar();
            int c   = (int) tc;

            BinaryStdOut.write(ind[c], 8);

            int curChar = ind[c];
            for (int i = c; i > 0; --i)
                ind[i] = ind[i - 1];
            ind[0] = curChar;
        }
        BinaryStdOut.close();
    }


    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        if (args[0].equals("+"))
            decode();
    }
}