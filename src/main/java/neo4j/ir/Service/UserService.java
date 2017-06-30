package neo4j.ir.Service;

import neo4j.ir.nodes.Film;
import neo4j.ir.nodes.User;
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
 * Created by Ali Asghar on 28/06/2017.
 */

@Service
public class UserService {

    final Driver driver;


    @Autowired
    public UserService(Driver driver) {
        this.driver = driver;
    }

    public boolean exists(String userName) {
        Session s = driver.session();
        String query = "MATCH (n:USER {userName:{userName}}) return n.userName";
        StatementResult sr = s.run(query, parameters("userName", userName));
        s.close();
        return sr.hasNext();
    }

    public void add(User newUser) {
        if (exists(newUser.getUserName()))
            return;
        Session session = driver.session();
        String query = "CREATE (u:USER {firstName:{firstName}, lastName:{lastName}, " +
                "userName:{userName}, password:{password}})";
        session.run(query, parameters("firstName", newUser.getFirstName(),
                "lastName", newUser.getLastName(),
                "userName", newUser.getUserName(),
                "password", newUser.getPassword()));
        session.close();
    }

    public void update(User newUser) {
        if (!exists(newUser.getUserName()))
            return;
        Session session = driver.session();
        String query = "MATCH (u:USER) WHERE u.userName = {userName} SET u.firstName = {firstName} " +
                ", u.lastName = {lastName}, u.password = {password}";
        session.run(query, parameters("userName", newUser.getUserName(),
                "firstName", newUser.getFirstName(),
                "lastName", newUser.getLastName(),
                "password", newUser.getPassword()));
        session.close();
    }

    public List<User> getAllUsers() {
        List<User> users;
        Session session = driver.session();
        StatementResult sr = session.run("MATCH (u:USER) return u.firstName as firstName" +
                ", u.lastName as lastName" +
                ", u.userName as userName" +
                ", u.password as password" +
                ", id(u) as id");
        users = convertToUser(sr);
        session.close();
        return users;
    }

    public List<User> convertToUser(StatementResult sr) {
        List<User> users = new ArrayList<>();
        while (sr.hasNext()) {
            Record r = sr.next();
            User u = new User(r.get("firstName").asString(),
                    r.get("lastName").asString(),
                    r.get("userName").asString(),
                    r.get("password").asString(),
                    r.get("id").asInt());
            users.add(u);
        }
        return users;
    }

    public User getUser(String userName) {
        Session session = driver.session();
        StatementResult sr = session.run("MATCH (u:USER) WHERE u.userName = {userName} " +
                        "return u.firstName as firstName" +
                        ", u.lastName as lastName" +
                        ", u.userName as userName" +
                        ", u.password as password" +
                        ", ID(u) as id"
                , parameters("userName", userName));
        User u = convertToUser(sr).get(0);
        session.close();
        return u;
    }

    public List<User> getFriendsList(String currentUser) {
        List<User> friends;
        Session session = driver.session();
        StatementResult sr = session.run("MATCH (u1:USER {userName:{userName}})-[:IS_FRIEND]->(u:USER) " +
                        "return u.firstName as firstName" +
                        ", u.lastName as lastName" +
                        ", u.userName as userName" +
                        ", u.password as password" +
                        ", id(u) as id"
                , parameters("userName", currentUser));
        friends = convertToUser(sr);
        session.close();
        return friends;
    }

    public void createRelationToUser(String currentUser, String userName) {
        Session session = driver.session();
        String query = "MATCH (u1:USER), (u2:USER) " +
                "WHERE u1.userName = {userName1}" +
                "and u2.userName = {userName2} " +
                "CREATE (u1)-[:IS_FRIEND]->(u2)";
        session.run(query, parameters("userName1", currentUser, "userName2", userName));
        session.close();
    }

    public List<Film> getRelatedFilms(String user) {
        List<Film> films = new ArrayList<>();
        Session session = driver.session();
        String query = "MATCH (u:USER {userName:{userName}})-->(f:FILM) return f.name as name";
        StatementResult sr = session.run(query, parameters("userName", user));
        while (sr.hasNext()) {
            Record r = sr.next();
            films.add(new Film(r.get("name").asString()));
        }
        session.close();

        return films;
    }

    public int getID(String userName){
        return getUser(userName).getId();
    }

    public void addComment(String username, int id, String comment, int score) {
        Session session = driver.session();
        String query = "MATCH (u:User {userName:{userName}}), (i) where id(i) = {id} " +
                "create (u)-[:COMMENTED {comment:{comment}, score:{score}}]->(i)";
        session.run(query, parameters("userName",username, "id", id, "comment", comment, "score", score));
        session.close();
    }
}
