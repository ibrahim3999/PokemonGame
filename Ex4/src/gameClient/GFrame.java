package gameClient;

import api.*;
import ex4_java_client.Client;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Iterator;
import java.util.List;

public class GFrame extends JFrame {

    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    private Image graphImg;
    private int Sl;
    private Client _game;
    private DirectedWeightedGraph _graph;
    private DirectedWeightedGraphAlgorithms _graphAlgo;

    public GFrame(String a, int level, Client game, DirectedWeightedGraphAlgorithms g){
        super(a);
        this.Sl = level;
        _game = game;
        _graphAlgo = g;
        _graph = g.getGraph();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public GFrame(String a, int level, Client game, DirectedWeightedGraph graph){
        super(a);
        this.Sl = level;
        _game = game;
        _graph = graph;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }



    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
        setVisible(true);
    }

    private void updateFrame() {
        Range rx = new Range(20, this.getWidth() - 20);
        Range ry = new Range(this.getHeight() - 10, 150);
        Range2D frame = new Range2D(rx, ry);
        DirectedWeightedGraph g = _ar.getGraph();
        _w2f = Arena.w2f(g, frame);
    }

    public void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        graphImg = createImage(w, h);
        Graphics graphics = graphImg.getGraphics();
        paintComponents(graphics);
        g.drawImage(graphImg, 0, -20, this);
        updateFrame();
    }

    @Override
    public void paintComponents(Graphics g) {
        Graphics2D gd = (Graphics2D) g;
        gd.setStroke(new BasicStroke(2f));
        Toolkit t1 = Toolkit.getDefaultToolkit();
        Image im1;
        im1= t1.getImage("pics/sea.jpg");
        g.drawImage(im1, 0, 0, getWidth(), getHeight(), this);
        drawPokemons(g);
        drawGraph(g);
        drawAgants(g);
        drawInfo(g);
        drawLevel(g);
        drawTime(g);
        drawResults(g);
    }

    public void drawLevel(Graphics g){
        /*
        Font graphFont = new Font("Level",Font.BOLD,30);
        g.setFont(graphFont);
        g.drawString("Level :" + Sl,this.getWidth()/35,90);

         */
    }

    private void drawResults(Graphics g) {
        Font graphFontG = new Font("Level",Font.BOLD,27);
        Font graphFontA = new Font("Level",Font.BOLD,15);
        g.setFont(graphFontA);
        double agentsValue = 0 ;
        String lg =_game.getAgents();
        List<CL_Agent> log = Arena.getAgents(lg,_graph);
        int changeY = 105;
        for(int i = 0; i < log.size() ; i ++) {
            CL_Agent ag = log.get(i);
            double agv = ag.getValue();
            agentsValue+= agv;
            g.drawString("agent " + i + " : " + agv,  this.getWidth()/35, changeY);
            //g.drawString("agent " + i + " : " + agv,  this.getWidth()/1 -110, changeY);
            changeY += 20;
        }
        g.setFont(graphFontG);
        g.drawString("GameResult : " +agentsValue, this.getWidth()/35 , 85);
       // g.drawString("GameResult : " +agentsValue, this.getWidth()/1 -280 , 85);
    }
    private void drawTime(Graphics g){
       // int sec = (int) (_game.timeToEnd()/1000);
       // int min = (int) (_game.timeToEnd()/60000);
        int sec = (int) (Integer.parseInt(_game.timeToEnd())/1000);
        int min = (int) (Integer.parseInt(_game.timeToEnd())/60000);
        g.drawString("Time :" + min+":"+sec,this.getWidth()/2 - 40,90);
    }

    private void drawInfo(Graphics g) {
        List<String> str = _ar.get_info();
        String dt = "none";
        for (int i = 0; i < str.size(); i++) {
            g.drawString(str.get(i) + " dt: " + dt, 100, 60 + i * 20);
        }

    }

    private void drawGraph(Graphics g) {
        DirectedWeightedGraph gg = _ar.getGraph();
        Iterator<NodeData> iter = gg.getV().iterator();
        while (iter.hasNext()) {
            NodeData n = iter.next();
            g.setColor(Color.blue);
            drawNode(n, 5, g);
            Iterator<EdgeData> itr = gg.getE(n.getKey()).iterator();
            while (itr.hasNext()) {
                EdgeData e = itr.next();
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }

    private void drawPokemons(Graphics g) {
        Toolkit t = Toolkit.getDefaultToolkit();
        Image im;
        List<CL_Pokemon> fs = _ar.getPokemons();
        if (fs != null) {
            Iterator<CL_Pokemon> itr = fs.iterator();

            while (itr.hasNext()) {
                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r = 10;
                //g.setColor(Color.green);
                im = t.getImage("pics/charizard.png");
               //g.drawImage(im,(int)(x-10),(int)(y-10),40,40,this);
                if (f.getType() < 0) {
                    //g.setColor(Color.orange);
                    im = t.getImage("pics/bulbasour.png");

                }
                if (c != null) {
                    GeoLocation fp = this._w2f.world2frame(c);
                    //g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                    //im = t.getImage("pics/bulbasour.png");
                    g.drawImage(im,(int)(fp.x() - r),(int)(fp.y()-r),4 * r,4 * r,this);
                    //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);

                }
            }
        }
    }

    private void drawAgants(Graphics g) {
        Toolkit t = Toolkit.getDefaultToolkit();
        Image im;
        List<CL_Agent> rs = _ar.getAgents();
        //	Iterator<OOP_Point3D> itr = rs.iterator();
        g.setColor(Color.red);
        im = t.getImage("pics/pokeball.png");
        int i = 0;
        while (rs != null && i < rs.size()) {
            GeoLocation c = rs.get(i).getLocation();
            int r = 8;
            i++;
            if (c != null) {
                GeoLocation fp = this._w2f.world2frame(c);
                //g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                g.drawImage(im,(int)(fp.x() - r),(int)(fp.y()-r),2*r,2*r,this);
            }
        }
    }

    private void drawNode(NodeData n, int r, Graphics g) {
        GeoLocation pos = n.getLocation();
        GeoLocation fp = this._w2f.world2frame(pos);
        g.setColor(new Color(225, 100, 0));
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.setFont(new Font("",Font.BOLD,20));
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }

    private void drawEdge(EdgeData e, Graphics g) {
        DirectedWeightedGraph gg = _ar.getGraph();
        GeoLocation s = gg.getNode(e.getSrc()).getLocation();
        GeoLocation d = gg.getNode(e.getDest()).getLocation();
        GeoLocation s0 = this._w2f.world2frame(s);
        GeoLocation d0 = this._w2f.world2frame(d);
        //g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
        g.setColor(Color.BLACK);
        drawArrowLine(g,(int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y(),15,5);
        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }

    private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D , cos = dx / D ;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);
    }

}