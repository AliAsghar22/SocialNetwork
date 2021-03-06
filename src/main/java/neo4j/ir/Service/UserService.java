package neo4j.ir.Service;

import neo4j.ir.nodes.Movie;
import neo4j.ir.nodes.User;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Created by Ali Asghar on 28/06/2017.
 */

@Service
public class UserService {

    final Driver driver;
    @Autowired
    MovieService movieService;


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
                "userName:{userName}, password:{password}, age:{age}, male:{male}})";
        session.run(query, parameters("firstName", newUser.getFirstName(),
                "lastName", newUser.getLastName(),
                "userName", newUser.getUserName(),
                "password", newUser.getPassword(),
                "male", newUser.isMale(),
                "age", newUser.getAge()));
        session.close();
    }

    public void update(User newUser) {
        if (!exists(newUser.getUserName()))
            return;
        Session session = driver.session();
        String query = "MATCH (u:USER{userName:{userName}}) SET u.firstName={firstName}" +
                ",u.lastName={lastName},u.password={password},u.age={age},u.male={male}";
        session.run(query, parameters("userName", newUser.getUserName(),
                "firstName", newUser.getFirstName(),
                "lastName", newUser.getLastName(),
                "password", newUser.getPassword(),
                "male", newUser.isMale(),
                "age", newUser.getAge()));
        session.close();
    }

    public List<User> getAllUsers() {
        List<User> users;
        Session session = driver.session();
        StatementResult sr = session.run("MATCH (u:USER) return u.firstName as firstName" +
                ", u.lastName as lastName" +
                ", u.userName as userName" +
                ", u.password as password" +
                ", id(u) as id" +
                ", u.male as male" +
                ", u.age as age");
        users = convertToUser(sr);
        session.close();
        return users;
    }

    public List<User> convertToUser(StatementResult sr) {
        List<User> users = new ArrayList<>();
        while (sr.hasNext()) {
            Record r = sr.next();
            User u = new User();
            u.setFirstName(r.get("firstName").asString());
            u.setLastName(r.get("lastName").asString());
            u.setPassword(r.get("password").asString());
            u.setUserName(r.get("userName").asString());
            u.setId(r.get("id").asInt());
            u.setAge(r.get("age").asInt());
            u.setMale(r.get("male").asBoolean());
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
                        ", ID(u) as id" +
                        ", u.male as male" +
                        ", u.age as age"
                , parameters("userName", userName));

        List<User> u = convertToUser(sr);
        if(u == null || u.isEmpty())
            return null;

        User user = u.get(0);
        session.close();
        return user;
    }

    public List<User> getFriendsList(String currentUser) {
        List<User> friends;
        Session session = driver.session();
        StatementResult sr = session.run("MATCH (u1:USER {userName:{userName}})-[:IS_FRIEND]->(u:USER) " +
                        "return u.firstName as firstName" +
                        ", u.lastName as lastName" +
                        ", u.userName as userName" +
                        ", u.password as password" +
                        ", id(u) as id" +
                        ", u.male as male" +
                        ", u.age as age"
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

    public List<Movie> getRelatedMovies(String user) {
        List<Movie> films = new ArrayList<>();
        Session session = driver.session();
        String query = "MATCH (u:USER {userName:{userName}})-->(f:MOVIE) return f.title as title, ID(f) as id";
        StatementResult sr = session.run(query, parameters("userName", user));
        while (sr.hasNext()) {
            Record r = sr.next();
            Movie m = new Movie();
            m.setTitle(r.get("title").asString());
            m.setId(r.get("id").asInt());
        }
        session.close();
        return films;
    }

    public int getID(String userName) {
        return getUser(userName).getId();
    }

    public void addComment(String username, int id, String comment) {
        Session session = driver.session();
        String query = "MATCH (u:USER), (item) where ID(item) = {id} and u.userName = {userName} " +
                "create (u)-[:COMMENTED {comment:{comment}}]->(item)";
        session.run(query, parameters("userName", username, "id", id, "comment", comment));
        session.close();
    }

    public void addScore(String username, int id, float score) {
        Session session = driver.session();
        String query = "MATCH (u:USER), (item) where ID(item) = {id} and u.userName = {userName} " +
                "create UNIQUE (u)-[s:SCORED]->(item) set s.score = {score}";
        session.run(query, parameters("userName", username, "id", id, "score", score));
        session.close();
        movieService.calculateRate(id);
    }

//    public List<User> suggestFriend(String userName) {
//        Session session = driver.session();
//        String query = "MATCH (u:USER{userName:{userName}})-[]->(m:MOVIE),(n:User)" +
//                " WHERE not (u)-[]-(n)" +
//                " with n " +
//                " ";
//        session.run(query, parameters("userName", userName));
//        session.close();
//    }

    public List<User> suggestFriend(String userName) {
        Session session = driver.session();
        String query = "MATCH (u:USER{userName:{userName}})-[*]->(n:USER)" +
                " WHERE not (u)-[:IS_FRIEND]->(n)" +
                "return distinct n.userName as userName" +
                ", n.lastName as lastName" +
                ", n.firstName as firstName" +
                ", n.password as password" +
                ", id(n) as id" +
                ", n.male as male" +
                ", n.age as age";
        StatementResult sr = session.run(query, parameters("userName", userName));
        List<User> suggestions = convertToUser(sr);

        String query2 = "MATCH (u:USER)-[*]->(n:USER{userName:{userName}}) " +
                "return distinct u.userName as userName" +
                ", u.lastName as lastName" +
                ", u.firstName as firstName" +
                ", u.password as password" +
                ", id(u) as id" +
                ", u.male as male" +
                ", u.age as age";
        StatementResult sr2 = session.run(query2, parameters("userName", userName));
        List<User> suggestions2 = convertToUser(sr2);

        Set<User> result = new HashSet<>();
        result.addAll(suggestions);
        result.addAll(suggestions2);

        session.close();

        return new ArrayList<>(result);
    }
}
