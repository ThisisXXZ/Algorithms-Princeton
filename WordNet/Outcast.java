import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    
    private final WordNet wNet;

    public Outcast(WordNet wordnet) {
        wNet = wordnet;
    }

    public String outcast(String[] nouns) {
        int len = nouns.length;
        if (len < 2)
            throw new IllegalArgumentException();
        for (String word : nouns) {
            if (!wNet.isNoun(word))
                throw new IllegalArgumentException();
        }
        int[] dis = new int[len];
        for (int i = 0; i < len; ++i)   dis[i] = 0;
        for (int i = 0; i < len; ++i) {
            for (int j = i + 1; j < len; ++j) {
                int delta = wNet.distance(nouns[i], nouns[j]);
                dis[i] += delta;
                dis[j] += delta;
            }
        }
        int res = 0, maxDis = dis[0];
        for (int i = 1; i < len; ++i) {
            if (dis[i] > maxDis) {
                maxDis = dis[i];
                res = i;
            }
        }
        return nouns[res];
    }

    public static void main(String[] args) {
        String init1, init2;
        init1 = StdIn.readString();
        init2 = StdIn.readString();
        WordNet wordnet = new WordNet(init1, init2);
        Outcast outcast = new Outcast(wordnet);
        while (!StdIn.isEmpty()) {
            String init3 = StdIn.readString();
            In in = new In(init3);
            String[] nouns = in.readAllStrings();
            StdOut.println(init3 + ": " + outcast.outcast(nouns));
        }
    }
}
