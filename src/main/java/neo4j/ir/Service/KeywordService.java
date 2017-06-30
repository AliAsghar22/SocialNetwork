package neo4j.ir.Service;

import neo4j.ir.nodes.Keyword;
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
 * Created by Ali Asghar on 30/06/2017.
 */
@Service
public class KeywordService {
    private final Driver driver;

    @Autowired
    public KeywordService(org.neo4j.driver.v1.Driver driver) {
        this.driver = driver;
    }

    public boolean exists(String name) {
        Session session = driver.session();
        String query = "MATCH (n:KEYWORD {name:{name}}) return n.name as name";
        StatementResult sr = session.run(query, parameters("name", name));
        session.close();
        return sr.hasNext();
    }

    public void add(Keyword key){
        if(exists(key.getName()))
            return;
        Session session = driver.session();
        String query = "CREATE (n:KEYWORD {name:{name}})";
        session.run(query, parameters("name", key.getName()));
        session.close();
    }

    public List<Keyword> convertToKeyword(StatementResult sr){
        List<Keyword> keywords = new ArrayList<>();
        while (sr.hasNext()){
            Record r = sr.next();
            Keyword g = new Keyword();
            g.setName(r.get("name").asString());
            g.setId(r.get("id").asInt());
            keywords.add(g);
        }
        return keywords;
    }

    public Keyword getByName(String name){
        Session session = driver.session();
        String query = "MATCH (g:KEYWORD {name:{name}}) return g.name as name, ID(g) as id";
        StatementResult sr = session.run(query, parameters("name", name));
        Keyword p = convertToKeyword(sr).get(0);
        session.close();
        return p;
    }

    public List<Keyword> getAll(){
        Session session = driver.session();
        String query = "MATCH (g:KEYWORD) return g.name as name, ID(g) as id";
        StatementResult sr = session.run(query);
        List<Keyword> genres = convertToKeyword(sr);
        session.close();
        return genres;
    }

    public List<Keyword> getMovieKeywords(int movieId){
        Session session = driver.session();
        String query = "MATCH (m:MOVIE)-[:HAS_KEYWORD]->(g:KEYWORD) WHERE ID(m) = {movieId}" +
                " return g.name as name, ID(g) as id";
        StatementResult sr = session.run(query, parameters("movieId", movieId));
        List<Keyword> pe = convertToKeyword(sr);
        session.close();
        return pe;
    }
}
