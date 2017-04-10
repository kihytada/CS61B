package graph;
import java.util.HashMap;

/* See restrictions in Graph.java. */

/** A partial implementation of ShortestPaths that contains the weights of
 *  the vertices and the predecessor edges.   The client needs to
 *  supply only the two-argument getWeight method.
 *  @author Leslie Yang
 */
public abstract class SimpleShortestPaths extends ShortestPaths {

    /** The shortest paths in G from SOURCE. */
    public SimpleShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public SimpleShortestPaths(Graph G, int source, int dest) {

        super(G, source, dest);
        _matchweights = new HashMap<Integer, Double>();
        _matchpredecessors = new HashMap<Integer, Integer>();

    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    @Override
    protected abstract double getWeight(int u, int v);

    @Override
    public double getWeight(int v) {
        if (!_G.contains(v)) {
            return Double.MAX_VALUE;
        }
        return _matchweights.get(v);

    }

    @Override
    protected void setWeight(int v, double w) {
        _matchweights.put(v, w);
    }

    @Override
    public int getPredecessor(int v) {
        if (!_matchpredecessors.containsKey(v)) {
            return 0;
        }
        return _matchpredecessors.get(v);

    }

    @Override
    protected void setPredecessor(int v, int u) {
        _matchpredecessors.put(v, u);
    }


    /** Edge weights matching. */
    protected HashMap<Integer, Double> _matchweights;
    /** Predecessor matching. */
    protected HashMap<Integer, Integer> _matchpredecessors;


}
