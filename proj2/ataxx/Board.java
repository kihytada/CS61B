package ataxx;

/* Author: P. N. Hilfinger, (C) 2008. */

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Observable;


import static ataxx.PieceColor.*;
import static ataxx.GameException.error;

/** An Ataxx board.   The squares are labeled by column (a char value between
 *  'a' - 2 and 'g' + 2) and row (a char value between '1' - 2 and '7'
 *  + 2) or by linearized index, an integer described below.  Values of
 *  the column outside 'a' and 'g' and of the row outside '1' to '7' denote
 *  two layers of border squares, which are always blocked.
 *  This artificial border (which is never actually printed) is a common
 *  trick that allows one to avoid testing for edge conditions.
 *  For example, to look at all the possible moves from a square, sq,
 *  on the normal board (i.e., not in the border region), one can simply
 *  look at all squares within two rows and columns of sq without worrying
 *  about going off the board. Since squares in the border region are
 *  blocked, the normal logic that prevents moving to a blocked square
 *  will apply.
 *
 *  For some purposes, it is useful to refer to squares using a single
 *  integer, which we call its "linearized index".  This is simply the
 *  number of the square in row-major order (counting from 0).
 *
 *  Moves on this board are denoted by Moves.
 *  @author Leslie Yang
 */
class Board extends Observable {

    /** Number of squares on a side of the board. */
    static final int SIDE = 7;
    /** Length of a side + an artificial 2-deep border region. */
    static final int EXTENDED_SIDE = SIDE + 4;

    /** Number of non-extending moves before game ends. */
    static final int JUMP_LIMIT = 25;

    /** A new, cleared board at the start of the game. */
    Board() {
        _board = new PieceColor[EXTENDED_SIDE * EXTENDED_SIDE];
        _redPieces = 0;
        _bluePieces = 0;
        _numJumps = 0;
        _numMoves = 0;
        clear();
    }

    /** A copy of B. */
    Board(Board b) {
        _board = b._board.clone();
        _redPieces = b.redPieces();
        _bluePieces = b.bluePieces();
        _numJumps = b.numJumps();
        _numMoves = b.numMoves();
        _allMoves = b.allMoves();
        _whoseMove = b.whoseMove();
        _allBlocks = b._allBlocks;
        _allNumMoves = b._allNumMoves;
        _allNumJumps = b._allNumJumps;
        _allBluePieces = b._allBluePieces;
        _allRedPieces = b._allRedPieces;
        _states = b._states;
    }

    /** Return the linearized index of square COL ROW. */
    static int index(char col, char row) {
        return (row - '1' + 2) * EXTENDED_SIDE + (col - 'a' + 2);
    }

    /** Return the linearized index of the square that is DC columns and DR
     *  rows away from the square with index SQ. */
    static int neighbor(int sq, int dc, int dr) {
        return sq + dc + dr * EXTENDED_SIDE;
    }

    /** Clear me to my starting state, with pieces in their initial
     *  positions and no blocks. */
    void clear() {
        for (char m = ('a' - 2); m <= 'g' + 2; m++) {
            for (char n = ('1' - 2); n <= '7' + 2; n++) {
                unrecordedSet(m, n, BLOCKED);
            }
        }
        for (char i = 'a'; i <= 'g'; i++) {
            for (char j = '1'; j <= '7'; j++) {
                unrecordedSet(i, j, EMPTY);
            }
        }
        _whoseMove = RED;
        _redPieces = 2;
        _bluePieces = 2;
        unrecordedSet('a', '1', BLUE);
        unrecordedSet('g', '7', BLUE);
        unrecordedSet('a', '7', RED);
        unrecordedSet('g', '1', RED);
        setChanged();
        notifyObservers();
    }

    /** Return true iff the game is over: i.e., if neither side has
     *  any moves, if one side has no pieces, or if there have been
     *  MAX_JUMPS consecutive jumps without intervening extends. */
    boolean gameOver() {
        if (_numJumps >= JUMP_LIMIT) {
            return true;
        } else if ((_redPieces == 0) || (_bluePieces == 0)) {
            return true;
        } else if ((!canMove(RED)) && (!canMove(BLUE))) {
            return true;
        }
        return false;
    }

    /** Return number of red pieces on the board. */
    int redPieces() {
        return numPieces(RED);
    }

    /** Return number of blue pieces on the board. */
    int bluePieces() {
        return numPieces(BLUE);
    }

