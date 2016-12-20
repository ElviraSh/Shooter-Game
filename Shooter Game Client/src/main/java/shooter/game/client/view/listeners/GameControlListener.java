package shooter.game.client.view.listeners;

import game_lib.entities.Arsenal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Elvira on 19.12.16.
 */
public class GameControlListener implements KeyListener {
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    public Arsenal.Weapon weapon = Arsenal.Weapon.handgun;

    public void keyTyped(KeyEvent keyEvent) {
        switch (keyEvent.getKeyChar()) {
            case '1':
                weapon = Arsenal.Weapon.handgun;
                break;
            case '2':
                weapon = Arsenal.Weapon.rifle;
                break;
            case '3':
                weapon = Arsenal.Weapon.shotgun;
                break;
        }
    }

    public void keyPressed(KeyEvent keyEvent) {
        switch (java.lang.Character.toLowerCase(keyEvent.getKeyChar())) {
            case 'w' :
                up = true;
                break;
            case 's':
                down = true;
                break;
            case 'a':
                left = true;
                break;
            case 'd':
                right = true;
                break;
        }
    }

    public void keyReleased(KeyEvent keyEvent) {
        switch (java.lang.Character.toLowerCase(keyEvent.getKeyChar())) {
            case 'w' :
                up = false;
                break;
            case 's':
                down = false;
                break;
            case 'a':
                left = false;
                break;
            case 'd':
                right = false;
                break;
        }
    }

}
