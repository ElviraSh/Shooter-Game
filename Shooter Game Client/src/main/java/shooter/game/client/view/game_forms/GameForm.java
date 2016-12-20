package shooter.game.client.view.game_forms;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Elvira on 19.12.16.
 */
public class GameForm extends JFrame {

    private MainPanel mainPanel;
    private InformationPanel informationPanel;

    public GameForm() throws HeadlessException {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setLayout(null);
        setResizable(false);

        mainPanel = new MainPanel();
        mainPanel.setBounds(0, 0, 800, 600);
        mainPanel.setVisible(true);
        add(mainPanel);

        informationPanel = new InformationPanel();
        informationPanel.setBounds(0, 0, 200, 80);
        informationPanel.setVisible(true);
        informationPanel.setBackground(Color.BLUE);
        add(informationPanel);

        setVisible(true);
    }

    public void showError(String errorText) {
        JOptionPane.showMessageDialog(this,
                errorText,
                "Some error :(",
                JOptionPane.ERROR_MESSAGE);
    }
    public void showInformaion(String errorText) {
        JOptionPane.showMessageDialog(this,
                errorText,
                "Вы проиграли",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void drawMiniMap(BufferedImage image) {
        mainPanel.setMapImage(image);
    }

    public void setCharacterInformation(String health, String weapon, String bullets) {
        mainPanel.drawString(health, weapon, bullets);
//        informationPanel.setInformation(health, weapon, bullets);
    }

    public void drawGameTable(BufferedImage bufferedImage, int x, int y){
        mainPanel.render(bufferedImage, x, y);
    }
}
