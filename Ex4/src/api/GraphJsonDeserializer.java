package api;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GraphJsonDeserializer implements JsonDeserializer<DirectedWeightedGraphAlgo>{

    @Override
    public DirectedWeightedGraphAlgo deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray nodes = jsonObject.get("Nodes").getAsJsonArray();
        JsonArray edges = jsonObject.get("Edges").getAsJsonArray();


        DirectedWeightedGraphAlgo ds = new DirectedWeightedGraphAlgo();

        for(int i=0 ; i<nodes.size(); i++){
            JsonObject n = nodes.get(i).getAsJsonObject();
            String pos =n.get("pos").getAsString();
            NodeDataAlgo node = new NodeDataAlgo(n.get("id").getAsInt() , new GeoLocationAlgo(pos));
            ds.addNode(node);
        }
        for (int i=0  ; i<edges.size() ; i++){
            JsonObject ed = edges.get(i).getAsJsonObject();
            EdgeData e =new EdgeDataAlgo(ed.get("src").getAsInt() , ed.get("dest").getAsInt() , ed.get("w").getAsDouble());
            ds.connect(e.getSrc() , e.getDest() , e.getWeight());
        }

        return ds;
    }
}

