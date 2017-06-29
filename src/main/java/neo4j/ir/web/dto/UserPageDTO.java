package neo4j.ir.web.dto;

import neo4j.ir.nodes.Film;

import java.util.List;

/**
 * Created by Ali Asghar on 29/06/2017.
 */
public class UserPageDTO {
    private List<Film> film;

    public List<Film> getFilm() {
        return film;
    }

    public void setFilm(List<Film> film) {
        this.film = film;
    }
}
