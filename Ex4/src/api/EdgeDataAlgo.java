package api;

public class EdgeDataAlgo implements EdgeData{

    private int src,dest,tag;
    private  String info;
    private double weight;

    public EdgeDataAlgo() {
        this.src = 0;
        this.dest = 0;
        this.weight = 0;
        this.tag = 0;
        this.info = null;
    }

    public EdgeDataAlgo(int src, int dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.tag = 0;
        this.info = null;
    }

    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dest;
    }

    @Override
    public double getWeight() {
        return this.weight;
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
}
