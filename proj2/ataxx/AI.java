package ataxx;

import java.util.ArrayList;

import static ataxx.PieceColor.*;
import static java.lang.Math.min;
import static java.lang.Math.max;

/** A Player that computes its own moves.
 *  @author Leslie Yang
 */
class AI extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 4;
    /** A position magnitude indicating a win (for red if positive, blue
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI for GAME that will play MYCOLOR. */
    AI(Game game, PieceColor myColor) {
        super(game, myColor);
    }

    @Override
    Move myMove() {
        if (!board().canMove(myColor())) {
            if (board().numPieces(myColor()) >= 1) {
                return Move.pass();
            }
        }
        Move move = findMove();
        System.out.printf("%s moves %s.\n", myColor(), move.toString());
        return move;
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        if (myColor() == RED) {
            findMove(b, MAX_DEPTH, true, 1, -INFTY, INFTY);
        } else {
            findMove(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** Used to communicate best moves found by findMove, when asked for. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value >= BETA if SENSE==1,
     *  and minimal value or value <= ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels before using a static estimate. */

    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        ArrayList<Move> legalmoves = board.findlegalmoves(board, myColor());
        int bestSoFar = -INFTY * sense;
        if (sense == 1) {
            if ((depth == 0) || (board.gameOver())) {
                simpleFindMove(board, -1, alpha, beta);
            } else {
                for (Move m : legalmoves) {
                    if (board.legalMove(m)) {
                        board.makeMove(m);
                    } else {
                        continue;
                    }
                    int response = findMove(board, depth - 1,
                            false, -1, alpha, beta);
                    if (response >= bestSoFar) {
                        bestSoFar = response;
                        if (saveMove) {
                            _lastFoundMove = m;
                        }
                        alpha = max(alpha, bestSoFar);
                        if (beta <= alpha) {
                            board.undo();
                            break;
                        }
                    }
                    board.undo();
                }
            }
        } else {
            if ((depth == 0) || (board.gameOver())) {
                simpleFindMove(board, 1, alpha, beta);
            } else {
                for (Move m : legalmoves) {
                    if (board.legalMove(m)) {
                        board.makeMove(m);
                    } else {
                        continue;
                    }
                    int response = findMove(board, depth - 1,
                        false, 1, alpha, beta);
                    if (response <= bestSoFar) {
                        bestSoFar = response;
                        if (saveMove) {
                            _lastFoundMove = m;
                        }
                        beta = min(beta, bestSoFar);
                        if (beta <= alpha) {
                            board.undo();
                            break;
                        }
                    }
                    board.undo();
                }
            }
        }
        return bestSoFar;
    }


     /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value >= BETA if SENSE==1,
     *  and minimal value or value <= ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels before using a static estimate. */
    private int simpleFindMove(Board board, int sense, int alpha, int beta) {
        PieceColor winner = board.whoseMove().opposite();
        ArrayList<Move> legalmoves = board.findlegalmoves(board, myColor());
        int bestSoFar;
        int nextval;
        if (sense == 1) {
            bestSoFar = -INFTY;
            if ((board.gameOver()) && (winner == RED)) {
                return INFTY;
            } else if ((board.gameOver()) && (winner == BLUE)) {
                return -INFTY;
            }
            for (Move m : legalmoves) {
                board.makeMove(m);
                nextval = staticScore(board);
                if (nextval >= bestSoFar) {
                    bestSoFar = nextval;
                    alpha = max(alpha, nextval);
                    _lastFoundMove = m;
                    if (beta <= alpha) {
                        board.undo();
                        break;
                    }
                }
                board.undo();
            }
        } else {
            bestSoFar = INFTY;
            if ((board.gameOver()) && (winner == RED)) {
                return INFTY;
            } else if ((board.gameOver()) && (winner == BLUE)) {
                return -INFTY;
            }
            for (Move m : legalmoves) {
                board.makeMove(m);
                nextval = staticScore(board);
                if (nextval <= bestSoFar) {
                    bestSoFar = nextval;
                    beta = min(beta, nextval);
                    _lastFoundMove = m;
                    if (beta <= alpha) {
                        board.undo();
                        break;
                    }
                }
                board.undo();
            }
        }
        return bestSoFar;
    }


    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        if ((board.redPieces() - board.bluePieces()) > 0) {
            return board.redPieces();
        } else {
            return -board.bluePieces();
        }
    }


}

