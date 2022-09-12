package gameClient;
import api.EdgeData;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class CL_Pokemon {
    private EdgeData _edge;
    private double _value;
    private int _type;
    private Point3D _pos;
    private double min_dist;
    private int min_ro;
    private boolean isClear;

    public CL_Pokemon(Point3D p, int t, double v, double s, EdgeData e) {
        _type = t;
        //	_speed = s;
        _value = v;
        set_edge(e);
        _pos = p;
        min_dist = -1;
        min_ro = -1;
        isClear=true;
    }

    public static CL_Pokemon init_from_json(String json) {
        CL_Pokemon ans = null;
        try {
            JSONObject p = new JSONObject(json);
            int id = p.getInt("id");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return ans;
    }
    public String toString() {return "F:{v="+_value+", t="+_type+"}";}
    public EdgeData get_edge() {
        return _edge;
    }

    public void set_edge(EdgeData _edge) {
        this._edge = _edge;
    }

    public Point3D getLocation() {
        return _pos;
    }
    public int getType() {return _type;}
    //	public double getSpeed() {return _speed;}
    public double getValue() {return _value;}

    public double getMin_dist() {
        return min_dist;
    }

    public void setMin_dist(double mid_dist) {
        this.min_dist = mid_dist;
    }

    public int getMin_ro() {
        return min_ro;
    }

    public void setMin_ro(int min_ro) {
        this.min_ro = min_ro;
    }

    public boolean isClear() {
        return isClear;
    }

    public void setClear(boolean clear) {
        isClear = clear;
    }
}
