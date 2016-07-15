/**
 * @author UCSD MOOC development team
 * 
 * Utility class to add vertices and edges to a graph
 *
 */
package util;

import graph.BipGraph;
import warmup.Graph;

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

    /**
     * Construct a graph from a file.
     * The file is in Metis format. There is one header line: N M
     * indicating the total number of nodes N and the total number of edges M.
     * Following the header line, there are N adjacency list lines, one for
     * each node, in order. the vertex is 1-named
     *
     * notice: for this yahoo ads data, 459678 ads is in the front part.
     */
    public static BipGraph loadYahooGraph(String f_name) {

        Scanner sc;
        try {
            sc = new Scanner(new File(f_name));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // read header, E can be used for sanity check later.
        String[] header = sc.nextLine().split(" ");
        int V = Integer.parseInt(header[0]);

        // construct graph use V, E is for later sanity check
        BipGraph g = new BipGraph(V);
        final int ads = 459678;

        int v = 0;
        System.out.print("loading");
        while (sc.hasNextLine() && v < ads) {
            String[] toks = sc.nextLine().split(" ");
            for (String s : toks) {
                g.addEdge(v, Integer.parseInt(s)-1);
            }
            v++;
            if (v % (ads/10) == 0) System.out.print(".." + Math.round((float) 100*v/ads) + "%");
        }
        System.out.println();
        sc.close();
        return g;
    }
}
