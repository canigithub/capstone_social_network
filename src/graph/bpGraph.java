package graph;

import util.GraphLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * this graph stores the yahoo ads market data.
 * this is a bipartite graph contains 459678 ads and 193582 buyers
 */
public class bpGraph{

    private int V;              // # of vertices
    private int E;              // # of edges
    private Set<Integer>[] adj; // adjacency list

    public bpGraph(int V) {

        this.V = V;

        adj = new Set[this.V];
        for (int i = 0; i < adj.length; ++i) {
            adj[i] = new HashSet<>();
        }
    }

    public boolean addEdge(int from, int to) {

        if (from < 0 || from > V-1 || to < 0 || to > V-1) {
            throw new IndexOutOfBoundsException("vertex out of range @ bpGraph.addEdge");
        }

        return adj[from].add(to);
    }

    public static void main(String[] args) {

        bpGraph g = GraphLoader.loadYahooGraph(args[0]);

//        bpGraph bg = new bpGraph(5);


    }

}
