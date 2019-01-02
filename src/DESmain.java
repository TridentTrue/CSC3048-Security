import java.util.ArrayList;
import java.util.Arrays;
public class DESmain {
		
    public static void main(String[] args) {

        // GENERATE KEYS
    	DESkeygen myDESkeygen = new DESkeygen();
    	System.out.println("  ____  _____ ____  _                               \n" +
                            " |  _ \\| ____/ ___|| | _____ _   _  __ _  ___ _ __  \n" +
                            " | | | |  _| \\___ \\| |/ / _ \\ | | |/ _` |/ _ \\ '_ \\ \n" +
                            " | |_| | |___ ___) |   <  __/ |_| | (_| |  __/ | | |\n" +
                            " |____/|_____|____/|_|\\_\\___|\\__, |\\__, |\\___|_| |_|\n" +
                            "                             |___/ |___/            " +
    	                    "\nDESkeygen Shared Key = " + myDESkeygen.getSharedKey() +
    	                    "\nDESkeygen P10 = " + Arrays.toString(myDESkeygen.getP10()) +
    	                    "\nDESkeygen P8 = " + Arrays.toString(myDESkeygen.getP8()));

    	myDESkeygen.binarySet();
        myDESkeygen.perm10();
        myDESkeygen.leftShift(1);
        myDESkeygen.perm8();
        myDESkeygen.leftShift(3);
        myDESkeygen.perm8();

        // ENCRYPT BITS
        System.out.println("  ____  _____ ____                                   _             \n" +
                            " |  _ \\| ____/ ___|  ___ _ __   ___ _ __ _   _ _ __ | |_ ___  _ __ \n" +
                            " | | | |  _| \\___ \\ / _ \\ '_ \\ / __| '__| | | | '_ \\| __/ _ \\| '__|\n" +
                            " | |_| | |___ ___) |  __/ | | | (__| |  | |_| | |_) | || (_) | |   \n" +
                            " |____/|_____|____/ \\___|_| |_|\\___|_|   \\__, | .__/ \\__\\___/|_|   \n" +
                            "                                         |___/|_|                  ");

        String plaintext = "markfrequency";

        // convert plaintext string to binary and seperate into bytes
        byte[] bytes = plaintext.getBytes();
        StringBuilder plaintextBinary = new StringBuilder();
        for (byte b : bytes)
        {
            int val = b;
            for (int i = 0; i < 8; i++)
            {
                plaintextBinary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            plaintextBinary.append(' ');
        }
        System.out.println("'" + plaintext + "' to binary: " + plaintextBinary + "\n");

        String[] input = plaintextBinary.toString().split(" ");

        String[] output = new String[input.length];
        Arrays.fill(output, "");
        for (int i = 0; i < input.length; i++) {
            boolean verbose = false;

            if (i == input.length - 1){
                verbose = true;
            }

            DESencryptor myDESencryptor = new DESencryptor(DESencryptor.binarySet(input[i]));
            if (verbose) {
                System.out.println("\nDESencryptor ip = " + Arrays.toString(myDESencryptor.getIp()) +
                        "\nDESencryptor fp = " + Arrays.toString(myDESencryptor.getFp()) +
                        "\nDESencryptor ep = " + Arrays.toString(myDESencryptor.getEp()));
            }

            int[] ipPlaintext = myDESencryptor.perm(myDESencryptor.getPlaintextArray(), myDESencryptor.getIp());

            if (verbose) {
                System.out.println("\nAfter initial permutation, IP(P) = " + Arrays.toString(ipPlaintext));
                System.out.println("\nGENERATE fk1:");
            }

            myDESencryptor.functionF(ipPlaintext, myDESkeygen.getK1(), myDESencryptor.getFk1(), verbose);

            if (verbose) {
                System.out.println("fk1 = " + Arrays.toString(myDESencryptor.getFk1()));
                System.out.println("\nSWITCH fk1 AND GENERATE fk2:");
            }

            myDESencryptor.functionF(myDESencryptor.functionSwitch(myDESencryptor.getFk1(), verbose), myDESkeygen.getK2(), myDESencryptor.getFk2(), verbose);

            if (verbose) {
                System.out.println("fk2 = " + Arrays.toString(myDESencryptor.getFk2()));
            }

            int[] encryptedBits = myDESencryptor.perm(myDESencryptor.getFk2(), myDESencryptor.getFp());
            for (int j = 0; j < encryptedBits.length; j++) {
                output[i] += Integer.toString(encryptedBits[j]);
            }
            System.out.println("After final permutation, ENCRYPTED BYTE(" + i + ") = " + Arrays.toString(encryptedBits));
        }

        System.out.println("\nFINAL ENCRYPTED OUTPUT = " + Arrays.toString(output));
    }
}