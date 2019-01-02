import java.util.Arrays;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

public class AES {
	
	public static void main(String[] args) {
		
		//Scanner password = new Scanner(System.in);
		//System.out.println("Enter a Password: ");
		//String plaintext = password.nextLine();
		String plaintext = "mark_frequency__";

		System.out.println("Plaintext in: " + plaintext);
		
		
//		while(plaintext.length() % 16 != 0){
//			plaintext += "_";
//		}
		
//		System.out.println("TEXT: " + plaintext);
		
		String ciphertext="";
		String[][] state = new String[4][4];
		int Nb = 4;
		int Nr = 10;
		byte[] in;
		byte[] out;
		String[][] key = keyExpansion();
		
		System.out.println("Key used: ");
		System.out.println(Arrays.deepToString(initialisationKey()));
		
		in = plaintext.getBytes();
		
		int count = 0;
		for (int j = 0; j < 4; j++) {
			for (int k = 0; k < 4; k++) {
				state[j][k] = String.format("%02X", in[count]);

				count++;
			}
		}

		state = addRoundKey(state, key);
		for (int i = 0; i < Nr - 1; i++) {
			state = subBytes(state);
			state = shiftRows(state);
			state = mixColumns(state);
			state = addRoundKey(state, key);
		}
		
		state = subBytes(state);
		state = shiftRows(state);
		String[][] finalstate = addRoundKey(state, key);
		
		System.out.println("Final State: ");
		for (int x = 0; x < 4; x++) {
			for (int z = 0; z < 4; z++) {
				ciphertext += finalstate[x][z].toString();
			}
			//System.out.println();
		}
		
		
		
		System.out.println(Arrays.deepToString(finalstate));
		System.out.println("Cipher text" + ciphertext);
		
		//Integer.decode(ciphertext);
//		fromHexString(ciphertext);
//		System.out.println(ciphertext);
		
//		 byte[] bytes = DatatypeConverter.parseHexBinary(ciphertext);
//	        String ascii = new String(bytes);
//	        System.out.println("Hex to ASCII: " + ascii);
		
		//String t = hexToASCII(ciphertext);
		//System.out.println(t);
		
//		out = state;
	}
	
	
	private static String hexToASCII(String hexValue)
	{
	    StringBuilder output = new StringBuilder("");
	    for (int i = 0; i < hexValue.length(); i += 2)
	    {
	        String str = hexValue.substring(i, i + 2);
	        output.append((char) Integer.parseInt(str, 16));
	    }
	    return output.toString();
	}
	
