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
        this.cc = this.g.getConnectedComponent();

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

        Map<Edge, Double> edge_score = new HashMap<>();

        for (Integer i : g.getGraph().keySet()) {
            BFS bfs = new BFS(g, i);

            HashSet<Edge> added = new HashSet<>();
            for (Integer v : g.getGraph().keySet()) {
                for (Edge e : g.getGraph().get(v)) {
                    if (!added.contains(e)) {
                        added.add(e);
                        if (!edge_score.containsKey(e))
                            edge_score.put(e, e.getScore());
                        else
                            edge_score.put(e, edge_score.get(e) + e.getScore());
                        e.setScore(0);
                    }
                }
            }

        }

//        for (Integer v : g.getGraph().keySet()) {
//            System.out.print("v=" + v + ": ");
//            for (Edge e : g.getGraph().get(v)) {
//                System.out.print(e.other(v) + "(" + String.format("%.2f", e.getScore())+ ") ");
//            }
//            System.out.println();
//        }

        double max = -1;
        Edge ret = null;

        for (Edge e : edge_score.keySet()) {
            if (edge_score.get(e) > max) {
                ret = e;
                max = edge_score.get(e);
            }
        }

        return ret;
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

    public static void main(String[] args) {
        Graph g = GraphLoader.loadUndirGraph(args[0]);
        GirvanNewman gn = new GirvanNewman(g);
        System.out.println("before:");
        for (Graph gr : gn.cc) {
            System.out.println(gr.getGraph().keySet());
        }
        gn.split();
        System.out.println("after:");
        for (Graph gr : gn.cc) {
            System.out.println(gr.getGraph().keySet());
        }

//        Graph g = new Graph(2);
//        g.addEdge(0,1);
//        Graph g0 = new Graph(g);
//        Graph g1 = new Graph(g);
//        for (Edge e : g0.getGraph().get(0)) {e.setScore(5.0);}
//        for (Edge e : g0.getGraph().get(0)) {System.out.println(e.getScore());}
//        for (Edge e : g1.getGraph().get(0)) {System.out.println(e.getScore());}
    }
}
