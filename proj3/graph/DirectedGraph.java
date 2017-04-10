package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;

/** Represents a general unlabeled directed graph whose vertices are denoted by
 *  positive integers. Graphs may have self edges.
 *
 *  @author Leslie Yang
 */
public class DirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public int inDegree(int v) {
        if (!getallVertices().contains(v)) {
            return 0;
        }
        return findallpredecessors(v).size();
    }

    /** Return an arraylist that contains successors of V. */
    private ArrayList<Integer> findallpredecessors(int v) {
        ArrayList<Integer> allpredecessors = new ArrayList<Integer>();
        for (int node : getallVertices()) {
            if (contains(node, v)) {
                allpredecessors.add(node);
            }
        }
        return allpredecessors;
    }

    /** Return an arraylist that contains all predecessors of V. */
    ArrayList<Integer> getallpredecessors(int v) {
        return findallpredecessors(v);
    }

    @Override
    public int predecessor(int v, int k) {
        if (contains(v)) {
            ArrayList<Integer> allpredecessors = new ArrayList<Integer>();
            allpredecessors = findallpredecessors(v);
            if ((allpredecessors.size() == 0)
                    || (k >= allpredecessors.size())) {
                return 0;
            }
            return allpredecessors.get(k);
        }
        return 0;
    }

    /** Return an arraylist that contains predecessors of V
     * in edge insertion order. */
    private ArrayList<Integer> findallpredecessorsEdgesorder(int v) {
        ArrayList<Integer> allpredecessors = new ArrayList<Integer>();
        if (isDirected()) {
            for (ArrayList<Integer> edge : getallEdges()) {
                if (edge.get(1).equals(v)) {
                    allpredecessors.add(edge.get(0));
                }
            }
            return allpredecessors;
        } else {
            for (ArrayList<Integer> edge : getallEdges()) {
                if (edge.get(0).equals(v)) {
                    allpredecessors.add(edge.get(1));
                } else if (edge.get(1).equals(v)) {
                    allpredecessors.add(edge.get(0));
                }
            }
            return allpredecessors;
        }
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        if (!contains(v)) {
            return Iteration.iteration(new ArrayList<Integer>());
        } else {
            return Iteration.iteration(findallpredecessorsEdgesorder(v));
        }
    }


}
