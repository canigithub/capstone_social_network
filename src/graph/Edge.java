package graph;

import java.util.Map;
import java.util.Objects;

/**
 * Undirected edge for social network clustering.
 *
 */
public class Edge {

    private final int v;
    private final int w;
    private double score;

    public Edge(int v, int w, double score) {
        this.v = v;
        this.w = w;
        this.score = score;
    }

    public Edge(int v, int w) {
        this(v, w, 0);
    }

    public int other(int x) {
        if (x == v) return w;
        if (x == w) return v;
        throw new IllegalArgumentException("vertex " + x + " not in this edge");
    }

    public void setScore(double score) { this.score = score; }

    public double getScore() { return score; }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Edge) {
            Edge e = (Edge) o;
            return (v == e.v && w == e.w) || (v == e.w && w == e.v);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash*31 + Math.min(v, w);
        hash = hash*31 + Math.max(v, w);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(v + "-" + w);
        return sb.toString();
    }
}
