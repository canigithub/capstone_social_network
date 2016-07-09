package graph;

import util.GraphLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * this graph will store the yahoo ad market data.
 * this is a bipartite graph contains 459678 ads and 193582 buyers
 */
public class bpGraph{

    private int V;      // # of vertices
    private int E;      // # of edges

    public bpGraph(String f_name) {

    }

    public static void main(String[] args) {

        GraphLoader.loadYahooGraph(null, args[0]);
    }

}
