
package client;



import server.Server;
import server.ServerAPI;

public class ClientSenderThread implements Runnable {
	
	private Eagle eagle;
	private ChatRoomManager crm;
	private ServerAPI server;
	
	public ClientSenderThread(Eagle eagle, ChatRoomManager crm){
		this.eagle = eagle;
		this.crm = crm;
		this.server = crm.ServerIP;
		
	}


	public void run() {
		String msg = crm.readFromKeyboard();
		if (msg.startsWith("put")){
			eagle.showBag();
			System.out.println("Choose a gift from the bag.");
			int index = Integer.parseInt(crm.readFromKeyboard()) - 1;
			System.out.println("Enter a message you want to leave.");
			String text = crm.readFromKeyboard();
			Gift.DriftBottle bottle = new Gift.DriftBottle(eagle.getBag().get(index), text);
			server.putDriftBottle(eagle.getID(), bottle);
		}	
		else if (msg.startsWith("get")){
			server.getDriftBottle(eagle.getLocation());
		}
		else {
			server.sendMessage(eagle.getID(), msg);
		}
	}

}
