package algorithm;

import draw.Draw;
import graph.Edge;
import graph.Graph;
import util.GMLFileLoader;
import util.GraphLoader;
import util.NoValidEdgeException;

import java.util.*;

/**
 * Girvan-Newman algorighm
 * use priority queue to manage partitions from different splits.
 * notice: passing graph as parameters using vertex set and global
 * variable original_graph since can't make too many copys of graph
 * (edges use a lot of memory)
 *
 * could find two local peaks before terminating splitting.
 * also, could terminate if a single node is splited.
 * also, could use a pq to manage connected component, then you can
 * extract the largest k components
 *
 * Improvment idea: could penalize a cut if one of it's split has size
 * less than a threshold.
 * also, need to consider case of dangling node.
 * A Feasible idea: adaptivly decrease min-set-size.
 *
 * Dangling node cases and single vertex cluster dealed. Should analysis
 * the difference with and without it. --> update: there's no sufficient
 *
 *
 * !!The modularity for karate club data on bisection is not solved yet!!
 *
 *
 *
 */
public class GirvanNewman {

    private static final String NEWLINE = System.getProperty("line.separator");
    private final Graph original_graph;     // original uncutted graph
    private List<Group> partitions;         // partitions from each cut. starting from 0 cut.
    private double maxModularity;

    public GirvanNewman(Graph g) {
        this(g, g.V()/3);
    }

    public GirvanNewman(Graph g, int max_pass) {
        original_graph = new Graph(g);      // copy the input graph
        partitions = new ArrayList<>();
        cluster(max_pass);
    }

    public void cluster(int max_pass) {
        // original uncutted graph: lastsplit = -1
        List<Set<Integer>> sets = original_graph.getConnectedVertex();
//        Group group = null;
        maxModularity = 0;
        int pass = 0;
        int localpeakcnt = 0;
        boolean decrease = true;
        final int minsplitsize = 3;

        while (pass < max_pass) {
            ++pass;
            System.out.print("pass " + String.format("%3d", pass) + ": ");

            List<Set<Integer>> temp = new LinkedList<>();
            for (Set<Integer> set : sets) {
                temp.add(set);
            }

            Group max_group = null;
            List<Set<Integer>> setsNextIter = new LinkedList<>();
            for (Set<Integer> set : sets) {

                if (set.size() <= minsplitsize) {  // if set size <= 2 it must generate 1 node
                    continue;
                }

                temp.remove(set);
                List<Set<Integer>> splitted = null;

                try {
                    splitted = split(set);
                }
                catch (NoValidEdgeException e) {
                    continue;
                }

                for (Set<Integer> s : splitted) {
                    temp.add(s);
                }

                Group group = new Group(temp);

                if (max_group == null) {
                    max_group = group;
                    for (Set<Integer> s : temp) {
                        setsNextIter.add(s);    // must copy by element since temp will be restored.
                    }
                }
                else if (group.compareTo(max_group) > 0) {
                    max_group = group;
                    setsNextIter = new LinkedList<>();
                    for (Set<Integer> s : temp) {
                        setsNextIter.add(s);
                    }
                }

                for (Set<Integer> s : splitted) {
                    temp.remove(s);
                }
                temp.add(set);
            }

            if (max_group == null) {
                System.out.print("no valid partition");
                break;
            }

            System.out.print("modularity=" + String.format("%.5f", max_group.modularity) + "  \t");
            if (pass % 4 == 0) System.out.println();

            if (Double.compare(max_group.modularity, maxModularity) < 0) {
                if (!decrease) {
                    localpeakcnt++;
                }
                decrease = true;
                if (localpeakcnt == 2) break;
            } else {
                maxModularity = max_group.modularity;
                int c = 0;
                for (Set<Integer> s : max_group.group) {
                    c += s.size();
                }
//                System.out.println("cluster total size = " + c);
                partitions.add(max_group);
                decrease = false;
            }

            sets = setsNextIter;


        }
    }

    private List<Set<Integer>> split(Set<Integer> set) throws NoValidEdgeException{  // split a connected graph

        Graph g = original_graph.createSubgraph(set);
        List<Set<Integer>> sets = new LinkedList<>();
        sets.add(set);

        if (sets.size() > 1) {
            throw new IllegalArgumentException("must feed a connected graph.");
        }


        while (sets.size() == 1) {          // as long as the graph is still connected, keep cutting
            Edge cutedge = findCutEdge(g);
            if (cutedge == null) {
                throw new NoValidEdgeException("no valid edge to cut");
            }

            g.removeEdge(cutedge);

            sets = g.getConnectedVertex();
        }

        return sets;
    }

