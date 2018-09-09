package project;

import java.util.Scanner;
import encryptions.AdvancedEncryptionStandard;

public class Cipher{
	public static final int ECB = 0, CBC = 1;
	public static final int AES = 0;
	private int mode = ECB;
	private int e = AES;
	private String IV = "aef12d1e14f12d12375f2d284d343668";
	
	public void setIV(String IV){
		this.IV = IV;
	}

	public Cipher(){
	};
	
	public Cipher(int function){
		this.e = function;
	}
	
	public Cipher(int function, int mode){
		this.e = function;
		this.mode = mode;
	}
	
	public void setMode(int mode){
		this.mode = mode;
	}
	
	public void setFunction(int function){
		this.e = function;
	}
	
	public String encrypt(String message, String key){
		switch(mode){
			case CBC:
				return cbcEncrypt(message,key);
			case ECB:
			default:
				return ecbEncrypt(message,key);
		}
	}
	
	public String decrypt(String encrypted, String key){
		switch(mode){
			case CBC:
				return cbcDecrypt(encrypted,key);
			case ECB:
			default:
				return ecbDecrypt(encrypted,key);
		}
	}
	
	

	private byte[] encrytionFunction(byte[] plaintext, byte[] key){
		switch(e){
			case AES:
			default:
				return AdvancedEncryptionStandard.encrypt(plaintext,key);
		}
	}
	
	private byte[] decryptionFunction(byte[] plaintext, byte[] key){
		switch(e){
			case AES:
			default:
				return AdvancedEncryptionStandard.decrypt(plaintext,key);
		}
	}
	
	private String cbcEncrypt(String message, String key){
		byte[] keyArray = hexStringToByteArray(key);
		char[] letters = addSpaces(message);
		byte[] previous = hexStringToByteArray(IV);
		String result = "";
		for(int i = 0; i < letters.length; i += 16){
			byte[] chunck = new byte[16];
			for(int j = i; j < i + 16; j++){
				chunck[j - i] = (byte)(letters[j] ^ previous[j - i]);
			}
			previous = encrytionFunction(chunck,keyArray);
			for(int j = 0; j < previous.length; j++){
				result += String.format("%02x",previous[j]);
			}
		}
		return result;
	}
	
	private String ecbEncrypt(String message, String key){
		byte[] keyArray = hexStringToByteArray(key);
		char[] letters = addSpaces(message);
		String result = "";
		byte[] chunck = new byte[16];
		for(int i = 0; i < letters.length; i += 16){
			for(int j = i; j < i + 16; j++){
				chunck[j - i] = (byte)(letters[j]);
			}
			chunck = encrytionFunction(chunck,keyArray);
			for(int j = 0; j < chunck.length; j++){
				result += String.format("%02x",chunck[j]);
			}
		}
		return result;
	}
	
	private String cbcDecrypt(String encrypted, String key){
		byte[] keyArray = hexStringToByteArray(key);
		byte[] letters = hexStringToByteArray(encrypted);
		byte[] previous = hexStringToByteArray(IV);
		String result = "";
		for(int i = 0; i < letters.length; i += 16){
			byte[] chunck = new byte[16];
			for(int j = i; j < i + 16; j++){
				chunck[j - i] = letters[j];
			}
			chunck = decryptionFunction(chunck,keyArray);
			for(int j = 0; j < chunck.length; j++){
				previous[j]^=chunck[j];
			}
			for(int j = 0; j < previous.length; j++){
				result += (char)(previous[j]);
				previous[j]=letters[i+j];
			}
		}
		return result.trim();
	}
	
	private String ecbDecrypt(String ciphertext, String key){
		byte[] keyArray = hexStringToByteArray(key);
		byte[] letters = hexStringToByteArray(ciphertext);
		String result = "";
		byte[] chunck = new byte[16];
		for(int i = 0; i < letters.length; i += 16){
			for(int j = i; j < i + 16; j++){
				chunck[j - i] = letters[j];
			}
			chunck = decryptionFunction(chunck,keyArray);
			for(int j = 0; j < chunck.length; j++){
				result += String.format("%c",chunck[j]);
			}
		}
		return result.trim();
	}
	
	private char[] addSpaces(String message){
		while(message.length() % 16 != 0){
			message += " ";
		}
		return message.toCharArray();
	}
	
	public byte[] hexStringToByteArray(String s){
		int len = s.length();
		byte[] data = new byte[len / 2];
		for(int i = 0; i < len; i += 2){
			data[i / 2] = (byte)((Character.digit(s.charAt(i),16) << 4) + Character.digit(s.charAt(i + 1),16));
		}
		return data;
	}
	
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		System.out.print("Message: ");
		String message = scan.nextLine();
		System.out.print("Hex Key: ");
		String key = "6273647388364537902982736875756a";
		System.out.println(key);
		Cipher cipher = new Cipher();
		cipher.setMode(Cipher.CBC);
		String encrypted = cipher.encrypt(message,key);
		System.out.println("encrypted: "+encrypted);
		System.out.println("decrypted: "+cipher.decrypt(encrypted,key));
		scan.close();
	}
}
