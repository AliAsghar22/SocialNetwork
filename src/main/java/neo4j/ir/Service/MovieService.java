package neo4j.ir.Service;

import neo4j.ir.nodes.Genre;
import neo4j.ir.nodes.Movie;
import neo4j.ir.nodes.Person;
import neo4j.ir.web.dto.MovieDTO;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Created by Ali Asghar on 29/06/2017.
 */
@Service
public class MovieService {
    private final Driver driver;

    @Autowired
    public MovieService(Driver driver) {
        this.driver = driver;
    }

    public boolean exists(String name) {
        Session session = driver.session();
        String query = "MATCH (n:MOVIE {title:{title}}) return n.title as title";
        StatementResult sr = session.run(query, parameters("title", name));
        session.close();
        return sr.hasNext();
    }

    public void add(MovieDTO dto) {
        if (exists(dto.getMovie().getTitle())) {
            System.out.println("movie already added");
            return;
        }
        Session session = driver.session();
        Movie m = dto.getMovie();
        String query = "CREATE (n:MOVIE {title:{title}, summary:{summary}, tagline:{tagline}" +
                ", productionYear:{productionYear}, duration:{duration}, imageURL:{imageURL}, rate:{rate}}) return ID(n) as id";
        StatementResult sr = session.run(query, parameters("title", m.getTitle(),
                "tagline", m.getTagline(),
                "productionYear", m.getProductionYear(),
                "duration", m.getDuration(),
                "imageURL", m.getImageURL(),
                "rate", m.getRate(),
                "summary", m.getSummary()));
        int movieID = sr.next().get("id").asInt();
        if (dto.getDirector() != null) {
            addDirector(dto.getDirector().getId(), movieID);
        }
        if (dto.getWriter() != null) {
            addWriter(dto.getWriter().getId(), movieID);
        }

        for (Genre g :
                dto.getGenres()) {
            addGenre(movieID, g.getId());
        }

        for (Person person :
                dto.getPlayers()) {
            addActor(person.getId(), movieID);
        }
        session.close();
    }
    public void update(MovieDTO dto){
        if(!exists(dto.getMovie().getTitle())){
            System.out.println("this movie doesn't exist");
            return;
        }
        delete(dto.getMovie().getTitle());
        add(dto);
    }

    public void delete(String title){
        Session session = driver.session();
        String query = "MATCH (n:MOVIE{title:{title}})-[r]-() delete r";
        session.run(query, parameters("title", title));
        query = "MATCH (n:MOVIE {title:{title}}) delete n";
        session.run(query);
        session.close();
    }

    private void relation(int id1, String type1, int id2, String type2, String relationType, Map<String, Object> properties) {
        Session session = driver.session();
        String prop = null;
        for (String key : properties.keySet()) {
            prop += key + ":" + properties.get(key) + ", ";
        }
        String query;
        if (prop != null) {
            prop = prop.substring(0, prop.length() - 2);

            query = "MATCH (t1:" + type1 + "), (t2:" + type2 + ")" +
                    " WHERE ID(t1) = {id1} and ID(t2) = {id2} " +
                    "CREATE (t1)-[r:" + relationType + "{" + prop + "}]->(t2) return ID(r) as id";
        } else {
            query = "MATCH (t1:" + type1 + "), (t2:" + type2 + ")" +
                    " WHERE ID(t1) = {id1} and ID(t2) = {id2} " +
                    "CREATE (t1)-[r:" + relationType + "]->(t2) return ID(r) as id";
        }
        session.run(query, parameters("id1", id1, "id2", id2));
        session.close();
    }

    public void addDirector(int directorId, int movieId) {
        relation(directorId, "PERSON", movieId, "MOVIE", "DIRECTED_IN", new HashMap<>());
    }

    public void addActor(int actorId, int movieId) {
        relation(actorId, "PERSON", movieId, "MOVIE", "ACTED_IN", new HashMap<>());
    }

    public void addWriter(int writerId, int movieId) {
        relation(writerId, "PERSON", movieId, "MOVIE", "WRITER_OF", new HashMap<>());
    }

    public void addGenre(int movieId, int genreId) {
        relation(movieId, "MOVIE", genreId, "GENRE", "HAS_GENRE", new HashMap<>());
    }

    public List<Movie> convertToMovies(StatementResult sr) {
        List<Movie> movies = new ArrayList<>();
        while (sr.hasNext()) {
            Record r = sr.next();
            Movie m = new Movie();
            m.setTitle(r.get("title").asString());
            m.setId(r.get("id").asInt());
            m.setDuration(r.get("duration").asString());
            m.setImageURL(r.get("imageURL").asString());
            m.setProductionYear(r.get("productionYear").asString());
            m.setRate(r.get("rate").asString());
            m.setTagline(r.get("tagline").asString());
            movies.add(m);
        }
        return movies;
    }

    public Movie getByTitle(String title) {
        Session s = driver.session();
        String query = "MATCH (m:MOVIE {title:{title}})" +
                " return ID(m) as id," +
                " m.title as title," +
                " m.duration as duration," +
                " m.imageURL as imageURL," +
                " m.productionYear as productionYear," +
                " m.rate as rate," +
                " m.tagline as tagline";
        StatementResult sr = s.run(query, parameters("title", title));
        Movie m = convertToMovies(sr).get(0);
        s.close();
        return m;
    }

    public List<Movie> getAll(){
        Session s = driver.session();
        String query = "MATCH (m:MOVIE)" +
                " return ID(m) as id," +
                " m.title as title," +
                " m.duration as duration," +
                " m.imageURL as imageURL," +
                " m.productionYear as productionYear," +
                " m.rate as rate," +
                " m.tagline as tagline";
        StatementResult sr = s.run(query);
        List<Movie> m = convertToMovies(sr);
        s.close();
        return m;
    }

    public List<Movie> search(MovieDTO dto){
        Movie m = dto.getMovie();
        if(dto.getGenres() != null && dto.getGenres().size() != 0){

        }

        return null;
    }
}
