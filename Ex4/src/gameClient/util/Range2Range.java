package gameClient.util;

import api.GeoLocation;

/**
 * This class represents a simple world 2 frame conversion (both ways).
 *
 */

public class Range2Range {
    private gameClient.util.Range2D _world, _frame;

    public Range2Range(gameClient.util.Range2D w, gameClient.util.Range2D f) {
        _world = new gameClient.util.Range2D(w);
        _frame = new gameClient.util.Range2D(f);
    }
    public GeoLocation world2frame(GeoLocation p) {
        Point3D d = _world.getPortion(p);
        Point3D ans = _frame.fromPortion(d);
        return ans;
    }
    public GeoLocation frame2world(GeoLocation p) {
        Point3D d = _frame.getPortion(p);
        Point3D ans = _world.fromPortion(d);
        return ans;
    }
    public gameClient.util.Range2D getWorld() {
        return _world;
    }
    public gameClient.util.Range2D getFrame() {
        return _frame;
    }
}