package encryptions;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DES{
	//@formatter:off
    private static final byte[] IP = { 
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9,  1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7
    };
    private static final byte[] FP = {
        40, 8, 48, 16, 56, 24, 64, 32,
        39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30,
        37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28,
        35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26,
        33, 1, 41, 9, 49, 17, 57, 25
    };
    private static final byte[] E = {
        32, 1,  2,  3,  4,  5,
        4,  5,  6,  7,  8,  9,
        8,  9,  10, 11, 12, 13,
        12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21,
        20, 21, 22, 23, 24, 25,
        24, 25, 26, 27, 28, 29,
        28, 29, 30, 31, 32, 1
    };
    private static final byte[][] S = { {
        14, 4,  13, 1,  2,  15, 11, 8,  3,  10, 6,  12, 5,  9,  0,  7,
        0,  15, 7,  4,  14, 2,  13, 1,  10, 6,  12, 11, 9,  5,  3,  8,
        4,  1,  14, 8,  13, 6,  2,  11, 15, 12, 9,  7,  3,  10, 5,  0,
        15, 12, 8,  2,  4,  9,  1,  7,  5,  11, 3,  14, 10, 0,  6,  13
    }, {
        15, 1,  8,  14, 6,  11, 3,  4,  9,  7,  2,  13, 12, 0,  5,  10,
        3,  13, 4,  7,  15, 2,  8,  14, 12, 0,  1,  10, 6,  9,  11, 5,
        0,  14, 7,  11, 10, 4,  13, 1,  5,  8,  12, 6,  9,  3,  2,  15,
        13, 8,  10, 1,  3,  15, 4,  2,  11, 6,  7,  12, 0,  5,  14, 9
    }, {
        10, 0,  9,  14, 6,  3,  15, 5,  1,  13, 12, 7,  11, 4,  2,  8,
        13, 7,  0,  9,  3,  4,  6,  10, 2,  8,  5,  14, 12, 11, 15, 1,
        13, 6,  4,  9,  8,  15, 3,  0,  11, 1,  2,  12, 5,  10, 14, 7,
        1,  10, 13, 0,  6,  9,  8,  7,  4,  15, 14, 3,  11, 5,  2,  12
    }, {
        7,  13, 14, 3,  0,  6,  9,  10, 1,  2,  8,  5,  11, 12, 4,  15,
        13, 8,  11, 5,  6,  15, 0,  3,  4,  7,  2,  12, 1,  10, 14, 9,
        10, 6,  9,  0,  12, 11, 7,  13, 15, 1,  3,  14, 5,  2,  8,  4,
        3,  15, 0,  6,  10, 1,  13, 8,  9,  4,  5,  11, 12, 7,  2,  14
    }, {
        2,  12, 4,  1,  7,  10, 11, 6,  8,  5,  3,  15, 13, 0,  14, 9,
        14, 11, 2,  12, 4,  7,  13, 1,  5,  0,  15, 10, 3,  9,  8,  6,
        4,  2,  1,  11, 10, 13, 7,  8,  15, 9,  12, 5,  6,  3,  0,  14,
        11, 8,  12, 7,  1,  14, 2,  13, 6,  15, 0,  9,  10, 4,  5,  3
    }, {
        12, 1,  10, 15, 9,  2,  6,  8,  0,  13, 3,  4,  14, 7,  5,  11,
        10, 15, 4,  2,  7,  12, 9,  5,  6,  1,  13, 14, 0,  11, 3,  8,
        9,  14, 15, 5,  2,  8,  12, 3,  7,  0,  4,  10, 1,  13, 11, 6,
        4,  3,  2,  12, 9,  5,  15, 10, 11, 14, 1,  7,  6,  0,  8,  13
    }, {
        4,  11, 2,  14, 15, 0,  8,  13, 3,  12, 9,  7,  5,  10, 6,  1,
        13, 0,  11, 7,  4,  9,  1,  10, 14, 3,  5,  12, 2,  15, 8,  6,
        1,  4,  11, 13, 12, 3,  7,  14, 10, 15, 6,  8,  0,  5,  9,  2,
        6,  11, 13, 8,  1,  4,  10, 7,  9,  5,  0,  15, 14, 2,  3,  12
    }, {
        13, 2,  8,  4,  6,  15, 11, 1,  10, 9,  3,  14, 5,  0,  12, 7,
        1,  15, 13, 8,  10, 3,  7,  4,  12, 5,  6,  11, 0,  14, 9,  2,
        7,  11, 4,  1,  9,  12, 14, 2,  0,  6,  10, 13, 15, 3,  5,  8,
        2,  1,  14, 7,  4,  10, 8,  13, 15, 12, 9,  0,  3,  5,  6,  11
    } };
    private static final byte[] P = {
        16, 7,  20, 21,
        29, 12, 28, 17,
        1,  15, 23, 26,
        5,  18, 31, 10,
        2,  8,  24, 14,
        32, 27, 3,  9,
        19, 13, 30, 6,
        22, 11, 4,  25
    };
    private static final byte[] PC1 = {
        57, 49, 41, 33, 25, 17, 9,
        1,  58, 50, 42, 34, 26, 18,
        10, 2,  59, 51, 43, 35, 27,
        19, 11, 3,  60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
        7,  62, 54, 46, 38, 30, 22,
        14, 6,  61, 53, 45, 37, 29,
        21, 13, 5,  28, 20, 12, 4
    };
    private static final byte[] PC2 = {
        14, 17, 11, 24, 1,  5,
        3,  28, 15, 6,  21, 10,
        23, 19, 12, 4,  26, 8,
        16, 7,  27, 20, 13, 2,
        41, 52, 31, 37, 47, 55,
        30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53,
        46, 42, 50, 36, 29, 32
    };
    private static final byte[] rotations = {
        1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };
	//@formatter:on
    
    public static void main(String[] args){
		char[] message = "hello hi".toCharArray();
		byte[] plaintext = new byte[message.length];
		for(int i = 0; i < plaintext.length; i++){
			plaintext[i]=(byte)message[i];
		}
		byte[] key = { 0x3b, 0x38, (byte)0x98, 0x37, 0x15, 0x20, (byte)0xf7, 0x5e};
		byte[] encrypted = encrypt(plaintext,key);
		for(int i = 0; i < encrypted.length; i++){
			System.out.print(String.format("%02x ",encrypted[i]));
		}
	}
	
	private static byte S(int boxNumber, byte src){
		src = (byte)(src & 0x20 | ((src & 0x01) << 4) | ((src & 0x1E) >> 1));
		return S[boxNumber][src];
	}
	
	private static void initialPermutation(long in){
		char[] binary = String.format("%64s",Long.toBinaryString(in)).replaceAll(" ","0").toCharArray();
		String result = "";
		for(int i = 0; i < binary.length; i++){
			result += binary[IP[i] - 1];
		}
		leftInt = (int)Long.parseLong(result.substring(0,32),2);
		rightInt = (int)Long.parseLong(result.substring(32),2);
	}
	
	public byte[] longToBytes(long x){
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(x);
		return buffer.array();
	}
	
	public static long bytesToLong(byte[] bytes){
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.put(bytes);
		buffer.flip();//need flip 
		return buffer.getLong();
	}
	
	private static int leftInt, rightInt;
	private static ArrayList<byte[]> subkeys;
	
	public static byte[] encrypt(byte[] message, byte[] key){
		long plaintext = bytesToLong(message);
		generateKeys(key);
		initialPermutation(plaintext);
		while(!subkeys.isEmpty()){
			int temp = f(rightInt,subkeys.remove(0))^leftInt;
			leftInt=rightInt;
			rightInt=temp;
		}
		return finalPerumutation();
	}
	
	private static byte[] finalPerumutation(){
		char[] bits = String.format("%32s%32s",Integer.toBinaryString(leftInt),Integer.toBinaryString(rightInt)).replaceAll(" ","0").toCharArray();
		String result = "";
		byte[] bytes = new byte[8];
		int counter=0;
		for(int i = 0; i < FP.length; i++){
			result+=bits[FP[i]-1];
			if(result.length()==8){
				bytes[counter]=Byte.parseByte(result,2);
				counter++;
				result="";
			}
		}
		return bytes;
	}

	private static int f(int right, byte[] subkey){
		char[] rightChar = String.format("%32s",Integer.toBinaryString(right)).replaceAll(" ","0").toCharArray();
		String resulting = "";
		for(int i = 0; i < E.length; i++){
			resulting += rightChar[E[i] - 1];
		}
		byte[] xor = new byte[6];
		for(int i = 0; i < xor.length; i++){
			String temp = resulting.substring(0,7);
			resulting = resulting.substring(7);
			xor[i] = (byte)(Byte.parseByte(temp,2) ^ subkey[i]);
		}
		for(int i = 0; i < xor.length; i++){
			resulting += String.format("%6s",Integer.toBinaryString(xor[i] & 0xFF)).replace(" ","0");
		}
		char[] afterS = sbox(resulting).toCharArray();
		String p = "";
		for(int i = 0; i < afterS.length; i++){
			p+=afterS[P[i]-1];
		}
		return Integer.parseInt(p,2);
	}
	
	private static String sbox(String before){
		String result = "";
		System.out.println(before);
		for(int i = 0; i < 8; i++){
			String temp = before.substring(0,7);
			before = before.substring(7);
			byte part = S(i,Byte.parseByte(temp,2));
			result += String.format("%4s",Integer.toBinaryString(part & 0xFF)).replace(" ","0");
		}
		return result;
	}
	
	private static void leftShift(char[] left, char[] right,int round){
		for(int i = 0; i < rotations[round]; i++){
			char temp = left[0];
			for(int j = 1; j < left.length; j++){
				left[j-1]=left[j];
			}
			left[left.length-1]=temp;
			
			temp = right[0];
			for(int j = 1; j < right.length; j++){
				right[j-1]=right[j];
			}
			right[right.length-1]=temp;
		}
	}
	
	private static void generateKeys(byte[] key){
		String masterKeyBits = "";
		for(int i = 0; i < key.length; i++){
			masterKeyBits+=String.format("%8s",Integer.toBinaryString(key[i] & 0xFF)).replace(" ","0");
		}
		char[] original = masterKeyBits.toCharArray();
		char[] left = new char[28];
		char[] right = new char[28];
		for(int i = 0; i < left.length; i++){
			left[i]=original[PC1[i]-1];
		}
		for(int i = left.length; i < PC1.length; i++){
			right[i-left.length]=original[PC1[i]-1];
		}
		for(int i = 0; i < rotations.length; i++){
			leftShift(left,right,i);
			addPermuted(left,right);
		}
	}

	private static void addPermuted(char[] left, char[] right){
		subkeys = new ArrayList<byte[]>();
		char[] combined = (new String(left)+new String(right)).toCharArray();
		String result = "";
		byte[] subkey = new byte[6];
		int counter=0;
		for(int i = 0; i < PC2.length; i++){
			result+=combined[PC2[i]-1];
			if(result.length()==8){
				subkey[counter]=(byte)Integer.parseInt(result,2);
				counter++;
				result="";
			}
		}
		subkeys.add(subkey);
	}
}
