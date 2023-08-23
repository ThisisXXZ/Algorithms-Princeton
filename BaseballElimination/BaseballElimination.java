import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class BaseballElimination {
    
    private final int n;
    private final String[] teams;
    private final int[] w, l, r;
    private final int[][] g; 
    private int[][] idv;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();

        teams = new String[n];
        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];
        for (int i = 0; i < n; ++i) {
            teams[i] = in.readString();
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < n; ++j)
                g[i][j] = in.readInt();
        }
    }

    public int numberOfTeams() { return n; }

    public Iterable<String> teams() { return Arrays.asList(teams); }

    private int id(String team) {
        for (int i = 0; i < n; ++i) {
            if (teams[i].equals(team))   return i;
        }
        throw new IllegalArgumentException();
    }

    public int wins(String team) { return w[id(team)]; }

    public int losses(String team) { return l[id(team)]; }

    public int remaining(String team) { return r[id(team)]; }

    public int against(String team1, String team2) { return g[id(team1)][id(team2)]; }

    private FlowNetwork buildNetwork(int team, int v) {
        int s = 0, t = v - 1;

        idv = new int[n][n];
        int cnt = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = i; j < n; ++j) {
                if (i == team || j == team) continue;
                ++cnt;
                idv[i][j] = cnt;
                idv[j][i] = cnt;
            }
        }

        FlowNetwork G = new FlowNetwork(v);
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                if (i == team || j == team) continue;
                G.addEdge(new FlowEdge(s, idv[i][j], g[i][j]));
                G.addEdge(new FlowEdge(idv[i][j], idv[i][i], Double.POSITIVE_INFINITY));
                G.addEdge(new FlowEdge(idv[i][j], idv[j][j], Double.POSITIVE_INFINITY));
            }
        }
        for (int i = 0; i < n; ++i) {
            if (i == team)  continue;
            G.addEdge(new FlowEdge(idv[i][i], t, w[team] + r[team] - w[i]));
        }

        return G;
    }

    private FordFulkerson buildmaxFlow(int team) {
        int v = 1 + (n - 1) * (n - 2) / 2 + (n - 1) + 1;
        return new FordFulkerson(buildNetwork(team, v), 0, v - 1);
    }

    private int triviallyEliminatedBy(int team) {
        for (int i = 0; i < n; ++i) {
            if (i == team)    continue;
            if (w[i] > w[team] + r[team])   return i;
        }
        return -1;
    }

    private boolean isAllFlow(FordFulkerson maxFlow, int team) {
        double total = 0.0;
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                if (i == team || j == team) continue;
                total += g[i][j];
            }
        }
        return maxFlow.value() == total;
    }

    public boolean isEliminated(String team) {
        int teamid = id(team);
        return (triviallyEliminatedBy(teamid) != -1) || !isAllFlow(buildmaxFlow(teamid), teamid);
    }

    public Iterable<String> certificateOfElimination(String team) {
        int teamid = id(team);
        List<String> res = new ArrayList<String>();
        int x = triviallyEliminatedBy(teamid);
        if (x != -1) {
            res.add(teams[x]);
            return res;
        }
        FordFulkerson maxFlow = buildmaxFlow(teamid);
        if (isAllFlow(maxFlow, teamid))
            return null;
        for (int i = 0; i < n; ++i) {
            if (i == teamid)    continue;
            if (maxFlow.inCut(idv[i][i]))    res.add(teams[i]);
        }
        return res;
    }

    public static void main(String[] args) {
        String filename = "BaseballElimination/teams54.txt";
        BaseballElimination division = new BaseballElimination(filename);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
