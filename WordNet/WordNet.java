import edu.princeton.cs.algs4.In;
import java.util.HashMap;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {
    
    private final HashMap<String, ArrayList<Integer>> position;
    private final String[] info;
    private final SAP getSap;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();
        position = new HashMap<String, ArrayList<Integer>>();
        int numOfSynsets = 0;
        info = new String[120000];

        In syn = new In(synsets);
        In hyp = new In(hypernyms);
        String line;
        while ((line = syn.readLine()) != null) {
            String[] sepStrings = line.split(",");
            info[numOfSynsets++] = sepStrings[1];
            for (String noun : sepStrings[1].split(" ")) {
                if (!position.containsKey(noun)) {
                    ArrayList<Integer> newNounPos = new ArrayList<Integer>();
                    newNounPos.add(Integer.parseInt(sepStrings[0]));
                    position.put(noun, newNounPos);
                } else {
                    ArrayList<Integer> nounPos = position.get(noun);
                    nounPos.add(Integer.parseInt(sepStrings[0]));
                }
            }
        }

        Digraph G = new Digraph(numOfSynsets);
        while ((line = hyp.readLine()) != null) {
            String[] sepStrings = line.split(",");
            for (int i = 1; i < sepStrings.length; ++i)
                G.addEdge(Integer.parseInt(sepStrings[0]), Integer.parseInt(sepStrings[i]));
        }
        DirectedCycle detectCycle = new DirectedCycle(G);
        if (detectCycle.hasCycle())
            throw new IllegalArgumentException();
        getSap = new SAP(G);
    }

    public Iterable<String> nouns() {
        return position.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        return position.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !position.containsKey(nounA) || !position.containsKey(nounB))
            throw new IllegalArgumentException();
        return getSap.length(position.get(nounA), position.get(nounB));
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !position.containsKey(nounA) || !position.containsKey(nounB))
            throw new IllegalArgumentException();
        return info[getSap.ancestor(position.get(nounA), position.get(nounB))];
    }

    public static void main(String[] args) {
        // WordNet test = new WordNet("synsets.txt", "hypernyms.txt");
    }
}
