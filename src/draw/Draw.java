package draw;

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
 * draw graphs
 */
public class Draw {

    private static final Color GINGER = new Color(218, 165, 32);
    private static final Color BROWN = new Color(165, 42, 42);
    private static final Color GRASS = new Color(0, 128, 0);
    private static final Color GOLD = new Color(255, 215, 0);
    private static final Color INK = new Color(8, 141, 165);
    private static final Color PEACH = new Color(255, 218, 185);
    private static final Color SKY = new Color(198, 226, 255);
    private static final Color KHAKI = new Color(255, 246, 143);
    private static final Color FLUOR = new Color(204, 255, 0);
    private static final Color TOSCA = new Color(121, 64, 68);
    private static final Color DARK_RED = new Color(128, 0, 0);
    private static final Color LIGHT_RED = new Color(255, 102, 102);
    private static final Color DARK_CYAN = new Color(0, 128, 128);
    private static final Color LIGHT_CYAN = new Color(211, 255, 206);
    private static final Color DARK_BLUE = new Color(0, 51, 102);
    private static final Color LIGHT_BLUE = new Color(51, 153, 255);
    private static final Color DARK_PURPLE = new Color(102, 0, 102);
    private static final Color LIGHT_PURPLE = new Color(138, 43, 226);


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

        // good color: rgb(255,68,68),
        gs.addAttribute("ui.stylesheet", "node {shape: box; fill-color: rgb(255,68,68); " +
                "text-mode:normal; text-background-mode: plain; fill-mode: dyn-plain;}");
        gs.addAttribute("ui.antialias", true);

        for (Node n : gs) {
            n.addAttribute("ui.label", n.getId());
        }

        gs.display();
        return gs;
    }

    public static void drawGroupedSingleGraph(Graph g, List<Set<Integer>> group, String name) {

//        org.graphstream.graph.Graph gs = drawSingleGraph(g, name);
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


//        gs.addAttribute("ui.stylesheet", "graph {fill-color: pink; fill-mode: dyn-plain}");
        gs.addAttribute("ui.stylesheet", "node {shape: box; fill-color: black; " +
                "text-mode:normal; text-background-mode: plain; fill-mode: dyn-plain;}");
        gs.addAttribute("ui.antialias");

//        gs.addAttribute("ui.label", gs.getId());
//        for (Node n : gs) {
//            n.addAttribute("ui.label", n.getId());
//        }

        final int totalcolor = 12;
        int k = 0;
        for (Set<Integer> i : group) {
            for (Integer v : i) {
                Node node = gs.getNode(Integer.toString(v));
                switch (k) {
                    case 0:
                        node.addAttribute("ui.color", Color.RED); break;
                    case 1:
                        node.addAttribute("ui.color", Color.BLUE); break;
                    case 2:
                        node.addAttribute("ui.color", Color.GREEN); break;
                    case 3:
                        node.addAttribute("ui.color", Color.MAGENTA); break;
                    case 4:
                        node.addAttribute("ui.color", Color.PINK); break;
                    case 5:
                        node.addAttribute("ui.color", Color.YELLOW); break;
                    case 6:
                        node.addAttribute("ui.color", Color.CYAN); break;
                    case 7:
                        node.addAttribute("ui.color", Color.ORANGE); break;
                    case 8:
                        node.addAttribute("ui.color", DARK_PURPLE); break;
                    case 9:
                        node.addAttribute("ui.color", GRASS); break;
                    case 10:
                        node.addAttribute("ui.color", BROWN); break;
                    case 11:
                        node.addAttribute("ui.color", LIGHT_BLUE); break;
                    case 12:
                        node.addAttribute("ui.color", GINGER); break;
                    case 13:
                        node.addAttribute("ui.color", GOLD); break;
                    case 14:
                        node.addAttribute("ui.color", PEACH); break;
                    case 15:
                        node.addAttribute("ui.color", INK); break;
                    case 16:
                        node.addAttribute("ui.color", SKY); break;
                    case 17:
                        node.addAttribute("ui.color", KHAKI); break;
                    case 18:
                        node.addAttribute("ui.color", TOSCA); break;
                    case 19:
                        node.addAttribute("ui.color", FLUOR); break;
                    case 20:
                        node.addAttribute("ui.color", DARK_RED); break;
                    case 21:
                        node.addAttribute("ui.color", LIGHT_RED); break;
                    case 22:
                        node.addAttribute("ui.color", DARK_BLUE); break;
                    case 23:
                        node.addAttribute("ui.color", LIGHT_PURPLE); break;
                    case 24:
                        node.addAttribute("ui.color", DARK_CYAN); break;
                    case 25:
                        node.addAttribute("ui.color", LIGHT_CYAN); break;
                    case 26:
                        node.addAttribute("ui.color", Color.YELLOW); break;
                    case 27:
                        node.addAttribute("ui.color", Color.GRAY); break;
                    default:
                        node.addAttribute("ui.color", Color.WHITE); break;
                }
            }
            k++;
        }

//        for (Node n : gs) {
//            n.addAttribute("ui.label", n.getId());
//        }

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
