package graph;

import util.GraphLoader;

import java.util.HashSet;
import java.util.Set;

public class UndirGraph {

    private static final String NEWLINE = System.getProperty("line.separator");
    private final int V;
    private int E;
    private Set<Integer>[] adj;

    public UndirGraph(int V) { // construct a graph from empty

        if (V < 0) {
            throw new IllegalArgumentException("vertex # must be positive.");
        }

        this.V = V;
        this.E = 0;

        adj = new Set[this.V];
        for (int i = 0; i < this.V; ++i) {
            adj[i] = new HashSet<>();
        }
    }

    public UndirGraph(UndirGraph g) {

        V = g.V();
        E = g.E();
        adj = new Set[this.V];
        for (int v = 0; v < this.V; ++v) {
            adj[v] = new HashSet<>(g.adj(v));
        }
    }

    public int V() { return V; }

    public int E() { return E; }

    public Set<Integer> adj(int v) {

        validateVertex(v);
        return adj[v];
    }

    private void validateVertex(int v) {
        if (v < 0 || v > V-1) {
            throw new IllegalArgumentException("vertex " + v + " out of range");
        }
    }

    private void validateEdge(int v, int w) {
        boolean b1, b2;
        b1 = adj[v].contains(w);
        b2 = adj[w].contains(v);
        if (b1 != b2) {
            throw new IllegalArgumentException("edge " + v + "-" + w + " invalid");
        }
    }

    public void addEdge(int v, int w) {

        validateVertex(v);
        validateVertex(w);
        validateEdge(v, w);

        if (!adj[v].contains(w)) E++;

        adj[v].add(w);
        adj[w].add(v);
    }

    public void removeEdge(int v, int w) {

        validateVertex(v);
        validateVertex(w);
        validateEdge(v, w);

        if (!adj[v].contains(w)) {
            throw new IllegalArgumentException("edge doesn't exist. @removeEdge");
        }

        adj[v].remove(w);
        adj[w].remove(v);

        E--;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(V + "Vertex  " + E + "Edges" + NEWLINE);
        for (int v = 0; v < V; v++) {
            sb.append(String.format("%d: ", v));
            for (int w : adj[v]) {
                sb.append(String.format("%d ", w));
            }
            sb.append(NEWLINE);
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        UndirGraph g = GraphLoader.loadUndirGraph(args[0]);
//        System.out.println(g);
    }


}
