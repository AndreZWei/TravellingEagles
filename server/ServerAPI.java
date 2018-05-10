package server;


import client.Gift;
import client.Location;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerAPI extends Remote {

    /**
     * Register the client's IP address with the server
     * @return an id number for the client
     * @throws RemoteException
     */
    int register() throws RemoteException;

    /**
     * notify the server of the new location the eagle travels to
     * @param eagleId the id number of the client
     * @param newLocation the new location
     * @throws RemoteException
     */
    void updateLocation(int eagleId, Location newLocation) throws RemoteException;

    /**
     * forward message to other eagles through server
     * @param eagleId id of the eagle that send the message
     * @param payload the content of the message
     * @throws RemoteException
     */
    void sendMessage(int eagleId, String payload) throws RemoteException;

    /**
     * notify the server that the client is quitting
     * @param eagleId the id number of the client
     * @throws RemoteException
     */
    void disconnect(int eagleId) throws RemoteException;

    /**
     * leave a drift bottle at the current location
     * @param eagleId the id number of the client
     * @param bottle the content of the bottle
     * @throws RemoteException
     */
    void putDriftBottle(int eagleId, Gift.DriftBottle bottle) throws RemoteException;

    /**
     * retrieve a drift bottle from the current location
     * @param location your current location
     * @return the bottle object, null if none
     * @throws RemoteException
     */
    Gift.DriftBottle getDriftBottle(Location location) throws RemoteException;

    /**
     * notify the server that the client is still alive
     * @param eagleId the id number of the client
     * @throws RemoteException
     */
    void heartbeat(int eagleId) throws RemoteException;

    /**
     * obtain a list of IP address to send message P2P
     * @param eagleId the id number of the client
     * @return a list of active clients that should receive the message
     * @throws RemoteException
     */
    InetAddress[] p2pSend(int eagleId) throws RemoteException;

}
