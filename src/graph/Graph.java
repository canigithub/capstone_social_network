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

    public Graph(Set<Integer> vertex) {

        if (vertex == null) {
            throw new NullPointerException("list is null");
        }

        if (vertex.size() == 0) {
            throw new IllegalArgumentException("vertex list invalid");
        }

        this.V = vertex.size();
        this.E = 0;
        G = new HashMap<>();
        for (Integer i : vertex) {
            G.put(i, new HashSet<>());
        }
    }

    public Graph(Graph g) {

        V = g.V();
        E = 0;

//        G = new HashMap<>(g.G);   // this way the edge only copy reference isteead of object -> incorrect.

        G = new HashMap<>();

        for (Integer v : g.G.keySet()) {
            G.put(v, new HashSet<>());
        }

        for (Integer v : g.G.keySet()) {
            for (Edge e : g.G.get(v)) {
                int w = e.other(v);
                this.addEdge(v, w);
            }
        }
    }

    public int V() { return V; }

    public int E() { return E; }

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

    public void addEdge(int v, int w) {

        validateVertex(v);
        validateVertex(w);
        validateEdge(v, w);

        Edge e = new Edge(v, w);
        if (G.get(v).contains(e)) return;   // if edge exists, do nothing.
        G.get(v).add(e);                    // otherwise, add edge to both vertex
        G.get(w).add(e);
        E++;
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

    public void removeEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        removeEdge(v, w);
    }

    public List<Set<Integer>> getConnectedVertex() {

        List<Set<Integer>> ret = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        for (Integer i : G.keySet()) {

            if (visited.contains(i)) continue;

            Set<Integer> component = new HashSet<>();
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

    public List<Graph> getConnectedComponent() {
        List<Set<Integer>> connectedvertexs = getConnectedVertex();
        List<Graph> cc = new LinkedList<>();
        for (Set<Integer> set : connectedvertexs) {
            cc.add(createSubgraph(set));
        }
        return cc;
    }

    private Graph createSubgraph(Set<Integer> vertex) {
        Graph subg = new Graph(vertex);
        for (Integer v : subg.G.keySet()) {
            for (Edge e : this.G.get(v)) {
                int w = e.other(v);
                if (subg.G.containsKey(w)) {
                    subg.addEdge(v,w);
                }
            }
        }
        return subg;
    }

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

        g.removeEdge(33, 8);
        List<Graph> cc = g.getConnectedComponent();
        System.out.println(cc);
        System.out.println(g.E());
    }

}
