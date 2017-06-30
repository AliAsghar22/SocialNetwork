package neo4j.ir;

import neo4j.ir.Service.UserService;
import neo4j.ir.nodes.User;
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


    @Autowired
    public InitData(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            User u1 = new User();
            u1.setFirstName("ali");
            u1.setLastName("taghi");
            u1.setPassword("123");
            u1.setUserName("ali");

            User u2 = new User();
            u2.setFirstName("ali");
            u2.setLastName("taghi");
            u2.setPassword("123");
            u2.setUserName("ali2");

            userService.add(u1);
            userService.add(u2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