    private Edge findCutEdge(Graph g) {     // find an edge to cut with exceptions use pq

        Map<Edge, Double> edgeflow = new HashMap<>();

        for (Integer i : g.getGraph().keySet()) {

            BFS bfs = new BFS(g, i);

            Map<Edge, Double> edgeset = bfs.getEdgeSet();
            for (Edge e : edgeset.keySet()) {
                if (!edgeflow.containsKey(e)) edgeflow.put(e, edgeset.get(e));
                else edgeflow.put(e, edgeflow.get(e) + edgeset.get(e));
            }
        }

        Edge maxedge = null;
        for (Edge e : edgeflow.keySet()) {
            e.setFlow(edgeflow.get(e));             // set the edge flow to total flow.
        }


//        for (Edge e : edgeflow.keySet()) {
//            if (e.compareTo(maxedge) < 0) {
//                maxedge = e;
//            }
//        }


        Queue<Edge> pq = new PriorityQueue<>();
        for (Edge e : edgeflow.keySet()) {
            pq.add(e);
        }

        maxedge = pq.remove();

        int v = maxedge.either();
        int w = maxedge.other(v);
        while (!pq.isEmpty() && (g.getGraph().get(v).size() == 1 || g.getGraph().get(w).size() == 1)) {
            maxedge = pq.remove();              // if after cut this edge, a single node cluster appear
            v = maxedge.either();               // then find the next max edge.
            w = maxedge.other(v);
        }

        if (pq.isEmpty()) return null;

        return maxedge;
    }

    public List<Set<Integer>> getBestCluster() {
        if (partitions.size() == 0) {
            System.out.println("no good partition is found.");
            return null;
        }


        return partitions.get(partitions.size()-1).group;
    }

    public Graph getOriginalGraph() {return original_graph;}

    private class Group implements Comparable<Group>{

        public final List<Set<Integer>> group;
        public final double modularity;

        public Group(List<Set<Integer>> sets) {
//            this.group = sets;
            this.group = new LinkedList<>();    // must deep copy of set
            for (Set<Integer> set : sets) {
                this.group.add(set);
            }
            modularity = calcModularity(sets);
        }

        public double calcModularity(List<Set<Integer>> sets) {

            int k = sets.size();
            final double[][] mat = new double[k][k];

            for (int i = 0; i < k; ++i) {
                for (int j = i; j < k; ++j) {
                    mat[i][j] = interCommunityEdgeCount(sets, i, j)/(double) original_graph.E();
                    mat[j][i] = mat[i][j];
                }
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

//            printModularity(mat);

            // penalize small size
//            int minsize = original_graph.V();
//            for (Set<Integer> set : sets) {
//                minsize = Math.min(minsize, set.size());
//            }
//            m = 1/Math.exp(1/m);
            return m;
        }

        public int interCommunityEdgeCount(List<Set<Integer>> sets, int i, int j) {

            if (i == j) {
                Graph graph = original_graph.createSubgraph(sets.get(i));
                return graph.E();
            }

            Set<Integer> ci = sets.get(i), cj = sets.get(j);
            int count = 0;

            for (Integer v : ci) {
                for (Edge e : original_graph.getGraph().get(v)) {
                    int w = e.other(v);
                    if (cj.contains(w)) count++;
                }
            }
            return count;
        }

        private void printModularity(double[][] mat) {
            int k = mat.length;
            for (int i = 0; i < k; ++i) {
                System.out.print("[");
                for (int j = 0; j < k; ++j) {
                    System.out.print(" " + String.format("%.2f", mat[i][j]));
                }
                System.out.println(" ]");
            }
        }

        @Override
        public int compareTo(Group that) {
            return Double.compare(modularity, that.modularity);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("modularity=" + modularity + NEWLINE);
            for (Set<Integer> set : group) {
                sb.append(set + NEWLINE);
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) {
//        Graph g = GraphLoader.loadUndirGraph(args[0]);
        Graph g = GMLFileLoader.loadUndirGraph(args[0]);

        Draw.drawSingleGraph(g, "funny");

//        GirvanNewman gn = new GirvanNewman(g);
//        if (gn.getBestCluster() != null) {
//            System.out.println(NEWLINE + "splits of best cluster: " + gn.getBestCluster().size());
//            Draw.drawGroupedSingleGraph(gn.getOriginalGraph(), gn.getBestCluster(), "funny");
//            int c = 0;
//            for (Set<Integer> s : gn.getBestCluster()) {
//                c += s.size();
//            }
//            System.out.println(g.V() + ", " + c);
//        } else {
//            Draw.drawSingleGraph(g, "funny");
//        }

    }
}
