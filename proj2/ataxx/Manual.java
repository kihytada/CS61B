package ataxx;

import static ataxx.PieceColor.*;

/** A Player that receives its moves from its Game's getMoveCmnd method.
 *  @author Leslie Yang
 */
class Manual extends Player {

    /** A Player that will play MYCOLOR on GAME, taking its moves from
     *  GAME. */
    Manual(Game game, PieceColor myColor) {
        super(game, myColor);
    }

    @Override
    Move myMove() {
        Command myCom = super.game().getMoveCmnd(
                String.format("%s: ", myColor()));
        String[] operands = myCom.operands();
        char oldcol =  operands[0].charAt(0);
        char oldrow =  operands[1].charAt(0);
        char newcol =  operands[2].charAt(0);
        char newrow =  operands[3].charAt(0);
        Move move = Move.move(oldcol, oldrow, newcol, newrow);
        return move;
    }

}

