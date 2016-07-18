package graph;

import java.util.HashSet;
import java.util.Set;

public class UndirectedGraph {

    private final int V;
    private int E;
    private Set<Integer>[] adj;

    public UndirectedGraph(int V) { // construct a graph from empty

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

    public UndirectedGraph(UndirectedGraph g) {

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

        if (v < 0 || v > V-1) {
            throw new IllegalArgumentException("vertex out of range");
        }

        return adj[v];
    }

    public void addEdge(int v, int w) {

        boolean b1, b2;
        b1 = adj[v].add(w);
        b2 = adj[w].add(v);

        if (b1 != b2) {
            throw new IllegalArgumentException("edge is not undirected. @addEdge");
        }

        if (b1) E++;
    }

    public void removeEdge(int v, int w) {

        boolean b1, b2;
        b1 = adj[v].contains(w);
        b2 = adj[w].contains(v);

        if (!b1 != b2) {
            throw new IllegalArgumentException("edge is not undirected. @removeEdge");
        }

        if (!b1) {
            throw new IllegalArgumentException("edge doesn't exist. @removeEdge");
        }

        adj[v].remove(w);
        adj[w].remove(v);

        E--;
    }

}
