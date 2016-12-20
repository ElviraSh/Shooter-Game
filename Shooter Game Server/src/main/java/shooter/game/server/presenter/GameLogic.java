package shooter.game.server.presenter;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import game_lib.entities.Arsenal;
import game_lib.entities.Character;
import game_lib.entities.from_client.Login;
import game_lib.entities.from_client.MoveCharacter;
import game_lib.entities.from_client.Register;
import game_lib.entities.from_client.Shot;
import game_lib.entities.from_server.*;
import org.apache.commons.codec.digest.DigestUtils;
import shooter.game.server.model.InformationProvider;
import shooter.game.server.model.LocationProvider;
import shooter.game.server.model.ServerProvider;
import shooter.game.server.model.entities.CharacterConnection;
import shooter.game.server.presenter.interfaces.BulletHandlerListener;
import shooter.game.server.view.ServerControlForm;

/**
 * Created by Elvira on 18.12.16.
 */
public class GameLogic extends Listener implements BulletHandlerListener {
    public static final int CHARACTER_SPEED = 5;
    private ServerControlForm controlForm;
    private ServerProvider serverProvider;
    private InformationProvider informationProvider;
    private LocationProvider locationProvider;
    private int bulletId = 0;

    public GameLogic(ServerControlForm controlForm) {
        this.controlForm = controlForm;
    }

    public void startGame() {
        serverProvider = new ServerProvider();
        if (serverProvider.startServer(this)) {
            controlForm.setStarted();
            informationProvider = new InformationProvider();
            locationProvider = new LocationProvider();
            locationProvider.buildNewRandomLocation();
        } else {
            controlForm.setError("Server can't start");
        }
    }

    public void stopGame() {
        if (serverProvider != null) {
            if (serverProvider.stopServer()) {
                controlForm.setStopped();
            } else {
                controlForm.setError("Server can't stop");
            }
        } else {
            controlForm.setError("Server not started");
        }
    }

    @Override
    public void received(Connection c, Object object) {
        CharacterConnection characterConnection = (CharacterConnection) c;
        Character character = characterConnection.character;

        if(character != null){
            if(character.health <= 0){
                informationProvider.removeFromAllPlayers(character.login);
                c.close();
                return;
            }
        }

        if (object instanceof Login) {
            //if login
            Login login = (Login) object;
            if (character != null) {
                ErrorMessage message = new ErrorMessage();
                message.errorString = "Already logged in";
                serverProvider.sendToClient(c.getID(), message);
                return;
            }
            if(informationProvider.isUserPlaying(login.login)){
                ErrorMessage message = new ErrorMessage();
                message.errorString = "This user is playing now";
                serverProvider.sendToClient(c.getID(), message);
                return;
            }
            character = informationProvider.getCharacter(getToken(login.login, login.password));
            characterConnection.character = character;
            if (character == null) {
                ErrorMessage message = new ErrorMessage();
                message.errorString = "User not found";
                serverProvider.sendToClient(c.getID(), message);
                return;
            }
            informPlayer(c.getID(), character);
            controlForm.setPlayersCount(informationProvider.getActivePlayersCount());

        } else if (object instanceof Register) {
            //if registration
            Register register = (Register) object;
            //check already registered or authorized
            if (character != null || (informationProvider.getCharacter(getToken(register.name, register.password))) != null) {
                ErrorMessage message = new ErrorMessage();
                message.errorString = "Already registered";
                serverProvider.sendToClient(c.getID(), message);
                return;
            }
            if(informationProvider.isLoginExist(register.name)){
                ErrorMessage message = new ErrorMessage();
                message.errorString = "This login already used, use another login";
                serverProvider.sendToClient(c.getID(), message);
                return;
            }
            character = new Character();
            characterConnection.character = character;
            character.arsenal.weapon = Arsenal.Weapon.handgun;
            character.login = register.name;
            while (true) {
                int x = (int) Math.abs(Math.round(Math.random() * LocationProvider.MAP_WIDTH));
                int y = (int) Math.abs(Math.round(Math.random() * LocationProvider.MAP_HEIGHT));
                if (!isThereWall(x, y)) {
                    character.x = x;
                    character.y = y;
                    break;
                }
            }
            informationProvider.addCharacter(character, getToken(register.name, register.password));
            informPlayer(c.getID(), character);
            controlForm.setPlayersCount(informationProvider.getActivePlayersCount());

        } else if (object instanceof Shot) {
            //if player shot
            if (character == null) {
                ErrorMessage message = new ErrorMessage();
                message.errorString = "You not authorized";
                serverProvider.sendToClient(c.getID(), message);
                return;
            }
            if (character.arsenal.weapon.equals(Arsenal.Weapon.handgun)) {
                startBullet(character, locationProvider.getLastLocation());
            } else if (character.arsenal.weapon.equals(Arsenal.Weapon.shotgun) && character.arsenal.shotgunBulletCount > 0) {
                character.arsenal.shotgunBulletCount--;
                startBullet(character, locationProvider.getLastLocation());
            } else if (character.arsenal.weapon.equals(Arsenal.Weapon.rifle) && character.arsenal.rifleBulletCount > 0) {
                character.arsenal.rifleBulletCount--;
                startBullet(character, locationProvider.getLastLocation());
            }
            UpdateCharacter updateCharacter = new UpdateCharacter();
            updateCharacter.character = character;
            serverProvider.broadcastUDP(updateCharacter);
        } else if (object instanceof MoveCharacter) {
            //if player moved
            MoveCharacter moveCharacter = (MoveCharacter) object;
            if (character == null) {
                ErrorMessage message = new ErrorMessage();
                message.errorString = "Character not found";
                serverProvider.sendToClient(c.getID(), message);
                return;
            }
            int possibleX = character.x + moveCharacter.x * CHARACTER_SPEED;
            int possibleY = character.y + moveCharacter.y * CHARACTER_SPEED;
            if (!isThereWall(possibleX, possibleY)) {
                character.x = possibleX;
                character.y = possibleY;
            }
            character.deg = moveCharacter.deg;
            character.arsenal.weapon = moveCharacter.weapon;
            UpdateCharacter updateCharacter = new UpdateCharacter();
            updateCharacter.character = character;
            serverProvider.broadcastUDP(updateCharacter);
        }
    }

