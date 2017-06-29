package neo4j.ir.web;

import neo4j.ir.Service.SecurityHelper;
import neo4j.ir.Service.UserService;
import neo4j.ir.nodes.Film;
import neo4j.ir.nodes.User;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Ali Asghar on 28/06/2017.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    GraphDatabaseService db;

    @Autowired
    UserService userService;

    @Autowired
    SecurityHelper securityHelper;

    @PostMapping("/edit")
    public ResponseEntity editUser(@RequestBody User user) {
        Transaction tx = db.beginTx();
        try{
            userService.update(user);
            tx.success();
            tx.close();
            return ResponseEntity.ok("user edited successfully");
        }catch (Exception e){
            tx.success();
            tx.close();
            return ResponseEntity.badRequest().body("couldn't edit user");
        }

    }

    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody User user) {
        Transaction tx = db.beginTx();
        try{
            userService.add(user);
            tx.success();
            tx.close();
            return ResponseEntity.ok("user added successfully");
        }catch (Exception e){
            tx.success();
            tx.close();
            return ResponseEntity.badRequest().body("couldn't add user");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List> getOfUsers(){
        Transaction tx = db.beginTx();
        List<User> users = userService.getAllUsers();
        tx.success();
        tx.close();
        return ResponseEntity.ok(users);

    }

    @GetMapping("/addFriendship")
    public ResponseEntity addRelation(@RequestParam("userName") String userName){
        Transaction tx = db.beginTx();
        String currentUser = securityHelper.getCurrentUserUserName();
        userService.createRelationToUser(currentUser, userName);
        tx.success();
        tx.close();
        return ResponseEntity.ok("created relation");
    }

    @GetMapping("/friendList")
    public ResponseEntity getFriendList(){
        Transaction tx = db.beginTx();
        String currentUser = securityHelper.getCurrentUserUserName();
        List<User> friends = userService.getFriendsList(currentUser);
        tx.success();
        tx.close();
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/page")
    public ResponseEntity getUserPage(){
        Transaction tx = db.beginTx();
        List<Film> films = userService.getRelatedFilms(securityHelper.getCurrentUserUserName());
        tx.success();
        tx.close();
        return ResponseEntity.ok(films);
    }

}
