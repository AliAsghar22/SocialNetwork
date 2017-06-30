package neo4j.ir.nodes;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ali Asghar on 29/06/2017.
 */
public class Person {
    @JsonProperty int id;
    @JsonProperty String name;
    @JsonProperty String born;
    @JsonProperty String imageURL;

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

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
