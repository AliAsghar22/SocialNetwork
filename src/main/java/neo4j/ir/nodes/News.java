package neo4j.ir.nodes;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ali Asghar on 30/06/2017.
 */
public class News {
    @JsonProperty int id;
    @JsonProperty String title;
    @JsonProperty String body;
    @JsonProperty long date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
