package ataxx;

import org.junit.Test;
import static org.junit.Assert.*;

/** Tests of the Board class.
 *  @author
 */
public class BoardTest {

    private static final String[]
        GAME1 = { "a7-b7", "a1-a2",
                  "a7-a6", "a2-a3",
                  "a6-a5", "a3-a4" },
        GAME2 = { "a7-b6", "a1-a2", "a6-a5", "g1-f2",
                    "a3-a4", "g7-g6", "c5-c6"},
        GAME3 = { "a7-b6", "a1-b1", "b6-c5", "g7-f6",
                  "g1-f2", "f6-e5", "c5-c4", "e5-d4", "b6-b4"};



    private static void makeMoves(Board b, String[] moves) {
        for (String s : moves) {
            b.makeMove(s.charAt(0), s.charAt(1),
                       s.charAt(3), s.charAt(4));
        }
    }



    @Test
    public void testUndo() {
        Board b0 = new Board();
        Board b1 = new Board(b0);
        makeMoves(b0, GAME1);
        Board b2 = new Board(b0);
        for (int i = 0; i < GAME1.length; i += 1) {
            b0.undo();
        }
        assertEquals("failed to return to start", b1, b0);
        makeMoves(b0, GAME1);
        assertEquals("second pass failed to reach same position", b2, b0);
    }

    @Test
    public void testSetBlock1() {
        Board b0 = new Board();
        b0.setBlock("b2");
        b0.setBlock("c3");
        b0.setBlock("c4");
    }

    @Test
    public void testSetBlock2() {
        Board b0 = new Board();
        b0.setBlock("b3");
        b0.setBlock("c5");
        b0.setBlock("c6");
    }

    @Test
    public void testMakeMove1() {
        Board b0 = new Board();
        makeMoves(b0, GAME3);
        b0.undo();
    }

    @Test
    public void testMakeMove2() {
        Board b0 = new Board();
        makeMoves(b0, GAME3);
        b0.undo();
    }

    @Test
    public void testClear() {
        Board b0 = new Board();
        b0.clear();
    }
}
