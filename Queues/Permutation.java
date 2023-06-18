import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        // int k = StdIn.readInt();
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        
        while (!StdIn.isEmpty()) {
            rq.enqueue(StdIn.readString());
        }        
        
        for (int i = 1; i <= k; ++i) {
            StdOut.println(rq.dequeue());
        }
        
    }
}
