import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

public class BoggleSolver {
    
    private static final int R = 26;
    private final int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
    private final int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};

    private Node root;
    private BoggleBoard arr;
    private boolean[][] marked;
    private int r;
    private int c;

    private static class Node {
        private boolean isString;
        private String str;
        private Node[] next = new Node[R];
    }

    public BoggleSolver(String[] dictionary) {
        for (String s : dictionary) add(s);
    }

    private void add(String s) {
        root = add(root, s, 0);
    }

    private Node add(Node x, String s, int d) {
        if (x == null)  x = new Node();
        if (d == s.length()) {
            x.isString = true;
            x.str = s;
        } else {
            int ch = s.charAt(d) - 'A';
            x.next[ch] = add(x.next[ch], s, d+1);
        }
        return x;
    }

    private boolean contains(String s) {
        Node x = get(root, s, 0);
        if (x == null) return false;
        return x.isString;
    }

    private Node get(Node x, String s, int d) {
        if (x == null) return null;
        if (d == s.length()) return x;
        int ch = s.charAt(d) - 'A';
        return get(x.next[ch], s, d+1);
    }

    private void depthFirstSearch(int x, int y, Node u, TreeSet<String> words) {
        if (x < 0 || x > r-1 || y < 0 || y > c-1)   return;
        if (marked[x][y])   return;

        marked[x][y] = true; 
        char curChar = arr.getLetter(x, y);
        int ch = curChar - 'A';

        if (u.next[ch] == null) {
            marked[x][y] = false;
            return;
        }
        u = u.next[ch];
        if (curChar == 'Q') {
            ch = 'U' - 'A';
            if (u.next[ch] == null) {
                marked[x][y] = false;
                return;
            }
            u = u.next[ch];
        }
        if (u.isString && !words.contains(u.str) && u.str.length() >= 3)
            words.add(u.str);
        
        boolean prune = true;
        for (int i = 0; i < R; ++i) {
            if (u.next[i] != null) {
                prune = false;
                break;
            }
        }
        if (prune) {
            marked[x][y] = false;
            return;
        }

        for (int i = 0; i < 8; ++i)
            depthFirstSearch(x + dx[i], y + dy[i], u, words);
        marked[x][y] = false;
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        r = board.rows();
        c = board.cols();
        arr = board;
        marked = new boolean[r][c];
        TreeSet<String> res = new TreeSet<String>();
        for (int i = 0; i < r; ++i)
            for (int j = 0; j < c; ++j)
                depthFirstSearch(i, j, root, res);
        return res;
    }

    public int scoreOf(String word) {
        if (!contains(word))        return 0;
        int len = word.length();
        if (len == 3 || len == 4)   return 1;
        if (len == 5)               return 2;
        if (len == 6)               return 3;
        if (len == 7)               return 5;
        if (len >= 8)               return 11;
        return 0;
    }

    public static void main(String[] args) {
        In in = new In("Boggle/dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("Boggle/board-points26539.txt");
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
