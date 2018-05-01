package server;


import client.Gift;
import client.Location;
import com.sun.org.apache.regexp.internal.RE;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerAPI extends Remote {

    int register() throws RemoteException;

    void updateLocation(int eagleId, Location newLocation) throws RemoteException;

    void sendMessage(int eagleId, String payload) throws RemoteException;

    void disconnect(int eagleId) throws RemoteException;

    void putDriftBottle(int eagleId, Gift.DriftBottle bottle) throws RemoteException;

    Gift.DriftBottle getDriftBottle(Location location) throws RemoteException;

    void heartbeat(int eagleId) throws RemoteException;

}
