package client;

import java.rmi.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

import server.Server;
import server.ServerAPI;

public class ChatRoomManager {
	
	private DatagramSocket socket;
	private int PORT;
	private String IPaddress;
	
	public ChatRoomManager(String IPaddress, int PORT) throws Exception{
		this.IPaddress = IPaddress;
		this.PORT = PORT;
		this.socket = new DatagramSocket();
	}

	public String getIPaddress(){
		return this.IPaddress;
	}

	public int getPort(){
		return this.PORT;
	}

	public  String readFromKeyboard() throws Exception {
		BufferedReader stdin; /* input from keyboard */
		String sendString; /* string to be sent */
		stdin = new BufferedReader(new InputStreamReader(System.in));
		sendString = stdin.readLine();
		return sendString;
	}

	public String readFromSocket() throws Exception{
		String socketString = null; /* string from socket */
		// get their responses!
		//byte[] buf is a byte array from the socket
		 byte[] buf = new byte[1000];
		 DatagramPacket recv = new DatagramPacket(buf, buf.length);
		 socket.receive(recv);
		 socketString = new String(recv.getData(), 0, recv.getLength());
		return 	socketString;	
	}
	public void sendToTerminal(String msg) throws Exception{
		//System.out.println("Multicast text: " + msg);
		System.out.println(msg);
	}

}
