package graph;

/* See restrictions in Graph.java. */

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Set;

/** The shortest paths through an edge-weighted graph.
 *  By overriding methods getWeight, setWeight, getPredecessor, and
 *  setPredecessor, the client can determine how to represent the weighting
 *  and the search results.  By overriding estimatedDistance, clients
 *  can search for paths to specific destinations using A* search.
 *  @author Leslie Yang
 */
public abstract class ShortestPaths {

    /** The shortest paths in G from SOURCE. */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public ShortestPaths(Graph G, int source, int dest) {
        _G = G;
        _source = source;
        _dest = dest;
        _comp = new CompareVertices();

    }

    /** A comparator. */
    private class CompareVertices implements Comparator<Integer> {
        @Override
        public int compare(Integer a, Integer b) {
            double x = estimatedDistance(a) + getWeight(a);
            double y = estimatedDistance(b) + getWeight(b);
            if (y <= x) {
                return 1;
            }
            return -1;
        }
    }



    /** Initialize the shortest paths.  Must be called before using
     *  getWeight, getPredecessor, and pathTo. */
    public void setPaths() {
        Set<Integer> visited = new HashSet<Integer>();
        PriorityQueue<Integer> queueofNext =
                new PriorityQueue<Integer>(_G.vertexSize(), _comp);
        for (int vertex: _G.vertices()) {
            setWeight(vertex, Double.MAX_VALUE);
            queueofNext.add(vertex);
            setPredecessor(vertex, 0);
        }
        setWeight(_source, 0);
        setPredecessor(_source, 0);
        queueofNext.add(_source);
        boolean found = false;
        while ((!queueofNext.isEmpty()) && (!found)) {
            int current = queueofNext.poll();
            visited.add(current);
            if (current == _dest) {
                found = true;
                return;
            }
            for (int next: _G.successors(current)) {
                double costsofar = getWeight(next);
                double newcost = getWeight(current) + getWeight(current, next);
                double costfunction = costsofar - newcost;
                if ((visited.contains(next)) && (costfunction < 0)) {
                    continue;
                } else if ((!queueofNext.contains(next))
                        || (costfunction > 0)) {
                    setWeight(next, newcost);
                    setPredecessor(next, current);
                    if (queueofNext.contains(next)) {
                        queueofNext.remove(next);
                        queueofNext.add(next);
                    }
                }
            }
        }
    }

    /** Returns the starting vertex. */
    public int getSource() {
        return _source;
    }

    /** Returns the target vertex, or 0 if there is none. */
    public int getDest() {
        return _dest;
    }

    /** Returns the current weight of vertex V in the graph.  If V is
     *  not in the graph, returns positive infinity. */
    public abstract double getWeight(int v);

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    protected abstract void setWeight(int v, double w);

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    public abstract int getPredecessor(int v);

    /** Set getPredecessor(V) to U. */
    protected abstract void setPredecessor(int v, int u);

    /** Returns an estimated heuristic weight of the shortest path from vertex
     *  V to the destination vertex (if any).  This is assumed to be less
     *  than the actual weight, and is 0 by default. */
    protected double estimatedDistance(int v) {
        return 0.0;
    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    protected abstract double getWeight(int u, int v);

    /** Returns a list of vertices starting at _source and ending
     *  at V that represents a shortest path to V.  Invalid if there is a
     *  destination vertex other than V. */
    public List<Integer> pathTo(int v) {
        ArrayList<Integer> path = new ArrayList<Integer>();
        int temp = v;
        path.add(temp);
        while (temp != getSource()) {
            path.add(getPredecessor(temp));
            temp = getPredecessor(temp);

        }
        Collections.reverse(path);
        return path;
    }

    /** Returns a list of vertices starting at the source and ending at the
     *  destination vertex. Invalid if the destination is not specified. */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }

    /** The graph being searched. */
    protected final Graph _G;
    /** The starting vertex. */
    private final int _source;
    /** The target vertex. */
    private final int _dest;
    /** The comparator. */
    private Comparator<Integer> _comp;

}
