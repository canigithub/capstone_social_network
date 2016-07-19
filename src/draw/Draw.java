package draw;

import algorithm.GirvanNewman;
import graph.Edge;
import graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import util.GraphLoader;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gerald on 7/19/16.
 */
public class Draw {

    public Draw(List<Graph> graphs) {

        org.graphstream.graph.Graph gs = new SingleGraph("karate club");
        for (Graph g : graphs) {
            for (Integer i : g.getGraph().keySet()) {

                String id = Integer.toString(i);
                gs.addNode(id);
//                gs.getNode(id).addAttribute("ui.stylesheet", "node {fill-color: red;}");
            }
        }

        for (Node node : gs) {
            node.addAttribute("ui.label", node.getId());
        }

        Set<Edge> drawed = new HashSet<>();

        for (Graph g : graphs) {

            for (Integer v : g.getGraph().keySet()) {
                for (Edge e : g.getGraph().get(v)) {
                    int w = e.other(v);
                    if (!drawed.contains(e)) {
                        drawed.add(e);
                        gs.addEdge(e.toString(), Integer.toString(v), Integer.toString(w));
                    }
                }
            }
        }


        gs.addAttribute("ui.stylesheet", "node {shape: box; fill-color: orange; " +
                "text-mode:normal; text-background-mode: plain; fill-mode: dyn-plain;}");

        int k = 0;
        for (Graph g : graphs) {
            for (Integer i : g.getGraph().keySet()) {
                switch (k) {
                    case 0:
                        gs.getNode(Integer.toString(i)).addAttribute("ui.color", Color.BLACK);
                        break;
                    case 1:
                        gs.getNode(Integer.toString(i)).addAttribute("ui.color", Color.RED);
                    default:
                }
            }
            k++;
        }

        gs.addAttribute("ui.antialias", true);
        gs.display();
    }

    public static void main(String[] args) {
        Graph g = GraphLoader.loadUndirGraph(args[0]);
        GirvanNewman gn = new GirvanNewman(g);
        Draw d = new Draw(gn.getConnectComponent());
    }
}
