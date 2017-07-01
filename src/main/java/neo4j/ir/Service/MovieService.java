package neo4j.ir.Service;

import neo4j.ir.nodes.Genre;
import neo4j.ir.nodes.Keyword;
import neo4j.ir.nodes.Movie;
import neo4j.ir.nodes.Person;
import neo4j.ir.web.dto.MovieDTO;
import neo4j.ir.web.dto.MovieSearchDTO;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.java2d.pipe.ValidatePipe;

import java.util.*;

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
//        String movieID = sr.next().get("title").asString();
        String movieID = m.getTitle();
        if (dto.getDirector() != null) {
            addDirector(dto.getDirector().getName(), movieID);
        }
        if (dto.getWriter() != null) {
            addWriter(dto.getWriter().getName(), movieID);
        }

        if (dto.getProducer() != null) {
            addProducer(dto.getProducer().getName(), movieID);
        }

        if (dto.getGenres() != null)
            for (Genre g :
                    dto.getGenres()) {
                addGenre(movieID, g.getName());
            }

        if(dto.getActors() != null)
            for (Person person :
                    dto.getActors()) {
                addActor(person.getName(), movieID);
            }

        if (dto.getKeywords() != null)
            for (Keyword keyword : dto.getKeywords()) {
                addKeyword(movieID, keyword.getName());
            }
        session.close();
    }

    public void update(MovieDTO dto) {
        if (!exists(dto.getMovie().getTitle())) {
            System.out.println("this movie doesn't exist");
            return;
        }
        delete(dto.getMovie().getTitle());
        add(dto);
    }

    public void delete(String title) {
        Session session = driver.session();
        String query = "MATCH (n:MOVIE{title:{title}})-[r]-() delete r";
        session.run(query, parameters("title", title));
        query = "MATCH (n:MOVIE {title:{title}}) delete n";
        session.run(query);
        session.close();
    }

    private void relation(String id1, String type1, String id2, String type2, String relationType, Map<String, Object> properties) {
        Session session = driver.session();
        String prop = null;
        for (String key : properties.keySet()) {
            prop += key + ":" + properties.get(key) + ", ";
        }
        String k1 = "name",k2 ="name";
        if(type1.equals("MOVIE"))
            k1 = "title";
        if(type2.equals("MOVIE"))
            k2 = "title";

        String query;

            query = "MATCH (t1:" + type1 +"{"+ k1 +":{id1}}" + "), (t2:" + type2 + "{"+k2+":{id2}})" +
                    " " +
                    "CREATE (t1)-[r:" + relationType + "]->(t2)";

        System.out.println(query);
        System.out.println(id1);
        System.out.println(id2);

        session.run(query, parameters("id1", id1, "id2", id2));
        session.close();
    }

    private void addProducer(String id, String movieID) {
        relation(id, "PERSON", movieID, "MOVIE", "PRODUCED", new HashMap<>());
    }

    private void addKeyword(String movieID, String id) {
        relation(movieID, "MOVIE", id, "KEYWORD", "HAS_KEYWORD", new HashMap<>());
    }

    public void addDirector(String directorId, String movieId) {
        System.out.println(directorId);
        System.out.println(movieId);
        relation(directorId, "PERSON", movieId, "MOVIE", "DIRECTED_IN", new HashMap<>());
    }

    public void addActor(String actorId, String movieId) {
        relation(actorId, "PERSON", movieId, "MOVIE", "ACTED_IN", new HashMap<>());
    }

    public void addWriter(String writerId, String movieId) {
        relation(writerId, "PERSON", movieId, "MOVIE", "WRITER_OF", new HashMap<>());
    }

    public void addGenre(String movieId, String genreId) {
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
            m.setRate(r.get("rate").asFloat());
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

    public List<Movie> getAll() {
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

    public List<Movie> search(MovieSearchDTO dto) {
        String query = "MATCH (m:MOVIE) with m ";
        if (dto.getGenres() != null)
            for (String g : dto.getGenres()) {
                query += "MATCH (m)-[r:HAS_GENRE ]->(g {name:\"" + g + "\"}) with m ";
            }
        if (dto.getActorNames() != null)
            for (String g : dto.getActorNames()) {
                query += "MATCH (a {name:\"" + g + "\"})-[r:ACTED_IN ]->(m) with m ";
            }
        if (dto.getDirectorName() != null) {
            query += "MATCH (d {name:\"" + dto.getDirectorName() + "\"})-[r:DIRECTED_IN]->(m) with m ";
        }
        if (dto.getWriterName() != null) {
            query += "MATCH (w {name:\"" + dto.getWriterName() + "\"})-[r:WRITER_OF]->(m) with m ";
        }
        if (dto.getProductionYear() != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.YEAR, Integer.valueOf(dto.getProductionYear()));
            long date1 = cal.getTimeInMillis();
            cal.set(Calendar.YEAR, Integer.valueOf(dto.getProductionYear()) + 1);
            long date2 = cal.getTimeInMillis();
            query += "where m.productionDate > " + date1 + " and m.productionDate < " + date2 + " with m ";
        }

        if (dto.getTitle() != null) {
            query += "where m.title contains \"" + dto.getTitle() + "\" with m";
        }

        query += " return ID(m) as id," +
                " m.title as title," +
                " m.duration as duration," +
                " m.imageURL as imageURL," +
                " m.productionDate as productionDate," +
                " m.rate as rate," +
                " m.tagline as tagline";
        Session s = driver.session();
        List<Movie> movies = convertToMovies(s.run(query));
        s.close();
        return movies;
    }

    public List<Movie> getTop5() {
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

    public List<Movie> getLatest() {
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

    public void calculateRate(int movieId) {
        Session s = driver.session();
        String query = "MATCH (u:USER)-[c:SCORED]->(m:MOVIE)" +
                " WHERE ID(m) = {id}" +
                " return count(u) as count" +
                " , sum(c.score) as score";
        StatementResult sr = s.run(query, parameters("id", movieId));
        Record r = sr.next();
        int count = r.get("count").asInt();
        float score = r.get("score").asFloat();
        float lastScore = score / count;

        query = "MATCH (m:MOVIE) " +
                " WHERE ID(m) = {id}" +
                " SET m.rate = {rate}";

        s.run(query, parameters("id", movieId, "rate", lastScore));
        s.close();
    }

    public List<Movie> suggestByGenre(String userName) {
        Session s = driver.session();
        String query =
                "MATCH (u:USER{userName:{userName}})-[:SCORED]->(m:MOVIE)-[:HAS_GENRE]->(g:GENRE)" +
                        " with g, count(*) as cnt , u" +
                        " where cnt > 5" +
                        " with u, g" +
                        " match (m:MOVIE) where not (u)-->(m)" +
                        " with m, g" +
                        " match (m)-[:HAS_GENRE]->(g)" +
                        " return ID(m) as id," +
                        " m.title as title," +
                        " m.duration as duration," +
                        " m.imageURL as imageURL," +
                        " m.productionDate as productionDate," +
                        " m.rate as rate," +
                        " m.tagline as tagline";
        StatementResult sr = s.run(query, parameters("userName", userName));
        List<Movie> movies = convertToMovies(sr);
        s.close();
        return movies;
    }

    public List<Movie> suggestByOtherPeople(String userName) {
        Session s = driver.session();
        String query =
                "MATCH (u:USER{userName:{userName}}), (m:MOVIE) where not (u)-->(m)" +
                        " with m , u" +
                        " match (n:USER)-[:SCORED]->(m)" +
                        " where n.male = u.male " +
                        " and (n.age - 8) < u.age and u.age < (n.age + 8)" +
                        " return ID(m) as id," +
                        " m.title as title," +
                        " m.duration as duration," +
                        " m.imageURL as imageURL," +
                        " m.productionDate as productionDate," +
                        " m.rate as rate," +
                        " m.tagline as tagline";
        StatementResult sr = s.run(query, parameters("userName", userName));
        List<Movie> movies = convertToMovies(sr);
        s.close();
        return movies;
    }

    public Double getScore(String userName,int movieId)
    {
        Session session = driver.session();
        String query =
                "MATCH (u:USER{userName:{userName}})-[s:SCORED]->(m:MOVIE)" +
                        "WHERE ID(m) = {movieId}" +
                        "return s.score";

        StatementResult sr = session.run(query,parameters("userName",userName,"movieId",movieId));

        if(!sr.hasNext())
            return null;

        Value value = sr.next().values().get(0);
        if(value == null)
            return null;

        return value.asDouble();
    }

    public String getComment(String userName,int movieId)
    {
        Session session = driver.session();
        String query =
                "MATCH (u:USER{userName:{userName}})-[c:COMMENTED]->(m:MOVIE)" +
                        "WHERE ID(m) = {movieId}" +
                        "return c.comment";

        StatementResult sr = session.run(query,parameters("userName",userName,"movieId",movieId));

        if(!sr.hasNext())
            return null;

        Value value = sr.next().values().get(0);
        if(value == null)
            return null;

        return value.asString();
    }

    public List<Movie> getSeenMovies(String userName)
    {
        Session session = driver.session();

        String query = "MATCH (u:USER{userName:{userName}})-->(m:MOVIE)" +
                " return distinct ID(m) as id," +
                " m.title as title," +
                " m.duration as duration," +
                " m.imageURL as imageURL," +
                " m.productionDate as productionDate," +
                " m.rate as rate," +
                " m.tagline as tagline";

        StatementResult sr = session.run(query,parameters("userName",userName));
        List<Movie> m = convertToMovies(sr);
        session.close();
        return m;
    }
}
