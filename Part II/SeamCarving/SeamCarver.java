import edu.princeton.cs.algs4.Picture;

import java.io.File;

public class SeamCarver {
    private Picture pic;
    private double[][] E;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        checkNull(picture);
        pic = new Picture(picture);
        E = new double[width()][height()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                E[x][y] = energy(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        return new Picture(pic);
    }

    // width of current picture
    public int width() {
        return pic.width();
    }

    // height of current picture
    public int height() {
        return pic.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IllegalArgumentException();
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return 1000;
        int Rx = pic.get(x + 1, y).getRed() - pic.get(x - 1, y).getRed();
        int Gx = pic.get(x + 1, y).getGreen() - pic.get(x - 1, y).getGreen();
        int Bx = pic.get(x + 1, y).getBlue() - pic.get(x - 1, y).getBlue();
        int Ry = pic.get(x, y + 1).getRed() - pic.get(x, y - 1).getRed();
        int Gy = pic.get(x, y + 1).getGreen() - pic.get(x, y - 1).getGreen();
        int By = pic.get(x, y + 1).getBlue() - pic.get(x, y - 1).getBlue();
        int Dx = Rx * Rx + Gx * Gx + Bx * Bx;
        int Dy = Ry * Ry + Gy * Gy + By * By;
        return Math.sqrt(Dx + Dy);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] edges = {-1, 0, 1};
        double[][] d = new double[width()][height()];
        int[][] p = new int[width()][height()];
        d[0] = E[0];    // first column has distance = energy
        for (int x = 1; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                d[x][y] = Double.MAX_VALUE;
            }
        }
        for (int x = 0; x < width() - 1; x++) {
            for (int y = 0; y < height(); y++) {
                int x_ = x + 1;
                for (int i : edges) {
                    int y_ = y + i;
                    if (outOfRange(y_, height())) continue;
                    double dist = d[x][y] + E[x_][y_];
                    if (d[x_][y_] > dist) {
                        d[x_][y_] = dist;
                        p[x_][y_] = -i;
                    }
                }
            }
        }
        int argmin = -1;
        double min = Double.MAX_VALUE;
        for (int y = 0; y < height(); y++) {
            double candidate = d[width() - 1][y];
            if (candidate < min) {
                min = candidate;
                argmin = y;
            }
        }
        int t = argmin;
        int[] seam = new int[width()];
        for (int x = width() - 1; x >= 0; x--) {
            seam[x] = t;
            t += p[x][t];
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] edges = {-1, 0, 1};
        double[][] d = new double[width()][height()];
        int[][] p = new int[width()][height()];
        for (int x = 0; x < width(); x++) d[x][0] = E[x][0];    // first row has distance = energy
        for (int y = 1; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                d[x][y] = Double.MAX_VALUE;
            }
        }
        for (int y = 0; y < height() - 1; y++) {
            for (int x = 0; x < width(); x++) {
                int y_ = y + 1;
                for (int i : edges) {
                    int x_ = x + i;
                    if (outOfRange(x_, width())) continue;
                    double dist = d[x][y] + E[x_][y_];
                    if (d[x_][y_] > dist) {
                        d[x_][y_] = dist;
                        p[x_][y_] = -i;
                    }
                }
            }
        }
        int argmin = -1;
        double min = Double.MAX_VALUE;
        for (int x = 0; x < width(); x++) {
            double candidate = d[x][height() - 1];
            if (candidate < min) {
                min = candidate;
                argmin = x;
            }
        }
        int t = argmin;
        int[] seam = new int[height()];
        for (int y = height() - 1; y >= 0; y--) {
            seam[y] = t;
            t += p[t][y];
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        checkSeam(seam, width(), height());

        Picture newPic = new Picture(width(), height() - 1);
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (y < seam[x]) newPic.set(x, y, pic.get(x, y));
                else if (y > seam[x]) newPic.set(x, y - 1, pic.get(x, y));
            }
        }
        pic = newPic;

        double[][] newE = new double[width()][height()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (y < seam[x] - 1) newE[x][y] = E[x][y];
                else if (y == seam[x] - 1 || y == seam[x]) newE[x][y] = energy(x, y);
                else newE[x][y] = E[x][y + 1];
            }
        }
        E = newE;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        checkSeam(seam, height(), width());

        Picture newPic = new Picture(width() - 1, height());
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (x < seam[y]) newPic.set(x, y, pic.get(x, y));
                else if (x > seam[y]) newPic.set(x - 1, y, pic.get(x, y));
            }
        }
        pic = newPic;

        double[][] newE = new double[width()][height()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) newE[x][y] = 1000;
                else if (x < seam[y] - 1) newE[x][y] = E[x][y];
                else if (x == seam[y] - 1 || x == seam[y]) newE[x][y] = energy(x, y);
                else newE[x][y] = E[x + 1][y];
            }
        }
        E = newE;
    }

    private void checkSeam(int[] seam, int length, int range) {
        checkNull(seam);
        if (seam.length != length || range <= 1) throw new IllegalArgumentException();
        int lastValue = seam[0];
        for (int value : seam) {
            if (outOfRange(value, range)) throw new IllegalArgumentException();
            int diff = value - lastValue;
            if (diff > 1 || diff < -1) throw new IllegalArgumentException();
            lastValue = value;
        }
    }

    private void checkRange(int a, int cap) {
        if (outOfRange(a, cap)) throw new IllegalArgumentException();
    }

    private boolean outOfRange(int a, int cap) {
        return a < 0 || a >= cap;
    }

    private void checkNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        File f = new File("/Users/xiaoleiwang/Desktop/", "IMG_0138.PNG");
        Picture pic = new Picture(f);
        SeamCarver sc = new SeamCarver(pic);
        sc.energy(1, 0);

        int x = 0, y = 0;
        while (x++ < 100) sc.removeVerticalSeam(sc.findVerticalSeam());
        while (y++ < 100) sc.removeHorizontalSeam(sc.findHorizontalSeam());

        sc.picture().show();
    }
}