    @Override
    public void disconnected(Connection c) {
        CharacterConnection connection = (CharacterConnection) c;
        Character character = connection.character;
        if (character != null) {
            informationProvider.removeFromActive(character.login);
            RemoveCharacter removeCharacter = new RemoveCharacter();
            removeCharacter.login = character.login;
            serverProvider.broadcastTCP(removeCharacter);
            connection.character = null;
        }
        controlForm.setPlayersCount(informationProvider.getActivePlayersCount());
    }

    private void startBullet(Character character, boolean[][] location) {
        bulletId++;
        if (bulletId == 1000000) {
            bulletId = 0;
        }
        BulletHandlingThread handlingThread = new BulletHandlingThread(location, informationProvider, this, character, bulletId);
        new Thread(handlingThread).start();
    }

    private String getToken(String login, String password) {
        return DigestUtils.md5Hex(login + DigestUtils.md5Hex(password));
    }

    private void informPlayer(int connectionID, Character character) {
        //sending map
        GameLocation location = new GameLocation();
        location.location = locationProvider.getLastLocation();
        serverProvider.sendToClient(connectionID, location);
        //sending all users
        for (Character k :
                informationProvider.getAllActiveCharacters()) {
            AddCharacter addCharacter = new AddCharacter();
            addCharacter.character = k;
            serverProvider.sendToClient(connectionID, addCharacter);
        }
        //sending character for all users;
        informationProvider.addToActive(character);
        AddCharacter addCharacter = new AddCharacter();
        addCharacter.character = character;
        serverProvider.broadcastTCP(addCharacter);
        //sending login
        AuthorizationSuccess success = new AuthorizationSuccess();
        success.login = character.login;
        serverProvider.sendToClient(connectionID, success);
    }

    private boolean isThereWall(int x, int y) {
        int locX = x / LocationProvider.LOCATION_POINT_SCALE;
        int locY = y / LocationProvider.LOCATION_POINT_SCALE;
        return locationProvider.getLastLocation()[locX][locY];
    }

    public void sendBullet(Bullet bullet) {
        serverProvider.broadcastUDP(bullet);
    }

    public void damageCharacter(Character character, Arsenal.Weapon weapon, String killerLogin) {
        if (weapon.equals(Arsenal.Weapon.handgun)) {
            character.health -= Arsenal.handgunDamage;
        } else if (weapon.equals(Arsenal.Weapon.rifle)) {
            character.health -= Arsenal.rifleDamage;
        } else if (weapon.equals(Arsenal.Weapon.shotgun)) {
            character.health -= Arsenal.shotgunDamage;
        }
        UpdateCharacter updateCharacter = new UpdateCharacter();
        updateCharacter.character = character;
        serverProvider.broadcastUDP(updateCharacter);
        if (character.health <= 0) {
            informationProvider.upKiller(killerLogin);
        }
    }

    public void sendBulletDeletion(DeleteBullet deleteBullet) {
        serverProvider.broadcastTCP(deleteBullet);
    }
}
