package graph;

import util.GraphLoader;

import java.util.HashSet;
import java.util.Set;

/**
 * this graph stores the yahoo ads market data.
 * for the yahoo ads data, it's a bipartite graph contains 459678 ads and 193582 buyers
 *
 * gonna use Ford-Fulkerson and push-relabel alg to partition the graph into ads and
 * buyers. In this dataset, the two algorithm should have similar performance.
 *
 * to partition the original graph, walk the graph and color every adjacent
 * vertex differently. ---> incorrect. which means you have to know a node's original
 * class, which is essentially what is looked for.
 * Therefor, PARTITION THE GRAPH CAN'T CLASSIFY THE ADS FROM THE BIDS!
 *
 */
public class BipGraph {

    private int V;              // # of vertices, plus source and sink
    private int E;              // # of edges
    private Set<Integer>[] adj; // adjacency list

    public BipGraph(int V) {

        this.V = V;         // 2 more for source and sink
        this.E = 0;

        adj = new Set[this.V];
        for (int i = 0; i < this.V; ++i) {
            adj[i] = new HashSet<>();
        }
    }

    public boolean addEdge(int from, int to) {

        if (from < 0 || from > V-1 || to < 0 || to > V-1) {
            throw new IndexOutOfBoundsException("vertex out of range @ bpGraph.addEdge");
        }

        boolean b =  adj[from].add(to);
        if (b) E++;
        return b;
    }

//    public boolean containEdge(int from, int to) {
//        if (from < 0 || from > V-1 || to < 0 || to > V-1) {
//            throw new IndexOutOfBoundsException("vertex out of range @ bpGraph.addEdge");
//        }
//        return adj[from].contains(to) || adj[to].contains(from);
//    }

//    private int countEdge(int range_lo, int range_hi) {
//        HashSet<Integer> side = new HashSet<>();
//        for (int v = range_lo; v < range_hi; ++v) {
//            for (int w : adj[v]) {
//                side.add(w);
//            }
//        }
//        return side.size();
//    }

    public static void main(String[] args) {

        BipGraph g = GraphLoader.loadYahooGraph(args[0]);

        System.out.println("V = " + g.V);
        System.out.println("E = " + g.E);
    }

}
