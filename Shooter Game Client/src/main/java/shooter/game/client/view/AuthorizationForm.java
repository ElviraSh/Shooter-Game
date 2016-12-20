package shooter.game.client.view;

import game_lib.entities.from_client.Login;
import game_lib.entities.from_client.Register;
import shooter.game.client.presenter.game_logic.GameThread;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Elvira on 19.12.16.
 */
public class AuthorizationForm extends JFrame{
    private JPanel mainPanel;
    private JTextField hostTextField;
    private JTextField loginTextField;
    private JPasswordField passwordField1;
    private JButton registerButton;
    private JButton loginButton;
    private GameThread gameThread;

    public AuthorizationForm() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setVisible(true);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String host = hostTextField.getText();
                String login = loginTextField.getText();
                String password = passwordField1.getText();
                boolean f = isInputDataValid(host, login, password);
                if (f){
                    Register register = new Register();
                    register.name = login;
                    register. password = password;
                    gameThread = new GameThread(register, host);
                }
            }
        });
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String host = hostTextField.getText();
                String login = loginTextField.getText();
                String password = passwordField1.getText();
                boolean f = isInputDataValid(host, login, password);
                if (f){
                    Login login1 = new Login();
                    login1.login = login;
                    login1.password = password;
                    gameThread = new GameThread(login1, host);
                }
            }
        });
    }

    public void showError(String errorText){
        JOptionPane.showMessageDialog(this,
                errorText,
                "Some error :(",
                JOptionPane.ERROR_MESSAGE);
    }

    private boolean isInputDataValid(String host, String login, String password) {
        boolean f = true;
        if (host.length() == 0) {
            showError("Enter host address!");
            f = false;
        }
        if (login.length() == 0) {
            showError("Enter login!");
            f = false;
        }
        if (password.length() == 0) {
            showError("Enter password!");
            f = false;
        }
        return f;
    }
}
