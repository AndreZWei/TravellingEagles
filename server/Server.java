package server;

import client.Gift;
import client.Location;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Server implements ServerAPI{
    private int eagleIdIndex = 0;
    private ArrayList<ChatRoom> rooms;

    public Server() {
        rooms = new ArrayList<>();
    }

    @Override
    public int register() throws RemoteException {
        return eagleIdIndex++;
    }

    @Override
    public void updateLocation(int eagleId, Location newLocation) throws RemoteException {
        Iterator<ChatRoom> roomIt = rooms.iterator();
        while (roomIt.hasNext()){
            ChatRoom chatRoom = roomIt.next();
            chatRoom.leaveRoom(eagleId);
        }
        rooms.s
    }

    @Override
    public void sendMessage(int eagleId, String payload) throws RemoteException {

    }

    @Override
    public void disconnect(int eagleId) throws RemoteException {

    }

    @Override
    public void putDriftBottle(int eagleId, String bottle) throws RemoteException {

    }

    @Override
    public Gift.DriftBottle getDriftBottle(Location location) throws RemoteException {
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

    }
}
