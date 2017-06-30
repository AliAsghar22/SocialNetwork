package neo4j.ir.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.graphdb.Node;

/**
 * Created by Ali Asghar on 27/06/2017.
 */
public class Serial {

    @JsonProperty
    private String name;

    @JsonProperty
    private String type;

    @JsonIgnore
    private Node node;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
