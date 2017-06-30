package neo4j.ir.nodes;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ali Asghar on 29/06/2017.
 */
public class Movie {

    @JsonProperty int id;
    @JsonProperty String title;
    @JsonProperty String tagline;
    @JsonProperty String summary;
    @JsonProperty String imageURL;
    @JsonProperty int duration;
    @JsonProperty float rate;
    @JsonProperty long productionDate;

    public long getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(long productionDate) {
        this.productionDate = productionDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