    /** Return number of COLOR pieces on the board. */
    int numPieces(PieceColor color) {
        int num = 0;
        for (char i = ('a' - 2); i <= 'g' + 2; i++) {
            for (char j = ('1' - 2); j <= '7' + 2; j++) {
                if (get(i, j).equals(color)) {
                    num++;
                }
            }
        }
        return num;
    }

    /** Increment numPieces(COLOR) by K. */
    private void incrPieces(PieceColor color, int k) {
        int temp = numPieces(color);
        temp += k;
    }

    /** The current contents of square CR, where 'a'-2 <= C <= 'g'+2, and
     *  '1'-2 <= R <= '7'+2.  Squares outside the range a1-g7 are all
     *  BLOCKED.  Returns the same value as get(index(C, R)). */
    PieceColor get(char c, char r) {
        return _board[index(c, r)];
    }

    /** Return the current contents of square with linearized index SQ. */
    PieceColor get(int sq) {
        return _board[sq];
    }

    /** Set get(C, R) to V, where 'a' <= C <= 'g', and
     *  '1' <= R <= '7'. */
    private void set(char c, char r, PieceColor v) {
        set(index(c, r), v);
    }

    /** Set square with linearized index SQ to V.  This operation is
     *  undoable. */
    private void set(int sq, PieceColor v) {
        _board[sq] = v;
    }

    /** Set square at C R to V (not undoable). */
    private void unrecordedSet(char c, char r, PieceColor v) {
        _board[index(c, r)] = v;
    }

    /** Set square at linearized index SQ to V (not undoable). */
    private void unrecordedSet(int sq, PieceColor v) {
        _board[sq] = v;
    }

    /** Return true iff MOVE is legal on the current board.
     * Return false if the destination tile is taken
     * or if the source tile is empty (amongst other things). */
    boolean legalMove(Move move) {
        if (move == null) {
            return false;
        }
        char oldcol = move.col0();
        char oldrow = move.row0();
        char newcol = move.col1();
        char newrow = move.row1();
        int oldpos = index(oldcol, oldrow);
        int newpos = index(newcol, newrow);
        if ((move.isExtend()) || (move.isJump())) {
            if ((_board[oldpos].equals(EMPTY))
                || (_board[oldpos].equals(BLOCKED))
                || (!_board[newpos].equals(EMPTY))) {
                return false;
            }
            return true;
        }
        return false;
    }


