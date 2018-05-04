
package client;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
		//System.out.println("ClientSenderThread server = " + crm.getIP());
		try {
			Registry registry = LocateRegistry.getRegistry(crm.getIPaddress(), crm.getPort());
			server = (ServerAPI) registry.lookup("server");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}


	public void run() {
		try{
			while(true){
				String msg = crm.readFromKeyboard();
				if (msg.startsWith("put")){
					eagle.showBag();
					if (!eagle.getBag().isEmpty()){
						System.out.println("Choose a gift from the bag.");
						int index = Integer.parseInt(crm.readFromKeyboard()) - 1;
						System.out.println("Enter a message you want to leave.");
						String text = crm.readFromKeyboard();
						Gift.DriftBottle bottle = new Gift.DriftBottle(eagle.getGift(index), text);
						server.putDriftBottle(eagle.getID(), bottle);
					} else {
						System.out.println("It looks like your bag is empty...");
					}
				}	
				else if (msg.startsWith("get")){
					System.out.println("Hey! You receive a drift bottle!");
					Gift.DriftBottle bottle = server.getDriftBottle(eagle.getLocation());
					if (bottle == null){
						System.out.println("Sorry there is no bottle left in this place...");
					} else {
						eagle.addGift(bottle.getGift());
						System.out.println("You also received a note:");
						System.out.println(bottle.getMessage());
					}
				}
				else if (msg.startsWith("dis")){
					File file = new File("eagle.log");
					// Create an eagle object
					OutputStream os = new FileOutputStream(file);
					ObjectOutputStream oos = new ObjectOutputStream(os);
					oos.writeObject(eagle);
					server.disconnect(eagle.getID());
					System.out.println("disconnected!");
					System.exit(0);
					break;
				}
				else {
					//server.sendMessage(eagle.getID(), msg);
					InetAddress[] peers = server.p2pSend(eagle.getID());
					byte[] buffer = msg.getBytes();
					DatagramSocket socket = new DatagramSocket();
					for (InetAddress peer : peers) {
						DatagramPacket p = new DatagramPacket(buffer, buffer.length, peer, 9999);
						socket.send(p);
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