	public static String fromHexString(String hex) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < hex.length(); i+=2) {
            str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return str.toString();
    }

	public static String[][] subBytes(String[][] state) {
		String[][] sbox = sBox();			
		String hex;
		int x, y;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				hex = state[i][j];
				String k = "" + hex.charAt(0);
				String l = "" + hex.charAt(1);
				x = Integer.parseInt(k, 16);
				y = Integer.parseInt(l, 16);
				state[i][j] = sbox[x][y];
			}
		}
		
		return state;
	}

	/**
	 * Shift columns to the left by row number
	 * 0%4 = 0 -> shift 0
	 * 1%4 = 1 -> shift 1
	 * 2%4 = 2 -> shift 2
	 * 3%4 = 3 -> shift 3
	 * 
	 * @param state
	 * @return state - String[][]
	 */
	public static String[][] shiftRows(String[][] state) {
		
		String[] temp = new String[4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				temp[j] = state[i][(j + i) % 4];
			}
			for (int j = 0; j < 4; j++) {
				state[i][j] = temp[j];
			}
		}
		
		return state;
	}
	
	public static String mixCol2(String num) {
		int n = 0;
		int num2 = Integer.valueOf(num.trim(), 16);
		String j = String.format("%8s", Integer.toBinaryString(num2)).replace(' ', '0');
		//if first bit = 0 shift by 1, if first bit 1 shift by 1 and xor with 27
		if(j.charAt(0)=='0') {
			num2 = (num2 << 1);
			j = String.format("%8s", Integer.toBinaryString(num2)).replace(' ', '0');
			j = j.substring(j.length() - 8);
			n = Integer.parseInt(j,2);
		} else if(j.charAt(0)=='1') {
			//shift left by one
			num2 = (num2 << 1);
			j = String.format("%8s",Integer.toBinaryString(num2)).replace(' ', '0');
			//remove first char of binary string after shift 
			j = j.substring(j.length() - 8);
			num2 = Integer.parseInt(j);
				
			String xor = "00011011";
			j = xor(j, xor);
			n = Integer.parseInt(j,2);
		}
			return num = String.format("%02X", n);
	}
		
	public static String mixCol3(String num) {
		int n = Integer.parseInt(num.trim(), 16);
		int temp = Integer.parseInt(mixCol2(num), 16);
		n = (n ^ temp);
		return num = String.format("%02X", n);
	}
	
	public static String[][] mixColumns(String[][] state) {
		
		int[][] mixColTran = { {2,3,1,1},
							{1,2,3,1},
							{1,1,2,3},
							{3,1,1,2} };
		 
		String[][] finalMix = new String[4][4];
		int[] helper = new int[4];
		
		for(int h=0; h<4; h++){
			for(int i=0; i<4; i++){
				for(int j=0; j<4; j++){
					if(mixColTran[i][j] == 1) {
						helper[j] = Integer.parseInt(state[j][h],16);
					} else if (mixColTran[i][j] == 2) {
						helper[j] = Integer.parseInt(mixCol2(state[j][h]),16);
					}else if(mixColTran[i][j] == 3) {
						helper[j] = Integer.parseInt(mixCol3(state[j][h]),16);
					}
				}
				int temp = (helper[0]^helper[1]);
				temp = (temp^helper[2]);
				temp = (temp^helper[3]);
				finalMix[i][h] = String.format("%02X", temp);
			}
		}
		
		return finalMix;
	}

	public static String[][] addRoundKey(String[][] state, String[][] key) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				String binstate = String.format("%8s", Integer.toBinaryString((Integer.parseInt(state[j][i], 16)))).replace(' ', '0');
				String binrndkey = String.format("%8s", Integer.toBinaryString((Integer.parseInt(key[j][i], 16)))).replace(' ', '0');
				String finalState = xor(binstate, binrndkey);
				state[j][i] = String.format("%02X", Integer.parseInt(finalState, 2));
			}
		}
		
		return state;
	}
	
	/**
	 * if values are equal then 0
	 * if values are different then 1
	 * @param binary1
	 * @param binary2
	 * @return result - String
	 */
	private static String xor(String binary1, String binary2) {
		String result = "";

		for (int i = 0; i < binary1.length(); i++) {
			if (binary1.charAt(i) == binary2.charAt(i)) {
				result = result + "0";
			} else {
				result = result + "1";
			}
		}
		
		return result;
	}
	
	/**
	 * if values are equal then 0
	 * if values are different then 1
	 * @param binary1
	 * @param binary2
	 * @return result - String
	 */
	private static String[] xor2(String[] hex1, String[] hex2) {
		String result[] = new String[hex1.length];

		for (int i = 0; i < hex1.length; i++) {
			String binary1 = String.format("%8s",Integer.toBinaryString((Integer.parseInt(hex1[i],16)))).replace(' ', '0');
			String binary2 = String.format("%8s",Integer.toBinaryString((Integer.parseInt(hex2[i],16)))).replace(' ', '0');
			String temp = xor(binary1, binary2);
			int n = Integer.parseInt(temp, 2);
			result[i] = String.format("%02X", n);
		}
		
		return result;
	}
	
	/**
	 * 
	 * 
	 * KEY EXPANSION
	 * 
	 * 
	 * @return key[][]
	 */
	
	public static String[][] keyExpansion() {
		String[][] key = initialisationKey();
		String[][] expandKey = new String[4][44];
		String[][] rCon = rCon();
		String[] temp = new String[4], word = new String[4], w = new String[4];
		
		for(int i=0; i < key.length; i++){
			for(int j=0; j < key.length; j++){
				expandKey[i][j] = key[i][j];
			}
		}
		
		for(int i=4; i < 44; i++){
			if((i%4) == 0) {
				int rCol = (i/4);
				// fill temp and rcon
				for (int j=0; j < 4; j++){
					temp[j] = expandKey[j][i-1 ];
					word[j] = rCon[j][rCol-1];
					w[j] = expandKey[j][i-4];
				}
				temp = rotWord(temp);
				//subWord
				temp = subWord(temp);
				// XOR with Rcon
				temp = xor2(temp, word);
				//XOR temp with w
				temp = xor2(temp, w);
				for(int j=0; j < 4; j++){
					expandKey[j][i] = temp[j];
				}
			}
			else{
				for (int j=0; j < 4; j++){
					w[j] = expandKey[j][i-4];
					temp[j] = expandKey[j][i-1 ];
				}
				//XOR temp with w
				temp = xor2(temp, w);
				for(int j=0; j < 4; j++){
					expandKey[j][i] = temp[j];
				}
			}
		}
		
		return expandKey;
	}
	
	public static String[] rotWord(String[] word) {
		//1234 becomes 2341
		String temp = word[0];
		for (int i = 1; i < 4; i++) {
			word[i-1] = word[i];
		}
		word[3] = temp;
		
		return word;
	}
	
	/**
	 * rConstant for key expansion
	 */
	public static String[][] rCon() {
		String rCon[][] = { {"01", "02", "04", "08", "10", "20", "40", "80", "1b", "36"},
							{"00", "00", "00", "00", "00", "00", "00", "00", "00", "00"},
							{"00", "00", "00", "00", "00", "00", "00", "00", "00", "00"},
							{"00", "00", "00", "00", "00", "00", "00", "00", "00", "00"} };
		
		return rCon;	
	}
	
	/**
	 * 
	 * @param word
	 * @return
	 */
	public static String[] subWord(String[] word) {
		String[][] sbox = sBox();
		String hex;
		int x, y;
		
		for (int i = 0; i < 4; i++) {
			hex = word[i];
			String j = "" + hex.charAt(0);
			String k = "" + hex.charAt(1);
			x = Integer.parseInt(j, 16);
			y = Integer.parseInt(k, 16);
			word[i] = sbox[x][y];
		}
		return word;	
	}
	
	/**
	 * 
	 * @return
	 */
	public static String[][] initialisationKey() {
		String[][] key = { {"2b", "28", "ab", "09"},
						   {"7e", "ae", "f7", "cf"},
						   {"15", "d2", "15", "4f"},
						   {"16", "a6", "88", "3c"} };
		
		return key;
	}
	
	public static String[][] sBox() {
		String[][] sbox = new String[][]{ {"63","7c","77","7b","f2","6b","6f","c5","30","01","67","2b","fe","d7","ab","76"},
										  {"ca","82","c9","7d","fa","59","47","f0","ad","d4","a2","af","9c","a4","72","c0"},
										  {"b7","fd","93","26","36","3f","f7","cc","34","a5","e5","f1","71","bd8","31","15"},
										  {"04","c7","23","c3","18","96","05","9a","07","12","80","e2","eb","27","b2","75"},
										  {"09","83","2c","1a","1b","6e","5a","a0","52","3b","d6","b3","29","43","2f","84"},
										  {"53","d1","00","ed","20","fc","b1","5b","6a","cb","be","39","4a","4c","58","cf"},	
										  {"d0","ef","aa","fb","43","4d","33","85","45","f9","02","7f","50","3c","9f","a8"},
										  {"51","a3","40","8f","92","9d","38","f5","bc","b6","da","21","10","ff","f3","d2"},
										  {"cd","0c","13","ec","5f","97","44","17","c4","a7","7e","3d","64","5d","19","73"},
									      {"60","81","4f","dc","22","2a","90","88","46","ee","b8","14","de","5e","0b","db"},
										  {"e0","32","3a","0a","49","06","24","5c","c2","d3","ac","62","91","95","e4","79"},
										  {"e7","c8","37","6d","8d","d5","4e","a9","6c","56","f4","ea","65","7a","ae","08"},
										  {"ba","78","25","2e","1c","a6","b4","c6","e8","dd","74","1f","4b","bd","8b","8a"},
										  {"70","3e","b5","66","48","03","f6","0e","61","35","57","b9","86","c1","1d","9e"},
										  {"e1","f8","98","11","69","d9","8e","94","9b","1e","87","e9","ce","55","28","df"},
										  {"8c","a1","89","0d","bf","e6","42","68","41","99","2d","0f","b0","54","bb","16"} };
				
		return sbox;
	}
	
	
}