package shooter.game.server.presenter.interfaces;

import game_lib.entities.Arsenal;
import game_lib.entities.Character;
import game_lib.entities.from_server.Bullet;
import game_lib.entities.from_server.DeleteBullet;

/**
 * Created by Elvira on 19.12.16.
 */
public interface BulletHandlerListener {
    void sendBullet(Bullet bullet);
    void damageCharacter(Character character, Arsenal.Weapon weapon, String killerLogin);
    void sendBulletDeletion(DeleteBullet deleteBullet);
}
