package api;

public class GeoLocationAlgo implements GeoLocation{

    private double x;
    private double y;
    private double z;


    public GeoLocationAlgo(){
        this.x=0;
        this.y=0;
        this.z=0;
    }

    public GeoLocationAlgo(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public GeoLocationAlgo(double x, double y){
        this.x=x;
        this.y=y;
        this.z=0;
    }

    public GeoLocationAlgo(String pos){

        int j=0;
        int index=0;
        for (int i=0 ; i<pos.length() ; i++){
            if(i==pos.length()-1){
                String temp = pos.substring(index , i);
                this.z=Double.parseDouble(temp);
                break;
            }
            if(pos.charAt(i)==','){
                String temp = pos.substring(index , i);
                index=i+1;
                if(j==0) {this.x = Double.parseDouble(temp); j++;}
                else {this.y = Double.parseDouble(temp);}
            }
        }
    }

    public GeoLocationAlgo(GeoLocation g){
        this(g.x(),g.y(),g.z());
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(GeoLocation g) {
        return Math.sqrt(Math.pow(this.x - g.x(), 2) + Math.pow(this.y - g.y(),2) + Math.pow(this.z - g.z(),2));
    }
}
