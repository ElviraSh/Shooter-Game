package game_lib;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import game_lib.entities.Arsenal;
import game_lib.entities.Character;
import game_lib.entities.from_client.Login;
import game_lib.entities.from_client.MoveCharacter;
import game_lib.entities.from_client.Register;
import game_lib.entities.from_client.Shot;
import game_lib.entities.from_server.UpdateCharacter;
import game_lib.entities.from_server.*;

/**
 * Created by Elvira on 18.12.16.
 */
public class Network {
    public static final int PORT_TCP = 54555;
    public static final int PORT_UDP = 54556;

    public static void register (EndPoint endPoint){
        Kryo kryo = endPoint.getKryo();
        //registration classes from client
        kryo.register(Login.class);
        kryo.register(MoveCharacter.class);
        kryo.register(Register.class);
        kryo.register(Shot.class);
        //registration classes from server
        kryo.register(AddCharacter.class);
        kryo.register(AuthorizationSuccess.class);
        kryo.register(ErrorMessage.class);
        kryo.register(GameLocation.class);
        kryo.register(RemoveCharacter.class);
        kryo.register(Bullet.class);
        kryo.register(DeleteBullet.class);
        kryo.register(UpdateCharacter.class);
        //registration common classes
        kryo.register(Arsenal.class);
        kryo.register(Arsenal.Weapon.class);
        kryo.register(Character.class);
        kryo.register(boolean[][].class);
        kryo.register(boolean[].class);
    }
}
