package neo4j.ir.nodes;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ali Asghar on 27/06/2017.
 */
public class Film {
    @JsonProperty private String name;
    @JsonProperty private int productionYear;
    @JsonProperty private String genre;
    @JsonProperty private String director;
    @JsonProperty private String writer;


    public Film(){

    }
    public Film(String name){
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}