    /** Return true iff player WHO can move, ignoring whether it is
     *  that player's move and whether the game is over. */
    boolean canMove(PieceColor who) {
        for (char i = ('a' - 2); i <= 'g' + 2; i++) {
            for (char j = ('1' - 2); j <= '7' + 2; j++) {
                PieceColor cur = get(i, j);
                if (cur.equals(who)) {
                    for (int m = -2; m <= 2; m++) {
                        for (int n = -2; n <= 2; n++) {
                            char p = (char) (i + m);
                            char q = (char) (j + n);
                            Move testMove = Move.move(i, j, p, q);
                            if (legalMove(testMove)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    /** Return the color of the player who has the next move.  The
     *  value is arbitrary if gameOver(). */
    PieceColor whoseMove() {
        return _whoseMove;
    }

    /** Return total number of moves and passes since the last
     *  clear or the creation of the board. */
    int numMoves() {
        return _numMoves;
    }

    /** Return number of non-pass moves made in the current game since the
     *  last extend move added a piece to the board (or since the
     *  start of the game). Used to detect end-of-game. */
    int numJumps() {
        return _numJumps;
    }

    /** Perform the move C0R0-C1R1, or pass if C0 is '-'.  For moves
     *  other than pass, assumes that legalMove(C0, R0, C1, R1). */
    void makeMove(char c0, char r0, char c1, char r1) {
        if (c0 == '-') {
            makeMove(Move.pass());
        } else {
            makeMove(Move.move(c0, r0, c1, r1));
        }
    }

    /** Make the MOVE on this Board, assuming it is legal. */
    void makeMove(Move move) {
        assert legalMove(move);
        if (move.isPass()) {
            pass();
            _numMoves++;
            _allMoves.add(move);
            return;
        } else {
            char oldcol = move.col0();
            char oldrow = move.row0();
            char newcol = move.col1();
            char newrow = move.row1();
            PieceColor prev = get(oldcol, oldrow);
            if (move.isExtend()) {
                _board[index(newcol, newrow)] = prev;
                _numJumps = 0;
            }
            if (move.isJump()) {
                _board[index(oldcol, oldrow)] = EMPTY;
                _board[index(newcol, newrow)] = prev;
                _numJumps++;
            }
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    PieceColor near = get((char) (newcol + i),
                        (char) (newrow + j));
                    if ((!near.equals(EMPTY)) && (!near.equals(BLOCKED))) {
                        _board[index((char) (newcol + i),
                        (char) (newrow + j))] = prev;
                    }
                }
            }
            int jumpstemp = _numJumps;
            _allNumJumps.add(jumpstemp);
            _numMoves++;
            int movestemp = _numMoves;
            _allNumMoves.add(movestemp);
            _redPieces = numPieces(RED);
            int redtemp = _redPieces;
            _allRedPieces.add(redtemp);
            _bluePieces = numPieces(BLUE);
            int bluetemp = _bluePieces;
            _allBluePieces.add(bluetemp);
            _allMoves.add(move);
            PieceColor[] temp = new PieceColor[_board.length];
            System.arraycopy(_board, 0, temp, 0, _board.length);
            _states.add(temp);

        }
        PieceColor opponent = _whoseMove.opposite();
        _whoseMove = opponent;
        setChanged();
        notifyObservers();
    }

    /** Update to indicate that the current player passes, assuming it
     *  is legal to do so.  The only effect is to change whoseMove(). */
    void pass() {
        assert !canMove(_whoseMove);
        PieceColor opponent = _whoseMove.opposite();
        _whoseMove = opponent;
        setChanged();
        notifyObservers();
    }

    /** Undo the last move. */
    void undo() {
        if (_states.size() == 1) {
            clear();
            for (char[] block: _allBlocks) {
                setBlock2(block[0], block[1]);
            }
            _states.remove(_states.size() - 1);
            return;
        }
        System.arraycopy(_states.get(
            _states.size() - 2), 0, _board, 0, _board.length);
        _states.remove(_states.size() - 1);
        _numJumps = _allNumJumps.get(_allNumJumps.size() - 2);
        _numMoves = _allNumMoves.get(_allNumMoves.size() - 2);
        _redPieces = _allRedPieces.get(_allRedPieces.size() - 2);
        _bluePieces = _allBluePieces.get(_allBluePieces.size() - 2);
        PieceColor opponent = _whoseMove.opposite();
        _whoseMove = opponent;
        setChanged();
        notifyObservers();
    }

    /** Return true iff it is legal to place a block at C R. */
    boolean legalBlock(char c, char r) {
        if (((c != 'a') || (r != '1'))
            && ((c != 'a') || (r != '7'))
            && ((c != 'g') || (r != '1'))
            && ((c != 'a') || (r != '7'))
            && ((!get(c, r).equals(BLUE))
            && (!get(c, r).equals(RED)))) {
            return true;
        }
        return false;
    }

    /** Return true iff it is legal to place a block at CR. */
    boolean legalBlock(String cr) {
        return legalBlock(cr.charAt(0), cr.charAt(1));
    }

    /** Set a block on the square C R and its reflections across the middle
     *  row and/or column, if that square is unoccupied and not
     *  in one of the corners. Has no effect if any of the squares is
     *  already occupied by a block.  It is an error to place a block on a
     *  piece. */
    void setBlock(char c, char r) {
        if (!legalBlock(c, r)) {
            throw error("illegal block placement");
        } else {
            set(c, r, BLOCKED);
            if (legalBlock((char) ('g' + 'a' - c), r)) {
                set((char) ('g' + 'a' - c), r, BLOCKED);
            }
            if (legalBlock(c, (char) ('1' + '7' - r))) {
                set(c, (char) ('1' + '7' - r), BLOCKED);
            }
            if (legalBlock((char) ('g' + 'a' - c), (char) ('1' + '7' - r))) {
                set((char) ('g' + 'a' - c), (char) ('1' + '7' - r), BLOCKED);
            }
        }
        char[] block  = {c, r};
        _allBlocks.add(block);
        setChanged();
        notifyObservers();
    }
    /** Set a block on the square C R and its reflections across the middle
     *  row and/or column, if that square is unoccupied and not
     *  in one of the corners. Has no effect if any of the squares is
     *  already occupied by a block.  It is an error to place a block on a
     *  piece. */
    void setBlock2(char c, char r) {
        if (!legalBlock(c, r)) {
            throw error("illegal block placement");
        } else {
            set(c, r, BLOCKED);
            if (legalBlock((char) ('g' + 'a' - c), r)) {
                set((char) ('g' + 'a' - c), r, BLOCKED);
            }
            if (legalBlock(c, (char) ('1' + '7' - r))) {
                set(c, (char) ('1' + '7' - r), BLOCKED);
            }
            if (legalBlock((char) ('g' + 'a' - c), (char) ('1' + '7' - r))) {
                set((char) ('g' + 'a' - c), (char) ('1' + '7' - r), BLOCKED);
            }
        }
        setChanged();
        notifyObservers();
    }

    /** Place a block at CR. */
    void setBlock(String cr) {
        setBlock(cr.charAt(0), cr.charAt(1));
    }


    /** Return a list of all moves made since the last clear (or start of
     *  game). */
    ArrayList<Move> allMoves() {
        return _allMoves;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    /* .equals used only for testing purposes. */
    @Override
    public boolean equals(Object obj) {
        Board other = (Board) obj;
        return Arrays.equals(_board, other._board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(_board);
    }

    /** Return a text depiction of the board (not a dump).  If LEGEND,
     *  supply row and column numbers around the edges. */
    String toString(boolean legend) {
        Formatter out = new Formatter();
        out.format("===\n");
        for (char j = '7'; j >= '1'; j--) {
            out.format("  ");
            for (char i = 'a'; i <= 'g'; i++) {
                PieceColor pc = get(i, j);
                String p = "";
                if (pc.equals(EMPTY)) {
                    p = "-";
                } else if (pc.equals(BLUE)) {
                    p = "b";
                } else if (pc.equals(RED)) {
                    p = "r";
                } else {
                    p = "X";
                }
                out.format("%s ", p);
            }
            out.format("%s", "\n");
        }
        out.format("===");

        return out.toString();
    }
    /** Return an ArrayList FINDLEGALMOVES with BOARD and WHO. */
    public ArrayList<Move> findlegalmoves(Board board, PieceColor who) {
        ArrayList<Move> legalmoves = new ArrayList<Move>();
        char oldcol;
        char oldrow;
        char newcol;
        char newrow;
        for (oldcol = 'a'; oldcol < 'h'; oldcol++) {
            for (oldrow = '1'; oldrow < '8'; oldrow++) {
                PieceColor curpos = get(oldcol, oldrow);
                if (curpos.equals(who)) {
                    for (int i = -2; i < 3; i += 1) {
                        for (int j = -2; j < 3; j += 1) {
                            newcol = (char) (oldcol + i);
                            newrow = (char) (oldrow + j);
                            Move move = Move.move(
                                oldcol, oldrow, newcol, newrow);
                            if (legalMove(move)) {
                                legalmoves.add(move);
                            }
                        }
                    }
                }
            }
        }
        return legalmoves;
    }

    /** For reasons of efficiency in copying the board,
     *  we use a 1D array to represent it, using the usual access
     *  algorithm: row r, column c => index(r, c).
     *
     *  Next, instead of using a 7x7 board, we use an 11x11 board in
     *  which the outer two rows and columns are blocks, and
     *  row 2, column 2 actually represents row 0, column 0
     *  of the real board.  As a result of this trick, there is no
     *  need to special-case being near the edge: we don't move
     *  off the edge because it looks blocked.
     *
     *  Using characters as indices, it follows that if 'a' <= c <= 'g'
     *  and '1' <= r <= '7', then row c, column r of the board corresponds
     *  to board[(c -'a' + 2) + 11 (r - '1' + 2) ], or by a little
     *  re-grouping of terms, board[c + 11 * r + SQUARE_CORRECTION]. */
    private final PieceColor[] _board;

    /** Player that is on move. */
    private PieceColor _whoseMove;

    /** Number of red pieces on board. */
    private int _redPieces;

    /** Number of blue pieces on board. */
    private int _bluePieces;

    /** Number of moves. */
    private int _numMoves;

    /** Number of jumps. */
    private int _numJumps;

    /** Store all moves. */
    private ArrayList<Move> _allMoves = new ArrayList<Move>();

    /** Store all states. */
    private ArrayList<PieceColor[]> _states = new ArrayList<PieceColor[]>();

    /** Store all numMoves. */
    private ArrayList<Integer> _allNumMoves = new ArrayList<>();

    /** Store all numJumps. */
    private ArrayList<Integer> _allNumJumps = new ArrayList<>();

    /** Store all numReds. */
    private ArrayList<Integer> _allRedPieces = new ArrayList<>();

    /** Store all numBlues. */
    private ArrayList<Integer> _allBluePieces = new ArrayList<>();

    /** Store all blocks. */
    private ArrayList<char[]> _allBlocks = new ArrayList<char[]>();

}
