import java.util.Arrays;

public class DESmain {
		
    public static void main(String[] args) {	

//        Keygen myKeygen = new Keygen(Keygen.binarySet(), Keygen.strSet("P10"), Keygen.strSet("P8"));
//
//        System.out.println("Keygen Shared Key = " + myKeygen.getSharedKey()
//                            + "\nKeygen P10 = " + Arrays.toString(myKeygen.getP10())
//                            + "\nKeygen P8 = " + Arrays.toString(myKeygen.getP8()));
    	
    	// TESTING ONLY
    	int[] p10 = new int[] {3,5,2,7,4,10,1,9,8,6};
    	int[] p8 = new int[] {6,3,7,4,8,5,10,9};
    	Keygen myKeygen = new Keygen(Keygen.binarySet(), p10, p8);
    	
    	System.out.println("Keygen Shared Key = " + myKeygen.getSharedKey()
    	                    + "\nKeygen P10 = " + Arrays.toString(myKeygen.getP10())
    	                    + "\nKeygen P8 = " + Arrays.toString(myKeygen.getP8()));
        
        myKeygen.perm10();
        myKeygen.leftShift(1);
        myKeygen.perm8();
        myKeygen.leftShift(3);
        myKeygen.perm8();
    }
}