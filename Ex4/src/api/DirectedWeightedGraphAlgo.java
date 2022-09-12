package api;

import java.util.*;

public class DirectedWeightedGraphAlgo implements DirectedWeightedGraph{

    private HashMap<Integer, NodeData> nodesCollection;// this variable collection tell me the collection nodes in my graph
    private HashMap<Integer,HashMap<Integer,EdgeData>> graph;
    private Vector<EdgeData> edges;
    private int MC=0;// this variable count the changes in my graph like add nodes and remove nodes in the graph .
    private int edgeSize;

    public Vector<EdgeData> getEdges() {
        return edges;
    }

    public DirectedWeightedGraphAlgo()
    {
        this.nodesCollection = new HashMap<>();
        this.graph=new HashMap<>();
        this.edges=new Vector<>();
        this.MC = 0;
        this.edgeSize = 0;
    }

    @Override
    public NodeData getNode(int key) {

        if (this.nodesCollection.containsKey(key)) return this.nodesCollection.get(key);
        return null;
    }

    @Override
    public EdgeData getEdge(int src, int dest) {

        if (nodesCollection.containsKey(src) && nodesCollection.containsKey(dest) && src != dest && graph.get(src).get(dest)!=null) {
            return graph.get(src).get(dest);
        }
        return null;
    }

    @Override
    public void addNode(NodeData n) {

        if (n != null && !this.nodesCollection.containsKey(n.getKey())) {
            this.nodesCollection.put(n.getKey(), n);
            this.graph.put(n.getKey(), new HashMap<Integer, EdgeData>());
            ++this.MC;
        }
    }

    @Override
    public void connect(int src, int dest, double w) {

        if (src != dest && w >= 0 && this.nodesCollection.containsKey(src) && this.nodesCollection.containsKey(dest)) {
            if (getEdge(src, dest) == null) ++edgeSize;
            ++MC;
            graph.get(src).put(dest, new EdgeDataAlgo(src, dest, w));
        }
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        if (this.nodesCollection != null) return this.nodesCollection.values().iterator();
        return null;

    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return this.edges.iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) { // not sure for the solution
        if (this.nodesCollection.containsKey(node_id) && graph.get(node_id).values()!=null) return graph.get(node_id).values().iterator();
        return null;

    }

    @Override
    public NodeData removeNode(int key) {

        if (!this.nodesCollection.containsKey(key)) return null;
        if (edgeIter(key) != null) {
            //for (NodeData n : getV())
            for (Iterator<NodeData> it = nodeIter(); it.hasNext(); ) {
                NodeData n = it.next();
                if (graph.get(n.getKey()) != null && graph.get(n.getKey()).containsKey(key)) {
                    graph.get(n.getKey()).remove(key);
                    --this.edgeSize;
                }
            }
        }
        int count = graph.get(key).size();
        graph.remove(key);
        this.edgeSize = this.edgeSize - count;
        return nodesCollection.remove(key);
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {

        if (this.nodesCollection.containsKey(src) && this.nodesCollection.containsKey(dest) && getEdge(src, dest) != null) {
            --this.edgeSize;
            ++this.MC;
            return graph.get(src).remove(dest);
        }
        return null;
    }

    @Override
    public int nodeSize() {
        return this.nodesCollection.size();
    }

    @Override
    public int edgeSize() {
        return this.edgeSize;
    }

    @Override
    public int getMC() {
        return this.MC;
    }

    @Override
    public Collection<NodeData> getV() {
        if (this.nodesCollection != null) return this.nodesCollection.values();
        return null;
    }

    @Override
    public Collection<EdgeData> getE(int node_id) {
        if (this.nodesCollection.containsKey(node_id) && graph.get(node_id).values()!=null) return graph.get(node_id).values();
        Collection<EdgeData> cl = new LinkedList<>();
        return cl;
    }
}
