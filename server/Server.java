package server;

import client.Gift;
import client.Location;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Server extends RemoteServer implements ServerAPI {
    private static final int CLIENT_UDP_PORT = 9999;
    private int eagleIdIndex = 0;
    private ArrayList<ChatRoom> rooms;
    private HashMap<Integer, InetAddress> sockets;

    public Server() {
        rooms = new ArrayList<>();
    }

    @Override
    public int register() throws RemoteException {
        int myIndex = eagleIdIndex++;
        try {
            sockets.put(myIndex, InetAddress.getByName(getClientHost()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
        return myIndex;
    }

    @Override
    public void updateLocation(int eagleId, Location newLocation) throws RemoteException {
        Iterator<ChatRoom> roomIt = rooms.iterator();
        while (roomIt.hasNext()){
            ChatRoom chatRoom = roomIt.next();
            chatRoom.leaveRoom(eagleId);
        }
        rooms.get(rooms.indexOf(newLocation)).joinRoom(eagleId);
    }

    @Override
    public void sendMessage(int eagleId, String payload) throws RemoteException {
        Iterator<ChatRoom> roomIt = rooms.iterator();
        while (roomIt.hasNext()) {
            ChatRoom chatRoom = roomIt.next();
            ArrayList<Integer> members = chatRoom.members;
            int index = members.indexOf(eagleId);
            if (index > -1) {
                for (int i=0; i < members.size(); i++) {
                    if (i != index) {
                        // TODO: 4/23/2018 make datagram packet and send message here
                    }
                }
                return;
            }
        }
    }

    @Override
    public void disconnect(int eagleId) throws RemoteException {
        Iterator<ChatRoom> roomIt = rooms.iterator();
        while (roomIt.hasNext()){
            ChatRoom chatRoom = roomIt.next();
            chatRoom.leaveRoom(eagleId);
        }
    }

    @Override
    public void putDriftBottle(int eagleId, Gift.DriftBottle bottle) throws RemoteException {
        // TODO: 4/22/18

    }

    @Override
    public Gift.DriftBottle getDriftBottle(Location location) throws RemoteException {
        // TODO: 4/22/18
        return null;
    }


    public static void main(String[] args) {
        int registryPort = Integer.parseInt(args[0]);
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(registryPort);
            Server server = new Server();
            ServerAPI stub = (ServerAPI) UnicastRemoteObject.exportObject(server, registryPort);
            registry.rebind("server", stub);
            System.out.println("Server ready");
        } catch (RemoteException e) {
            System.err.println("Server initialization exception: RMI bound failed");
            e.printStackTrace();
        }

    }

    private class ChatRoom {

        Location roomLocation;
        ArrayList<Integer> members;

        public ChatRoom(Location roomLocation) {
            this.roomLocation = roomLocation;
            members = new ArrayList<>();
        }

        public void joinRoom(int eagleId) {
            if (!members.contains(eagleId))
                members.add(eagleId);
        }

        public void leaveRoom(int eagleId) {
            if (members.contains(eagleId))
                members.remove(eagleId);
        }

        @Override
        public boolean equals(Object obj) {
            ChatRoom room = (ChatRoom) obj;
            return room.roomLocation.equals(this.roomLocation);
        }
    }
}
