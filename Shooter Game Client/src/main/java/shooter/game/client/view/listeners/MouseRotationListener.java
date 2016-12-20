package shooter.game.client.view.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Created by Elvira on 19.12.16.
 */
public class MouseRotationListener implements MouseMotionListener {
    private int centerX;
    private int centerY;
    private double rotation = 0;

    public MouseRotationListener(int screenWidth, int screenHeight) {
        centerX = screenWidth / 2;
        centerY = screenHeight / 2;
    }

    public void mouseDragged(MouseEvent mouseEvent) {

    }

    public void mouseMoved(MouseEvent mouseEvent) {
        double x = mouseEvent.getX() - centerX;
        double y = mouseEvent.getY() - centerY;
        double a = Math.sqrt(x * x + y * y);
        double deg = Math.toDegrees(Math.acos(x/a));
        if(mouseEvent.getY() < centerY){
            deg = 360 - deg;
        }
        rotation = deg;
    }

    public double getRotation(){
        return rotation;
    }
}
