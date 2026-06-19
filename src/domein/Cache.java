package domein;

public class Cache {
    public int[][] cache = new int[1][1];

    public Cache(int[][] vals) {
        this.cache = vals;
    }

    public int[][] getCache() {
        return cache;
    }
}
