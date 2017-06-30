package neo4j.ir.web;

import neo4j.ir.Service.MovieService;
import neo4j.ir.Service.SecurityHelper;
import neo4j.ir.Service.UserService;
import neo4j.ir.nodes.Movie;
import neo4j.ir.nodes.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ali Asghar on 28/06/2017.
 */
@Controller
public class MainController {

    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private MovieService movieService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String welcome(Map<String,Object> model){

        User currentUser = securityHelper.getCurrentUser();

        List<User> friends = userService.getFriendsList(currentUser.getUserName());
        List<User> users = userService.getAllUsers();
        List<Movie> items = movieService.getAll();
        List<Movie> seenItems = movieService.getSeenMovies(currentUser.getUserName());

        users.removeAll(friends);
        users.remove(currentUser);
        items.removeAll(seenItems);

        model.put("user",currentUser);
        model.put("gender",currentUser.isMale()?"male":"female");

        model.put("seenItems",seenItems);
        model.put("items",items);
        model.put("friends",friends);
        model.put("users",users);

        return "index";
    }



}
