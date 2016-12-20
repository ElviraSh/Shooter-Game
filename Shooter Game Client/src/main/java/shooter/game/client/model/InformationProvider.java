package shooter.game.client.model;

import game_lib.entities.Character;
import game_lib.entities.from_server.Bullet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Elvira on 19.12.16.
 */
public class InformationProvider {
    public static final int ROOM_SCALE = 5;
    public static final int MAP_LENGTH = 7;
    public static final int LOCATION_POINT_SCALE = 30;
    public static final int MAP_WIDTH = (ROOM_SCALE * MAP_LENGTH + 1)*LOCATION_POINT_SCALE;
    public static final int MAP_HEIGHT = (ROOM_SCALE * MAP_LENGTH + 1)*LOCATION_POINT_SCALE;

    private Map<String, Character> characterMap;
    private Map<Integer, Bullet> bulletMap;
    private String myName;
    private BufferedImage location;

    public String getMyName() {
        return myName;
    }

    public InformationProvider() {
        characterMap = Collections.synchronizedMap(new HashMap<String, Character>());
        bulletMap = Collections.synchronizedMap(new HashMap<Integer, Bullet>());
    }

    public void addCharacter(Character character){
        characterMap.put(character.login, character);
    }

    public void setMyName(String myName){
        this.myName = myName;
    }

    public Character getMyCharacter(){
        if (myName != null) {
            return characterMap.get(myName);
        }
        return null;
    }

    public Character[] getCharacterArray(){
        return characterMap.values().toArray(new Character[characterMap.size()]);
    }

    public void removeCharacter(String name){
        characterMap.remove(name);
    }

    public void addBullet(Bullet bullet){
        bulletMap.put(bullet.id, bullet);
    }

    public void deleteBullet(int bulletID){
        bulletMap.remove(bulletID);
    }

    public Bullet[] getBulletsArray(){
        return bulletMap.values().toArray(new Bullet[bulletMap.size()]);
    }

    public void setLocation(boolean[][] location){
        BufferedImage bufferedImage = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < location[0].length; y++) {
            for (int x = 0; x < location.length; x++) {
                drawLocationPoint(bufferedImage, y, x, location[x][y]);
            }
        }
        this.location = bufferedImage;
    }

    private void drawLocationPoint(BufferedImage bufferedImage, int y, int x, boolean wall) {
        int pointX = x * LOCATION_POINT_SCALE;
        int pointY = y * LOCATION_POINT_SCALE;
        if(wall) {
            for (int i = 0; i < LOCATION_POINT_SCALE; i++) {
                for (int j = 0; j < LOCATION_POINT_SCALE; j++) {
                    bufferedImage.setRGB(j + pointX, i + pointY, Color.BLACK.getRGB());
                }
            }
        } else {
            for (int i = 0; i < LOCATION_POINT_SCALE; i++) {
                for (int j = 0; j < LOCATION_POINT_SCALE; j++) {
                    if(i >= LOCATION_POINT_SCALE - 1 || j >= LOCATION_POINT_SCALE - 1){
                        bufferedImage.setRGB(j + pointX, i + pointY, Color.GRAY.getRGB());
                    } else {
                        bufferedImage.setRGB(j + pointX, i + pointY, Color.WHITE.getRGB());
                    }

                }
            }
        }
    }


    public BufferedImage getLocation() {
        return location;
    }
}
