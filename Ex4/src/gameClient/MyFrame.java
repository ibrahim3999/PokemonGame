package gameClient;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import ex4_java_client.Client;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;


/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 *
 */
public class MyFrame extends JFrame {
    private Client c = new Client();
    private int _ind;
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    private Graphics graph = getGraphics();

    MyFrame(String a) {
        super(a);
        int _ind = 0;
    }
    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
    }

    private void updateFrame() {
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        DirectedWeightedGraph g = _ar.getGraph();
        _w2f = Arena.w2f(g,frame);
    }

    public void paint(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();

        Graphics graph = getGraphics();
        paintComponents(graph);
        g = graph;
        drawAgants(g);
        drawPokemons(g);
        drawInfo(g);
        drawGraph(g);

        graph.clearRect(0, 0, w, h);
    }

    public void paintComponents(Graphics g){
        int w = this.getWidth();
        int h = this.getHeight();
        g.clearRect(0, 0, w, h);
        drawAgants(g);
        drawPokemons(g);
        drawInfo(g);
        drawGraph(g);
    }

    private void drawInfo(Graphics g) {
        List<String> str = _ar.get_info();
        String dt = "none";
        for(int i=0;i<str.size();i++) {
            g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
        }

    }
    private void drawGraph(Graphics g) {
        Graphics graph = getGraphics();
        DirectedWeightedGraph gg = _ar.getGraph();
        Iterator<NodeData> iter = gg.getV().iterator();
        while(iter.hasNext()) {
            NodeData n = iter.next();
            g.setColor(Color.blue);
            drawNode(n,5,g);
            Iterator<EdgeData> itr = gg.getE(n.getKey()).iterator();
            while(itr.hasNext()) {
                EdgeData e = itr.next();
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }

    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = _ar.getPokemons();
        if(fs!=null) {
            Iterator<CL_Pokemon> itr = fs.iterator();

            while(itr.hasNext()) {

                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r=10;
                g.setColor(Color.green);
                if(f.getType()<0) {g.setColor(Color.orange);}
                if(c!=null) {

                    GeoLocation fp = this._w2f.world2frame(c);
                    g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
                    //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);

                }
            }
        }
    }

    private void drawAgants(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        //	Iterator<OOP_Point3D> itr = rs.iterator();
        g.setColor(Color.red);
        int i=0;
        while(rs!=null && i<rs.size()) {
            GeoLocation c = rs.get(i).getLocation();
            int r=8;
            i++;
            if(c!=null) {

                GeoLocation fp = this._w2f.world2frame(c);
                g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
            }
        }
    }
    private void drawNode(NodeData n, int r, Graphics g) {
        GeoLocation pos = n.getLocation();
        GeoLocation fp = this._w2f.world2frame(pos);
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
    }
    private void drawEdge(EdgeData e, Graphics g) {
        DirectedWeightedGraph gg = _ar.getGraph();
        GeoLocation s = gg.getNode(e.getSrc()).getLocation();
        GeoLocation d = gg.getNode(e.getDest()).getLocation();
        GeoLocation s0 = this._w2f.world2frame(s);
        GeoLocation d0 = this._w2f.world2frame(d);
        g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }
}