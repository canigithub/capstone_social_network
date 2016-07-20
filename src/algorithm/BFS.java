package algorithm;

import draw.Draw;
import graph.Edge;
import graph.Graph;
import util.GraphLoader;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Breadth first search
 * given a source vertex, calculate all vertex weights and edge scores
 */
public class BFS {

    private Graph g;
    private int source;
    private int[] weight;    // weight of each node, meaning # of shortest path through node.
    private int[] distTo;
    private Set<Integer> leaves;
//    private PriorityQueue<Node> leaves;

    private class Node implements Comparable<Node> {

        public int vertex;
        public int dist;

        public Node(int v, int d) {
            vertex = v;
            dist = d;
        }

        public int compareTo(Node other) {
            return other.dist - dist;       // default is min-pq but we need max-pq.
        }
    }

    public BFS(Graph g, int source) {

        this.g = g;
        this.source = source;
        weight = new int[g.V()];
        distTo = new int[g.V()];
        leaves = new HashSet<>();
//        leaves = new PriorityQueue<>();

//        resetEdgeScore();
        calcVertexWeight();
        calcEdgeScore();
    }

    public void resetEdgeScore() {
        for (Integer v : g.getGraph().keySet()) {
            for (Edge e : g.getGraph().get(v)) {
                e.setScore(0);
            }
        }
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
                if (isleaf) leaves.add(v);
//                if (isleaf) leaves.add(new Node(v, distTo[v]));
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
                if (isleaf) leaves.add(v);
//                if (isleaf) leaves.add(new Node(v, distTo[v]));
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
            if (isleaf) leaves.add(v);
//            if (isleaf) leaves.add(new Node(v, distTo[v]));
        }
    }

    private void calcEdgeScore() {      // found problem: need to follow the path of the shortest path 

        Set<Integer> visited = new HashSet<>(); // record nodes close to leafs
        Set<Integer> queue = new HashSet<>(); // record nodes far from leafs
        Queue<Integer> q = new LinkedList<>();
        for (Integer i : leaves) {
            q.add(i);
            queue.add(i);
        }

//        while (!leaves.isEmpty()) {
//            int i = leaves.remove().vertex;
//            q.add(i);
//            queue.add(i);
//        }

//        for (Node n : leaves) {     // put all stuffs into queue.
//            queue.add(n.vertex);
//        }

        // pq version
//        while (!leaves.isEmpty()) {
//
//            int v = leaves.remove().vertex;
//            queue.remove(v);
//            visited.add(v);
//
//            if (leaves.contains(v)) {  // if is leaf
//                for (Edge e : g.getGraph().get(v)) {
//                    int w = e.other(v);
//                    e.setScore((double)weight[w]/weight[v]);
//                    if (!queue.contains(w)) { // if not in search queue, add to it
//                        leaves.add(new Node(w, distTo[w]));
//                        queue.add(w);
//                    }
//                }
//                continue;
//            }
//
//            double score = 1;
//            for (Edge e : g.getGraph().get(v)) {
//                int w = e.other(v);
//                if (visited.contains(w)) { // w below v
//                    score += e.getScore();
//                }
//            }
//
//            for (Edge e : g.getGraph().get(v)) {
//                int w = e.other(v);
//                if (!visited.contains(w)) {
//                    e.setScore(score * (double)weight[w]/weight[v]);
//                    if (!queue.contains(w)) {
//                        leaves.add(new Node(w, distTo[w]));
//                        queue.add(w);
//                    }
//                }
//            }
//        }


        // queue version
        while (!q.isEmpty()) {

            int v = q.remove();
            queue.remove(v);
            visited.add(v);

            if (leaves.contains(v)) {  // if is leaf
                for (Edge e : g.getGraph().get(v)) {
                    int w = e.other(v);
//                    e.setScore((double)weight[w]/weight[v]);
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
//                    e.setScore(score * (double)weight[w]/weight[v]);
                    e.setScore(score * (double)weight[w]/weight[v]);
                    if (!queue.contains(w)) {
                        q.add(w);
                        queue.add(w);
                    }
                }
            }
        }
    }

    public void printEdgeScore() {

        Map<Edge, Double> edgescore = new HashMap<>();

        for (Integer i : g.getGraph().keySet()) {
            for (Edge e : g.getGraph().get(i)) {
                edgescore.put(e, e.getScore());
            }
        }

        for (Edge e : edgescore.keySet()) {
            System.out.print(e.toString() + "(" + String.format("%.2f", edgescore.get(e)) + ")");
        }
    }

    public static void main(String[] args) {

        Graph g = GraphLoader.loadUndirGraph(args[0]);

        BFS b = null;
        for (int s = 6; s >= 0; --s) {
            b = new BFS(g, s);
        }
        b.printEdgeScore();


//        List<Graph> list = new LinkedList<>();
//        list.add(g);
//        Draw d = new Draw(list);
//
//        d.gs.getNode(Integer.toString(s)).addAttribute("ui.color", Color.BLUE);
//
//        for (Node i : b.leaves) {
//            d.gs.getNode(Integer.toString(i.vertex)).addAttribute("ui.color", Color.RED);
//        }
//        d.gs.display();

//        System.out.println(Arrays.toString(b.weight));
//        for (int v = 0; v < g.V(); ++v) {
//            System.out.print("v=" + v + ": ");
//            for (Edge e : g.getGraph().get(v)) {
//                System.out.print(String.format("%.2f", e.getScore()) + ", ");
//            }
//            System.out.println();
//        }
//        System.out.println(b.leafs);
    }
}
