package util;

import draw.Draw;
import graph.Graph;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * gml: graph modeling language
 */
public class GMLFileLoader {

    public static Graph loadUndirGraph(String filename) {
        Pattern p_fname = Pattern.compile("(/\\w+)+\\.gml");
        Matcher m_fname = p_fname.matcher(filename);

        if (!m_fname.matches()) {
            throw new IllegalArgumentException("must input .gml file");
        }

        Scanner sc;
        try {
            sc = new Scanner(new File(filename));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String line = nextLineAt(sc, "\\s*directed\\s[01]"); // make sure it's undirected

        if (line == null) {
            throw new IllegalArgumentException("invalid file.");
        }

        if (line.charAt(line.length()-1) != '0') {
            throw new IllegalArgumentException("file must be undirected graph");
        }

        Graph g = new Graph();

        nextLineAt(sc, "\\s*(node)?\\s*\\[");  // make sure pointer move to vertex section

        int s = -1, t;
        while (true) {
//            line = nextLineAt(sc, "(\\s*id\\s\\d+)|(\\s*source\\s\\d+)|(\\s*target\\s\\d+)");
            line = nextLineAt(sc, "\\s*(id|source|target)\\s+\\d+");
            if (line == null) break;
            String[] tok = line.split("\\s+");
            if (tok[tok.length-2].equals("id")) {
                g.addVertex(Integer.parseInt(tok[tok.length-1]));
            }
            else if (tok[tok.length-2].equals("source")) {
                s = Integer.parseInt(tok[tok.length-1]);
            }
            else if (tok[tok.length-2].equals("target")) {
                if (s == -1) {  // assume input vertex id are all positive or zero.
                    throw new IllegalArgumentException("invalid file format. " +
                            "source is not folowed by target.");
                }
                t = Integer.parseInt(tok[tok.length-1]);
                g.addEdge(s, t);
                s = -1;
            }
            else {
                throw new IllegalArgumentException("incorrect file input format.");
            }
        }
        sc.close();
        return g;
    }

    private static String nextLineAt(Scanner sc, String regex) {
        Pattern p = Pattern.compile(regex);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            if (m.matches()) return line;
        }
        return null;
    }

    public static void main(String[] args) {

        Graph g = loadUndirGraph(args[0]);
        Draw.drawSingleGraph(g, "dolphins");
    }
}
