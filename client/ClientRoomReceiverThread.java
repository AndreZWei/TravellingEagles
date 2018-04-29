package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ClientRoomReceiverThread implements Runnable {
	
	private ChatRoomManager crm;
	
	public ClientRoomReceiverThread (ChatRoomManager crm){
		this.crm = crm;
	}
	
	
	
	@Override
	public void run() {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(crm.getPort());
		} catch (SocketException e) {
			e.printStackTrace();
		}

		while (true) {
			byte[] buf = new byte[1024];
			DatagramPacket p = new DatagramPacket(buf, buf.length);

			try {
				socket.receive(p);
				String message = new String(p.getData(), 0, p.getLength());
				System.out.println("Message:"+message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	

}
