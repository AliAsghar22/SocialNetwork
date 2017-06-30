package neo4j.ir.Service;

import neo4j.ir.nodes.Genre;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Created by Ali Asghar on 29/06/2017.
 */
@Service
public class GenreService {
    private final Driver driver;

    @Autowired
    public GenreService(org.neo4j.driver.v1.Driver driver) {
        this.driver = driver;
    }

    public boolean exists(String name) {
        Session session = driver.session();
        String query = "MATCH (n:GENRE {name:{name}}) return n.name as name";
        StatementResult sr = session.run(query, parameters("name", name));
        session.close();
        return sr.hasNext();
    }

    public void add(Genre g){
        if(exists(g.getName()))
            return;
        Session session = driver.session();
        String query = "CREATE (n:GENRE {name:{name}})";
        session.run(query, parameters("name", g.getName()));
        session.close();
    }

    public List<Genre> convertToGenre(StatementResult sr){
        List<Genre> people = new ArrayList<>();
        while (sr.hasNext()){
            Record r = sr.next();
            Genre g = new Genre();
            g.setName(r.get("name").asString());
            g.setId(r.get("id").asInt());
            people.add(g);
        }
        return people;
    }

    public Genre getByName(String name){
        Session session = driver.session();
        String query = "MATCH (g:GENRE {name:{name}}) return g.name as name, ID(g) as id";
        StatementResult sr = session.run(query, parameters("name", name));
        Genre p = convertToGenre(sr).get(0);
        session.close();
        return p;
    }

    public List<Genre> getAll(){
        Session session = driver.session();
        String query = "MATCH (g:GENRE) return g.name as name, ID(g) as id";
        StatementResult sr = session.run(query);
        List<Genre> genres = convertToGenre(sr);
        session.close();
        return genres;
    }

    public List<Genre> getMovieGenres(int movieId){
        Session session = driver.session();
        String query = "MATCH (m:MOVIE)-[:HAS_GENRE]->(g:GENRE) WHERE ID(m) = {movieId}" +
                " return g.name as name, ID(g) as id";
        StatementResult sr = session.run(query, parameters("movieId", movieId));
        List<Genre> pe = convertToGenre(sr);
        session.close();
        return pe;
    }
}
