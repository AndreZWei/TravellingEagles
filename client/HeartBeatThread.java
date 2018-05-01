/* Heartbeat

*/

package client;

import server.Server;
import server.ServerAPI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HeartBeatThread implements Runnable{
	private ServerAPI server;
	private ChatRoomManager crm;
	private Eagle eagle;

	public HeartBeatThread(Eagle eagle, ChatRoomManager crm){
		this.eagle = eagle;
		this.crm = crm;
		try {
			Registry registry = LocateRegistry.getRegistry(crm.getIPaddress(), crm.getPort());
			server = (ServerAPI) registry.lookup("server");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	public void run(){
		while(true){
			try{
				Thread.sleep(1000);
			} catch (Exception e){
				e.printStackTrace();
			}
			server.heartbeat(eagle.getID());
		}
		
	}

}