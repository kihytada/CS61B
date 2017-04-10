package graph;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

/** Unit tests for the GraphObj class.
 *  @author Leslie Yang
 */
public class GraphObjTesting {

    @Test
    public void undirectedGraphObjConst() {
        UndirectedGraph ug = new UndirectedGraph();
        assertTrue(ug.getallVertices().isEmpty());
        assertTrue(ug.getallEdges().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChecktMyVertex2() {
        DirectedGraph dGraph = new DirectedGraph();
        dGraph.add();
        dGraph.checkMyVertex(1);
        dGraph.checkMyVertex(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChecktMyVertex4() {
        UndirectedGraph uDGraph = new UndirectedGraph();
        uDGraph.add();
        uDGraph.checkMyVertex(1);
        uDGraph.checkMyVertex(2);
    }

    @Test
    public void dirextedGraphObjConst() {
        DirectedGraph dg = new DirectedGraph();
        assertTrue(dg.getallVertices().isEmpty());
        assertTrue(dg.getallEdges().isEmpty());
    }

    @Test
    public void ugmaxVertex() {
        UndirectedGraph ug = new UndirectedGraph();
        assertEquals(0, ug.maxVertex());
    }

    @Test
    public void dgmaxVertex() {
        DirectedGraph dg = new DirectedGraph();
        assertEquals(0, dg.maxVertex());
    }

    @Test
    public void ugedgeSize() {
        UndirectedGraph ug = new UndirectedGraph();
        assertEquals(0, ug.edgeSize());
    }

    @Test
    public void dgedgeSize() {
        DirectedGraph dg = new DirectedGraph();
        assertEquals(0, dg.edgeSize());
    }

    @Test
    public void ugcontains() {
        UndirectedGraph ug = new UndirectedGraph();
        assertFalse(ug.contains(1, 2));
        assertFalse(ug.contains(2, 32));
        assertFalse(ug.contains(4, 7));
        assertFalse(ug.contains(1, 6));
        assertFalse(ug.contains(8, 2));
    }

    @Test
    public void dgcontains() {
        DirectedGraph dg = new DirectedGraph();
        assertFalse(dg.contains(1, 3));
        assertFalse(dg.contains(3, 4));
        assertFalse(dg.contains(5, 32));
        assertFalse(dg.contains(4, 6));
    }

    @Test
    public void ugtestaddinit() {
        UndirectedGraph ug1 = new UndirectedGraph();
        ug1.add();
        assertTrue(ug1.getallVertices().contains(1));
        assertTrue(ug1.getallsuccessors(ug1.getallVertices().get(0)).isEmpty());
        UndirectedGraph ug2 = new UndirectedGraph();
        ug2.add();
        ug2.add();
        ug2.add();
        assertTrue(ug2.getallVertices().contains(1));
        assertTrue(ug2.getallsuccessors(ug2.getallVertices().get(0)).isEmpty());
        assertTrue(ug2.getallVertices().contains(2));
        assertTrue(ug2.getallsuccessors(ug2.getallVertices().get(1)).isEmpty());
        assertTrue(ug2.getallVertices().contains(3));
        assertTrue(ug2.getallsuccessors(ug2.getallVertices().get(2)).isEmpty());
        assertFalse(ug2.getallVertices().contains(4));
        ug2.add();
        assertTrue(ug2.getallVertices().contains(4));
        assertTrue(ug2.getallsuccessors(ug2.getallVertices().get(3)).isEmpty());
    }

    @Test
    public void dgtestaddinit() {
        DirectedGraph dg1 = new DirectedGraph();
        dg1.add();
        assertTrue(dg1.getallVertices().contains(1));
        assertTrue(dg1.getallsuccessors(dg1.getallVertices().get(0)).isEmpty());
        DirectedGraph dg2 = new DirectedGraph();
        dg2.add();
        dg2.add();
        dg2.add();
        assertTrue(dg2.getallVertices().contains(1));
        assertTrue(dg2.getallsuccessors(dg2.getallVertices().get(0)).isEmpty());
        assertTrue(dg2.getallVertices().contains(2));
        assertTrue(dg2.getallsuccessors(dg2.getallVertices().get(1)).isEmpty());
        assertTrue(dg2.getallVertices().contains(3));
        assertTrue(dg2.getallsuccessors(dg2.getallVertices().get(2)).isEmpty());
        assertFalse(dg2.getallVertices().contains(4));
        dg2.add();
        assertTrue(dg2.getallVertices().contains(4));
        assertTrue(dg2.getallsuccessors(dg2.getallVertices().get(3)).isEmpty());
    }

    @Test
    public void ugextensiveadd() {
        UndirectedGraph ug1 = new UndirectedGraph();
        ug1.add();
        assertEquals(1, ug1.vertexSize());
        assertEquals(1, ug1.maxVertex());
        assertEquals(0, ug1.edgeSize());
        assertEquals(0, ug1.inDegree(1));
        assertEquals(0, ug1.inDegree(3));
        assertTrue(ug1.contains(1));
        assertFalse(ug1.contains(1, 3));
        UndirectedGraph ug2 = new UndirectedGraph();
        ug2.add();
        ug2.add();
        ug2.add();
        assertTrue(ug2.contains(1));
        assertFalse(ug2.contains(1, 3));
        assertTrue(ug2.contains(2));
        assertTrue(ug2.contains(3));
        assertFalse(ug2.contains(4));
        ug2.add();
        assertEquals(4, ug2.vertexSize());
        assertEquals(4, ug2.maxVertex());
        assertEquals(0, ug2.edgeSize());
        assertEquals(0, ug2.inDegree(1));
        assertEquals(0, ug2.inDegree(3));
    }

    @Test
    public void dgextensiveadd() {
        DirectedGraph dg1 = new DirectedGraph();
        dg1.add();
        assertEquals(1, dg1.vertexSize());
        assertEquals(1, dg1.maxVertex());
        assertEquals(0, dg1.edgeSize());
        assertEquals(0, dg1.inDegree(1));
        assertEquals(0, dg1.inDegree(3));
        assertTrue(dg1.contains(1));
        assertFalse(dg1.contains(1, 3));
        DirectedGraph dg2 = new DirectedGraph();
        dg2.add();
        dg2.add();
        dg2.add();
        assertTrue(dg2.contains(1));
        assertFalse(dg2.contains(1, 3));
        assertTrue(dg2.contains(2));
        assertTrue(dg2.contains(3));
        assertFalse(dg2.contains(4));
        dg2.add();
        assertEquals(4, dg2.vertexSize());
        assertEquals(4, dg2.maxVertex());
        assertEquals(0, dg2.edgeSize());
        assertEquals(0, dg2.inDegree(1));
        assertEquals(0, dg2.inDegree(3));
    }


    @Test
    public void ugtest() {
        UndirectedGraph ug = new UndirectedGraph();
        ug.add();
        ug.add();
        ug.add(1, 2);
        assertTrue(ug.contains(1, 2));
        assertFalse(ug.contains(1, 3));
        ug.add();
        ug.add(1, 3);
        assertTrue(ug.contains(3, 1));
        assertFalse(ug.contains(2, 3));
        ug.add();
        ug.add(2, 3);
        assertTrue(ug.contains(2, 3));
        ug.add(3, 4);
        assertTrue(ug.contains(4, 3));
        assertTrue(ug.contains(3, 1));
        assertTrue(ug.getallsuccessors(1).contains(2));
        assertTrue(ug.getallsuccessors(1).contains(3));
        assertFalse(ug.getallsuccessors(2).contains(4));
        assertTrue(ug.getallsuccessors(2).contains(1));
        assertTrue(ug.getallsuccessors(2).contains(3));
        assertFalse(ug.getallsuccessors(2).contains(4));
        assertTrue(ug.getallsuccessors(3).contains(1));
        assertTrue(ug.getallsuccessors(3).contains(2));
        assertTrue(ug.getallsuccessors(3).contains(4));
        assertFalse(ug.getallsuccessors(3).contains(3));
        assertEquals(1, ug.getallsuccessors(4).size());
        ug.add(3, 3);
        assertTrue(ug.getallsuccessors(3).contains(3));
    }

    @Test
    public void dgtest() {
        DirectedGraph dg = new DirectedGraph();
        dg.add();
        dg.add();
        dg.add(1, 2);
        assertTrue(dg.contains(1, 2));
        assertFalse(dg.contains(2, 1));
        assertFalse(dg.contains(1, 3));
        dg.add();
        dg.add(1, 3);
        assertTrue(dg.contains(1, 3));
        dg.add();
        dg.add(2, 3);
        assertTrue(dg.contains(2, 3));
        assertFalse(dg.contains(3, 2));
        dg.add(3, 4);
        assertTrue(dg.getallsuccessors(1).contains(2));
        assertTrue(dg.getallsuccessors(1).contains(3));
        assertFalse(dg.getallsuccessors(2).contains(1));
        assertTrue(dg.getallpredecessors(2).contains(1));
        assertTrue(dg.getallsuccessors(2).contains(3));
        assertFalse(dg.getallsuccessors(2).contains(4));
        assertTrue(dg.getallpredecessors(3).contains(1));
        assertFalse(dg.getallsuccessors(3).contains(2));
        assertTrue(dg.getallsuccessors(3).contains(4));
        assertFalse(dg.getallsuccessors(3).contains(3));
    }

    @Test
    public void ugremovetwo() {
        UndirectedGraph ug = new UndirectedGraph();
        ug.add();
        ug.add();
        ug.add(1, 2);
        ug.add();
        ug.add(1, 3);
        ug.add();
        ug.add(2, 3);
        ug.add(3, 4);
        assertTrue(ug.contains(3));
        ug.remove(3);
        assertFalse(ug.contains(3));
        ug.add();
        assertTrue(ug.contains(3));
        assertTrue(ug.contains(1, 2));
        assertTrue(ug.contains(2, 1));
        assertFalse(ug.contains(1, 3));
        assertFalse(ug.contains(2, 3));
        assertFalse(ug.contains(3, 4));
    }

    @Test
    public void dgremovetwo() {
        DirectedGraph ug = new DirectedGraph();
        ug.add();
        ug.add();
        ug.add(1, 2);
        ug.add();
        ug.add(1, 3);
        ug.add();
        ug.add(2, 3);
        ug.add(3, 4);
        assertTrue(ug.contains(3));
        ug.remove(3);
        assertFalse(ug.contains(3));
        ug.add();
        assertTrue(ug.contains(3));
        assertTrue(ug.contains(1, 2));
        assertFalse(ug.contains(1, 3));
        assertFalse(ug.contains(2, 3));
        assertFalse(ug.contains(3, 4));
        assertFalse(ug.contains(2, 1));
    }

    @Test
    public void ugtestSuccessor() {
        UndirectedGraph ug = new UndirectedGraph();
        ug.add();
        ug.add();
        ug.add();
        ug.add();
        ug.add(1, 2);
        ug.add(1, 3);
        ug.add(1, 4);
        ug.add(4, 3);
        assertEquals(2, ug.successor(1, 0));
        assertEquals(0, ug.successor(5, 0));
        assertEquals(3, ug.successor(1, 1));
        assertEquals(4, ug.successor(1, 2));
        assertEquals(0, ug.successor(1, 3));
        assertEquals(0, ug.successor(1, 4));
        assertEquals(1, ug.successor(2, 0));
        assertEquals(0, ug.successor(2, 1));
    }

    @Test
    public void dgtestSuccessor() {
        DirectedGraph ug = new DirectedGraph();
        ug.add();
        ug.add();
        ug.add();
        ug.add();
        ug.add(1, 2);
        ug.add(1, 3);
        ug.add(1, 4);
        ug.add(3, 4);
        assertEquals(2, ug.successor(1, 0));
        assertEquals(3, ug.successor(1, 1));
        assertEquals(4, ug.successor(1, 2));
        assertEquals(0, ug.successor(1, 3));
        assertEquals(0, ug.successor(5, 0));
        assertEquals(0, ug.successor(2, 0));
        assertEquals(4, ug.successor(3, 0));
        assertEquals(0, ug.successor(4, 0));
    }

    @Test
    public void ugtestaddtwice() {
        UndirectedGraph ug = new UndirectedGraph();
        ug.add();
        ug.add();
        ug.add(1, 2);
        ug.add(1, 2);
        ug.add(2, 1);
        assertEquals(1, ug.getallEdges().size());
        ug.add(2, 2);
        assertEquals(2, ug.getallEdges().size());
    }

    @Test
    public void dgtestaddtwice() {
        DirectedGraph dg = new DirectedGraph();
        dg.add();
        dg.add();
        dg.add(1, 2);
        dg.add(1, 2);
        assertEquals(1, dg.getallEdges().size());
        dg.add(2, 1);
        assertEquals(2, dg.getallEdges().size());
        dg.add(2, 2);
        assertEquals(3, dg.getallEdges().size());
    }

    @Test
    public void dgpredecessor() {
        DirectedGraph dg = new DirectedGraph();
        dg.add();
        dg.add();
        dg.add();
        dg.add();
        dg.add(1, 2);
        dg.add(2, 3);
        dg.add(1, 4);
        dg.add(4, 2);
        assertEquals(0, dg.getallpredecessors(1).size());
        assertEquals(2, dg.getallpredecessors(2).size());
        assertEquals(1, dg.getallpredecessors(3).size());
        assertEquals(1, dg.getallpredecessors(4).size());
        assertEquals(0, dg.predecessor(1, 0));
        assertEquals(1, dg.predecessor(2, 0));
        assertEquals(4, dg.predecessor(2, 1));
        assertEquals(0, dg.predecessor(2, 2));
        assertEquals(2, dg.predecessor(3, 0));
        assertEquals(0, dg.predecessor(3, 1));
        assertEquals(1, dg.predecessor(4, 0));
        assertEquals(0, dg.predecessor(4, 1));
    }

    @Test
    public void testvertices() {
        UndirectedGraph ug = new UndirectedGraph();
        ug.add();
        ug.add();
        ug.add();
        ug.add();
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int x : ug.vertices()) {
            result.add(x);
        }
        int first = result.get(0);
        int second = result.get(1);
        int third = result.get(2);
        int forth = result.get(3);
        assertEquals(1, first);
        assertEquals(2, second);
        assertEquals(3, third);
        assertEquals(4, forth);
    }

    @Test
    public void ugremovevertexTest() {
        UndirectedGraph ug = new UndirectedGraph();
        ug.add();
        ug.add();
        ug.add();
        ug.add();
        ug.add();
        ug.add(2, 3);
        ug.add(2, 4);
        ug.add(2, 5);
        assertEquals(3, ug.getallEdges().size());
        ug.remove(4);
        assertFalse(ug.contains(4));
        assertFalse(ug.contains(2, 4));
        assertFalse(ug.contains(4, 2));
        assertEquals(2, ug.getallEdges().size());
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int x : ug.successors(2)) {
            result.add(x);
        }
        assertEquals(2, result.size());
        int first = ug.successor(2, 0);
        int second = ug.successor(2, 1);
        assertEquals(3, first);
        assertEquals(5, second);
        int third = result.get(0);
        int forth = result.get(1);
        assertEquals(3, third);
        assertEquals(5, forth);
    }

    @Test
    public void dgremovevertexTest() {
        DirectedGraph ug = new DirectedGraph();
        ug.add();
        ug.add();
        ug.add();
        ug.add();
        ug.add();
        ug.add(2, 3);
        ug.add(2, 4);
        ug.add(2, 5);
        assertEquals(3, ug.getallEdges().size());
        ug.remove(4);
        assertFalse(ug.contains(4));
        assertFalse(ug.contains(2, 4));
        assertEquals(2, ug.getallEdges().size());
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (int x : ug.successors(2)) {
            result.add(x);
        }
        assertEquals(2, result.size());
        int first = ug.successor(2, 0);
        int second = ug.successor(2, 1);
        assertEquals(3, first);
        assertEquals(5, second);
        int third = result.get(0);
        int forth = result.get(1);
        assertEquals(3, third);
        assertEquals(5, forth);
    }

    @Test
    public void ugtestsucpred() {
        UndirectedGraph ug = new UndirectedGraph();
        ug.add();
        ug.add();
        ug.add();
        ug.add();
        assertEquals(0, ug.predecessor(0, 0));
    }

    @Test
    public void dgtestsucpred() {
        DirectedGraph dg = new DirectedGraph();
        dg.add();
        dg.add();
        dg.add();
        dg.add();
        assertEquals(0, dg.predecessor(0, 0));
    }

    @Test
    public void testOutgoing() {
        UndirectedGraph ug = new UndirectedGraph();
        ug.add();
        ug.add();
        ug.add();
        ug.add();
        ug.add(1, 1);
        ug.add(1, 2);
        ug.add(1, 3);
        ug.add(1, 4);
        assertEquals(4, ug.outDegree(1));
        ug.remove(1, 1);
        assertEquals(3, ug.outDegree(1));
        ug.add(1, 1);
        ug.remove(1);
        assertEquals(0, ug.outDegree(1));
        assertEquals(0, ug.outDegree(2));
    }

    @Test
    public void addremove() {
        DirectedGraph ug = new DirectedGraph();
        ug.add();
        ug.add();
        ug.add();
        ug.add();
        ug.remove(1);
        assertFalse(ug.getallVertices().contains(1));
        ug.add();
        assertTrue(ug.getallVertices().contains(1));
        ug.remove(1);
        ug.remove(2);
        assertFalse(ug.getallVertices().contains(1));
        assertFalse(ug.getallVertices().contains(2));
        ug.add();
        assertTrue(ug.getallVertices().contains(1));
        assertFalse(ug.getallVertices().contains(2));
    }

    @Test
    public void ugcheckedge() {
        UndirectedGraph ug = new UndirectedGraph();
        ug.add();
        ug.add(1, 1);
        assertEquals(1, ug.edgeSize());
    }

    @Test
    public void dgcheckedge() {
        DirectedGraph dg = new DirectedGraph();
        dg.add();
        dg.add(1, 1);
        assertEquals(1, dg.edgeSize());
    }

    @Test
    public void testDegree() {
        UndirectedGraph uDGraph = new UndirectedGraph();
        assertEquals(0, uDGraph.degree(1));
        uDGraph.add();
        uDGraph.add();
        uDGraph.add();
        assertEquals(0, uDGraph.degree(2));
        uDGraph.add(1, 2);
        uDGraph.add(1, 3);
        assertEquals(2, uDGraph.degree(1));
        assertEquals(1, uDGraph.degree(3));
        uDGraph.add(3, 1);
        assertEquals(1, uDGraph.degree(3));
    }
}
