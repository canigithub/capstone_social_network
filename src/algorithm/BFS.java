package algorithm;

import graph.Edge;
import graph.Graph;
import util.GraphLoader;

import java.util.*;

/**
 * Breadth first search
 * given a source vertex, calculate all vertex weights and edge scores
 */
public class BFS {      // this need to be rewrited. all use hashmap

    private Graph g;
    private int source;
    private Map<Integer, Integer> weight;   // weight of each node
    private Map<Integer, Integer> distTo;   // dist from source to each node
    private Queue<Node> leaves;
    private Map<Edge, Double> edgeset;

    private class Node implements Comparable<Node> {
        public int vertex;
        public int dist;
        public Node(int v, int d) { vertex = v; dist = d; }
        public int compareTo(Node that) {return that.dist-dist;}    // maxpq (default is minpq)
        @Override
        public String toString() {return Integer.toString(vertex);}
    }

    public BFS(Graph g, int source) {

        if (g == null) {
            throw new NullPointerException("graph is null.");
        }

        if (!g.getGraph().containsKey(source)) {
            throw new IllegalArgumentException("source is not in graph.");
        }

        this.g = g;     // BFS operates on the graph itself and modify it's edge flow.
        this.source = source;
        weight = new HashMap<>();
        distTo = new HashMap<>();
        leaves = new PriorityQueue<>();
        edgeset = new HashMap<>();

        resetEdgeFlow();
        calcVertexWeight(); // this must be in constructor
                            // below are not necessarily in ctor.
        findLeaves();
        calcEdgeScore();
        for (Integer v : g.getGraph().keySet()) {
            for (Edge e : g.getGraph().get(v)) {
                edgeset.put(e, (double) Math.round(e.getFlow()*100)/100);
            }
        }
    }

    public void resetEdgeFlow() {
        for (Integer v : g.getGraph().keySet()) {
            for (Edge e : g.getGraph().get(v)) {
                e.setFlow(0);
            }
        }
    }

    private void findLeaves() {

        for (Integer v : g.getGraph().keySet()) {       // for each node
            boolean isLeaf = true;
            for (Edge e : g.getGraph().get(v)) {        // for each edge to v
                int w = e.other(v);                     // for each adj node to v
                if (distTo.get(w) > distTo.get(v)) {    // if exist dist(w) > dist(v)
                    isLeaf = false;                     // then not leaf
                    break;
                }
            }
            if (isLeaf) {
                leaves.add(new Node(v, distTo.get(v)));
            }
        }
    }

    private void calcVertexWeight() {

        Set<Integer> visited = new HashSet<>();     // vertex above
        Queue<Integer> q = new LinkedList<>();      // vertex below

        q.add(source);
        visited.add(source);

        weight.put(source, 1);
        distTo.put(source, 0);

        while (!q.isEmpty()) {

            int v = q.remove();

            for (Edge e : g.getGraph().get(v)) {
                int w = e.other(v);
                if (!visited.contains(w)) {     // if not visited, dist(w) = dist(v)+1
                    distTo.put(w, distTo.get(v)+1);
                    q.add(w);
                    visited.add(w);
                }
                else if (distTo.get(w) == distTo.get(v) - 1) {  //if visited, dist(w)=dist(v)-1 or dist(v)
                    if (!weight.containsKey(v)) {
                        weight.put(v, weight.get(w));
                    }
                    else {
                        weight.put(v, weight.get(v) + weight.get(w));
                    }
                }
            }
        }
    }

    private void calcEdgeScore() {

        Set<Integer> visited = new HashSet<>();     // vertex close to leafs
        Map<Integer, Double> vertexflow = new HashMap<>();

        for (Integer i : g.getGraph().keySet()) {
            vertexflow.put(i, 1.0);                 // all vertex receive 1 unit flow from source
        }

        for (Node n : leaves) {                     // put all vertex into search queue.
            visited.add(n.vertex);
        }

        while (!leaves.isEmpty()) {                 // leaves is now the search queue

            int v = leaves.remove().vertex;

            for (Edge e : g.getGraph().get(v)) {
                int w = e.other(v);
                if (distTo.get(w) == distTo.get(v) - 1) {   // if w is right above v
                    double flow = vertexflow.get(v) * (double) weight.get(w)/weight.get(v);
                    e.setFlow(flow);
                    vertexflow.put(w, vertexflow.get(w) + flow); // update vertex flow of w

                    if (!visited.contains(w)) {
                        visited.add(w);
                        leaves.add(new Node(w, distTo.get(w)));
                    }
                }
            }

        }
    }

    public Map<Edge, Double> getEdgeSet() {return new HashMap<>(edgeset);}

    public static void main(String[] args) {

        Graph g = GraphLoader.loadUndirGraph(args[0]);

        for (int s = 0; s < 34; ++s ) {
            BFS b = new BFS(g, s);
//        System.out.println(g);
//        System.out.println("distTo: " + b.distTo);
//        System.out.println("weight: " + b.weight);
//        System.out.println(b.edgeset);
            double flow = 0;
            for (Edge e : b.g.getGraph().get(s)) {
                flow += e.getFlow();
            }
            System.out.println(flow);
        }
    }
}
