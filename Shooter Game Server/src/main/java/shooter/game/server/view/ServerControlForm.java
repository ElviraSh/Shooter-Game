package shooter.game.server.view;

import shooter.game.server.presenter.GameLogic;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Elvira on 18.12.16.
 */
public class ServerControlForm extends JFrame {
    private JButton startServerButton;
    private JPanel mainPanel;
    private JLabel statusLabel;
    private JLabel countLabel;
    private boolean isRunning;
    private GameLogic gameLogic;

    public ServerControlForm() {
        isRunning = false;
        gameLogic = new GameLogic(this);
        initDetails();
    }

    private void initDetails() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(mainPanel);
//
        pack();
        setVisible(true);
        startServerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (isRunning) {
                    gameLogic.stopGame();
                } else {
                    gameLogic.startGame();
                }
            }
        });
    }

    public void setStarted() {
        statusLabel.setText("Started");
        startServerButton.setText("Stop Server");
        isRunning = true;
    }

    public void setStopped() {
        statusLabel.setText("Stopped");
        startServerButton.setText("Start Server");
        countLabel.setText("");
        isRunning = false;
    }

    public void setPlayersCount(int count) {
        countLabel.setText("Players count: " + count);
    }

    public void setError(String error) {
        statusLabel.setText(":(  ERROR:" + error);
    }

}
