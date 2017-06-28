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

    public Node add(User newUser) {
        try (Transaction tx = db.beginTx()) {
            if(getNode(newUser.getUserName()) != null){
                throw new Exception("This user already exits");
            }
            Node node = db.createNode();
            convertToNode(node, newUser);
            tx.success();
            tx.close();
            return node;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User update(User newUser){
        try (Transaction tx = db.beginTx()) {
            //check to see if user already exists
            Node oldNode = getNode(newUser.getUserName());
            if(oldNode == null)
                throw new Exception("This user doesn't exits");
            convertToNode(oldNode, newUser);
            tx.success();
            tx.close();
            return newUser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers(){
        try (Transaction tx = db.beginTx()) {
            ResourceIterator<Node> nodes = db.findNodes(Types.USER);
            List<User> users = new ArrayList<>();
            while (nodes.hasNext()){
                Node n = nodes.next();
                users.add(convertToUser(n));
            }
            tx.success();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Node getNode(String userName){
        try (Transaction tx = db.beginTx()) {
            Node me = db.findNode(Types.USER, "userName", userName);
            System.out.println(me);
            System.out.println(userName);
            tx.success();
            return me;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private User convertToUser(Node n){
        Transaction tx = db.beginTx();
        User user = new User();
        user.setFirstName((String)n.getProperty("firstName"));
        user.setLastName((String) n.getProperty("lastName"));
        user.setUserName((String) n.getProperty("userName"));
        user.setPassword(((String) n.getProperty("password")));
        tx.success();
        return user;
    }

    private void convertToNode(Node n, User u){
        n.setProperty("userName", u.getUserName());
        n.setProperty("password", u.getPassword());
        n.setProperty("firstName", u.getFirstName());
        n.setProperty("lastName", u.getLastName());
        n.setProperty("type", Types.USER.name());
        n.addLabel(Types.USER);
    }

    public User getUser(String userName){
        Node n = getNode(userName);
        if(n != null){
            return convertToUser(n);
        }
        return null;
    }

    public List<User> getFriendsList(String currentUser) {
        Transaction tx = db.beginTx();
        Node current = getNode(currentUser);
        List<User> friends = new ArrayList<>();
        for (Relationship r: current.getRelationships(RelationshipTypes.IS_FRIEND)) {
            friends.add(convertToUser(r.getOtherNode(current)));
        }
        tx.success();
        return friends;
    }

    public void createRelationToUser(String currentUser, String userName) {
        Transaction tx = db.beginTx();
        Node current = getNode(currentUser);
        Node friend = getNode(userName);
        current.createRelationshipTo(friend, RelationshipTypes.IS_FRIEND);
        tx.success();
        tx.close();
    }
}
