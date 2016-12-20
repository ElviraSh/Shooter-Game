package shooter.game.client.view.game_forms;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Elvira on 19.12.16.
 */
public class MainPanel extends JPanel {
    private BufferedImage bufferedImage;
    private BufferedImage miniMap;
    private String health;
    private String weapon;
    private String bullets;
    private int x;
    private int y;

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        int halfWidth = getWidth() / 2;
        int halfHeight = getHeight() / 2;
        if(bufferedImage != null){
            Graphics2D g = (Graphics2D) graphics;
            g.drawImage(bufferedImage, halfWidth - x , halfHeight - y, null);
            g.drawImage(miniMap, 0, 450,150,150, null);
            g.setFont(new Font("TimesRoman", Font.BOLD, 15));
            g.drawString("Health: " + health, 10,10);
            g.drawString("Weapon: " + weapon, 10,25);
            g.drawString("Bullets: " + bullets, 10,40);
        }
    }

    public void render(BufferedImage bufferedImage, int x, int y){
        this.x = x;
        this.y = y;
        this.bufferedImage = bufferedImage;
        repaint();
    }


    public void setMapImage(BufferedImage image) {
        this.miniMap = image;
    }


    public void drawString(String health, String weapon, String bullets) {
        this.health = health;
        this.bullets = bullets;
        this.weapon = weapon;
    }
}
