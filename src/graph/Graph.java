package graph;

import util.GraphLoader;

import java.util.HashSet;
import java.util.Set;


/**
 * undirected graph for graph clustering problem.
 */
public class Graph {

    private static final String NEWLINE = System.getProperty("line.separator");
    private final int V;
    private int E;
    private Set<Edge>[] adj;
    private int[] weight;    // weight of each node, meaning # of shortest path through node.

    public Graph(int V) { // construct a graph from empty

        if (V < 0) {
            throw new IllegalArgumentException("vertex # must be positive.");
        }

        this.V = V;
        this.E = 0;
        this.weight = new int[this.V];

        adj = new Set[this.V];
        for (int i = 0; i < this.V; ++i) {
            adj[i] = new HashSet<>();
        }
    }

    public Graph(Graph g) {

        V = g.V();
        E = g.E();
        adj = new Set[this.V];
        for (int v = 0; v < this.V; ++v) {
            adj[v] = new HashSet<>(g.adj(v));
        }
    }

    public int V() { return V; }

    public int E() { return E; }

    public Set<Edge> adj(int v) {

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
        b1 = adj[v].contains(new Edge(v, w));
        b2 = adj[w].contains(new Edge(v, w));
        if (b1 != b2) {
            throw new IllegalArgumentException("edge " + v + "-" + w + " invalid");
        }
    }

    public void addEdge(int v, int w) {

        validateVertex(v);
        validateVertex(w);
        validateEdge(v, w);

        if (!adj[v].contains(new Edge(v, w))) E++;

        adj[v].add(new Edge(v, w));
        adj[w].add(new Edge(v, w));
    }

    public void removeEdge(int v, int w) {

        validateVertex(v);
        validateVertex(w);
        validateEdge(v, w);

        if (!adj[v].contains(new Edge(v, w))) {
            throw new IllegalArgumentException("edge doesn't exist. @removeEdge");
        }

        adj[v].remove(new Edge(v, w));
        adj[w].remove(new Edge(v, w));

        E--;
    }

    public void setVertexWeight(int v, int weight) {
        validateVertex(v);
        this.weight[v] = weight;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(V + " Vertex  " + E + " Edges" + NEWLINE);
        for (int v = 0; v < V; v++) {
            sb.append(String.format("%d: ", v));
            for (Edge e : adj[v]) {
                sb.append(String.format("%d ", e.other(v)));
            }
            sb.append(NEWLINE);
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        Graph g = GraphLoader.loadUndirGraph(args[0]);
        System.out.println(g);

    }


}