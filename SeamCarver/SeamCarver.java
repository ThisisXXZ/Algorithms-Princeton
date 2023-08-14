import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {

    private static final double BORDER_ENERGY = 1000.0;

    private Picture pic;
    private boolean transposed;

    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();
        pic = new Picture(picture);
        transposed = false;
    }

    public Picture picture() { 
        if (transposed) transpose();
        return new Picture(pic); 
    }

    public int width() { 
        if (transposed) transpose();
        return pic.width(); 
    }

    public int height() { 
        if (transposed) transpose();
        return pic.height(); 
    }

    private void transpose() {
        Picture picTran = new Picture(pic.height(), pic.width());
        for (int i = 0; i < pic.width(); ++i)
            for (int j = 0; j < pic.height(); ++j)
                picTran.setRGB(j, i, pic.getRGB(i, j));
        pic = picTran;
        transposed = !transposed;
    }

    private int square(int x) { return x * x; }

    private int squareDeltaX(int x, int y) {
        int lePix = pic.getRGB(x - 1, y);
        int riPix = pic.getRGB(x + 1, y);
        int xRed   = ((lePix >> 16) & 0xFF) - ((riPix >> 16) & 0xFF);
        int xGreen = ((lePix >> 8)  & 0xFF) - ((riPix >> 8)  & 0xFF);
        int xBlue  = ((lePix >> 0)  & 0xFF) - ((riPix >> 0)  & 0xFF);
        return square(xRed) + square(xGreen) + square(xBlue);
    }

    private int squareDeltaY(int x, int y) {
        int upPix = pic.getRGB(x, y - 1);
        int dwPix = pic.getRGB(x, y + 1);
        int xRed   = ((upPix >> 16) & 0xFF) - ((dwPix >> 16) & 0xFF);
        int xGreen = ((upPix >> 8)  & 0xFF) - ((dwPix >> 8)  & 0xFF);
        int xBlue  = ((upPix >> 0)  & 0xFF) - ((dwPix >> 0)  & 0xFF);
        return square(xRed) + square(xGreen) + square(xBlue);
    }

    private double countEnergy(int x, int y) {
        if (x < 0 || x >= pic.width() || y < 0 || y >= pic.height())
            throw new IllegalArgumentException();
        if (x == 0 || y == 0 || x == pic.width() - 1 || y == pic.height() - 1)
            return BORDER_ENERGY;
        return Math.sqrt(squareDeltaX(x, y) + squareDeltaY(x, y));
    }

    public double energy(int x, int y) {
        if (transposed) transpose();
        return countEnergy(x, y);
    }

    private void relax(int x, int y, double[][] distTo, int[][] edgeTo) {
        if (x - 1 >= 0) {
            double en = countEnergy(x - 1, y + 1);
            if (distTo[x][y] + en < distTo[x - 1][y + 1]) {
                distTo[x - 1][y + 1] = distTo[x][y] + en;
                edgeTo[x - 1][y + 1] = x;
            }
        }
        if (x + 1 < pic.width()) {
            double en = countEnergy(x + 1, y + 1);
            if (distTo[x][y] + en < distTo[x + 1][y + 1]) {
                distTo[x + 1][y + 1] = distTo[x][y] + en;
                edgeTo[x + 1][y + 1] = x;
            }
        }
        double en = countEnergy(x, y + 1);
        if (distTo[x][y] + en < distTo[x][y + 1]) {
            distTo[x][y + 1] = distTo[x][y] + en;
            edgeTo[x][y + 1] = x;
        }
    }

    private int[] findSeam() {
        int[] vs = new int[pic.height()];
        int[][] edgeTo = new int[pic.width()][pic.height()];
        double[][] distTo = new double[pic.width()][pic.height()];

        for (int i = 0; i < pic.width(); ++i) {
            distTo[i][0] = BORDER_ENERGY;
            edgeTo[i][0] = -1;
        }
        for (int j = 0; j < pic.height() - 1; ++j) {
            for (int i = 0; i < pic.width(); ++i)
                distTo[i][j + 1] = Double.POSITIVE_INFINITY;
            for (int i = 0; i < pic.width(); ++i)
                relax(i, j, distTo, edgeTo);
        }

        double minEnergy = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pic.width(); ++i) {
            if (distTo[i][pic.height() - 1] < minEnergy) {
                minEnergy = distTo[i][pic.height() - 1];
                vs[pic.height() - 1] = i;
            }
        }
        int cur = vs[pic.height() - 1];
        for (int j = pic.height() - 1; j >= 0; --j) {
            vs[j] = cur;
            cur = edgeTo[cur][j];
        }
        return vs;
    }

    public int[] findVerticalSeam() {
        if (transposed)     transpose();
        return findSeam();
    }

    public int[] findHorizontalSeam() {
        if (!transposed)    transpose();
        return findSeam();
    }

    private void checkValidSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException();
        if (seam.length != pic.height() || pic.width() <= 1)
            throw new IllegalArgumentException();
        for (int j = 0; j < pic.height(); ++j) {
            if (seam[j] < 0 || seam[j] >= pic.width())
                throw new IllegalArgumentException();
        }
        for (int i = 1; i < seam.length; ++i) {
            int diff = seam[i] - seam[i - 1];
            if (diff < -1 || diff > 1)
                throw new IllegalArgumentException();
        }
    }

    private void removeSeam(int[] seam) {
        Picture picRem = new Picture(pic.width() - 1, pic.height());
        for (int j = 0; j < pic.height(); ++j) {
            for (int i = 0; i < seam[j]; ++i)
                picRem.setRGB(i, j, pic.getRGB(i, j));
            for (int i = seam[j] + 1; i < pic.width(); ++i)
                picRem.setRGB(i - 1, j, pic.getRGB(i, j));
        }
        pic = picRem;
    }

    public void removeVerticalSeam(int[] seam) {
        if (transposed) transpose();
        checkValidSeam(seam);
        removeSeam(seam);
    }

    public void removeHorizontalSeam(int[] seam) {
        if (!transposed) transpose();
        checkValidSeam(seam);        
        removeSeam(seam);    
    }

    public static void main(String[] args) {
        String fileName = "SeamCarver/3x7.png";
        Picture picture = new Picture(fileName);
        SeamCarver sc = new SeamCarver(picture);
        StdOut.println("vertical seam: \n");
        for (int i : sc.findVerticalSeam()) {
            StdOut.println(i);
        }
        StdOut.println("horizontal seam: \n");
        for (int i : sc.findHorizontalSeam()) {
            StdOut.println(i);
        }
    }
}