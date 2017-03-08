/**
 * Created by Adam on 28/02/2017.
 */
import java.util.Arrays;
import java.util.Scanner;

public class Keygen {

    // variables
    private String sharedKey;
    private static int[] sharedKeyArray = new int[10];
    
    private int[] p10 = new int[10];
    private int[] p8 = new int[8];
    private int[] p10K = new int[10];
    private int[] shiftP10K = new int[10];
    
    private int count = 0;
    private int[] k1 = new int[8];
    private int[] k2 = new int[8];

    // accessor methods
    public String getSharedKey() {
        return sharedKey;
    }
    public void setSharedKey(String sharedKey) {
        this.sharedKey = sharedKey;
    }

    public int[] getSharedKeyArray() {
		return sharedKeyArray;
	}
	public void setSharedKeyArray(int[] sharedKeyArray) {
		Keygen.sharedKeyArray = sharedKeyArray;
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

    public int[] getP10K() {
		return p10K;
	}
	public void setP10K(int[] p10k) {
		p10K = p10k;
	}
	
	public int[] getShiftP10K() {
		return shiftP10K;
	}
	public void setShiftP10K(int[] shiftP10K) {
		this.shiftP10K = shiftP10K;
	}
	
	public int[] getK1() {
        return k1;
    }
    public void setK1(int[] k1) {
        this.k1 = k1;
    }

    public int[] getK2() {
        return k2;
    }
    public void setK2(int[] k2) {
        this.k2 = k2;
    }

    // constructor
    public Keygen(String sharedKey, int[] p10, int[] p8) {
        this.sharedKey = sharedKey;
        this.p10 = p10;
        this.p8 = p8;
    }

    // get Shared key from user input
    public static String binarySet() {

        Scanner userInput = new Scanner(System.in);

        System.out.print("10 bit Shared Key: ");
        String str = userInput.nextLine();
        
        for(int i = 0; i < str.length(); i++) {
        	sharedKeyArray[i] = str.charAt(i) - 48;
        }
        System.out.println("SharedKeyArray: " + Arrays.toString(sharedKeyArray));

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
    	System.out.println("After Permutation 10: " + Arrays.toString(p10K));
    	
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
    	System.out.println("Shifted Array: " + Arrays.toString(shiftP10K));
		return shiftP10K;
    }
    
    public void perm8() {
    	int[] k = new int[8];
    	
    	for(int i = 0; i < p8.length; i++) {
    		int temp = p8[i] - 1;
    		k[i] = shiftP10K[temp];
    	}
    	if (count == 0) {
    		k1 = k;
    		System.out.println("After Permutation 8, k1: " + Arrays.toString(k1));
    		count++;
    	} else {
    		k2 = k;
    		System.out.println("After Permutation 8, k1: " + Arrays.toString(k1));
    		System.out.println("After Permutation 8, k2: " + Arrays.toString(k2));
    	}
    }
}