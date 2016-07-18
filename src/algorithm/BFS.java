package algorithm;

import graph.Edge;
import graph.Graph;
import util.GraphLoader;

import java.util.*;

/**
 * Breadth first search to count vertex weight and edge score
 * given a source vertex
 */
public class BFS {

    private Graph g;
    private final int source;
    private int[] weight;    // weight of each node, meaning # of shortest path through node.
    private int[] distTo;


    public BFS(Graph g, int source) {

        this.g = new Graph(g);
        this.source = source;
        weight = new int[g.V()];
        distTo = new int[g.V()];

        Set<Integer> visited = new HashSet<>();
        Set<Integer> inqueue = new HashSet<>();
        Queue<Integer> q = new LinkedList<>();
        q.add(source);
        inqueue.add(source);

        while (!q.isEmpty()) {  // need to consider an inconnected graph

//            System.out.println("v=" + q.peek() + " queue:" + q + " visited:" + visited + " inqueue:" + inqueue);

            int v = q.remove();
            inqueue.remove(v);
            visited.add(v);

            // case 1: is source
            if (v == source) {
                weight[v] = 1;
                distTo[v] = 0;
                for (Edge e : g.adj(v)) {
                    int w = e.other(v);
                    q.add(w);
                    inqueue.add(w);
                }
                continue;
            }

            // case 2: is nextTo source
            boolean nextToSource = false;
            for (Edge e : g.adj(v)) {
                if (e.other(v) == source) {
                    nextToSource = true;
                    break;
                }
            }

            if (nextToSource) {
                weight[v] = 1;
                distTo[v] = 1;
                for (Edge e : g.adj(v)) {
                    int w = e.other(v);
                    if (!visited.contains(w) && !inqueue.contains(w)) {
                        q.add(w);
                        inqueue.add(w);
                    }
                }
                continue;
            }

            // case 3: next to vertex j (not source)
            for (Edge e : g.adj(v)) {

                int w = e.other(v);

                if (!visited.contains(w) && !inqueue.contains(w)) {
                    q.add(w);
                    inqueue.add(w);
                    continue;
                }

                if (visited.contains(w) && distTo[v] == 0) {
                    weight[v] = weight[w];
                    distTo[v] = distTo[w]+1;
                }
                else if (visited.contains(w) && distTo[v] == distTo[w]+1) {
                    weight[v] += weight[w];
                }
            }

//            System.out.println("w=" + weight[v] + " d=" + distTo[v]);
        }
    }

    public static void main(String[] args) {

        Graph g = GraphLoader.loadUndirGraph(args[0]);

        BFS b = new BFS(g, 0);
        System.out.println(Arrays.toString(b.weight));
        System.out.println(Arrays.toString(b.distTo));

    }
}
