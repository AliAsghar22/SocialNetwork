package neo4j.ir.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ali Asghar on 30/06/2017.
 */
public class ScoreDTO {
    @JsonProperty private int id;
    @JsonProperty private float score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
