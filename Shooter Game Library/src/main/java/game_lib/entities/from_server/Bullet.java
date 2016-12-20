package game_lib.entities.from_server;

/**
 * Created by Elvira on 19.12.16.
 */
public class Bullet {
    public int id;
    public int x;
    public int y;

    @Override
    public boolean equals(Object o) {
        if(o instanceof Bullet){
            return this.id == ((Bullet) o).id;
        }
        return false;
    }
}
