
import java.rmi.registry.*;

public class ChatRoomManager {
	
	
	String ServerIp;
	Registry registry;
	
	public ChatRoomManager(String ServerIP ){
		this.ServerIp = ServerIp;
		try {
	        registry = LocateRegistry.getRegistry(ServerIp);
	         
	     } catch (Exception e) {
	         System.err.println("Client exception: " + e.toString());
	         e.printStackTrace();
	     }
	}
	

}
