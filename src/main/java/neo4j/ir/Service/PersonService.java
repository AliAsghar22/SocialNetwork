package neo4j.ir.Service;

import neo4j.ir.nodes.Person;
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
public class PersonService {
    private final Driver driver;

    @Autowired
    public PersonService(Driver driver) {
        this.driver = driver;
    }

    public boolean exists(String name) {
        Session session = driver.session();
        String query = "MATCH (n:PERSON {name:{name}}) return n.name as name";
        StatementResult sr = session.run(query, parameters("name", name));
        session.close();
        return sr.hasNext();
    }

    public void add(Person p){
        if(exists(p.getName()))
            return;
        Session session = driver.session();
        String query = "CREATE (n:PERSON {name:{name}, born:{born}, imageURL:{imageURL}})";
        session.run(query, parameters("name", p.getName(),
                "born", p.getBorn(), "imageURL", p.getImageURL()));
        session.close();
    }

    public List<Person> convertToPerson(StatementResult sr){
        List<Person> people = new ArrayList<>();
        while (sr.hasNext()){
            Record r = sr.next();
            Person p = new Person();
            p.setName(r.get("name").asString());
            p.setBorn(r.get("born").asString());
            p.setId(r.get("id").asInt());
            p.setImageURL(r.get("imageURL").asString());
            people.add(p);
        }
        return people;
    }

    public Person getByName(String name){
        Session session = driver.session();
        String query = "MATCH (n:PERSON {name:{name}}) return n.name as name, n.born as born, ID(n) as id, n.imageURL as imageURL";
        StatementResult sr = session.run(query, parameters("name", name));
        Person p = convertToPerson(sr).get(0);
        session.close();
        return p;
    }

    public List<Person> getAll(){
        Session session = driver.session();
        String query = "MATCH (p:PERSON) return p.name as name, p.born as born, ID(p) as id, p.imageURL as imageURL";
        StatementResult sr = session.run(query);
        List<Person> pe = convertToPerson(sr);
        session.close();
        return pe;
    }

    public List<Person> getMovieActors(int movieId){
        Session session = driver.session();
        String query = "MATCH (p:PERSON)-[:ACTED_IN]->(m:MOVIE) WHERE ID(m) = {movieId}" +
                " return p.name as name, p.born as born, ID(p) as id, p.imageURL as imageURL";
        StatementResult sr = session.run(query, parameters("movieId", movieId));
        List<Person> pe = convertToPerson(sr);
        session.close();
        return pe;
    }

    public Person getMovieDirector(int movieId){
        Session session = driver.session();
        String query = "MATCH (p:PERSON)-[:DIRECTED_IN]->(m:MOVIE) WHERE ID(m) = {movieId}" +
                " return p.name as name, p.born as born, ID(p) as id, p.imageURL as imageURL";
        StatementResult sr = session.run(query, parameters("movieId", movieId));
        List<Person> pe = convertToPerson(sr);
        session.close();
        return pe.get(0);
    }

    public Person getMovieWriter(int movieId){
        Session session = driver.session();
        String query = "MATCH (p:PERSON)-[:WRITER_OF]->(m:MOVIE) WHERE ID(m) = {movieId}" +
                " return p.name as name, p.born as born, ID(p) as id, p.imageURL as imageURL";
        StatementResult sr = session.run(query, parameters("movieId", movieId));
        List<Person> pe = convertToPerson(sr);
        session.close();
        return pe.get(0);
    }

    public Person getMovieProducer(int movieId){
        Session session = driver.session();
        String query = "MATCH (p:PERSON)-[:PRODUCED]->(m:MOVIE) WHERE ID(m) = {movieId}" +
                " return p.name as name, p.born as born, ID(p) as id, p.imageURL as imageURL";
        StatementResult sr = session.run(query, parameters("movieId", movieId));
        List<Person> pe = convertToPerson(sr);
        session.close();
        return pe.get(0);
    }
}
