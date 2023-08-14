import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {
   
    private final Digraph copyG;
    
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        copyG = new Digraph(G);
    }

    public int length(int v, int w) {
        if (v < 0 || w < 0 || v >= copyG.V() || w >= copyG.V() || (Integer) v == null || (Integer) w == null)
            throw new IllegalArgumentException();
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(copyG, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(copyG, w);
        
        int dis = Integer.MAX_VALUE;
        for (int i = 0; i < copyG.V(); ++i) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i))
                dis = Integer.min(dis, bfs1.distTo(i) + bfs2.distTo(i));
        } 
        if (dis == Integer.MAX_VALUE)
            return -1;
        return dis;
    }

    public int ancestor(int v, int w) {
        if (v < 0 || w < 0 || v >= copyG.V() || w >= copyG.V() || (Integer) v == null || (Integer) w == null)
            throw new IllegalArgumentException();
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(copyG, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(copyG, w);

        int dis = Integer.MAX_VALUE, res = -1;
        for (int i = 0; i < copyG.V(); ++i) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                int curDis = bfs1.distTo(i) + bfs2.distTo(i);
                if (curDis < dis) {
                    dis = curDis;
                    res = i;
                }
            }
        }
        return res;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        for (int arg : v) {
            if (arg < 0 || arg >= copyG.V() || (Integer) arg == null)
                throw new IllegalArgumentException();
        }
        for (int arg : w) {
            if (arg < 0 || arg >= copyG.V() || (Integer) arg == null)
                throw new IllegalArgumentException();
        }
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(copyG, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(copyG, w);
        
        int dis = Integer.MAX_VALUE;
        for (int i = 0; i < copyG.V(); ++i) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i))
                dis = Integer.min(dis, bfs1.distTo(i) + bfs2.distTo(i));
        } 
        if (dis == Integer.MAX_VALUE)
            return -1;
        return dis;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        for (int arg : v) {
            if (arg < 0 || arg >= copyG.V() || (Integer) arg == null)
                throw new IllegalArgumentException();
        }
        for (int arg : w) {
            if (arg < 0 || arg >= copyG.V() || (Integer) arg == null)
                throw new IllegalArgumentException();
        }
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(copyG, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(copyG, w);

        int dis = Integer.MAX_VALUE, res = -1;
        for (int i = 0; i < copyG.V(); ++i) {
            if (bfs1.hasPathTo(i) && bfs2.hasPathTo(i)) {
                int curDis = bfs1.distTo(i) + bfs2.distTo(i);
                if (curDis < dis) {
                    dis = curDis;
                    res = i;
                }
            }
        }
        return res;
    }


    public static void main(String[] args) {
        In in = new In("digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
