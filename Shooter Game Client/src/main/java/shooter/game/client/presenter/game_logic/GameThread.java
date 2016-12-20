package shooter.game.client.presenter.game_logic;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import game_lib.entities.Character;
import game_lib.entities.from_client.Login;
import game_lib.entities.from_client.MoveCharacter;
import game_lib.entities.from_client.Register;
import game_lib.entities.from_client.Shot;
import game_lib.entities.from_server.*;
import shooter.game.client.model.InformationProvider;
import shooter.game.client.model.ServerProvider;
import shooter.game.client.view.game_forms.GameForm;
import shooter.game.client.view.listeners.GameControlListener;
import shooter.game.client.view.listeners.MouseRotationListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Elvira on 19.12.16.
 */
public class GameThread extends Listener implements Runnable, MouseListener {
    private ServerProvider serverProvider;
    private Object logReg;
    private InformationProvider informationProvider;
    private GameForm gameForm;
    private String myLogin;
    private GameControlListener controlListener;
    private MouseRotationListener rotationListener;
    private long delta = 60;
    private long lastTime = 0;
    private BufferedImage gameTable;
    private BufferedImage rifle;
    private BufferedImage handgun;
    private BufferedImage shotgun;
    private int spriteWidth;
    private int spriteHeight;
    private boolean playing;

    public GameThread(Object o, String host) {
        this.logReg = o;
        gameForm = new GameForm();
        gameTable = new BufferedImage(InformationProvider.MAP_WIDTH, InformationProvider.MAP_HEIGHT, BufferedImage.TYPE_INT_RGB);
        rotationListener = new MouseRotationListener(gameForm.getWidth(), gameForm.getHeight());
        controlListener = new GameControlListener();
        gameForm.addMouseMotionListener(rotationListener);
        gameForm.addMouseListener(this);
        gameForm.addKeyListener(controlListener);
        serverProvider = new ServerProvider();
        informationProvider = new InformationProvider();
        initImages();
        if (serverProvider.connectToServer(host, this)) {
            if (logReg instanceof Register) {
                Register register = (Register) logReg;
                serverProvider.sendTCP(register);
            } else if (logReg instanceof Login) {
                Login login = (Login) logReg;
                serverProvider.sendTCP(login);
            }
        } else {
            gameForm.showError("Can't connect to this host");
        }
    }

    private void initImages() {
        try {
            URL url = this.getClass().getClassLoader().getResource("shotgun.png");
            shotgun = ImageIO.read(url);
            url = this.getClass().getClassLoader().getResource("rifle.png");
            rifle = ImageIO.read(url);
            url = this.getClass().getClassLoader().getResource("handgun.png");
            handgun = ImageIO.read(url);
            spriteHeight = InformationProvider.LOCATION_POINT_SCALE;
            double scale = (double) shotgun.getHeight() / (double) spriteHeight;
            spriteWidth = (int) ((double) shotgun.getWidth() / scale);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (playing) {
            if (System.currentTimeMillis() - lastTime > delta) {
                lastTime = System.currentTimeMillis();
                tick();
                render();
            }
        }
    }


    private void render() {
        Character myCharacter = informationProvider.getMyCharacter();
        if (myCharacter != null) {
            Character[] characters = informationProvider.getCharacterArray();
            Bullet[] bullets = informationProvider.getBulletsArray();
            Graphics2D g = (Graphics2D) gameTable.getGraphics();
            g.setColor(Color.BLACK);
            g.drawImage(informationProvider.getLocation(), 0, 0, null);
            String bulletsCount = "";
            for (Character k :
                    characters) {
                g.rotate(Math.toRadians(k.deg), k.x, k.y);
                switch (k.arsenal.weapon) {
                    case handgun:
                        g.drawImage(handgun, k.x - spriteWidth / 2, k.y - spriteHeight / 2, spriteWidth, spriteHeight, null);
                        break;
                    case rifle:
                        g.drawImage(rifle, k.x - spriteWidth / 2, k.y - spriteHeight / 2, spriteWidth, spriteHeight, null);
                        break;
                    case shotgun:
                        g.drawImage(shotgun, k.x - spriteWidth / 2, k.y - spriteHeight / 2, spriteWidth, spriteHeight, null);
                        break;
                }
                g.rotate(Math.toRadians(-k.deg), k.x, k.y);
            }
            for (Bullet k :
                    bullets) {
                g.fillOval(k.x, k.y, 4, 4);
            }
            g.dispose();
            switch (myCharacter.arsenal.weapon) {
                case rifle:
                    bulletsCount = String.valueOf(myCharacter.arsenal.rifleBulletCount);
                    break;
                case shotgun:
                    bulletsCount = String.valueOf(myCharacter.arsenal.shotgunBulletCount);
                    break;
                case handgun:
                    bulletsCount = "∞";
                    break;
            }
            gameForm.drawGameTable(gameTable, myCharacter.x, myCharacter.y);
            gameForm.drawMiniMap(informationProvider.getLocation());
            gameForm.setCharacterInformation(String.valueOf(myCharacter.health), myCharacter.arsenal.weapon.toString(), bulletsCount);
        }
    }

    private void tick() {
        int x = 0;
        int y = 0;
        if (controlListener.up)
            y--;
        if (controlListener.down)
            y++;
        if (controlListener.left)
            x--;
        if (controlListener.right)
            x++;
        MoveCharacter moveCharacter = new MoveCharacter();
        moveCharacter.x = x;
        moveCharacter.y = y;
        moveCharacter.deg = rotationListener.getRotation();
        moveCharacter.weapon = controlListener.weapon;
        serverProvider.sendTCP(moveCharacter);
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof game_lib.entities.from_server.GameLocation) {
            GameLocation location = (GameLocation) object;
            informationProvider.setLocation(location.location);
        } else if (object instanceof game_lib.entities.from_server.AuthorizationSuccess) {
            AuthorizationSuccess success = (AuthorizationSuccess) object;
            myLogin = success.login;
            informationProvider.setMyName(myLogin);
            playing = true;
            new Thread(this).start();
        } else if (object instanceof game_lib.entities.from_server.ErrorMessage) {
            ErrorMessage message = (ErrorMessage) object;
            playing = false;
            gameForm.showError(message.errorString);
        } else if (object instanceof game_lib.entities.from_server.AddCharacter) {
            AddCharacter addCharacter = (AddCharacter) object;
            informationProvider.addCharacter(addCharacter.character);
        } else if (object instanceof game_lib.entities.from_server.UpdateCharacter) {
            UpdateCharacter updateCharacter = (UpdateCharacter) object;
            informationProvider.addCharacter(updateCharacter.character);
        } else if (object instanceof game_lib.entities.from_server.Bullet) {
            Bullet bullet = (Bullet) object;
            informationProvider.addBullet(bullet);
        } else if (object instanceof game_lib.entities.from_server.DeleteBullet) {
            DeleteBullet deleteBullet = (DeleteBullet) object;
            informationProvider.deleteBullet(deleteBullet.bulletId);
        } else if (object instanceof game_lib.entities.from_server.RemoveCharacter) {
            RemoveCharacter removeCharacter = (RemoveCharacter) object;
            informationProvider.removeCharacter(removeCharacter.login);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        playing = false;
        gameForm.showInformaion("Вы погибли");
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        Shot shot = new Shot();
        serverProvider.sendTCP(shot);
    }

    public void mousePressed(MouseEvent mouseEvent) {

    }

    public void mouseReleased(MouseEvent mouseEvent) {

    }

    public void mouseEntered(MouseEvent mouseEvent) {

    }

    public void mouseExited(MouseEvent mouseEvent) {

    }
}
