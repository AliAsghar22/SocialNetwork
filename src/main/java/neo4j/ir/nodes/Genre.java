package neo4j.ir.nodes;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ali Asghar on 29/06/2017.
 */
public class Genre {
    @JsonProperty int id;
    @JsonProperty String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
