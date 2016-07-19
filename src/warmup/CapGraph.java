
package warmup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author Nengyun Zhang
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 * Digraph
 *
 * Use helper class SCC to find the strongly connected components.
 */
public class CapGraph implements Graph {

	private HashMap<Integer, HashSet<Integer>> adj;


	public CapGraph(HashMap<Integer, HashSet<Integer>> adj) {	// construct from a hashmap
		this.adj = new HashMap<>(adj); // java default copy constructor, copy both keys and values
	}

	public CapGraph() {	// construct an empty graph
		this(new HashMap<>());
	}

	public CapGraph(Graph g) { // copy constructor
		this(g.exportGraph());
	}

	@Override
	public void addVertex(int num) {

		if (adj.containsKey(num)) {
			throw new IllegalArgumentException("vertex " + num + " already exists.");
		}

		adj.put(num, new HashSet<>());
	}

	@Override
	public void addEdge(int from, int to) {

		if (!adj.containsKey(from)) {
			adj.put(from, new HashSet<>());
		}

		if (!adj.containsKey(to)) {
			adj.put(to, new HashSet<>());
		}

		adj.get(from).add(to);
	}

	@Override
	public Graph getEgonet(int center) {

		if (!adj.containsKey(center)) {
			throw new IllegalArgumentException("center " + center + " doesn't exist.");
		}

		HashMap<Integer, HashSet<Integer>> ego_adj = new HashMap<>();
		HashSet<Integer> adj_center = adj.get(center);

		ego_adj.put(center, adj_center);
		Graph egonet = new CapGraph(ego_adj);

		for (Integer i : adj_center) {
			for (Integer j : adj.get(i)) {
				if (j == center || adj_center.contains(j)) {
					egonet.addEdge(i, j);
				}
			}
		}

		return egonet;
	}

	@Override
	public List<Graph> getSCCs() { // get the strongly connected components

		SCC scc = new SCC(this);
		return scc.getSCCs();
	}

	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		return adj;
	}

	public Graph reverse() {	// get the reverse graph

		Graph rev_g = new CapGraph();

		for (Integer i : adj.keySet()) {
			rev_g.addVertex(i);
		}

		for (Integer i : adj.keySet()) {
			for (Integer j : adj.get(i)) {
				rev_g.addEdge(j, i);
			}
		}

		return rev_g;
	}

	public Iterable<Integer> getAdjacent(int v) {	// get the adjacent vertices

		if (!adj.containsKey(v)) {
			throw new IllegalArgumentException("vertex " + v + " doesn't exist.");
		}

		return adj.get(v);
	}

	public boolean containsVertex(int v) {
		return adj.containsKey(v);
	}

	public boolean containsEdge(int from, int to) {
		return containsVertex(from) && containsVertex(to) && adj.get(from).contains(to);
	}

	public Graph createSubgraph(Iterable<Integer> vertices) { // create subgraph on selected vetices

		HashSet<Integer> sub = new HashSet<>();

		for (Integer i : vertices) {
			if (!sub.add(i)) {
				throw new IllegalArgumentException("duplicated vertex " + i + " in createSubgraph.");
			}
		}

		Graph sub_g = new CapGraph();

		for (Integer i : sub) {
			sub_g.addVertex(i);
		}

		for (Integer i : sub) {
			for (Integer j : adj.get(i)) {
				if (sub.contains(j)) {
					sub_g.addEdge(i, j);
				}
			}
		}

		return sub_g;
	}



	public static void main(String[] args) {

		CapGraph g = new CapGraph();
		g.addVertex(0);
		g.addVertex(1);
		g.addEdge(0,1);
		g.addEdge(1,0);
		g.addEdge(0,2);
		g.addEdge(1,2);
		System.out.println(g.exportGraph());

		Graph gg = g.reverse();
		System.out.println(gg.exportGraph());

		CapGraph ggg = (CapGraph) gg;
		System.out.println(ggg.reverse().exportGraph());


	}

}
