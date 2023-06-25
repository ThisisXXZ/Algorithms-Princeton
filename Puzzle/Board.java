import java.util.Queue;
import java.util.LinkedList;

public class Board {

    private int[][] a;
    private int n;
    private int posx, posy;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles[0].length;
        a = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                a[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    posx = i;
                    posy = j;
                }
            }
        }
    }
                                           
    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                s.append(String.format("%2d ", a[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int res = 0;
        for (int i = 1; i < n * n; ++i) {
            if (a[(i - 1) / n][(i - 1) % n] != i)
                ++res;
        }
        return res;
    }
    
    private int abs(int num) { return num < 0 ? -num : num; } 

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int res = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                int cur = a[i][j];
                if (cur == 0)   continue;
                int x = (cur - 1) / n, y = (cur - 1) % n;
                res += abs(x - i) + abs(y - j);
            }
        }
        return res;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }


    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y)  return true;
        if (y == null || getClass() != y.getClass())    return false;
        Board that = (Board) y;
        if (n != that.n)    return false;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j)
                if (a[i][j] != that.a[i][j]) return false;
        }
        return true;
    }

    private int[][] copy() {
        int[][] cop = new int[n][n];
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                cop[i][j] = a[i][j];
        return cop;
    }

    private void swap(int[][] arr, int x1, int y1, int x2, int y2) {
        int t = arr[x2][y2];
        arr[x2][y2] = arr[x1][y1];
        arr[x1][y1] = t;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> que = new LinkedList<Board>();
        if (posx > 0) {
            int [][] up = copy();
            swap(up, posx - 1, posy, posx, posy);
            que.add(new Board(up));
        }
        if (posx < n - 1) {
            int [][] down = copy();
            swap(down, posx + 1, posy, posx, posy);
            que.add(new Board(down));
        }
        if (posy > 0) {
            int [][] left = copy();
            swap(left, posx, posy - 1, posx, posy);
            que.add(new Board(left));
        }
        if (posy < n - 1) {
            int [][] right = copy();
            swap(right, posx, posy + 1, posx, posy);
            que.add(new Board(right));
        }
        return que;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinBoard = copy();
        swap(twinBoard, (posx + 1) % n, posy, posx, (posy + 1) % n);
        return new Board(twinBoard);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
    
    }

}