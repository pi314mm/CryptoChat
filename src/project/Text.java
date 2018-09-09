package project;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Text{
	
	public static void main(String[] args){
		Cipher cipher = new Cipher();
		cipher.setMode(Cipher.CBC);
		String key = "6273647388364537902982736875756a";
		Server server = new Server();
		server.setCipher(cipher);
		server.setKey(key);
		server.start();
		try{
			Socket socket = new Socket("10.14.210.171",63400);
			PrintStream out = new PrintStream(socket.getOutputStream(),true);
			Scanner scan = new Scanner(System.in);
			while(scan.hasNextLine()){
				out.println(cipher.encrypt(scan.nextLine(),key));
			}
			scan.close();
			socket.close();
		}catch(Exception e){
			System.out.println(e);
		}
		server.stopListening();
	}
	
}
