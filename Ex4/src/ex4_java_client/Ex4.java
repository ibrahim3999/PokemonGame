package ex4_java_client;

import api.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gameClient.Arena;
import gameClient.CL_Agent;
import gameClient.CL_Pokemon;
import gameClient.GFrame;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ex4 implements Runnable{
    private static GFrame _win;
    private static Arena _ar;
    private static long dt = 100;
    private static int id;

    @Override
    public void run() {

        Client client = new Client();
        try {
            client.startConnection("127.0.0.1",6666);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        String graphStr = client.getGraph();
        DirectedWeightedGraph graph = loadGraph(graphStr);
        //System.out.println(graphStr);

        client.addAgent("{\"id\":0}");
        String agentsStr = client.getAgents();
        //System.out.println(agentsStr);

        String pokemonsStr = client.getPokemons();
        //System.out.println(pokemonsStr);

        String isRunningStr = client.isRunning();
        //System.out.println(isRunningStr);

        //init(game,graph);
        init(client,graph);
        _win.setTitle("Ex4 - OOP: (NONE trivial Solution) " );
        int ind = 0;
        client.start();
         */
        String graphStr = client.getGraph();
        DirectedWeightedGraph graph = loadGraph(graphStr);
        String agentsStr = client.getAgents();
        String pokemonsStr = client.getPokemons();


        init(client,graph);
        client.start();
        _win.setTitle("Ex4 - OOP: (NONE trivial Solution) " );
        int ind = 0;


        while (client.isRunning().equals("true")) {
            moveAgants(client,graph);
            //moveAgants(game, graph);
            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                }
                Thread.sleep(dt);
                ind++;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = client.getInfo();
        //String res = game.toString();
        System.out.println(res);
        System.exit(0);
        }

    private static void moveAgants(Client game, DirectedWeightedGraph gg) {
        game.move();
        String str = "";

        String lg = game.getAgents();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
       // List<CL_Agent> log = Arena.json2Agents(lg);
        _ar.setAgents(log);

        String fs = game.getPokemons();
        List<CL_Pokemon> poks = Arena.json2Pokemons(fs);
        _ar.setPokemons(poks);

        for (int i = 0; i < poks.size(); i++) Arena.updateEdge(poks.get(i), gg);
        for (int i = 0; i < log.size(); i++) {
            CL_Agent ag = log.get(i);

            int id = ag.getID();
            int dest = ag.getNextNode();
            double v = ag.getValue();
            if (dest == -1) {
                int index = ag.getIndex();
                if (ag.hasTarget()) {
                    if (index < ag.getList().size()) {
                        if (index == ag.getList().size() - 1){
                            //str="{\"agent_id\":" + id + ", " + "\"next_node_id\": " + index + "}";
                            //game.chooseNextEdge(id, index);
                            game.chooseNextEdge("{\"agent_id\":" + id + ", " + "\"next_node_id\": " + index + "}");
                             }

                        System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + index);
                        index++;
                        ag.setIndex(index);
                    } else {
                        path(ag, gg, poks);//new shortest path }
                        ag.setIndex(1);
                        //str="{\"agent_id\":" + id + ", " + "\"next_node_id\": " + index + "}";
                        game.chooseNextEdge("{\"agent_id\":" + id + ", " + "\"next_node_id\": " + index + "}");
                        //game.chooseNextEdge(id, index);
                        System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + index);
                        index++;
                        ag.setIndex(index);
                    }
                } else {
                    path(ag, gg, poks);//new shortest path }
                    ag.setIndex(1);
                    int temp = ag.getList().get(index).getKey();
                    //str="{\"agent_id\":" + id + ", " + "\"next_node_id\": " + index + "}";
                    //game.chooseNextEdge(str);
                    game.chooseNextEdge("{\"agent_id\":" + id + ", " + "\"next_node_id\": " + temp + "}");
                    //game.chooseNextEdge(id, temp);
                    System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + temp);
                    index++;
                    ag.setIndex(index);
                }

            }
        }
    }

    private static void path(CL_Agent ca, DirectedWeightedGraph gg, List<CL_Pokemon> poks) {
        DirectedWeightedGraphAlgorithms ga = new DirectedWeightedGraphAlgorithmsAlgo();
        ga.init(gg);

        List<NodeData> p = new ArrayList<>();
        List<NodeData> min = new ArrayList<>();
        CL_Pokemon curPk = null;
        int i = 0;
        for (int j = 0; j < poks.size(); j++) {
            CL_Pokemon pk = poks.get(j);
            if (pk.isClear()) {
                p = ga.shortestPath(ca.getSrcNode(), pk.get_edge().getSrc());
                NodeData node = gg.getNode(pk.get_edge().getDest());
                p.add(node);
                if (i == 0) {
                    min = p;
                    curPk = pk;
                }
                if (p.size() < min.size()) {
                    min = p;
                    curPk = pk;
                }
                i++;
            }

        }
        ca.setList(min);
        if (curPk != null) curPk.setClear(false);
        ca.setTarget(true);
    }

    private void init(Client game, DirectedWeightedGraph graph) {
        String pokemons = game.getPokemons();

        _ar = new Arena();
        _ar.setGraph(graph);
        //_ar.setPokemons(Arena.json2Pokemons(pokemons));
        _ar.setPokemons(Arena.json2Pokemons(pokemons));
        String info = game.getInfo();

        //id = Integer.parseInt(args[0]);
        //scenario_num = _log.getScenario();

        _win = new GFrame("test Ex4", 11, game, graph);
        _win.setSize(1000, 700);
        ImageIcon m = new ImageIcon("pics/icon.jpg");
        _win.setIconImage(m.getImage());

        _win.update(_ar);
        _win.show();
        _win.setVisible(true);


        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject gs = line.getJSONObject("GameServer");
            int rs = gs.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            for (int a = 0; a < cl_fs.size(); a++) {
                Arena.updateEdge(cl_fs.get(a), graph);
            }
            int temp = 0;

            //check if have 2 pokemons on the same edge
            for (int i = 0; i < cl_fs.size() - 1; i++) {
                CL_Pokemon pk1 = cl_fs.get(i);
                for (int j = i + 1; j < cl_fs.size(); j++) {
                    CL_Pokemon pk2 = cl_fs.get(j);
                    if (pk1.get_edge().getSrc() == pk2.get_edge().getSrc() || pk1.get_edge().getSrc() == pk2.get_edge().getDest()) {
                        temp = pk1.get_edge().getSrc();
                    }
                }
            }
            for (int a = 0; a < rs; a++) {
                int ind = a % cl_fs.size();
                CL_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if (c.getType() < 0) {
                    nn = c.get_edge().getSrc();
                }
                if (c.getType() > 0) {
                    nn = c.get_edge().getDest();
                }
                if (temp != 0) {
                    nn = temp;
                }
                String str = "{\"id\":" + nn + "}";
                game.addAgent(str);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public DirectedWeightedGraph loadGraph(String g) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DirectedWeightedGraphAlgo.class, new GraphJsonDeserializer());
        Gson gson = gsonBuilder.create();
        DirectedWeightedGraph ds = gson.fromJson(g, DirectedWeightedGraphAlgo.class);
        return ds;
    }




    public static void main(String[] args) {
        Thread t = new Thread(new Ex4());

        t.start();

    }

}
