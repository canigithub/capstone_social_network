package algorithm;

import graph.Edge;
import graph.Graph;
import util.GraphLoader;

import java.util.*;

/**
 * Breadth first search
 * given a source vertex, calculate all vertex weights and edge scores
 */
public class BFS {

    private Graph g;
    private final int source;
    private int[] weight;    // weight of each node, meaning # of shortest path through node.
    private int[] distTo;
    private List<Integer> leafs;

    public BFS(Graph g, int source) {

        this.g = new Graph(g);
        this.source = source;
        weight = new int[g.V()];
        distTo = new int[g.V()];
        leafs = new LinkedList<>();

        calcVertexWeight();
    }

    private void calcVertexWeight() {

        Set<Integer> visited = new HashSet<>();
        Set<Integer> inqueue = new HashSet<>();
        Queue<Integer> q = new LinkedList<>();
        q.add(source);
        inqueue.add(source);

        while (!q.isEmpty()) {  // need to consider an inconnected graph

            int v = q.remove();
            inqueue.remove(v);
            visited.add(v);

            boolean isleaf = true;

            // case 1: is source
            if (v == source) {
                weight[v] = 1;
                distTo[v] = 0;
                for (Edge e : g.adj(v)) {
                    isleaf = false;
                    int w = e.other(v);
                    q.add(w);
                    inqueue.add(w);
                }
                if (isleaf) leafs.add(v);
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
                    if (!visited.contains(w)) { // as long as a vertex is trying to enqueue its child, it's not leaf
                        isleaf = false;         // visited set is the vertex 'before' v.
                        if (!inqueue.contains(w)) {
                            q.add(w);
                            inqueue.add(w);
                        }
                    }
                }
                if (isleaf) leafs.add(v);
                continue;
            }

            // case 3: next to vertex j (not source)
            for (Edge e : g.adj(v)) {

                int w = e.other(v);

                if (!visited.contains(w)) {
                    isleaf = false;
                    if (!inqueue.contains(w)) {
                        q.add(w);
                        inqueue.add(w);
                    }
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
            if (isleaf) leafs.add(v);
        }
    }

    private void calcEdgeScore() {

    }

    public int weight(int v) {return weight[v];}

    public int distTo(int v) {return distTo[v];}

    public static void main(String[] args) {

        Graph g = GraphLoader.loadUndirGraph(args[0]);

        BFS b = new BFS(g, 0);
        System.out.println(Arrays.toString(b.weight));
        System.out.println(Arrays.toString(b.distTo));
        System.out.println(b.leafs);

    }
}
