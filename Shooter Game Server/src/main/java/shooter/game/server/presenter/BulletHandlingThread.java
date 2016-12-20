package shooter.game.server.presenter;

import game_lib.entities.Arsenal;
import game_lib.entities.Character;
import game_lib.entities.from_server.Bullet;
import game_lib.entities.from_server.DeleteBullet;
import shooter.game.server.model.InformationProvider;
import shooter.game.server.model.LocationProvider;
import shooter.game.server.presenter.interfaces.BulletHandlerListener;

import java.util.List;

/**
 * Created by Elvira on 19.12.16.
 */
public class BulletHandlingThread implements Runnable {
    private boolean[][] location;
    private InformationProvider informationProvider;
    private BulletHandlerListener listener;
    private Character character;
    private Bullet bullet;
    private int bulletId;
    private double deg;
    public static final int BULLET_SPEED = 28;
    private long lastTime;
    private Arsenal.Weapon weapon;

    public BulletHandlingThread(boolean[][] location, InformationProvider informationProvider, BulletHandlerListener listener, Character character, int bulletId) {
        this.location = location;
        this.informationProvider = informationProvider;
        this.listener = listener;
        this.character = character;
        this.bulletId = bulletId;
        bullet = new Bullet();
        bullet.id = bulletId;
        bullet.x = character.x;
        bullet.y = character.y;
        deg = character.deg;
        weapon = character.arsenal.weapon;
    }

    public void run() {
        while (true) {
            if (System.currentTimeMillis() - lastTime > 10) {
                lastTime = System.currentTimeMillis();
                bullet.x += BULLET_SPEED /2 * Math.cos(Math.toRadians(deg));
                bullet.y += BULLET_SPEED /2* Math.sin(Math.toRadians(deg));
                listener.sendBullet(bullet);
                if (isThereWall(bullet.x, bullet.y)) {
                    sendDeletion();
                    return;
                } else {
                    List<Character> characters = informationProvider.getAllActiveCharacters();
                    for (Character k:
                         characters) {
                        if(!k.equals(character) && getDistance(bullet, k) <= LocationProvider.CHARACTER_COLLISION_RADIUS){
                            listener.damageCharacter(k, weapon, character.login);
                            sendDeletion();
                            return;
                        }
                    }
                }
            }
        }
    }

    private void sendDeletion() {
        DeleteBullet deleteBullet = new DeleteBullet();
        deleteBullet.bulletId = bulletId;
        listener.sendBulletDeletion(deleteBullet);
    }

    private boolean isThereWall(int x, int y) {
        int locX = x / LocationProvider.LOCATION_POINT_SCALE;
        int locY = y / LocationProvider.LOCATION_POINT_SCALE;
        return location[locX][locY];
    }

    private double getDistance(Bullet bullet, Character character){
        int deltaX = bullet.x - character.x;
        int deltaY = bullet.y - character.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
