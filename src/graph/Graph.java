package graph;

import util.GraphLoader;

import java.util.*;


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

    public Graph() {
        this.V = 0;
        this.E = 0;
        G = new HashMap<>();
    }

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

    public Graph(List<Integer> list) {

        if (list == null) {
            throw new NullPointerException("list is null");
        }

        if (list.size() == 0) {
            throw new IllegalArgumentException("vertex list invalid");
        }

        this.V = list.size();
        this.E = 0;
        G = new HashMap<>();
        for (Integer i : list) {
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

    public Map<Integer, Set<Edge>> export() { return new HashMap<>(G); }

    public Map<Integer, Set<Edge>> getGraph() { return G; }

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
        V++;
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

    public List<List<Integer>> connectedVertex() {

        List<List<Integer>> ret = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        for (Integer i : G.keySet()) {

            if (visited.contains(i)) continue;

            List<Integer> component = new LinkedList<>();
            Queue<Integer> q = new LinkedList<>();
            Set<Integer> queue = new HashSet<>();
            q.add(i);
            queue.add(i);
            while (!q.isEmpty()) {
                int v = q.remove();
                queue.remove(v);
                visited.add(v);
                component.add(v);
                for (Edge e : G.get(v)) {
                    int w = e.other(v);
                    if (!visited.contains(w) && !queue.contains(w)) {
                        q.add(w);
                        queue.add(w);
                    }
                }
            }
            ret.add(component);
        }

        return ret;
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
        System.out.println(g);
        List<List<Integer>> list = g.connectedVertex();
        System.out.println(list);
        g.removeEdge(6, 4);
        System.out.println(g);
        list = g.connectedVertex();
        System.out.println(list);
    }

}
