package graph;

import util.GraphLoader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * undirected graph for graph clustering problem.
 * single edge.
 * need to check if the graph is connected. (use bfs)
 */
public class Graph {

    private static final String NEWLINE = System.getProperty("line.separator");
    private int V;
    private int E;
    private Map<Integer, Set<Edge>> G;

    public Graph(int V) { // construct a graph from empty

        if (V < 0) {
            throw new IllegalArgumentException("vertex # must be positive.");
        }

        this.V = V;
        this.E = 0;

        G = new HashMap<>();
        for (int i = 0; i < V; ++i) {
            G.put(i, new HashSet<>());
        }
    }

    public Graph(Graph g) {

        V = g.V();
        E = g.E();
        G = g.export();
    }

    public int V() { return V; }

    public int E() { return E; }

    public Map<Integer, Set<Edge>> export() {
        return new HashMap<>(G);
    }

    public Map<Integer, Set<Edge>> getGraph() {
        return G;
    }

    private void validateVertex(int v) {
        if (!G.containsKey(v)) {
            throw new IllegalArgumentException("vertex " + v + " doesn't exist");
        }
    }

    private void validateEdge(int v, int w) {
        boolean b1, b2;
        Edge e = new Edge(v, w);
        b1 = G.get(v).contains(e);
        b2 = G.get(w).contains(e);

        if (b1 != b2) {
            throw new IllegalArgumentException("edge " + v + "-" + w + " invalid");
        }
    }

    public void addVertex(int v) {

        if (G.containsKey(v)) {
            throw new IllegalArgumentException("vertex " + v + " exists");
        }
        G.put(v, new HashSet<>());
    }

    public void addEdge(int v, int w) {

        validateVertex(v);
        validateVertex(w);
        validateEdge(v, w);

        Edge e = new Edge(v, w);
        if (!G.get(v).contains(e)) E++;
        G.get(v).add(e);
        G.get(w).add(e);

    }

    public void removeEdge(int v, int w) {

        validateVertex(v);
        validateVertex(w);
        validateEdge(v, w);

        Edge e = new Edge(v, w);
        if (!G.get(v).contains(e)) {
            throw new IllegalArgumentException("edge doesn't exist. @removeEdge");
        }
        G.get(v).remove(e);
        G.get(w).remove(e);
        E--;

    }

    public Iterable<Graph> connectedComponent() {
        return null;
    }

    private void bfs() {

    }

//    private Graph createSubgraph(Iterable<Integer> vertices) { // create subgraph on selected vetices
//
//        HashSet<Integer> sub = new HashSet<>();
//
//        for (Integer i : vertices) {
//            if (!sub.add(i)) {
//                throw new IllegalArgumentException("duplicated vertex " + i + " in createSubgraph.");
//            }
//        }
//
//        Graph sub_g = new wuGraph();
//
//        for (Integer i : sub) {
//            sub_g.addVertex(i);
//        }
//
//        for (Integer i : sub) {
//            for (Integer j : adj.get(i)) {
//                if (sub.contains(j)) {
//                    sub_g.addEdge(i, j);
//                }
//            }
//        }
//
//        return sub_g;
//    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(V + " Vertex  " + E + " Edges" + NEWLINE);

        for (Integer v : G.keySet()) {
            sb.append(String.format("%d: ", v));
            for (Edge e : G.get(v)) {
                sb.append(String.format("%d ", e.other(v)));
            }
            sb.append(NEWLINE);
        }

        return sb.toString();
    }

    public static void main(String[] args) {

        Graph g = GraphLoader.loadUndirGraph(args[0]);
        Graph gg = new Graph(g);
        System.out.println(gg);
    }


}
