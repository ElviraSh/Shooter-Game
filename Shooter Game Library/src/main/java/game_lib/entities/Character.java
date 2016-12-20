package game_lib.entities;

/**
 * Created by Elvira on 18.12.16.
 */
public class Character {
    public String login;
    public int x;
    public int y;
    public double deg;
    public int health;
    public Arsenal arsenal;
    public int killsCount;
    public int deathCount;

    public Character() {
        deg = 0;
        health = 100;
        arsenal = new Arsenal();
        arsenal.weapon = Arsenal.Weapon.handgun;
        arsenal.rifleBulletCount = 30;
        arsenal.shotgunBulletCount = 10;
        killsCount = 0;
        deathCount = 0;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Character){
            return login.equals(((Character) o).login);
        }
        return false;
    }
}
