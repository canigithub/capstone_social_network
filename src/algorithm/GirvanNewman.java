package algorithm;

import graph.Edge;
import graph.Graph;
import util.GraphLoader;

import java.util.*;

/**
 * Girvan-Newman algorighm
 */
public class GirvanNewman {

    private final Graph g0; // original graph
    private Graph g;
    private List<Graph> cc; // connected components of graph g

    public GirvanNewman(Graph g) {

        this.g0 = new Graph(g);
        this.g = new Graph(g);
//        this.cc = this.g.getConnectedComponent();
        split();
    }

    private void split() {

        cc = g.getConnectedComponent();
        while (cc.size() == 1) {
            Edge cut_edge = findCutEdge();
            g.removeEdge(cut_edge);
            cc = g.getConnectedComponent();
        }
    }

    private Edge findCutEdge() {

        Map<Edge, Double> edgeflow = new HashMap<>();

        for (Integer i : g.getGraph().keySet()) {
            BFS bfs = new BFS(g, i);

            Map<Edge, Double> edgeset = bfs.getEdgeSet();
            for (Edge e : edgeset.keySet()) {
                if (!edgeflow.containsKey(e)) edgeflow.put(e, edgeset.get(e));
                else edgeflow.put(e, edgeflow.get(e) + edgeset.get(e));
            }
        }

        double maxflow = -1;
        Edge maxedge = null;

        for (Edge e : edgeflow.keySet()) {
            if (edgeflow.get(e) > maxflow) {
                maxflow = edgeflow.get(e);
                maxedge = e;
            }
        }
//        System.out.println(maxedge + " " + String.format("%.2f", maxflow));
        return maxedge;
    }

    public double modularity() {

        int k = cc.size();
        double[][] mat = new double[k][k];

        for (int i = 0; i < k; ++i) {
            for (int j = i; j < k; ++j) {
                    mat[i][j] = (double) interCommunityEdgeCount(i, j)/g0.E();
                    mat[j][i] = mat[i][j];
            }
        }

        for (int i = 0; i < k; ++i) {
            System.out.print("[");
            for (int j = 0; j < k; ++j) {
                System.out.print(" " + String.format("%.2f", mat[i][j]));
            }
            System.out.println(" ]");
        }

        double[] a = new double[k];
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                a[i] += mat[i][j];
            }
        }

        double m = 0.;
        for (int i = 0; i < k; ++i) {
            m += (mat[i][i] - a[i]*a[i]);
        }

        return m;
    }

    private int interCommunityEdgeCount(int i, int j) {

        if (i == j) return cc.get(i).E();

        Graph ci = cc.get(i), cj = cc.get(j);

        int count = 0;
        for (Integer v : ci.getGraph().keySet()) {
            for (Edge e : g0.getGraph().get(v)) {
                int w = e.other(v);
                if (cj.getGraph().containsKey(w)) count++;
            }
        }
        return count;
    }

    public List<Graph> getConnectComponent() {return cc;}

    public Graph getGraph() {return g0;}

    public static void main(String[] args) {
        Graph g = GraphLoader.loadUndirGraph(args[0]);
        GirvanNewman gn = new GirvanNewman(g);
        gn.split();
        System.out.println(gn.cc.get(0).getGraph().keySet());
        System.out.println(gn.cc.get(1).getGraph().keySet());

    }
}
