package neo4j.ir.web;

import neo4j.ir.Service.SecurityHelper;
import neo4j.ir.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * Created by Ali Asghar on 28/06/2017.
 */
@Controller
public class MainController {

    @Autowired
    private UserService userService;
    @Autowired
    private SecurityHelper securityHelper;

    @GetMapping("/")
    public String welcome(Map<String,Object> model){

        String username = securityHelper.getCurrentUserUserName();
//        Node node = userService.getNode(username);
//
//        for(Map.Entry<String,Object> entry : node.getAllProperties().entrySet())
//            model.put(entry.getKey(),entry.getValue());

        return "index";
    }



}
