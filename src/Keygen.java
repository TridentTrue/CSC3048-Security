/**
 * Created by Adam on 28/02/2017.
 */
import java.util.Arrays;
import java.util.Scanner;
import java.util.BitSet;

public class Keygen {

    // variables
    private String[] sharedKey = new String[10];
    private int[] p10 = new int[10];
    private int[] p8 = new int[8];
    private String[] k1;
    private String[] k2;

    // accessor methods
    public String[] getSharedKey() {
        return sharedKey;
    }
    public void setSharedKey(String[] sharedKey) {
        this.sharedKey = sharedKey;
    }

    public int[] getP10() {
        return p10;
    }
    public void setP10(int[] p10) {
        this.p10 = p10;
    }

    public int[] getP8() {
        return p8;
    }
    public void setP8(int[] p8) {
        this.p8 = p8;
    }

    public String[] getK1() {
        return k1;
    }
    public void setK1(String[] k1) {
        this.k1 = k1;
    }

    public String[] getK2() {
        return k2;
    }
    public void setK2(String[] k2) {
        this.k2 = k2;
    }

    // constructor
    public Keygen(String[] sharedKey, int[] p10, int[] p8) {
        this.sharedKey = sharedKey;
        this.p10 = p10;
        this.p8 = p8;
    }

    // get Shared key from user input
    public static String[] binarySet() {

        Scanner userInput = new Scanner(System.in);

        System.out.print("10 bit Shared Key: ");
        String[] str = userInput.nextLine().split("");

        return str;
    }

    // get permutations from user input
    public static int[] strSet(String name) {

        Scanner userInput = new Scanner(System.in);

        System.out.print("Permutation " + name + ": ");
        String[] str = userInput.nextLine().split(" ");

        // convert to int[]
        int[] perm = new int[str.length];
        for (int i = 0; i < str.length; i++) {
            perm[i] = Integer.parseInt(str[i]);
        }
        return perm;
    }

    // permutation P10
    public static void perm10(String[] k, int[] p10) {

        String[] p10k = k;

        for (int i = 0; i < p10.length; i++) {
            /*int replaceValue = p10[i];
            String replacer = k[replaceValue];
            p10k[i] = k[replacer];

            System.out.print(p10k[i]);*/
        }

        System.out.println(Arrays.toString(p10k));
    }

    /*    public static BitSet convertToBitSet() {

        String str;

        // convert a string of binary into a BitSet
        BitSet bits = new BitSet();
        for (int i = 0; i < 10; i++) {
            char c = str.charAt(i);
            if (c == '1') bits.set(i, true);
            if (c == '0') bits.set(i, false);
        }
        return bits;
    }*/
}