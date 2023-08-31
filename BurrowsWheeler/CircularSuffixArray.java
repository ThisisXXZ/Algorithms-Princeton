import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    
    private static final int CUTOFF = 12;

    private final int len;
    private final String s;
    private int[] index;

    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException();
        this.s = s;
        this.len = s.length();
        this.index = new int[len];
        
        for (int i = 0; i < len; ++i) index[i] = i;

        sort(0, len - 1, 0);
    }

    private int charAt(int start, int pos) {
        return (int) s.charAt((start + pos) % len);
    }

    private void sort(int lo, int hi, int d) { 
        if (hi <= lo)   return;
        if (d >= len)   return;

        if (hi <= lo + CUTOFF) {
            insertion(lo, hi, d);
            return;
        }

        int v = charAt(index[lo], d);
        int lt = lo, gt = hi;
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(index[i], d);
            if      (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else            i++;
        }

        sort(lo, lt - 1, d);
        sort(lt, gt, d + 1);
        sort(gt + 1, hi, d);
    }

    private void insertion(int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(index[j], index[j - 1], d); j--)
                exch(j, j - 1);
    }

    private boolean less(int i, int j, int d) {
        if (i == j) return false;
        for (int t = 0; t < len; ++t) {
            int le = charAt(i, d);
            int ri = charAt(j, d);
            if (le < ri) return true;
            if (le > ri) return false;
            d++;
        }
        return false;
    }

    private void exch(int i, int j) {
        int swap = index[i];
        index[i] = index[j];
        index[j] = swap;
    }
 

    public int length() { return len; }

    public int index(int i) { 
        if (i < 0 || i >= len)
            throw new IllegalArgumentException();
        return index[i]; 
    }

    public static void main(String[] args) {
        CircularSuffixArray c = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < c.length(); ++i)
            StdOut.println(c.index(i));
    }
}
