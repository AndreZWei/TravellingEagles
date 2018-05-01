package server;

import java.util.Iterator;
import java.util.Map;

public class HeartbeatCheck implements Runnable{

    private Server server;

    @Override
    public void run() {
        Iterator it = server.sockets.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Server.Session> pair = (Map.Entry<Integer, Server.Session>) it.next();
            Server.Session session = pair.getValue();
            if (System.currentTimeMillis() - session.timestamp > 3000) {
                it.remove();
            }
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
