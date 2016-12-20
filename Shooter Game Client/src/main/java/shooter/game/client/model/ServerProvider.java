package shooter.game.client.model;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import game_lib.Network;

import java.io.IOException;

/**
 * Created by Elvira on 19.12.16.
 */
public class ServerProvider {
    private Client client;

    public boolean connectToServer(String host, Listener listener){
        try{
            client = new Client(5114, 5118);
            Network.register(client);
            client.start();
            client.addListener(new Listener.ThreadedListener(listener));
            client.connect(5000, host, Network.PORT_TCP, Network.PORT_UDP);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean disconnect(){
        if (client != null){
            try {
                client.dispose();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void addListener(Listener listener){
        if(client != null){
            client.addListener(listener);
        }
    }

    public void sendTCP(Object object){
        client.sendTCP(object);
    }
}
