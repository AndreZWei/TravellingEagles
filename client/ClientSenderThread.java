
package client;



import server.Server;
import server.ServerAPI;

public class ClientSenderThread implements Runnable {
	
	private ChatRoomManager crm;
	private ServerAPI server;
	
	public ClientSenderThread(ChatRoomManager crm){
		this.crm = crm;
		this.server = crm.ServerIP;
		
	}


	public void run() {
		String msg = crm.readFromKeyboard();
		server.sendMessage(text);
	}

}
