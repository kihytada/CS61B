package cube;

import java.util.Observable;

import static java.lang.System.arraycopy;

/** Models an instance of the Cube puzzle: a cube with color on some sides
 *  sitting on a cell of a square grid, some of whose cells are colored.
 *  Any object may register to observe this model, using the (inherited)
 *  addObserver method.  The model notifies observers whenever it is modified.
 *  @author P. N. Hilfinger
 */
class CubeModel extends Observable {

    /** A blank cube puzzle of size 4. */
    CubeModel() {
        _row0=_col0=0;
        _side=4;
        _move=0;
        _painted = new boolean [_side][_side];
        _facePainted= new boolean [6];
    }


    /** A copy of CUBE. */
    CubeModel(CubeModel cube) {
        initialize(cube);
    }

    /** Initialize puzzle of size SIDExSIDE with the cube initially at
     *  ROW0 and COL0, with square r, c painted iff PAINTED[r][c], and
     *  with face k painted iff FACEPAINTED[k] (see isPaintedFace).
     *  Assumes that
     *    * SIDE > 2.
     *    * PAINTED is SIDExSIDE.
     *    * 0 <= ROW0, COL0 < SIDE.
     *    * FACEPAINTED has length 6.
     */
    void initialize(int side, int row0, int col0, boolean[][] painted,
                    boolean[] facePainted) {
        _row0 = row0;
        _col0 = col0;
        _painted = painted;
        _facePainted = facePainted;
        _side = side;
        _move = 0;


        setChanged();
        notifyObservers();
    }

    /** Initialize puzzle of size SIDExSIDE with the cube initially at
     *  ROW0 and COL0, with square r, c painted iff PAINTED[r][c].
     *  The cube is initially blank.
     *  Assumes that
     *    * SIDE > 2.
     *    * PAINTED is SIDExSIDE.
     *    * 0 <= ROW0, COL0 < SIDE.
     */
    void initialize(int side, int row0, int col0, boolean[][] painted) {
        initialize(side, row0, col0, painted, new boolean[6]);
    }

    /** Initialize puzzle to be a copy of CUBE. */
    void initialize(CubeModel cube) {
        _row0 = cube.cubeRow();
        _col0 = cube.cubeCol();
        _painted = new boolean[cube._painted.length][cube._painted[0].length];
        for (int i = 0; i < cube._painted.length; i+=1) {
            arraycopy(cube._painted[i], 0, _painted[i], 0,
                      cube._painted[i].length);
        }

        _facePainted = new boolean[cube._facePainted.length];
        arraycopy(cube._facePainted, 0, _facePainted, 0,
                  cube._facePainted.length);
        _side = cube.side();
        _move = cube.moves();

        setChanged();
        notifyObservers();
    }

    /** Move the cube to (ROW, COL), if that position is on the board and
     *  vertically or horizontally adjacent to the current cube position.
     *  Transfers colors as specified by the rules.
     *  Throws IllegalArgumentException if preconditions are not met.
     */
    void move(int row, int col) {
        if ((Math.abs(_row0 - row) + Math.abs(_col0 - col) == 1)
             && (row < _side) && (col < _side) && (row >= 0) && (col >= 0)) {
            boolean tmp1;
            if (row == _row0) {
                if (col > _col0) {
                    tmp1 = _facePainted[3];
                    _facePainted[3] = _facePainted[5];
                    _facePainted[5] = _facePainted[2];
                    _facePainted[2] = _facePainted[4];
                    _facePainted[4] = tmp1;
                }
                if (col < _col0) {
                    tmp1 = _facePainted[2];
                    _facePainted[2] = _facePainted[5];
                    _facePainted[5] = _facePainted[3];
                    _facePainted[3] = _facePainted[4];
                    _facePainted[4] = tmp1;
                }
            }
            if (col == _col0) {
                if (row > _row0) {
                    tmp1 = _facePainted[1];
                    _facePainted[1] = _facePainted[5];
                    _facePainted[5] = _facePainted[0];
                    _facePainted[0] = _facePainted[4];
                    _facePainted[4] = tmp1;
                }
                if (row < _row0) {
                    tmp1 = _facePainted[0];
                    _facePainted[0] = _facePainted[5];
                    _facePainted[5] = _facePainted[1];
                    _facePainted[1] = _facePainted[4];
                    _facePainted[4] = tmp1;
                }
            }
            _row0 = row;
            _col0 = col;

            boolean tmp2;
            tmp2 = _painted[row][col];
            _painted[row][col] = _facePainted[4];
            _facePainted[4] = tmp2;
            _move += 1;
        }
        else{
            throw new IllegalArgumentException();
        }
        setChanged();
        notifyObservers();
    }

    /** Return the number of squares on a side. */
    int side() {
        return _side;
    }

    /** Return true iff square ROW, COL is painted.
     *  Requires 0 <= ROW, COL < board size. */
    boolean isPaintedSquare(int row, int col) {
        return _painted[row][col];
    }

    /** Return current row of cube. */
    int cubeRow() {
        return _row0;
    }

    /** Return current column of cube. */
    int cubeCol() {
        return _col0;
    }

    /** Return the number of moves made on current puzzle. */
    int moves() {
        return _move;
    }

    /** Return true iff face #FACE, 0 <= FACE < 6, of the cube is painted.
     *  Faces are numbered as follows:
     *    0: Vertical in the direction of row 0 (nearest row to player).
     *    1: Vertical in the direction of last row.
     *    2: Vertical in the direction of column 0 (left column).
     *    3: Vertical in the direction of last column.
     *    4: Bottom face.
     *    5: Top face.
     */
    boolean isPaintedFace(int face) {
        return _facePainted[face];
    }

    /** Return true iff all faces are painted. */
    boolean allFacesPainted() {
        boolean result = true;
        for (int i = 0; i < 6; i++) {
            result = result && _facePainted[i];
        }
        return result;
    }

    /** Current row of cube. */
    private int _row0;
    /** Current colomun of cube. */
    private int _col0;
    /** Current side length. */
    private int _side;
    /** Current face on the grid. */
    private boolean[][] _painted;
    /** Current face of the cube. */
    private boolean[] _facePainted;
    /** Current count of moves. */
    private int _move;

}
