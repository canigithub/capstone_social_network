
package graph;

import java.util.*;

public class SCC {

    private int group_num;
    private CapGraph g;
    private CapGraph rev_g;
    private HashSet<Integer> visited;
    private Stack<Integer> post_order;
    private HashMap<Integer, Integer> group;

    public SCC (CapGraph g) {

        this.g   = new CapGraph(g);
        rev_g = (CapGraph) this.g.reverse();
        visited  = new HashSet<>();
        post_order = new Stack<>();
        group    = new HashMap<>();

        dfs_1();

        group_num = -1;
        visited = new HashSet<>();  // need to clear visited hashset.

        dfs_2();
    }

    private void dfs_1() { // dfs phase 1: post order

        Iterator<Integer> it = g.exportGraph().keySet().iterator(); // iterator of reverse graph

        while (it.hasNext()) {
            dfs_1(it.next());
        }
    }

    private void dfs_1(int v) {

        if (!g.containsVertex(v)) {
            throw new IllegalArgumentException("vertex " + v + " doesn't exist.");
        }

        if (visited.contains(v)) {return;}

        visited.add(v);

        for (Integer w : g.getAdjacent(v)) {
            dfs_1(w);
        }

        post_order.push(v);
    }

    private void dfs_2() { // dfs phase 2: dfs on reverse graph

        while (!post_order.isEmpty()) {

            Integer v = post_order.pop();
            if (!visited.contains(v)) {
                ++group_num;
                dfs_2(v);
            }
        }
    }

    private void dfs_2(int v) {

        if (!rev_g.containsVertex(v)) {
            throw new IllegalArgumentException("vertex " + v + " doesn't exist.");
        }

        if (visited.contains(v)) {return;}

        visited.add(v);
        group.put(v, group_num);

        for (Integer w : rev_g.getAdjacent(v)) {
            dfs_2(w);
        }
    }

    public List<Graph> getSCCs() {

        List<Integer>[] scc_vertices = new List[group_num+1]; // group_num is the index of each group
        for (int i = 0; i < scc_vertices.length; ++i) {
            scc_vertices[i] = new LinkedList<>();
        }

        for (Integer i : group.keySet()) {
            scc_vertices[group.get(i)].add(i);
        }


        List<Graph> scc_subgraphs = new LinkedList<>();
        for (int i = 0; i < scc_vertices.length; ++i) {
            scc_subgraphs.add(g.createSubgraph(scc_vertices[i]));
        }

        return scc_subgraphs;
    }

    public static void main(String[] args) {

        CapGraph g = new CapGraph();
//        g.addEdge(32,44);
//        g.addEdge(32,50);
//        g.addEdge(44,50);
//        g.addEdge(18,44);
//        g.addEdge(25,65);
//        g.addEdge(65,23);
//        g.addEdge(25,18);
//        g.addEdge(18,23);
//        g.addEdge(25,23);
//        g.addEdge(23,25);
//        g.addEdge(23,18);

        g.addEdge(0,1);
        g.addEdge(0,5);
        g.addEdge(2,0);
        g.addEdge(2,3);
        g.addEdge(3,2);
        g.addEdge(3,5);
        g.addEdge(4,2);
        g.addEdge(4,3);
        g.addEdge(5,4);
        g.addEdge(6,0);
        g.addEdge(6,4);
        g.addEdge(6,8);
        g.addEdge(6,9);
        g.addEdge(7,6);
        g.addEdge(7,9);
        g.addEdge(8,6);
        g.addEdge(9,10);
        g.addEdge(9,11);
        g.addEdge(10,12);
        g.addEdge(11,4);
        g.addEdge(11,12);
        g.addEdge(12,9);

        SCC scc = new SCC(g);
        List<Graph> sub = scc.getSCCs();
        for (Graph gg : sub) {
            System.out.println(gg.exportGraph());
        }

    }
}
