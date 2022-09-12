package api;

import java.util.HashMap;

public class NodeDataAlgo implements NodeData , Comparable<NodeData>{

    private  double weight;
    private String info;
    private int tag,key;
    private GeoLocation location;
    private HashMap<Integer, EdgeData> outEdges;
    private HashMap<Integer, EdgeData> inEdges;


    public NodeDataAlgo(int key,double x, double y){
        this.key = key;
        this.location = new GeoLocationAlgo(x,y);
        this.weight = 0;
        this.info = "";
        this.tag = 0;
    }

    public NodeDataAlgo(int key) {
        this.key = key;
        this.tag = 0;
        this.info = "";
        this.weight=0;
        this.location = new GeoLocationAlgo();
        this.inEdges = new HashMap<Integer, EdgeData>();
        this.outEdges=new HashMap<Integer, EdgeData>();
    }
    public NodeDataAlgo(NodeData node){
        this.key =node.getKey();
        this.tag =node.getTag();
        this.weight =node.getWeight();
        this.info =new String(node.getInfo());
        this.location=new GeoLocationAlgo(node.getLocation());


    }

    public NodeDataAlgo(GeoLocation pos, int id){
        this.key = id;
        this.location = new GeoLocationAlgo(pos);
        this.weight = 0;      //default
        this.info = "";      //default
        this.tag = 0;       //default
        this.inEdges = new HashMap<Integer, EdgeData>();
        this.outEdges=new HashMap<Integer, EdgeData>();
    }

    public NodeDataAlgo(int key, GeoLocation location){
        this.key = key;
        this.location = new GeoLocationAlgo(location);
    }


    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public GeoLocation getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(GeoLocation p) { this.location = p; }

    @Override
    public double getWeight() { return this.weight; }

    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }


    @Override
    public int compareTo(NodeData o) {
        if(this.getWeight()-o.getWeight()>0) return 1;
        else if(this.getWeight()-o.getWeight()<0) return -1;
        return 0;
    }
}

