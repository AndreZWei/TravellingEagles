package server;

import client.Gift;
import client.Location;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Server extends RemoteServer implements ServerAPI {
    private static final int CLIENT_UDP_PORT = 9999;
    private int eagleIdIndex = 0;
    ArrayList<ChatRoom> rooms;
    HashMap<Integer, Session> sockets;
    private HashMap<String, LinkedList<Gift.DriftBottle>> driftBottles;

    public Server() throws IOException {
        rooms = new ArrayList<>();
        BufferedReader configReader = new BufferedReader(new FileReader("map.config"));
        String currentLine;
        configReader.readLine();
        while ((currentLine = configReader.readLine()) != null) {
            String[] tokens = currentLine.split("\\s+");
            rooms.add(new ChatRoom(new Location(tokens[0], new Gift(tokens[1], System.currentTimeMillis()))));
        }
        sockets = new HashMap<>();
        driftBottles = new HashMap<>();
    }

    @Override
    public int register() throws RemoteException {
        int myIndex = eagleIdIndex++;
        try {
            sockets.put(myIndex, new Session(InetAddress.getByName(getClientHost())));
            System.out.println("");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
        return myIndex;
    }

    @Override
    public synchronized void updateLocation(int eagleId, Location newLocation) throws RemoteException {
        Iterator<ChatRoom> roomIt = rooms.iterator();
        while (roomIt.hasNext()) {
            ChatRoom chatRoom = roomIt.next();
            chatRoom.leaveRoom(eagleId);
        }
        for (ChatRoom room : rooms) {
            if (room.roomLocation.equals(newLocation)) {
                System.out.println("joined room " + newLocation.getName());
                room.joinRoom(eagleId);
                return;
            }
        }
    }

    @Override
    public synchronized void sendMessage(int eagleId, String payload) throws RemoteException {
        System.out.println("Received message, send text");
        Iterator<ChatRoom> roomIt = rooms.iterator();
        while (roomIt.hasNext()) {
            ChatRoom chatRoom = roomIt.next();
            HashSet<Integer> members = chatRoom.members;
            if (members.contains(eagleId)) {
                byte[] buffer = payload.getBytes();
                DatagramSocket socket = null;
                try {
                    socket = new DatagramSocket();
                } catch (SocketException e) {
                    e.printStackTrace();
                    System.err.println("Unable to init socket");
                }
                Iterator<Integer> membersIt = members.iterator();
                while (membersIt.hasNext()) {
                    int memberId = membersIt.next();
                    if (memberId != eagleId) {
                        System.out.println("Entered");
                        if (sockets.containsKey(memberId)) {
                            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, sockets.get(memberId).inetAddress,
                                    CLIENT_UDP_PORT);
                            System.out.println(socket);
                            System.out.println(new String(packet.getData(), 0, packet.getLength()));
                            try {
                                socket.send(packet);
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.err.println("Unable to init socket");
                            }
                        }

                    }
                }
                return;
            }
        }
    }

    @Override
    public void disconnect(int eagleId) throws RemoteException {
        Iterator<ChatRoom> roomIt = rooms.iterator();
        while (roomIt.hasNext()) {
            ChatRoom chatRoom = roomIt.next();
            chatRoom.leaveRoom(eagleId);
        }
        sockets.remove(eagleId);
    }

    @Override
    public void putDriftBottle(int eagleId, Gift.DriftBottle bottle) throws RemoteException {
        System.out.println("Received request, put bottle ");
        // TODO: 4/22/18
        Location eagleLocation;
        for (ChatRoom room : rooms) {
            HashSet members = room.members;
            Location location;
            if (members.contains(eagleId)) {
                location = room.roomLocation;
                LinkedList<Gift.DriftBottle> bottles = driftBottles.get(location.getName());
                if (bottles == null) {
                    bottles = new LinkedList<>();
                    driftBottles.put(location.getName(), bottles);
                    System.out.println("add new list");
                }
                bottles.addLast(bottle);
                System.out.println("put bottle in room " + location.getName());
                return;
            }
        }
    }

    @Override
    public Gift.DriftBottle getDriftBottle(Location location) throws RemoteException {
        System.out.println("Get request, get bottle " + location.getName());
        LinkedList<Gift.DriftBottle> bottleHere = driftBottles.get(location.getName());
        if (bottleHere == null || bottleHere.isEmpty())
            return null;
        return bottleHere.removeFirst();
    }

    @Override
    public void heartbeat(int eagleId) {
        Session session = sockets.get(eagleId);
        session.timestamp = System.currentTimeMillis();
    }

    public static void main(String[] args) {
        int registryPort = Integer.parseInt(args[0]);
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(registryPort);
            Server server = new Server();
            ServerAPI stub = (ServerAPI) UnicastRemoteObject.exportObject(server, registryPort);
            registry.rebind("server", stub);
            HeartbeatCheck checker = new HeartbeatCheck(server);
            Thread t = new Thread(checker);
            t.start();
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server initialization exception: RMI bound failed");
            e.printStackTrace();
        }

    }

    private class ChatRoom {

        Location roomLocation;
        HashSet<Integer> members;

        public ChatRoom(Location roomLocation) {
            this.roomLocation = roomLocation;
            members = new HashSet<>();
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

    class Session {

        InetAddress inetAddress;
        long timestamp;

        public Session(InetAddress inetAddress) {
            this.inetAddress = inetAddress;
            this.timestamp = System.currentTimeMillis();
        }
    }
}
