package game_lib.entities;

/**
 * Created by Elvira on 18.12.16.
 */
public class Arsenal {
    public int rifleBulletCount;
    public int shotgunBulletCount;
    public Weapon weapon;
    public enum Weapon {
        handgun,
        rifle,
        shotgun
    }
    public static final int handgunDamage = 5;
    public static final int rifleDamage = 15;
    public static final int shotgunDamage = 45;
}
