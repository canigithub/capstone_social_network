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
    private Set<Integer> leafs;

    public BFS(Graph g, int source) {

//        this.g = new Graph(g);
        this.g = g;
        this.source = source;
        weight = new int[g.V()];
        distTo = new int[g.V()];
        leafs = new HashSet<>();

        calcVertexWeight();
        calcEdgeScore();
    }

    private void calcVertexWeight() {

        Set<Integer> visited = new HashSet<>();
        Set<Integer> queue = new HashSet<>();
        Queue<Integer> q = new LinkedList<>();
        q.add(source);
        queue.add(source);

        while (!q.isEmpty()) {  // need to consider an inconnected graph

            int v = q.remove();
            queue.remove(v);
            visited.add(v);

            boolean isleaf = true;

            // case 1: is source
            if (v == source) {
                weight[v] = 1;
                distTo[v] = 0;
                for (Edge e : g.getGraph().get(v)) {
                    isleaf = false;
                    int w = e.other(v);
                    q.add(w);
                    queue.add(w);
                }
                if (isleaf) leafs.add(v);
                continue;
            }

            // case 2: is nextTo source
            boolean nextToSource = false;
            for (Edge e : g.getGraph().get(v)) {
                if (e.other(v) == source) {
                    nextToSource = true;
                    break;
                }
            }

            if (nextToSource) {
                weight[v] = 1;
                distTo[v] = 1;
                for (Edge e : g.getGraph().get(v)) {
                    int w = e.other(v);
                    if (!visited.contains(w)) { // a vertex is trying to enqueue child, not leaf
                        isleaf = false;         // visited set is the vertex 'before' v.
                        if (!queue.contains(w)) {
                            q.add(w);
                            queue.add(w);
                        }
                    }
                }
                if (isleaf) leafs.add(v);
                continue;
            }

            // case 3: next to vertex j (not source)
            for (Edge e : g.getGraph().get(v)) {

                int w = e.other(v);

                if (!visited.contains(w)) {
                    isleaf = false;
                    if (!queue.contains(w)) {
                        q.add(w);
                        queue.add(w);
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

        Set<Integer> visited = new HashSet<>(); // record nodes close to leafs
        Set<Integer> queue = new HashSet<>(); // record nodes far from leafs
        Queue<Integer> q = new LinkedList<>();
        for (Integer i : leafs) {
            q.add(i);
            queue.add(i);
        }

        while (!q.isEmpty()) {

            int v = q.remove();
            queue.remove(v);
            visited.add(v);

            if (leafs.contains(v)) {  // if is leaf
                for (Edge e : g.getGraph().get(v)) {
                    int w = e.other(v);
                    e.setScore((double)weight[w]/weight[v]);
                    if (!queue.contains(w)) { // if not in search queue, add to it
                        q.add(w);
                        queue.add(w);
                    }
                }
                continue;
            }

            double score = 1;
            for (Edge e : g.getGraph().get(v)) {
                int w = e.other(v);
                if (visited.contains(w)) { // w below v
                    score += e.getScore();
                }
            }

            for (Edge e : g.getGraph().get(v)) {
                int w = e.other(v);
                if (!visited.contains(w)) {
                    e.setScore(score * (double)weight[w]/weight[v]);
                    if (!queue.contains(w)) {
                        q.add(w);
                        queue.add(w);
                    }
                }
            }
        }
    }

    public int[] getWeight() {return weight;}

    public static void main(String[] args) {

        Graph g = GraphLoader.loadUndirGraph(args[0]);
        BFS b = new BFS(g, 4);
        System.out.println(Arrays.toString(b.weight));

        for (int v = 0; v < g.V(); ++v) {
            System.out.print("v=" + v + ": ");
            for (Edge e : g.getGraph().get(v)) {
                System.out.print(String.format("%.2f", e.getScore()) + ", ");
            }
            System.out.println();
        }
        System.out.println(b.leafs);
    }
}
