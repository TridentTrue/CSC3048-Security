/**
 * Created by Adam on 28/02/2017.
 */
import java.util.Arrays;

public class DESkeygen {

    // variables
    //The initial key
    private static String sharedKey = "1111011000";
    //Initial key stored as an array
    private static int[] sharedKeyArray = new int[10];

    //Permutation10, this is the before any operations are applied
    private int[] p10 = {3,5,2,7,4,10,1,9,8,6};
    //This is the output after we have p10 to the shared key
    private int[] p10K = new int[10];
    //Output after we have shifted x amount of times (rotated)
    private int[] shiftP10K = new int[10];

    //Permutation8, applied to to generate the two 8-bit keys
    private int[] p8 = {6,3,7,4,8,5,10,9};

    //How many times we have done p8 (could pass array in params)
    private int count = 0;
    //These are the generated keys after we have completed out operations
    private int[] k1 = new int[8];
    private int[] k2 = new int[8];

    // accessor methods
    public String getSharedKey() {
        return sharedKey;
    }
	public int[] getP10() {
        return p10;
    }
    public int[] getP8() {
        return p8;
    }
    public int[] getK1() {
        return k1;
    }
    public int[] getK2() {
        return k2;
    }

    // constructor
    public DESkeygen() {
    }

    // conver tsharedKey into int array for bitwise operations
    public static String binarySet() {

        System.out.println();
        String str = sharedKey;
        
        for(int i = 0; i < str.length(); i++) {
        	sharedKeyArray[i] = str.charAt(i) - 48;
        }

        return str;
    }
    
    /* P10
     * Take shared key
     * Cycle through in loop for length of array
     * Need an array to store new array order
     * i in the loop = position
     * value in P10 = bit from key
     * we store number at i
     */
    
    public int[] perm10() {
    	
    	for(int i = 0; i < p10.length; i++) {
    		int temp = p10[i] - 1;
    		p10K[i] = sharedKeyArray[temp];
    	}
    	System.out.println("After Permutation 10, P10(k) = " + Arrays.toString(p10K));
    	
		return p10K;
    }

    // We want to left shift first and last half of p10K
    public int[] leftShift(int shiftAmount) {
    	
    	int[] firsthalf = new int[5];
    	int[] lasthalf = new int[5];
    	
    	// split p10K in two
    	System.arraycopy(p10K, 0, firsthalf, 0, firsthalf.length);
    	System.arraycopy(p10K, firsthalf.length, lasthalf, 0, lasthalf.length);
    	
    	int[] temp1 = new int[firsthalf.length];
    	int[] temp2 = new int[lasthalf.length];
    	int pos = shiftAmount;
    	
    	// insert bits that would be lost to beginning of temps
    	for(int i = 0; i < firsthalf.length - shiftAmount; i++){
            temp1[i] = firsthalf[pos];
            temp2[i] = lasthalf[pos];
            pos++;
        }
    	
    	// insert start of initial arrays into rest of temps
    	int start = 0;
        for(int i = firsthalf.length - shiftAmount; i < firsthalf.length; i++){
            temp1[i] = firsthalf[start];
            temp2[i] = lasthalf[start];
            start++;
        }
		
        // combine both temp arrays to make the final array
    	System.arraycopy(temp1, 0, shiftP10K, 0, temp1.length);
    	System.arraycopy(temp2, 0, shiftP10K, temp1.length, temp2.length);
    	System.out.println("After Lshift, shiftP10(k) = " + Arrays.toString(shiftP10K));
		return shiftP10K;
    }

    //Applying P8 on shiftP10K and generating the two output keys k1 and k2
    //Then it produces the two keys we can use for our encryption/decryption
    public void perm8() {
    	int[] k = new int[8];
    	
    	for(int i = 0; i < p8.length; i++) {
    		int temp = p8[i] - 1;
    		k[i] = shiftP10K[temp];
    	}
    	if (count == 0) {
    		k1 = k;
    		System.out.println("After 1st Permutation 8, k1 = " + Arrays.toString(k1));
    		count++;
    	} else {
    		k2 = k;
    		System.out.println("\nAfter 2nd Permutation 8:\nk1 = " + Arrays.toString(k1));
    		System.out.println("k2 = " + Arrays.toString(k2));
    	}
    }
}