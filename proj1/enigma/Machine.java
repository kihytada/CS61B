package enigma;

import java.util.HashMap;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Leslie.Yang
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        _allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }


    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        rotorsAry = new Rotor[numRotors()];
        HashMap<String, Rotor> rotorsMap = new HashMap<String, Rotor>();
        for (Rotor theRotor : _allRotors) {
            rotorsMap.put(theRotor.name().toUpperCase(), theRotor);
        }
        for (int i = 0; i < rotors.length; i += 1) {
            String searchKey = rotors[i];
            if (rotorsMap.containsKey(searchKey)) {
                rotorsAry[i] = rotorsMap.get(searchKey);
            }
        }


    }

    /** Set my rotors according to SETTING, which must be a string of four
     *  upper-case letters. The first letter refers to the leftmost
     *  rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 1; i < rotorsAry.length; i += 1) {
            rotorsAry[i].set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] checkadv = new boolean [rotorsAry.length - 1];
        rotorsAry[rotorsAry.length - 1].advance();
        checkadv[rotorsAry.length - 2] = true;
        for (int i = rotorsAry.length - 1; i > 0; i -= 1) {
            if ((checkadv[i - 1]) && (rotorsAry[i - 1].rotates())
                && ((rotorsAry[i].atNotch()) || (rotorsAry[i - 1].trythis()))) {
                rotorsAry[i - 1].advance();
                checkadv[i - 2] = true;
            }
        }
        int cOut = _plugboard.permute(c);
        for (int j = rotorsAry.length - 1; j >= 0; j -= 1) {
            cOut = rotorsAry[j].convertForward(cOut);
        }
        for (int k = 1; k < rotorsAry.length; k += 1) {
            cOut = rotorsAry[k].convertBackward(cOut);
        }
        cOut = _plugboard.permute(cOut);
        return cOut;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        msg = msg.replaceAll(" ", "");
        String[] msgAry = msg.split("");
        int[] intAry = new int[msgAry.length];
        for (int i = 0; i < msgAry.length; i += 1) {
            intAry[i] = _alphabet.toInt(msgAry[i].charAt(0));
        }
        char[] temp = new char [intAry.length];
        String[] msgAryOut = new String [intAry.length];
        for (int j = 0; j < msgAry.length; j += 1) {
            intAry[j] = convert(intAry[j]);
            temp[j] = _alphabet.toChar(intAry[j]);
            msgAryOut[j] = Character.toString(temp[j]);
        }
        String cmsg = "";
        for (int k = 0; k < msgAryOut.length; k += 1) {
            cmsg += msgAryOut[k];
        }
        return cmsg;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** addtional. */
    private int _numRotors;
    /** addtional. */
    private int _numPawls;
    /** addtional. */
    private Collection<Rotor> _allRotors;
    /** addtional. */
    private Permutation _plugboard;
    /** addtional. */
    private Rotor[] rotorsAry;
}
