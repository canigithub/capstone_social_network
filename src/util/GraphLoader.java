/**
 * @author UCSD MOOC development team
 * 
 * Utility class to add vertices and edges to a graph
 *
 */
package util;

import graph.UndirGraph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import warmup.Graph;

import java.awt.Color;
import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class GraphLoader {
    /**
     * Loads graph with data from a file.
     * The file should consist of lines with 2 integers each, corresponding
     * to a "from" vertex and a "to" vertex.
     */ 
    public static void loadGraph(Graph g, String filename) {
        Set<Integer> seen = new HashSet<Integer>();
        Scanner sc;
        try {
            sc = new Scanner(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        // Iterate over the lines in the file, adding new
        // vertices as they are found and connecting them with edges.
        while (sc.hasNextInt()) {
            int v1 = sc.nextInt();
            int v2 = sc.nextInt();
            if (!seen.contains(v1)) {
                g.addVertex(v1);
                seen.add(v1);
            }
            if (!seen.contains(v2)) {
                g.addVertex(v2);
                seen.add(v2);
            }
            g.addEdge(v1, v2);
        }
        
        sc.close();
    }

    public static UndirGraph loadUndirGraph(String filename) {
        Scanner sc;
        try {
            sc = new Scanner(new File(filename));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String[] header = sc.nextLine().split(" ");
        if (header.length != 2) {
            throw new IllegalArgumentException("incorrect input file format.");
        }
        int V = Integer.parseInt(header[0]);
        UndirGraph g = new UndirGraph(V);

        org.graphstream.graph.Graph gs = new SingleGraph("demo");
        for (int i = 0; i < V; ++i) {
            gs.addNode(Integer.toString(i));
        }

        while (sc.hasNextLine()) {
            String[] toks = sc.nextLine().split(" ");
            if (toks.length != 2) {
                throw new IllegalArgumentException("Incorret input file format.");
            }
            int v = Integer.parseInt(toks[0]);
            int w = Integer.parseInt(toks[1]);
            g.addEdge(v, w);
            gs.addEdge(toks[0]+toks[1], toks[0], toks[1]);
        }
        sc.close();
        System.out.println(gs.getId());
        for (Node node : gs) {
            node.addAttribute("ui.label", node.getId());
        }
        gs.addAttribute("ui.antialias", true);
        gs.addAttribute("ui.stylesheet", "node {shape: box;fill-color: blue, green, red;" +
                "text-mode:normal;text-background-mode: plain; fill-mode: dyn-plain;}");
        gs.getNode("0").addAttribute("ui.color", Color.RED);
        gs.getNode("33").addAttribute("ui.color", Color.RED);
        System.out.println(gs.getNode("0").getClass());
//        System.out.println(gs.getNode("33").getId());
        gs.display();

        return g;
    }

}
