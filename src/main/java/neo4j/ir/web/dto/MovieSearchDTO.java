package neo4j.ir.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Ali Asghar on 30/06/2017.
 */
public class MovieSearchDTO {
    @JsonProperty private List<String> genres;
    @JsonProperty private List<String> actorNames;
    @JsonProperty private String productionYear;
    @JsonProperty private String writerName;
    @JsonProperty private String directorName;
    @JsonProperty private String title;

    public List<String> getActorNames() {
        return actorNames;
    }

    public void setActorNames(List<String> actorNames) {
        this.actorNames = actorNames;
    }

    public String getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(String productionYear) {
        this.productionYear = productionYear;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
