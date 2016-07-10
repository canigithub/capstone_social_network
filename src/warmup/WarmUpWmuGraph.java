
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
public class WarmUpWmuGraph implements wmuGraph {

	private HashMap<Integer, HashSet<Integer>> adj;


	public WarmUpWmuGraph(HashMap<Integer, HashSet<Integer>> adj) {	// construct from a hashmap
		this.adj = new HashMap<>(adj); // java default copy constructor, copy both keys and values
	}

	public WarmUpWmuGraph() {	// construct an empty graph
		this(new HashMap<>());
	}

	public WarmUpWmuGraph(wmuGraph g) { // copy constructor
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
	public wmuGraph getEgonet(int center) {

		if (!adj.containsKey(center)) {
			throw new IllegalArgumentException("center " + center + " doesn't exist.");
		}

		HashMap<Integer, HashSet<Integer>> ego_adj = new HashMap<>();
		HashSet<Integer> adj_center = adj.get(center);

		ego_adj.put(center, adj_center);
		wmuGraph egonet = new WarmUpWmuGraph(ego_adj);

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
	public List<wmuGraph> getSCCs() { // get the strongly connected components

		WarmUpSCC warmUpScc = new WarmUpSCC(this);
		return warmUpScc.getSCCs();
	}

	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		return adj;
	}

	public wmuGraph reverse() {	// get the reverse graph

		wmuGraph rev_g = new WarmUpWmuGraph();

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

	public wmuGraph createSubgraph(Iterable<Integer> vertices) { // create subgraph on selected vetices

		HashSet<Integer> sub = new HashSet<>();

		for (Integer i : vertices) {
			if (!sub.add(i)) {
				throw new IllegalArgumentException("duplicated vertex " + i + " in createSubgraph.");
			}
		}

		wmuGraph sub_g = new WarmUpWmuGraph();

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

		WarmUpWmuGraph g = new WarmUpWmuGraph();
		g.addVertex(0);
		g.addVertex(1);
		g.addEdge(0,1);
		g.addEdge(1,0);
		g.addEdge(0,2);
		g.addEdge(1,2);
		System.out.println(g.exportGraph());

		wmuGraph gg = g.reverse();
		System.out.println(gg.exportGraph());

		WarmUpWmuGraph ggg = (WarmUpWmuGraph) gg;
		System.out.println(ggg.reverse().exportGraph());


	}

}
