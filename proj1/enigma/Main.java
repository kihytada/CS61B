package enigma;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Leslie.Yang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }
        _config = getInput(args[0]);
        _newconfig = getInput(args[0]);
        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }
        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output.
     *  * B BETA III IV I AXLE (YF) (ZH) */

    private void process() {
        _machine = readConfig();
        String testline = _input.nextLine();
        if (!(testline.substring(0, 1).equals("*"))) {
            throw error("no config");
        }
        stl = testline.split(" ");
        if (stl.length <= _numOfRotors + 1) {
            throw error("Wrong number of rotors.");
        }
        helper(stl);
        rt = new String[_numOfRotors];
        System.arraycopy(stl, 1, rt, 0, _numOfRotors);
        _machine.insertRotors(rt);
        _fourletter = stl[_numOfRotors + 1];
        setUp(_machine, _fourletter);
        if (testline.contains("(")) {
            int c = testline.indexOf("(");
            _plugcycle = testline.substring(c);
            _plugperm = new Permutation(_plugcycle, _alphabet);
        } else {
            _plugcycle = "";
            _plugperm = new Permutation(_plugcycle, _alphabet);
        }
        _machine.setPlugboard(_plugperm);
        while (_input.hasNextLine()) {
            _msg = _input.nextLine();
            if (_msg.equals("")) {
                _cmsg = "";
                printMessageLine(_cmsg);
            } else if (_msg.substring(0, 1).equals("*")) {
                _settingline = _msg;
                stl = _settingline.split(" ");
                if (stl.length <= _numOfRotors + 1) {
                    throw error("Wrong number of rotors. ");
                }
                helper(stl);
                rt = new String[_numOfRotors];
                System.arraycopy(stl, 1, rt, 0, _numOfRotors);
                _machine.insertRotors(rt);
                _fourletter = stl[_numOfRotors + 1];
                setUp(_machine, _fourletter);
                if (_settingline.contains("(")) {
                    int c = _settingline.indexOf("(");
                    _plugcycle = _settingline.substring(c);
                    _plugperm = new Permutation(_plugcycle, _alphabet);
                } else {
                    _plugcycle = "";
                    _plugperm = new Permutation(_plugcycle, _alphabet);
                }
                _machine.setPlugboard(_plugperm);
            } else {
                _cmsg = _machine.convert(_msg.toUpperCase());
                printMessageLine(_cmsg.toUpperCase());
            }
        }
    }
    /** HELPER STL2. */
    private void helper(String[] stl2) {
        boolean isReflector = false;
        for (int i = 0; i < _reflectors.size(); i += 1) {
            if (stl[1].equals(_reflectors.get(i))) {
                isReflector = true;
            }
        }
        if (!isReflector) {
            throw error("Reflector in the wrong place.");
        }
        boolean nonFixed = false;
        for (int i = 0; i < _mvrotors.size(); i += 1) {
            if (stl[2].equals(_mvrotors.get(i))) {
                nonFixed = true;
            }
        }
        if (nonFixed) {
            throw error("Wrong number of arguments.");
        }

    }
    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alpha = _config.nextLine();
            _alphabet = new Alphabet(alpha);
            int sS = _config.nextInt();
            int pP = _config.nextInt();
            _numOfRotors = sS;
            _numOfPawls = pP;
            ary2();
            ArrayList<Rotor> aryRotors = new ArrayList<Rotor>();
            for (int i = 0; i < _aryhasall.length; i += 1) {
                aryRotors.add(readRotor(_aryhasall[i]));
            }
            return new Machine(_alphabet, _numOfRotors, _numOfPawls, aryRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a ROTORSTR, reading its description from _config. */
    private Rotor readRotor(String[] rotorstr) {
        try {
            String cycles = rotorstr[1];
            String name;
            String notches;
            String rotorstr0Trim = rotorstr[0].trim();
            String[] check = rotorstr0Trim.split(" ");
            Map<Character, Character> closeToOpen
                    = new HashMap<Character, Character>();
            closeToOpen.put(')', '(');
            if ((!isBalanced(cycles,
                    new LinkedList<Character>(), closeToOpen))) {
                throw error("Bad format of cycles. ");
            }
            Permutation perm = new Permutation(cycles, _alphabet);
            name = check[0];
            if (check[1].charAt(0) == 'M') {
                notches = check[1].substring(1);
                _mvrotors.add(name);
                return new MovingRotor(name, perm, notches);
            } else if (check[1].charAt(0) == 'N') {
                _fixedrotors.add(name);
                return new FixedRotor(name, perm);
            } else {
                _reflectors.add(name);
                return new Reflector(name, perm);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Return a 2-D array of rotor descriptions.*/
    private void ary2() {
        _config.nextLine();
        len();
        _newconfig.nextLine();
        _newconfig.nextLine();
        String [][] ary2 = new String [_configlength][1];
        for (int i = 0; i < _configlength; i += 1) {
            ary2[i][0] = _newconfig.nextLine();
        }
        String [][] ary2cut = new String [_configlength][2];
        for (int j = 0; j < _configlength; j += 1) {
            ary2cut[j] = cut(ary2[j]);
        }
        ArrayList<String> temp = new ArrayList<String>();
        for (int k = 0; k < ary2cut.length; k += 1) {
            String test0 = ary2cut[k][0];
            test0 = test0.replaceAll(" ", "");
            if (test0.length() == 0) {
                ary2cut[k - 1][1] = ary2cut[k - 1][1] + " " + ary2cut[k][1];
            }
        }
        for (int y = 0; y < ary2cut.length; y += 1) {
            String test1 = ary2cut[y][0];
            test1 = test1.replaceAll(" ", "");
            if (test1.length() != 0) {
                temp.add(ary2cut[y][0]);
                temp.add(ary2cut[y][1]);
            }
        }
        String[][] aryfinal = new String[(temp.size()) / 2][2];
        for (int u = 0; u < (temp.size()) / 2; u += 1) {
            aryfinal[u][0] = temp.get(u * 2);
            aryfinal[u][1] = temp.get(u * 2 + 1);
        }
        _aryhasall = aryfinal;
    }

    /** Return ARY20 Cut lines in _config. */
    private String[] cut(String[] ary20) {
        String tocut = ary20[0];
        int c = tocut.indexOf("(");
        String s1 = tocut.substring(0, c);
        String s2 = tocut.substring(c);
        String[] aftercut = {s1, s2};
        return aftercut;
    }


    /** Return the LEN (number of lines -2) in _config. */
    private void len() {
        int i = 0;
        while (_config.hasNextLine()) {
            _config.nextLine();
            i += 1;
        }
        _configlength = i;
    }

    /** Return true if STR1, has any OPENEDBRACKETS, from the Map CLOSETOOPEN.
     *  Source of code: stackOverFlow.*/
    private boolean isBalanced(final String str1,
                               final LinkedList<Character> openedBrackets,
                               final Map<Character, Character> closeToOpen) {
        if ((str1 == null) || str1.isEmpty()) {
            return openedBrackets.isEmpty();
        } else if (closeToOpen.containsValue(str1.charAt(0))) {
            openedBrackets.add(str1.charAt(0));
            return isBalanced(str1.substring(1), openedBrackets, closeToOpen);
        } else if (closeToOpen.containsKey(str1.charAt(0))) {
            if (openedBrackets.getLast() == closeToOpen.get(str1.charAt(0))) {
                openedBrackets.removeLast();
                return isBalanced(str1.substring(1),
                        openedBrackets, closeToOpen);
            } else {
                return false;
            }
        } else {
            return isBalanced(str1.substring(1), openedBrackets, closeToOpen);
        }
    }
    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /** Print MS in groups of five (except that the last group may
     *  have fewer letters).
     *  "ILOVECS" */
    private void printMessageLine(String ms) {
        for (int q = 0; q < ms.length(); q += 1) {
            if (q % 6 == 0) {
                ms = ms.substring(0, q) + " " + ms.substring(q, ms.length());
            }
        }
        _cmsg = ms.trim();
        _output.println(_cmsg);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;
    /** Source of input messages. */
    private Scanner _input;
    /** Source of machine configuration. */
    private Scanner _config;
    /** Source of machine configuration. */
    private Scanner _newconfig;
    /** File for encoded/decoded messages. */
    private PrintStream _output;
    /** addtional. */
    private Machine _machine;
    /** addtional. */
    private String _settingline;
    /** addtional. */
    private int _numOfRotors;
    /** addtional. */
    private int _numOfPawls;
    /** addtional. */
    private String _fourletter;
    /** addtional. */
    private String _plugcycle;
    /** addtional. */
    private Permutation _plugperm;
    /** addtional. */
    private int _configlength;
    /** addtional. */
    private String[][] _aryhasall;
    /** addtional. */
    private String _msg;
    /** addtional. */
    private String _cmsg;
    /** addtional. */
    private String[] stl;
    /** addtional. */
    private String[] rt;
    /** addtional. */
    private ArrayList<String> _reflectors = new ArrayList<String>();
    /** addtional. */
    private ArrayList<String> _fixedrotors = new ArrayList<String>();
    /** addtional. */
    private ArrayList<String> _mvrotors = new ArrayList<String>();
}
