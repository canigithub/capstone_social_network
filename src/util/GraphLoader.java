/**
 * @author UCSD MOOC development team
 * 
 * Utility class to add vertices and edges to a graph
 *
 */
package util;

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
     * @param g: graph object
     * @param f_name: file path
     */
    public static void loadYahooGraph(graph.bpGraph g, String f_name) {

        Scanner sc;
        try {
            sc = new Scanner(new File(f_name));
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // read header
        String[] header = sc.nextLine().split(" ");
        int V = Integer.parseInt(header[0]), E = Integer.parseInt(header[1]);

        // now construct graph, E can be used for sanity check

        int v = 0;
        while (sc.hasNextLine()) {
            String[] toks = sc.nextLine().split(" ");
            // now add edges
        }
        sc.close();
    }

}
