package neo4j.ir.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

/**
 * Created by Ali Asghar on 27/06/2017.
 */
public class User {

    @JsonProperty
    private String userName;

    @JsonProperty
    private String password;

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String lastName;

    @JsonIgnore
    private Node node;

    public User(String userName, String password, String firstName, String lastName) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public boolean save(GraphDatabaseService db) {
        try (Transaction tx = db.beginTx()) {
            Node n = loadByUserName(this.getUserName(), db).getNode();
            if (n == null) {
                n = db.createNode();
            }
            n.setProperty("userName", getUserName());
            n.setProperty("password", getPassword());
            n.setProperty("firstName", getFirstName());
            n.setProperty("lastName", getLastName());
            n.setProperty("type", Types.USER);
            n.addLabel(Types.USER);
            tx.success();
            tx.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static User loadByUserName(String userName, GraphDatabaseService db) {
        try (Transaction tx = db.beginTx()) {
            Node me = db.findNode(Types.USER, "userName", userName);
            User u = new User((String) me.getProperty("userName"),
                    (String) me.getProperty("password"),
                    (String) me.getProperty("firstName"),
                    (String) me.getProperty("lastName"));
            u.setNode(me);
            tx.success();
            return u;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
