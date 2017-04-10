package enigma;

import static enigma.EnigmaException.*;

/* Extra Credit Only */

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Leslie.Yang
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _chars = chars;
        cAry = new char[chars.length()];
        for (int i = 0; i < chars.length(); i += 1) {
            cAry[i] = _chars.charAt(i);
        }
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _chars.length();
    }

    /** Returns true if C is in this alphabet. */
    boolean contains(char c) {
        return _chars.contains(String.valueOf(c));
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index >= size()) {
            throw error("character index out of range");
        }
        return cAry[index];
    }

    /** Returns the index of character C, which must be in the alphabet. */
    int toInt(char c) {
        boolean ifin = false;
        int index = -1;
        for (int i = 0; i < size(); i += 1) {
            if (cAry[i] == c) {
                ifin = true;
                index = i;
            }
        }
        if (!ifin) {
            throw error("Char not in alphabet.");
        }
        return index;
    }
    /** addtional.*/
    private String _chars;
    /** addtional.*/
    private char[] cAry;
}
