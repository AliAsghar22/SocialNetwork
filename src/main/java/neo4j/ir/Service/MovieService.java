package neo4j.ir.Service;

import neo4j.ir.nodes.Genre;
import neo4j.ir.nodes.Keyword;
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
                ", productionDate:{productionDate}, duration:{duration}, imageURL:{imageURL}" +
                ", rate:{rate}}) return ID(n) as id";
        StatementResult sr = session.run(query,
                parameters("title", m.getTitle(),
                "tagline", m.getTagline(),
                "productionDate", m.getProductionDate(),
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

        if(dto.getProducer() != null){
            addProducer(dto.getProducer().getId(), movieID);
        }

        for (Genre g :
                dto.getGenres()) {
            addGenre(movieID, g.getId());
        }

        for (Person person :
                dto.getActors()) {
            addActor(person.getId(), movieID);
        }

        for(Keyword keyword: dto.getKeywords()){
            addKeyword(movieID, keyword.getId());
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

    private void addProducer(int id, int movieID) {
        relation(id, "PERSON", movieID, "MOVIE", "PRODUCED", new HashMap<>());
    }

    private void addKeyword(int movieID, int id) {
        relation(movieID, "MOVIE", id, "KEYWORD", "HAS_KEYWORD", new HashMap<>());
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
            m.setDuration(r.get("duration").asInt());
            m.setImageURL(r.get("imageURL").asString());
            m.setProductionDate(r.get("productionDate").asLong());
            m.setRate(r.get("rate").asInt());
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
                " m.productionDate as productionDate," +
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
                " m.productionDate as productionDate," +
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

    public List<Movie> getTop5(){
        Session s = driver.session();
        String query = "MATCH (m:MOVIE)" +
                " return ID(m) as id," +
                " m.title as title," +
                " m.duration as duration," +
                " m.imageURL as imageURL," +
                " m.productionDate as productionDate," +
                " m.rate as rate," +
                " m.tagline as tagline" +
                " ORDER BY rate desc" +
                " LIMIT 5";
        StatementResult sr = s.run(query);
        List<Movie> m = convertToMovies(sr);
        s.close();
        return m;
    }

    public List<Movie> getLatest(){
        Session s = driver.session();
        String query = "MATCH (m:MOVIE)" +
                " return ID(m) as id," +
                " m.title as title," +
                " m.duration as duration," +
                " m.imageURL as imageURL," +
                " m.productionDate as productionDate," +
                " m.rate as rate," +
                " m.tagline as tagline" +
                " ORDER BY productionDate desc" +
                " LIMIT 20";
        StatementResult sr = s.run(query);
        List<Movie> m = convertToMovies(sr);
        s.close();
        return m;
    }

    public Movie getById(int movieId) {
        Session s = driver.session();
        String query = "MATCH (m:MOVIE)" +
                " WHERE ID(m) = {id}" +
                " return ID(m) as id," +
                " m.title as title," +
                " m.duration as duration," +
                " m.imageURL as imageURL," +
                " m.productionDate as productionDate," +
                " m.rate as rate," +
                " m.tagline as tagline";
        StatementResult sr = s.run(query, parameters("id", movieId));
        Movie m = convertToMovies(sr).get(0);
        s.close();
        return m;
    }

    public void calculateRate(int movieId){
        Session s = driver.session();
        String query = "MATCH (u:USER)-[c:COMMENTED]->(m:MOVIE)" +
                " WHERE ID(m) = {id}" +
                " return count(u) as count" +
                " , sum(c.score) as score";
        StatementResult sr = s.run(query, parameters("id", movieId));
        int count = sr.next().get("count").asInt();
        float score = sr.next().get("score").asFloat();
        float lastScore = score/count;

        query = "MATCH (u:USER)-[c:COMMENTED]->(m:MOVIE)" +
                " WHERE ID(m) = {id}" +
                " SET m.score = {score}";
        s.run(query, parameters("score", lastScore));
        s.close();
    }
}
