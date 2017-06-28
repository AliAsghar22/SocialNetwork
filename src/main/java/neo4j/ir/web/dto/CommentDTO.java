package neo4j.ir.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Amir Shams on 6/28/2017.
 */
public class CommentDTO {

    @JsonProperty
    private String body;
    @JsonProperty
    private String itemId;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
