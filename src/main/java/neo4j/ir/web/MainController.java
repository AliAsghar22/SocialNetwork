package neo4j.ir.web;

import neo4j.ir.Service.MovieService;
import neo4j.ir.Service.SecurityHelper;
import neo4j.ir.Service.UserService;
import neo4j.ir.nodes.Movie;
import neo4j.ir.nodes.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

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
        List<User> users = userService.suggestFriend(currentUser.getUserName());

        Set<Movie> movieSet = new HashSet<>();
        movieSet.addAll(movieService.suggestByGenre(currentUser.getUserName()));
        movieSet.addAll(movieService.suggestByOtherPeople(currentUser.getUserName()));

        List<Movie> seenItems = movieService.getSeenMovies(currentUser.getUserName());

        users.remove(currentUser);
        movieSet.removeAll(seenItems);

        model.put("user",currentUser);
        model.put("gender",currentUser.isMale()?"male":"female");

        model.put("seenItems",seenItems);
        model.put("items",movieSet);
        model.put("friends",friends);
        model.put("users",users);

        return "index";
    }



}
