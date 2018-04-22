package server;

import client.Location;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements ServerAPI{
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

    @Override
    public int register() throws RemoteException {
        return 0;
    }

    @Override
    public void updateLocation(int eagleId, Location newLocation) throws RemoteException {

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
}
