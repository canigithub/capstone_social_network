package draw;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import org.jgraph.JGraph;
import org.jgraph.graph.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Iterator;

public class HelloWorld {

    public static void jgraph_tutorial() {
        GraphModel model = new DefaultGraphModel();
        GraphLayoutCache view = new GraphLayoutCache(model, new DefaultCellViewFactory());
        JGraph graph = new JGraph(model, view);

        DefaultGraphCell[] cells = new DefaultGraphCell[3];

        cells[0] = new DefaultGraphCell(new String("Hello"));
        GraphConstants.setBounds(cells[0].getAttributes(), new Rectangle2D.Double(20, 20, 40, 20));
        GraphConstants.setGradientColor(cells[0].getAttributes(), Color.ORANGE);
        GraphConstants.setOpaque(cells[0].getAttributes(), true);
        DefaultPort port0 = new DefaultPort();
        cells[0].add(port0);

        cells[1] = new DefaultGraphCell(new String("World!"));
        GraphConstants.setBounds(cells[1].getAttributes(), new Rectangle.Double(140, 140, 40, 20));
        GraphConstants.setGradientColor(cells[1].getAttributes(), Color.RED);
        GraphConstants.setOpaque(cells[1].getAttributes(), true);
        DefaultPort port1 = new DefaultPort();
        cells[1].add(port1);

        DefaultEdge edge = new DefaultEdge();
        edge.setSource(cells[0].getChildAt(0));
        edge.setTarget(cells[1].getChildAt(0));
        cells[2] = edge;
        GraphConstants.setLineEnd(edge.getAttributes(), GraphConstants.ARROW_CLASSIC);
        GraphConstants.setEndFill(edge.getAttributes(), true);

        graph.getGraphLayoutCache().insert(cells);
//        graph.setEnabled(false);

        JFrame frame = new JFrame();
        frame.getContentPane().add(new JScrollPane(graph));
        frame.pack();
        frame.setVisible(true);
    }

    public static void graph_stream_tutorial() {

    }


    public static void main(String[] args) {

        org.graphstream.graph.Graph g = new SingleGraph("tutorial_1");

        g.addNode("a");
        g.addNode("b");
        g.addNode("c");
        g.addEdge("ab", "a", "b");
        g.addEdge("bc", "b", "c");
        g.addEdge("ca", "c", "a");

        g.display();

        for (Node n : g) {
            System.out.println(n.getId());
        }

        for (org.graphstream.graph.Edge e : g.getEachEdge()) {
            System.out.println(e.getId());
        }

        Collection<Node> nodes = g.getNodeSet();

        Iterator<? extends Node> nnodes = g.getNodeIterator();


//        jgraph_tutorial();

    }
}
