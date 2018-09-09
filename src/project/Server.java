package project;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server extends Thread{
	private boolean continueRunning = true;
	private String key;
	private Cipher cipher;
	
	public void stopListening(){
		continueRunning = false;
	}
	public void setKey(String key){
		this.key=key;
	}
	
	public void setCipher(Cipher cipher){
		this.cipher=cipher;
	}
	
	
	@Override
	public void run(){
		// Wait for client to connect on 63400
		while(continueRunning){
			try{
				ServerSocket serverSocket = new ServerSocket(63400);
				Socket clientSocket = serverSocket.accept();
				// Create a reader
				Scanner scan = new Scanner(clientSocket.getInputStream());
				// Get the client message
				while(scan.hasNextLine()){
					if(key==null||cipher==null){
						System.out.println(scan.nextLine().trim());
					}else{
						String ciphertext = scan.nextLine();
						System.out.println("ciphertext: " + ciphertext);
						System.out.println(cipher.decrypt(ciphertext,key));
					}
				}
				scan.close();
				serverSocket.close();
			}catch(IOException e){
				System.out.println(e);
			}
		}
	}
}
