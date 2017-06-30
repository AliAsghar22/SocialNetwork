package neo4j.ir;

import neo4j.ir.Service.MovieService;
import neo4j.ir.Service.UserService;
import neo4j.ir.nodes.Movie;
import neo4j.ir.nodes.User;
import neo4j.ir.web.dto.MovieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by Ali Asghar on 29/06/2017.
 */
@Component
public class InitData implements ApplicationListener<ContextRefreshedEvent> {

    UserService userService;
    MovieService movieService;


    @Autowired
    public InitData(UserService userService,MovieService movieService) {
        this.userService = userService;this.movieService = movieService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            User u1 = new User();
            u1.setFirstName("ali");
            u1.setLastName("taghi");
            u1.setPassword("123");
            u1.setUserName("ali");
            u1.setAge(21);
            u1.setMale(true);

            User u2 = new User();
            u2.setFirstName("ali");
            u2.setLastName("taghi");
            u2.setPassword("123");
            u2.setUserName("ali2");
            u2.setAge(21);
            u2.setMale(true);

            User u3 = new User();
            u3.setUserName("admin");
            u3.setPassword("admin");
            u3.setFirstName("Admin");
            u3.setLastName("AdminPoor");
            u3.setAge(21);
            u3.setMale(true);

            Movie m1 = new Movie();
            MovieDTO dto1 = new MovieDTO();
            dto1.setMovie(m1);
            m1.setTitle("Interstellar");

            Movie m2 = new Movie();
            MovieDTO dto2 = new MovieDTO();
            dto2.setMovie(m2);
            m2.setTitle("Inception");

            userService.add(u1);
            userService.add(u2);
            userService.add(u3);

            movieService.add(dto1);
            movieService.add(dto2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
