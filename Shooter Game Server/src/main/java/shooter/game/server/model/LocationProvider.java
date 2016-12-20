package shooter.game.server.model;

import shooter.game.server.model.map_building.LabBuilder;

/**
 * Created by Elvira on 18.12.16.
 */
public class LocationProvider {
    public static final int CHARACTER_COLLISION_RADIUS = LocationProvider.LOCATION_POINT_SCALE / 2;
    public static final int ROOM_SCALE = 5;
    public static final int MAP_LENGTH = 7;
    public static final int LOCATION_POINT_SCALE = 30;
    public static final int MAP_WIDTH = (ROOM_SCALE * MAP_LENGTH + 1)*LOCATION_POINT_SCALE;
    public static final int MAP_HEIGHT = (ROOM_SCALE * MAP_LENGTH + 1)*LOCATION_POINT_SCALE;
    private LabBuilder labBuilder;
    private boolean[][] lastLocation;

    public void buildNewRandomLocation(){
        labBuilder = new LabBuilder(MAP_LENGTH, MAP_LENGTH);
        labBuilder.build();
        lastLocation = labBuilder.getAsLocation(ROOM_SCALE);
    }

    public boolean[][] getLastLocation(){
        return lastLocation;
    }
}
