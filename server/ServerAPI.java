package server;


import client.Location;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerAPI extends Remote {

    int register() throws RemoteException;

    void updateLocation(int eagleId, Location newLocation) throws RemoteException;

    void sendMessage(int eagleId, String payload) throws RemoteException;

    void disconnect(int eagleId) throws RemoteException;

    void putDriftBottle(int eagleId, String bottle) throws RemoteException;

}
