
package client;



import server.Server;
import server.ServerAPI;
import java.io.*;

public class ClientSenderThread implements Runnable {
	
	private Eagle eagle;
	private ChatRoomManager crm;
	private ServerAPI server;
	
	public ClientSenderThread(Eagle eagle, ChatRoomManager crm){
		this.eagle = eagle;
		this.crm = crm;
		this.server = crm.getIP();
		
	}


	public void run() {
		try{
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
			else if (msg.startsWith("dis")){
				File file = new File("eagle.log");
				// Create an eagle object
				OutputStream os = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeObject(eagle);
				server.disconnect(eagle.getID());
			}
			else {
				server.sendMessage(eagle.getID(), msg);
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

}
