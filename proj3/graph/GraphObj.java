package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/* See restrictions in Graph.java. */

/** A partial implementation of Graph containing elements common to
 *  directed and undirected graphs.
 *
 *  @author Leslie Yang
 */
abstract class GraphObj extends Graph {

    /** A new, empty Graph. */
    GraphObj() {
        allVertices = new ArrayList<Integer>();
        allEdges = new ArrayList<ArrayList<Integer>>();
    }

    @Override
    public int vertexSize() {
        return allVertices.size();
    }

    /** Return an arraylist that contains all vertices. */
    ArrayList<Integer> getallVertices() {
        return allVertices;
    }

    @Override
    public int maxVertex() {
        if (allVertices.isEmpty()) {
            return 0;
        }
        return Collections.max(allVertices);
    }

    @Override
    public int edgeSize() {
        return allEdges.size();
    }

    /** Return an arraylist that contains all edges. */
    ArrayList<ArrayList<Integer>> getallEdges() {
        return allEdges;
    }

    @Override
    public abstract boolean isDirected();

    @Override
    public int outDegree(int v) {
        if (!allVertices.contains(v)) {
            return 0;
        }
        return findallsuccessors(v).size();

    }


    @Override
    public abstract int inDegree(int v);

    @Override
    public boolean contains(int u) {
        return allVertices.contains(u);
    }

    @Override
    public boolean contains(int u, int v) {
        ArrayList<Integer> edge = new ArrayList<Integer>();
        edge.add(u);
        edge.add(v);
        if ((allVertices.contains(u)) && (allVertices.contains(v))) {
            if (isDirected()) {
                for (ArrayList<Integer> i : allEdges) {
                    if (i.equals(edge)) {
                        return true;
                    }
                }
                return false;

            } else {
                ArrayList<Integer> edgeSame = new ArrayList<Integer>();
                edgeSame.add(v);
                edgeSame.add(u);
                for (ArrayList<Integer> i : allEdges) {
                    if ((i.equals(edge)) || (i.equals(edgeSame))) {
                        return true;
                    }
                }
                return false;

            }
        }
        return false;

    }

    @Override
    public int add() {
        if (allVertices.size() == 0)  {
            allVertices.add(1);
            return 1;
        } else {
            if (keeptrack.isEmpty()) {
                allVertices.add(maxVertex() + 1);
                return maxVertex();
            } else {
                int toadd = keeptrack.poll();
                allVertices.add(toadd);
                return toadd;
            }
        }
    }

    @Override
    public int add(int u, int v) {
        ArrayList<Integer> edge = new ArrayList<Integer>();
        edge.add(u);
        edge.add(v);
        if (isDirected()) {
            if (!allEdges.contains(edge)) {
                allEdges.add(edge);
            }
            return edgeId(u, v);
        } else {
            ArrayList<Integer> edgeSame = new ArrayList<Integer>();
            edgeSame.add(v);
            edgeSame.add(u);
            if (!allEdges.contains(edge) && (!allEdges.contains(edgeSame))) {
                allEdges.add(edge);
            }
            return edgeId(u, v);
        }
    }

    @Override
    public void remove(int v) {
        if (allVertices.contains(v)) {
            allVertices.remove(new Integer(v));
            keeptrack.add(v);
            List<ArrayList<Integer>> copy
                    = new ArrayList<ArrayList<Integer>>(allEdges);
            for (ArrayList<Integer> edge : copy) {
                if (edge.contains(v)) {
                    allEdges.remove(edge);
                }
            }

        }
    }

    @Override
    public void remove(int u, int v) {
        ArrayList<Integer> edge = new ArrayList<Integer>();
        edge.add(u);
        edge.add(v);
        if (isDirected()) {
            List<ArrayList<Integer>> copy
                    = new ArrayList<ArrayList<Integer>>(allEdges);
            for (ArrayList<Integer> i : copy) {
                if (i.equals(edge)) {
                    allEdges.remove(i);
                }
            }
        } else {
            ArrayList<Integer> edgeSame = new ArrayList<Integer>();
            edgeSame.add(v);
            edgeSame.add(u);
            List<ArrayList<Integer>> copy
                    = new ArrayList<ArrayList<Integer>>(allEdges);
            for (ArrayList<Integer> i : copy) {
                if ((i.equals(edge)) || (i.equals(edgeSame))) {
                    allEdges.remove(i);
                }
            }
        }
    }

    @Override
    public Iteration<Integer> vertices() {
        return Iteration.iteration(allVertices);
    }

    @Override
    public int successor(int v, int k) {
        if (contains(v)) {
            ArrayList<Integer> allsuccessors = new ArrayList<Integer>();
            allsuccessors = findallsuccessors(v);
            if ((allsuccessors.size() == 0) || (k >= allsuccessors.size())) {
                return 0;
            }
            return allsuccessors.get(k);
        }
        return 0;

    }

    /** Return an arraylist that contains successors of V. */
    private ArrayList<Integer> findallsuccessors(int v) {
        ArrayList<Integer> allsuccessors = new ArrayList<Integer>();
        for (int node : allVertices) {
            if (contains(v, node)) {
                allsuccessors.add(node);
            }
        }
        return allsuccessors;
    }

    /** Return an arraylist that contains successors of V. */
    ArrayList<Integer> getallsuccessors(int v) {
        return findallsuccessors(v);
    }

    @Override
    public abstract int predecessor(int v, int k);

    @Override
    public Iteration<Integer> successors(int v) {
        if (!contains(v)) {
            return Iteration.iteration(new ArrayList<Integer>());
        } else {
            return Iteration.iteration(findallsuccessorsEdgesorder(v));
        }
    }

    /** Return an arraylist that contains successors of V
     * in edge insertion order. */
    private ArrayList<Integer> findallsuccessorsEdgesorder(int v) {
        ArrayList<Integer> allsuccessors = new ArrayList<Integer>();
        if (isDirected()) {
            for (ArrayList<Integer> edge : allEdges) {
                if (edge.get(0).equals(v)) {
                    allsuccessors.add(edge.get(1));
                }
            }
            return allsuccessors;
        } else {
            for (ArrayList<Integer> edge : allEdges) {
                if (edge.get(0).equals(v)) {
                    allsuccessors.add(edge.get(1));
                } else if (edge.get(1).equals(v)) {
                    allsuccessors.add(edge.get(0));
                }

            }
            return allsuccessors;
        }
    }

    @Override
    public abstract Iteration<Integer> predecessors(int v);

    @Override
    public Iteration<int[]> edges() {
        ArrayList<int[]> tempEdges = new ArrayList<int[]>();
        for (ArrayList<Integer> edge : allEdges) {
            tempEdges.add(new int[]{edge.get(0), edge.get(1)});
        }
        return Iteration.iteration(tempEdges.iterator());

    }


    @Override
    protected int edgeId(int u, int v) {
        if (!isDirected()) {
            int x = Math.max(u, v);
            int y = Math.min(u, v);
            return ((x + y) * (x + y + 1)) / 2 + y;
        }
        return ((u + v) * (u + v + 1)) / 2 + v;
    }

    /** All edges. */
    private ArrayList<Integer> allVertices;
    /** All edges. */
    private ArrayList<ArrayList<Integer>> allEdges;
    /** Keeping track. */
    private PriorityQueue<Integer> keeptrack = new PriorityQueue<>();
}
