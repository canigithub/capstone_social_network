package util;

import graph.Graph;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gerald on 7/20/16.
 */
public class GMLFileLoader {

    public static Graph UndirGraphLoader(String filename) {
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

        String line = nextLineWhen(sc, "\\s*directed\\s[01]"); // make sure it's undirected
        if (line.charAt(line.length()-1) == '1') {
            throw new IllegalArgumentException("must input undirect graph file");
        }

        Graph g = new Graph();

        nextLineWhen(sc, "\\s*(node)?\\s*\\[");  // make sure pointer move to vertex section

//        line = nextLineWhen(sc, "\\s*id\\s\\d+");


        while (sc.hasNextLine()) {
//            line = nextLineWhen(sc, "(\\s*id\\s\\d+)|(\\s*source\\s\\d+)|(\\s*target\\s\\d+)");
            line = nextLineWhen(sc, "\\s*(id|source|target)\\s\\d+");
            if (line == null) break;
            String[] token = line.split("\\s");
            System.out.println(token[token.length-2] + "," + token[token.length-1]);
        }

//        while (sc.hasNextLine()) {
//            System.out.println(sc.nextLine());
//        }

        sc.close();
        return null;
    }

    private static String nextLineWhen(Scanner sc, String regex) {

        Pattern p = Pattern.compile(regex);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher m = p.matcher(line);
            if (m.matches()) return line;
        }

        return null;
    }

    public static void main(String[] args) {

        Graph g = UndirGraphLoader(args[0]);
    }
}
