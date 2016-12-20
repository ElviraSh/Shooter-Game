package shooter.game.server.model;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import game_lib.Network;
import shooter.game.server.model.entities.CharacterConnection;

import java.io.IOException;

import static com.esotericsoftware.minlog.Log.LEVEL_TRACE;

/**
 * Created by Elvira on 18.12.16.
 */
public class ServerProvider {
    private Server server;

    public boolean startServer(Listener serverListener) {
        try {
            server = new Server() {
                protected Connection newConnection() {
                    return new CharacterConnection();
                }
            };
            Network.register(server);
            server.addListener(serverListener);
            server.bind(Network.PORT_TCP, Network.PORT_UDP);
            server.start();
            // TODO: 18.12.16 remove logging
            Log.set(LEVEL_TRACE);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean stopServer() {
        try {

            if (server != null) {
                server.stop();
                server.dispose();
                server = null;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void broadcastTCP(Object object){
        if(server != null){
            server.sendToAllTCP(object);
        }
    }
    public void broadcastUDP(Object object){
        if(server != null){
            server.sendToAllUDP(object);
        }
    }

    public void sendToClient(int connectionID, Object object){
        if(server != null){
            server.sendToTCP(connectionID, object);
        }
    }
}
