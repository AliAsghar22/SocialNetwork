package neo4j.ir.Service;

import neo4j.ir.nodes.RelationshipTypes;
import neo4j.ir.nodes.Types;
import neo4j.ir.nodes.User;
import org.neo4j.graphdb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali Asghar on 28/06/2017.
 */
@Service
public class UserService {

    @Autowired
    GraphDatabaseService db;

    public Node add(User newUser) throws Exception {
        if (getNode(newUser.getUserName()) != null) {
            throw new Exception("This user already exits");
        }
        Node node = db.createNode();
        convertToNode(node, newUser);
        return node;
    }

    public User update(User newUser) throws Exception {
        Node oldNode = getNode(newUser.getUserName());
        if (oldNode == null)
            throw new Exception("This user doesn't exits");
        convertToNode(oldNode, newUser);
        return newUser;
    }

    public List<User> getAllUsers() {
        ResourceIterator<Node> nodes = db.findNodes(Types.USER);
        List<User> users = new ArrayList<>();
        while (nodes.hasNext()) {
            Node n = nodes.next();
            users.add(convertToUser(n));
        }

        return users;
    }

    public Node getNode(String userName) {
        return db.findNode(Types.USER, "userName", userName);
    }

    private User convertToUser(Node n) {
        User user = new User();
        user.setFirstName((String) n.getProperty("firstName"));
        user.setLastName((String) n.getProperty("lastName"));
        user.setUserName((String) n.getProperty("userName"));
        user.setPassword(((String) n.getProperty("password")));
        return user;
    }

    private void convertToNode(Node n, User u) {
        n.setProperty("userName", u.getUserName());
        n.setProperty("password", u.getPassword());
        n.setProperty("firstName", u.getFirstName());
        n.setProperty("lastName", u.getLastName());
        n.setProperty("type", Types.USER.name());
        n.addLabel(Types.USER);
    }

    public User getUser(String userName) {
        Transaction tx = db.beginTx();
        Node n = getNode(userName);
        if (n != null) {
            User u = convertToUser(n);
            tx.success();
            return u;
        }
        tx.success();
        return null;
    }

    public List<User> getFriendsList(String currentUser) {
        Node current = getNode(currentUser);
        List<User> friends = new ArrayList<>();
        for (Relationship r : current.getRelationships(RelationshipTypes.IS_FRIEND)) {
            friends.add(convertToUser(r.getOtherNode(current)));
        }
        return friends;
    }

    public void createRelationToUser(String currentUser, String userName) {
        Node current = getNode(currentUser);
        Node friend = getNode(userName);
        current.createRelationshipTo(friend, RelationshipTypes.IS_FRIEND);
    }
}
