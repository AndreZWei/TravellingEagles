/* Heartbeat

*/

package client;

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
		Thread.sleep(1000);
		server.heartbeat(eagle.getID());
	}

}