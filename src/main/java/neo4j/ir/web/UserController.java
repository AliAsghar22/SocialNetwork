package neo4j.ir.web;

import neo4j.ir.Service.MovieService;
import neo4j.ir.Service.SecurityHelper;
import neo4j.ir.Service.UserService;
import neo4j.ir.nodes.Movie;
import neo4j.ir.nodes.User;
import neo4j.ir.web.dto.CommentDTO;
import neo4j.ir.web.dto.ScoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Ali Asghar on 28/06/2017.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private MovieService movieService;


    @PostMapping("/edit")
    public ResponseEntity editUser(@RequestBody User user) {
        user.setUserName(securityHelper.getCurrentUserUserName());
        userService.update(user);
        return ResponseEntity.ok().body("hello");
    }

    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody User user) {
        userService.add(user);
        return ResponseEntity.ok().body("hello");
    }

    @GetMapping("/list")
    public ResponseEntity<List> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);

    }

    @PostMapping("/addFriendship")
    public ResponseEntity addRelation(@RequestBody String userName){
        String currentUser = securityHelper.getCurrentUserUserName();
        userService.createRelationToUser(currentUser, userName);
        return ResponseEntity.ok("created relation");
    }

    @GetMapping("/friendList")
    public ResponseEntity getFriendList(){
        String currentUser = securityHelper.getCurrentUserUserName();
        List<User> friends = userService.getFriendsList(currentUser);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/page")
    public ResponseEntity getUserPage(){
        List<Movie> films = userService.getRelatedMovies(securityHelper.getCurrentUserUserName());
        return ResponseEntity.ok(films);
    }

    @PostMapping("/addComment")
    public ResponseEntity addComment(@RequestBody CommentDTO commentDTO) {
        String username = securityHelper.getCurrentUserUserName();
        userService.addComment(username,commentDTO.getItemId(), commentDTO.getBody());
        return ResponseEntity.ok("added");
    }

    @PostMapping("/addScore")
    public ResponseEntity addScore(@RequestBody ScoreDTO scoreDTO) {
        String username = securityHelper.getCurrentUserUserName();
        userService.addScore(username, scoreDTO.getId(), scoreDTO.getScore());
        return ResponseEntity.ok("added");
    }

    @GetMapping(value = "/{username}")
    public String getDetail(@PathVariable("username") String username,Map<String,Object> model){

        User user = userService.getUser(username);
        List<User> friends = userService.getFriendsList(securityHelper.getCurrentUserUserName());
        boolean isFriend = false;

        if(friends.contains(user))
            isFriend = true;

        List<User> profileOwnerFriends = userService.getFriendsList(username);
        List<Movie> seenMovies = movieService.getSeenMovies(username);

        model.put("seenItems",seenMovies);
        model.put("friends",profileOwnerFriends);
        model.put("user",user);
        model.put("gender",user.isMale()?"male":"female");
        model.put("isFriend",isFriend);

        return "Profile";
    }
}
