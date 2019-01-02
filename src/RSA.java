package algorithms;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

public class RSA {
	
	private BigInteger P,Q;
	private static BigInteger W,N,D,E;
	
	private static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private static String inputText, encryptedText, decryptedText;
	
	private static String cipherBlock = "";
	
	public RSA()
	{
		// step 1 - choose 2 prime numbers p & q
		P = new BigInteger("41");
		Q = new BigInteger("67");
		System.out.println("The value of P = " + P);
		System.out.println("The value of Q = " + Q);
				
		//step 2 - Calculate N=P*Q
		N = P.multiply(Q); //2747
		System.out.println("The value of N = " + N);
				
		//Step 3 - Calculate W = (P-1)*(Q-1)
		W = (P.subtract(BigInteger.valueOf(1))).multiply(Q.subtract(BigInteger.valueOf(1)));
		System.out.println("The value of W = " + W); //2640
				
		//Step 4 
		//Choose D such that D and W are relatively prime and D<W ie. gcd(D,W) = 1
		D = new BigInteger("83");
		System.out.println("The value of D = " + D);
				
		//Step 5 - Compute E such that DE = 1(mod W)
		E = W.divide(D);
		System.out.println("The value of E = " + E);
		
	}
	
	public static void main(String args[]){
		RSA rsa = new RSA();
		
		System.out.println("\n\n\n ---RSA Encryption---");
		System.out.println("\nPrivate Key: <" + D + "\\" + N + ">");
        System.out.println("Public Key: <" + E + "\\" + N + ">");

		/**
         * Extended Euclid's
         */
        extendedEuclid(BigInteger.valueOf(2640),BigInteger.valueOf(83));
        
        inputText = JOptionPane.showInputDialog(null,"Enter message for encryption :");
		
		// encrypt the user's input text
		encryptedText = doRsaEncrypt(inputText);
		JOptionPane.showMessageDialog(null, "Encrypted Text : \n" + encryptedText);
		
		// decrypt the encrypted data
		decryptedText = doRsaDecrypt(encryptedText);
		JOptionPane.showMessageDialog(null, "Decrypted Text : \n" + decryptedText);
	}
	
	private static BigInteger extendedEuclid(BigInteger a, BigInteger b){
	//a = W, b = D
		
	 
	  BigInteger r,v;
	  
	  ArrayList<BigInteger> rArray = new ArrayList<BigInteger>();
	  rArray.add(a);
	  rArray.add(b);
	  
	  ArrayList<BigInteger> vArray = new ArrayList<BigInteger>();
      
      for (int i=2;i<6;i++){
    	  v = rArray.get(i-2).divide(rArray.get(i-1));
    	  vArray.add(v);
    	  
    	  r = rArray.get(i-2).mod(rArray.get(i-1));
    	  rArray.add(r);
      }
      
      return null;
	
	}
	
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
	
	public static String doRsaEncrypt(String plaintext)
	{
		String encrypted = "";
		String binary = Integer.toBinaryString(E.intValue());
		
		// assign each character of plaintext a number
		for(int i=0; i<plaintext.length(); i++){
			cipherBlock += String.format("%02d",chars.indexOf((plaintext.charAt(i)+1)));
			
			// split cipherBlock into blocks of 2 - separated by spaces
			if(i%2 == 1)
				cipherBlock += " ";
		}
		
		System.out.println("cipherblock : " + cipherBlock);
		
		// apply square & divide method to each block
		for(String s : cipherBlock.split(" "))
		{
			BigInteger C = new BigInteger("1");
			for(int i=0; i<binary.length(); i++){
				// raise C to the power of 2, & modulus N on the result
				C = (C.pow(2).mod(N));
				
				// do multiplication of current binary char is a '1'
				if(binary.charAt(i) == '1')
					C = (C.multiply(new BigInteger(s)).mod(N));
			}
			
			// attach encrypted value of each block to the encrypted string
			encrypted += C + " ";
		}
		
		// reset the cipherblock to blank
		cipherBlock = "";
		
		return encrypted;
	}
	
	public static String doRsaDecrypt(String ciphertext)
	{
		String decrypted = "";
		String binary = Long.toBinaryString(D.intValue());
		String temp = "";
		
		// do decrypted M = C^D (%N)
		for (String s : ciphertext.split(" ")) {
			
			//int D = 1;
			BigInteger D = new BigInteger("1");
			
			for(int i=0; i<binary.length(); i++){
				D = (D.pow(2).mod(N));
				//D = (int) (Math.pow(D, 2)) % N.intValue();
			
				if(binary.charAt(i) == '1')
					D = D.multiply(new BigInteger(s)).mod(N);
					//D = (D * Integer.parseInt(s)) % N.intValue();
			}
			
			temp += D;
			if(temp.length() % 2 == 1)
				temp = "0" + temp;
			cipherBlock += temp;
			cipherBlock += " ";
			temp = "";
		}
		
		for(String s : cipherBlock.split(" "))
		{
			for(int i=0; i<s.length(); i+=2)
			{
				String number = s.charAt(i) + "" + s.charAt(i+1);
				decrypted += chars.charAt(Integer.parseInt(number)-1);
			}
		}
		
		System.out.println("Decrypted text : " + decrypted);
		
		return decrypted;
	}
	
}
