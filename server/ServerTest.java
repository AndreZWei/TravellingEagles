package server;

import client.Eagle;
import client.Gift;
import client.Location;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    @org.junit.jupiter.api.Test
    void updateLocation() {
        Server testServer = new Server();
        Eagle testEagle = null;
        try {
            testEagle = new Eagle(testServer.register(), "test eagle", new Location("Boston",
                    new Gift("clam chowder", System.currentTimeMillis())));
            testServer.updateLocation(testEagle.getID(), new Location("New York", new Gift("red apple",
                    System.currentTimeMillis())));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        assertEquals(testServer.rooms.);
    }

    @org.junit.jupiter.api.Test
    void putDriftBottle() {
    }

    @org.junit.jupiter.api.Test
    void getDriftBottle() {
    }



}