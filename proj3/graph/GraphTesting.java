package graph;

import org.junit.Test;
import static org.junit.Assert.*;

/** Unit tests for the Graph class.
 *  @author Leslie Yang
 */
public class GraphTesting {

    /** See GraphObjTesting.*/

    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }

}
