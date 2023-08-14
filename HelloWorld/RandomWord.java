import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champion = "";
        int i = 0;
        while (!StdIn.isEmpty()) {
            String cur = StdIn.readString();
            ++i;
            double p = 1.00 / i;
            if (StdRandom.bernoulli(p))
                champion = cur;
        }
        StdOut.println(champion);
    }
}
