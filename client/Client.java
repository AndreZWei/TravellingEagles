package client; /** The main class of the program
 */

import server.Server;
import server.ServerAPI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.io.*;

public class Client{
	public static Map map;
	public static Eagle eagle;
	public static final String SERVER = "172.20.10.5";

	public static void main(String[] args){

		// read the map
		try{
			File file = new File("map.config");
			Scanner scan = new Scanner(file);
			map = readMap(scan);
		} catch (FileNotFoundException e){
			System.out.println(e);
		}
		ServerAPI server = null;
		try {
			Registry registry = LocateRegistry.getRegistry(SERVER, 9999);
			System.out.print("Checking network connection...");
			server = (ServerAPI) registry.lookup("server");
			System.out.println("complete!");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}

		Eagle eagle = null;

		try{
			File file = new File("eagle.log");
			if (file.exists()){
				InputStream is = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(is);
				eagle = (Eagle) ois.readObject();
				eagle.setID(server.register());
				System.out.println("Read saved file complete!");
			}
			else {
				// Create an eagle object
				// System.out.println("Registering");
				int eagleID = server.register();
				Scanner scan = new Scanner(System.in);
				System.out.println("Give your eagle a name!");
				eagle = new Eagle(eagleID, scan.nextLine(), map.getLocations()[0]);
				OutputStream os = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeObject(eagle);
			}
		} catch (Exception e){
			e.printStackTrace();
			System.out.println(e);
		}

		// Create Sender and Receiver Threads
		try{
			ChatRoomManager crm = new ChatRoomManager(SERVER, 9999);
			Thread sender = new Thread(new ClientSenderThread(eagle, crm));
			sender.start();
			Thread receiver = new Thread(new ClientRoomReceiverThread(crm));
			receiver.start();
			Thread heartbeat = new Thread(new HeartBeatThread(eagle, crm));
			heartbeat.start();
			while(true){
				if (!sender.isAlive())
					break;
				Location loc = eagle.travel(map);
				server.updateLocation(eagle.getID(), loc);
				Thread.sleep(1000);
				//server.updateLocation(eagle.getID(), loc);
			} 
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public static Map readMap(Scanner scan){
		int l = Integer.parseInt(scan.nextLine());

		Location[] locs = new Location[l];
		double[][] matrix = new double[l][l];

		int index = 0;
		while(scan.hasNextLine()){
			String[] tokens = scan.nextLine().split(" ");
			Gift gift = new Gift(tokens[1], System.currentTimeMillis());
			Location loc = new Location(tokens[0], gift);
			locs[index] = loc;

			// read the transition matrix
			for (int i = 2; i < tokens.length; i++){
				matrix[index][i-2] = Double.parseDouble(tokens[i]);
			}
			index++;
		}

		return new Map(matrix, locs);
	}
}
