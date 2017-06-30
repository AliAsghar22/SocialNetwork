package neo4j.ir.Service;

import neo4j.ir.nodes.News;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Created by Ali Asghar on 30/06/2017.
 */
@Service
public class NewsService {
    @Autowired
    Driver driver;

    public boolean exists(String title) {
        Session session = driver.session();
        String query = "MATCH (n:NEWS {title:{title}}) return n.title as title";
        StatementResult sr = session.run(query, parameters("title", title));
        session.close();
        return sr.hasNext();
    }

    public void add(News g){
        Session session = driver.session();
        String query = "CREATE (n:NEWS {title:{title}, body:{body}, date:{date}})";
        session.run(query, parameters("title", g.getTitle(),"body" , g.getBody(), "date", new Date().getTime()));
        session.close();
    }

    public List<News> convertToNews(StatementResult sr){
        List<News> people = new ArrayList<>();
        while (sr.hasNext()){
            Record r = sr.next();
            News g = new News();
            g.setTitle(r.get("title").asString());
            g.setId(r.get("id").asInt());
            g.setDate(r.get("date").asLong());
            g.setBody(r.get("body").asString());
            people.add(g);
        }
        return people;
    }

    public List<News> getAll(){
        Session session = driver.session();
        String query = "MATCH (g:NEWS) return g.name as name, g.body as body ,ID(g) as id, g.date as date order by date";
        StatementResult sr = session.run(query);
        List<News> news = convertToNews(sr);
        session.close();
        return news;
    }
}
