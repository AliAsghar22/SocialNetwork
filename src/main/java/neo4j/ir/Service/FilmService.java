package neo4j.ir.Service;

import neo4j.ir.nodes.Types;
import neo4j.ir.nodes.User;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amir Shams on 6/28/2017.
 */
@Service
public class FilmService {
    @Autowired
    GraphDatabaseService db;

//    public Node add(User newUser) {
//        try (Transaction tx = db.beginTx()) {
//            if(getNode(newUser.getUserName()) != null){
//                throw new Exception("This user already exits");
//            }
//            Node node = db.createNode();
//            convertToNode(node, newUser);
//            tx.success();
//            tx.close();
//            return node;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public User update(User newUser){
//        try (Transaction tx = db.beginTx()) {
//            //check to see if user already exists
//            Node oldNode = getNode(newUser.getUserName());
//            if(oldNode == null)
//                throw new Exception("This user doesn't exits");
//            convertToNode(oldNode, newUser);
//            tx.success();
//            tx.close();
//            return newUser;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public List<User> getAllUsers(){
//        try (Transaction tx = db.beginTx()) {
//            ResourceIterator<Node> nodes = db.findNodes(Types.USER);
//            List<User> users = new ArrayList<>();
//            while (nodes.hasNext()){
//                Node n = nodes.next();
//                users.add(convertToUser(n));
//            }
//            tx.success();
//            return users;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public Node getNode(String title){
        try (Transaction tx = db.beginTx()) {
            Node me = db.getNodeById(Long.valueOf(title));
            tx.success();
            return me;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public User convertToUser(Node n){
//        User user = new User();
//        user.setFirstName((String)n.getProperty("firstName"));
//        user.setLastName((String) n.getProperty("lastName"));
//        user.setUserName((String) n.getProperty("userName"));
//        user.setPassword(((String) n.getProperty("password")));
//        return user;
//    }
//
//    public void convertToNode(Node n, User u){
//        n.setProperty("userName", u.getUserName());
//        n.setProperty("password", u.getPassword());
//        n.setProperty("firstName", u.getFirstName());
//        n.setProperty("lastName", u.getLastName());
//        n.setProperty("type", Types.USER.name());
//        n.addLabel(Types.USER);
//    }
//
//    public User getUser(String userName){
//        Node n = getNode(userName);
//        if(n != null){
//            return convertToUser(n);
//        }
//        return null;
//    }
}
