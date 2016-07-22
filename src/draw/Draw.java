package draw;

import algorithm.GirvanNewman;
import graph.Edge;
import graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import util.GraphLoader;

import java.awt.Color;
import java.sql.Time;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * draw graphs
 */
public class Draw {

    public static org.graphstream.graph.Graph drawSingleGraph(Graph g, String name) {

        org.graphstream.graph.Graph gs = new SingleGraph(name);

        for (Integer i : g.getGraph().keySet()) {
            String id = Integer.toString(i);
            gs.addNode(id);
        }

        Set<Edge> drawededge = new HashSet<>();
        for (Integer v : g.getGraph().keySet()) {
            for (Edge e : g.getGraph().get(v)) {
                int w = e.other(v);
                if (!drawededge.contains(e)) {
                    drawededge.add(e);
                    gs.addEdge(e.toString(), Integer.toString(v), Integer.toString(w));
                }
            }
        }

        gs.addAttribute("ui.stylesheet", "node {shape: box; fill-color: orange; " +
                "text-mode:normal; text-background-mode: plain; fill-mode: dyn-plain;}");
        gs.addAttribute("ui.antialias", true);
        gs.display();
        return gs;
    }

    public static void drawGroupedSingleGraph(Graph g, List<Graph> group, String name) {

        org.graphstream.graph.Graph gs = drawSingleGraph(g, name);

        gs.addAttribute("ui.stylesheet", "node {shape: box; fill-color: orange; " +
                "text-mode:normal; text-background-mode: plain; fill-mode: dyn-plain;}");
        gs.addAttribute("ui.antialias", true);

        int k = 0;
        for (Graph i : group) {
            for (Integer v : i.getGraph().keySet()) {
                Node node;
                switch (k) {
                    case 0:
                        node = gs.getNode(Integer.toString(v));
                        node.addAttribute("ui.color", Color.RED);
                        break;
                    case 1:
                        node = gs.getNode(Integer.toString(v));
                        node.addAttribute("ui.color", Color.BLACK);
                    default:
                }
            }
            k++;
        }

        gs.display();
    }

    public static void main(String[] args) {

        Graph g = GraphLoader.loadUndirGraph(args[0]);
        drawSingleGraph(g, "funny");


//        long t0 = System.currentTimeMillis();
//        System.out.println("t0 = " + t0);
//        Graph g = GraphLoader.loadUndirGraph(args[0]);
//        long t1 = System.currentTimeMillis();
//        System.out.println("load: " + (t1-t0));
//        GirvanNewman gn = new GirvanNewman(g);
//        long t2 = System.currentTimeMillis();
//        System.out.println("split: " + (t2-t1));
//        Draw d = new Draw(gn.getGraph(), gn.getConnectComponent());
//        long t3 = System.currentTimeMillis();
//        System.out.println("draw: " + (t3-t2));

    }
}
