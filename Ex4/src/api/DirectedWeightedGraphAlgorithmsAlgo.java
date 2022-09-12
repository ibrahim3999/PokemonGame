package api;
import com.google.gson.*;

import java.io.*;
import java.util.*;

public class DirectedWeightedGraphAlgorithmsAlgo implements DirectedWeightedGraphAlgorithms{

    private DirectedWeightedGraph graph;
    private HashMap<Integer, NodeData> parent = new HashMap<>();

    @Override
    public void init(DirectedWeightedGraph g) { this.graph = g; }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.graph;
    }

    @Override
    public DirectedWeightedGraph copy() {
        if (this.graph == null) return null;

        DirectedWeightedGraphAlgo copyGraph = new DirectedWeightedGraphAlgo();
        for (Iterator<NodeData> it = graph.nodeIter(); it.hasNext(); ) {
            NodeData node = it.next();
            NodeData temp = new NodeDataAlgo(node.getKey());
            temp.setWeight(node.getWeight());
            temp.setInfo(node.getInfo());
            temp.setTag(node.getTag());
            temp.setLocation(node.getLocation());
            copyGraph.addNode(temp);
        }
        //connect all edges
        for (Iterator<NodeData> it = graph.nodeIter(); it.hasNext(); ) {
            NodeData node = it.next();
            if (graph.edgeIter(node.getKey()) != null)
            {
                for (Iterator<EdgeData> iter = graph.edgeIter(node.getKey()); iter.hasNext(); ) {
                    EdgeData edge = iter.next();
                    copyGraph.connect(edge.getSrc(), edge.getDest(), edge.getWeight());
                    copyGraph.getEdge(edge.getSrc(), edge.getDest()).setTag(this.graph.getEdge(edge.getSrc(), edge.getDest()).getTag());
                    copyGraph.getEdge(edge.getSrc(), edge.getDest()).setInfo(this.graph.getEdge(edge.getSrc(), edge.getDest()).getInfo());
                }
            }
        }
        return copyGraph;
    }

    @Override
    public boolean isConnected() {
        if (this.graph != null && this.graph.nodeSize() == 0)
            return true;// if there is no nodes in the graph then is connected
        if (this.graph != null && this.graph.nodeSize() == 1)
            return true;// If there is one node in the graph then is connected
        if (this.graph == null) return true;

        Collection<NodeData> c = this.graph.getV();
        Iterator<NodeData> j = c.iterator();
        NodeData n1 = j.next();
        Iterator<NodeData> myNodes = c.iterator();
        while (myNodes.hasNext()) {
            NodeData node = myNodes.next();
            if (this.graph.getE(node.getKey()) == null || this.graph.getE(node.getKey()).size() == 0)
                //  if (this.graph.edgeIter(node.getKey()) == null || Iterators.size(this.graph.edgeIter(node.getKey())) == 0)
                return false;
        }
        BFS((DirectedWeightedGraphAlgo) this.graph, this.graph.getNode(n1.getKey()));
        Iterator<NodeData> i = c.iterator();
        while (i.hasNext()) {
            NodeData node = i.next();
            if (node != null)
                //if one of the nodes info is not black , the bfs didn't reach the node
                if (!node.getInfo().equals("black")) return false;
        }
        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        if (src == dest) return 0;
        List<NodeData> list = shortestPath(src, dest);
        if (list == null) return -1;
        if (list.get(list.size() - 1).getWeight() == Double.POSITIVE_INFINITY) return -1;
        return list.get(list.size() - 1).getWeight();
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        if (graph.getNode(src) == null || graph.getNode(dest) == null) return null;
        List<NodeData> list = new ArrayList<>();
        if (src == dest && graph.getNode(src) != null) {
            list.add(graph.getNode(src));
            return list;
        }
        Dijkstra(src);
        NodeData vertex = graph.getNode(dest);
        list.add(vertex);
        int i = 1;
        while (vertex != graph.getNode(src) && vertex != null) {

            vertex = parent.get(vertex.getKey());
            list.add(vertex);
            i++;
            if (vertex != null && vertex.getKey() == src) break;
            if (i > parent.size()) return null;
        }
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j).getWeight() == Double.POSITIVE_INFINITY) return null;
        }

        Collections.reverse(list);
        return list;
    }


    @Override
    public NodeData center() {

        if (!this.isConnected())
            return null;
        NodeData ans = null;
        double minN = Double.MAX_VALUE;
        Iterator<NodeData> itr_node1 = graph.nodeIter();
        while (itr_node1.hasNext()) {
            Iterator<NodeData> itr_node2 = graph.nodeIter();
            NodeData node1 = itr_node1.next();
            double temp = 0;
            while (itr_node2.hasNext()) {
                NodeData node2 = itr_node2.next();
                if (temp > minN) {
                    break;
                }
                if (temp < this.shortestPathDist(node1.getKey(), node2.getKey()))
                    temp = this.shortestPathDist(node1.getKey(), node2.getKey());
            }
            if (minN > temp) {
                minN = temp;
                ans = node1;
            }

        }
        return ans;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {

        List<NodeData>[] temp = new LinkedList[cities.size()];
        NodeData[] arr = new NodeDataAlgo[cities.size()];
        //the new LinkedList for all the temp
        for (int i = 0; i < temp.length; i++) {
            temp[i] = new LinkedList<>();
            temp[i].add(cities.get(i));//add from node(i) to temp[i]
        }
        for (int i = 0; i < temp.length; i++) {
            List<NodeData> list_cities = new LinkedList<>();
            //the list for all the nodes
            for (int j = 0; j < cities.size(); j++) {
                list_cities.add(cities.get(j));
            }
            //remove the temp[i] from list_cities(i)
            NodeData id = list_cities.get(i);
            list_cities.remove(i);
            double total = 0;//the sum of weights
            for (int j = 0; j < list_cities.size(); j++) {
                double sort = Double.MAX_VALUE;
                int kay = list_cities.get(j).getKey();
                int koko = kay;
                NodeData remov = list_cities.get(j);
                for (int k = 0; k < list_cities.size(); k++) {
                    double w = this.shortestPathDist(id.getKey(), list_cities.get(k).getKey());
                    if (sort > w) {
                        sort = w;
                        kay = list_cities.get(k).getKey();
                        remov = list_cities.get(k);
                    }
                }
                temp[i].add(this.graph.getNode(kay));
                list_cities.remove(remov);
                total += sort;
                id = remov;
                j = -1;
                if (id == null)
                    break;
            }
            cities.get(i).setWeight(total);
            arr[i] = cities.get(i);
        }
        int ind = 0;
        double w = Double.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i].getWeight());
            double t = arr[i].getWeight();
            if (w > t) {
                w = t;
                ind = i;
            }
        }
        return temp[ind];
    }

    @Override
    public boolean save(String file) {
        try {
            FileWriter f = new FileWriter(file);
            Gson gson = new Gson();

            JsonObject jo = new JsonObject();
            JsonArray nodes = new JsonArray();
            JsonArray edges = new JsonArray();

            for (NodeData node : graph.getV()) {
                JsonObject jsonObject = new JsonObject();
                String s = String.valueOf(node.getLocation().x());
                s = s + ',' + +node.getLocation().y();
                s = s + ',' + node.getLocation().z();
                jsonObject.addProperty("pos", s);
                nodes.add(jsonObject);
                jsonObject.addProperty("id", node.getKey());
            }


            for (NodeData n : graph.getV()) {
                for (EdgeData e : graph.getE(n.getKey())) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("src", e.getSrc());
                    jsonObject.addProperty("w", e.getWeight());
                    jsonObject.addProperty("dest", e.getDest());
                    edges.add(jsonObject);
                }
            }
            jo.add("Edges", edges);
            jo.add("Nodes", nodes);
            f.flush();
            gson.toJson(jo, f);
            f.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean load(String file) {

        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(DirectedWeightedGraphAlgo.class, new GraphJsonDeserializer());
            Gson gson = gsonBuilder.create();
            FileReader fr = new FileReader(file);
            DirectedWeightedGraph ds = gson.fromJson(fr, DirectedWeightedGraphAlgo.class);
            init(ds);
            return true;

        } catch (FileNotFoundException e) {
            System.err.println("file unsuccessfully loaded, the graph remain as is");
            return false;
        }

    }

    public void Dijkstra(int src) {
        NodeData n = graph.getNode(src);
        PriorityQueue<NodeData> pq = new PriorityQueue<>();
        pq.add(n);
        //set all nodes tag in white(white = no visited) and add them to PQ
        for (Iterator<NodeData> it = graph.nodeIter(); it.hasNext(); ) {
            NodeData vertex = it.next();
            if (vertex.getKey() != src) vertex.setWeight(Double.POSITIVE_INFINITY);
            vertex.setInfo("white");
            pq.add(vertex);
        }
        n.setWeight(0);
        n.setInfo("black");
        while (!pq.isEmpty()) {
            NodeData curr = pq.remove();
            if (graph.edgeIter(curr.getKey()) != null)
            {
                for (Iterator<EdgeData> it = graph.edgeIter(curr.getKey()); it.hasNext(); ) {
                    EdgeData edge = it.next();
                    if (graph.getNode(edge.getDest()) != null) {
                        NodeData vertex = graph.getNode(edge.getDest());
                        double dist = curr.getWeight() + this.graph.getEdge(curr.getKey(), vertex.getKey()).getWeight();
                        if (vertex.getInfo().equals("white") && (dist < vertex.getWeight())) {
                            vertex.setWeight(curr.getWeight());
                            if (vertex.getWeight() < dist) {
                                vertex.setWeight(dist);
                                parent.put(vertex.getKey(), curr);
                                //update queue
                                pq.remove(vertex);
                                pq.add(vertex);
                            }
                        }
                        curr.setInfo("black");
                    }
                }
            }
        }
    }


    public void BFS(DirectedWeightedGraphAlgo g , NodeData n) {
        if(n != null && g != null) {
            Iterator<NodeData> c =  g.nodeIter();
            Iterator<NodeData> j = c;
            /* Go through all the nodes in the graph and paint them "white" and gives them tag = 0 at the first */
            while(j.hasNext()) {
                NodeData nodeS = j.next();
                if(nodeS != null) {
                    nodeS.setInfo("white");
                }
            }
            g.getNode(n.getKey()).setInfo("gray");

            Queue<NodeData> queue = new LinkedList<NodeData>();
            queue.add(n);//add the node to the queue

            //Run while queue is not empty
            while(!queue.isEmpty()) {
                NodeData u = queue.remove();
                if(u.getInfo() != "black")
                    u.setInfo("gray");
                Iterator<EdgeData> neighbours = this.graph.edgeIter(u.getKey());
                Iterator <EdgeData> i = neighbours;

                /* Check if the node is not black: there are two options: - white then paint it gray and add it to the queue,
                 gray and then just paint it black The difference between white and gray is:
                 white it means that the node has not been tested at all,
                 gray means it has been tested but still not sure Then it needs to be checked again,
                 if the node is painted black it means it has been checked and
                 it is connected to another node whose color is black (also connected)*/
                while(i.hasNext()) {
                    EdgeData index = i.next();
                    if(this.graph.getNode(index.getDest()).getInfo() == "white") {
                        this.graph.getNode(index.getDest()).setInfo("black");
                        queue.add(this.graph.getNode(index.getDest()));
                    }
                    else if(this.graph.getNode(index.getDest()).getInfo() == "gray"){
                        this.graph.getNode(index.getDest()).setInfo("black");
                    }
                }

            }
        }
        return;
    }
}


