package neo4j.ir.web;

import neo4j.ir.Service.SecurityHelper;
import neo4j.ir.Service.UserService;
import neo4j.ir.nodes.RelationshipTypes;
import neo4j.ir.nodes.User;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        Node node = userService.getNode(username);

        for(Map.Entry<String,Object> entry : node.getAllProperties().entrySet())
            model.put(entry.getKey(),entry.getValue());

        return "index";
    }



}
