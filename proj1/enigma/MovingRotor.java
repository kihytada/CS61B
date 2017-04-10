package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Leslie.Yang
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
        _permutation = perm;
    }

    /** Return my norches. */
    String getmynotche() {
        return _notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        String[] spnot = _notches.split("");
        char [] temp = new char [spnot.length];
        int [] temp2 = new int [spnot.length];
        for (int i = 0; i < spnot.length; i += 1) {
            temp[i] = spnot[i].charAt(0);
            temp2[i] = _permutation.alphabet().toInt(temp[i]);
        }
        for (int k = 0; k < temp2.length; k += 1) {
            if (super.setting() == _permutation.wrap(temp2[k] + 1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean trythis() {
        String[] spnot = _notches.split("");
        char [] temp = new char [spnot.length];
        int [] temp2 = new int [spnot.length];
        for (int i = 0; i < spnot.length; i += 1) {
            temp[i] = spnot[i].charAt(0);
            temp2[i] = _permutation.alphabet().toInt(temp[i]);
        }
        for (int k = 0; k < temp2.length; k += 1) {
            if (super.setting() == _permutation.wrap(temp2[k])) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        super.set(_permutation.wrap(super.setting() + 1));
    }

    /** addtional.*/
    private String _notches;
    /** addtional. */
    private Permutation _permutation;
    /** addtional. */
    private Alphabet _alphabet;
}
