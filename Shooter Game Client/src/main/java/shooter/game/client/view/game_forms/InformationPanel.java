package shooter.game.client.view.game_forms;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Elvira on 19.12.16.
 */
public class InformationPanel extends JPanel {
    private JLabel health;
    private JLabel weapon;
    private JLabel bulletsCount;

    public InformationPanel() {
        setLayout(new GridLayout(3, 2));
        JLabel healthText = new JLabel("Health:");
        JLabel weaponText = new JLabel("Weapon:");
        JLabel bulletsCountText = new JLabel("Bullets");
        health = new JLabel();
        weapon = new JLabel();
        bulletsCount = new JLabel();
        add(healthText);
        add(health);
        add(weaponText);
        add(weapon);
        add(bulletsCountText);
        add(bulletsCount);
    }

    public void setInformation(String healthS, String weaponS, String bulletsS){
        health.setText(healthS);
        weapon.setText(weaponS);
        bulletsCount.setText(bulletsS);
        repaint();
    }
}
