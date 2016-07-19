package algorithm;

import java.util.Arrays;

/**
 * weighted quick union find
 */
public class UnionFind {

    private int[] parent;
    private int[] size;
    private int num_component;

    public UnionFind (int N) {

        parent = new int[N];
        size = new int[N];
        num_component = N;
        for (int i = 0; i < N; ++i) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    private void validate(int v) {
        if (v < 0 || v > parent.length-1) {
            throw new IllegalArgumentException("input out of range. @UnionFind");
        }
    }

    private int findRoot(int v) {
        while (v != parent[v]) {
            v = parent[v];
        }
        return v;
    }

    public boolean isConnected(int v, int w) {
        validate(v);
        validate(w);
        return findRoot(v) == findRoot(w);
    }

    public void union(int v, int w) {
        validate(v);
        validate(w);

        int rv = findRoot(v);
        int rw = findRoot(w);

        if (rv == rw) return;

        if (size[rv] > size[rw]) {
            parent[rw] = rv;
            size[rv] += size[rw];
        }
        else {
            parent[rv] = rw;
            size[rw] += size[rv];
        }

        num_component--;
    }

    public static void main(String[] args) {

        int[] arr = new int[] {0,1,2,3,4,5,6};

        UnionFind uf = new UnionFind(arr.length);

        uf.union(0,1);
        uf.union(2,3);
        uf.union(0,3);
        uf.union(4,5);
        uf.union(4,6);
        uf.union(5,6);
        uf.union(1,6);

        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(uf.parent));
        System.out.println(Arrays.toString(uf.size));
        System.out.println(uf.num_component);

    }

}
