/*
 * Class for encrypting in DES format, using the DESkeygen
 */
import java.util.Arrays;

public class DESencryptor {

    // variables
    private String plaintext;
    private static int[] plaintextArray = new int[8];

    private int[] ip = {2, 6, 3, 1, 4, 8, 5, 7};
    private int[] fp = {4, 1, 3, 5, 7, 2, 8, 6};

    private int[] ep = {4, 1, 2, 3, 2, 3, 4, 1};

    private int[][] sBox1 = {
            {1, 0, 3, 2},
            {3, 2, 1, 0},
            {0, 2, 1, 3},
            {3, 1, 3, 2}
    };
    private int[][] sBox2 = {
            {0, 1, 2, 3},
            {2, 0, 1, 3},
            {3, 0, 1, 0},
            {2, 1, 0, 3}
    };

    private int[] fk1 = new int[8];
    private int[] fk2 = new int[8];

    private int[] p4 = {2, 4, 3, 1};

    // Accessor methods
    public String getPlaintext() {
        return plaintext;
    }

    public static int[] getPlaintextArray() {
        return plaintextArray;
    }

    public int[] getFp() {
        return fp;
    }

    public int[] getIp() {
        return ip;
    }

    public int[] getEp() {
        return ep;
    }

    public int[] getFk1() {
        return fk1;
    }

    public int[] getFk2() {
        return fk2;
    }

    // constructor
    public DESencryptor(String plaintext) {
        this.plaintext = plaintext;
    }

    //converts string to int array
    public static String binarySet(String input) {

        for (int i = 0; i < input.length(); i++) {
            plaintextArray[i] = input.charAt(i) - 48;
        }

        return input;
    }

    // runs the input through the specified permutation
    public int[] perm(int[] input, int[] perm) {

        int[] result = new int[8];

        for (int i = 0; i < perm.length; i++) {
            int temp = perm[i] - 1;
            result[i] = input[temp];
        }

        return result;
    }

    public void functionF(int[] inputByte, int[] key, int[] fk, boolean v) {

        // take leftmost bits of inputByte as l
        int[] l = new int[4];
        System.arraycopy(inputByte, 0, l, 0, l.length);

        // take rightmost bits of inputByte as r
        int[] r = new int[4];
        System.arraycopy(inputByte, 4, r, 0, r.length);

        int[] epR = new int[8];

        // expansion procedure on r for first 4 bits
        for (int i = 0; i < r.length; i++) {
            int temp = ep[i] - 1;
            epR[i] = r[temp];
        }
        // second 4 bits
        for (int i = 4; i < epR.length; i++) {
            int temp = ep[i] - 1;
            epR[i] = r[temp];
        }


        // XOR epR with key
        int[] epRk = xorIntArray(epR, key);

        if (v) {
            System.out.println("L = " + Arrays.toString(l));
            System.out.println("R = " + Arrays.toString(r));
            System.out.println("After expansion, E/P(R) = " + Arrays.toString(epR));
            System.out.println("After XOR with key, E/P(R)^k = " + Arrays.toString(epRk));
        }


        // split epRk in two
        int[] firsthalf = new int[4];
        int[] lasthalf = new int[4];

        System.arraycopy(epRk, 0, firsthalf, 0, firsthalf.length);
        System.arraycopy(epRk, firsthalf.length, lasthalf, 0, lasthalf.length);

        // S-box 2d arrays using the two halves
        // look up numbers in index [0],[3] and [1],[2]
        String sBoxCombine = sBox(firsthalf, sBox1, v) + sBox(lasthalf, sBox2, v);

        // convert sBoxCombine back into int[]
        int[] sBoxOut = stringToIntArray(sBoxCombine);

        // run through permutation P4
        int[] fRk = new int[4];
        for (int i = 0; i < p4.length; i++) {
            int temp = p4[i] - 1;
            fRk[i] = sBoxOut[temp];
        }


        int[] xorFRkL = xorIntArray(fRk, l);
        // combine xorFRkL and r to make the final array
        System.arraycopy(xorFRkL, 0, fk, 0, xorFRkL.length);
        System.arraycopy(r, 0, fk, xorFRkL.length, r.length);

        if (v) {
            System.out.println("\nCombined S-box binary output: " + sBoxCombine);
            System.out.println("After permutation P4, F(R,k) =  " + Arrays.toString(fRk));
        }
    }

    public int[] xorIntArray(int[] arr1, int[] arr2) {

        int[] result = new int[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] == arr2[i]) {
                result[i] = 0;
            } else {
                result[i] = 1;
            }
        }

        return result;
    }

    // switches bits around
    public int[] functionSwitch(int[] fk, boolean v) {

        int[] switchedFk = new int[8];

        System.arraycopy(fk, 4, switchedFk, 0, 4);
        System.arraycopy(fk, 0, switchedFk, 4, 4);

        if (v) {
            System.out.println("After switch, switchFk = " + Arrays.toString(switchedFk));
        }

        return switchedFk;
    }

    public static int[] stringToIntArray(String input) {

        // take input and split on each char
        String[] str = input.split("");

        // convert str[] to int[]
        int[] intArray = new int[str.length];
        for (int i = 0; i < str.length; i++) {
            intArray[i] = Integer.parseInt(str[i]);
        }

        return intArray;
    }

    // runs 4 bit array through sbox
    public String sBox(int[] bits, int[][] sBox, boolean v) {

        int x = getCoordinate(bits[0], bits[3]);
        int y = getCoordinate(bits[1], bits[2]);

        String sboxResultBin = Integer.toBinaryString(sBox[x][y]);
        if (sboxResultBin.length() != 2) {
            sboxResultBin = "0" + sboxResultBin;
        }

        if (v) {
            System.out.println("\nS-box x coordinate: " + x);
            System.out.println("S-box y coordinate: " + y);
            System.out.println("S-box result in decimal: " + sBox[x][y]);
            System.out.println("S-box result in binary: " + sboxResultBin);
        }

        return sboxResultBin;
    }

    // converts two bits into a decimal coordinate for use with sBox
    public int getCoordinate(int c1, int c2) {
        String temp = Integer.toString(c1) + Integer.toString(c2);

        int num = Integer.parseInt(temp, 2);
        return num;
    }

}
