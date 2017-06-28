package neo4j.ir.web;

import neo4j.ir.Service.CommentService;
import neo4j.ir.Service.SecurityHelper;
import neo4j.ir.Service.UserService;
import neo4j.ir.nodes.RelationshipTypes;
import neo4j.ir.nodes.User;
import neo4j.ir.web.dto.CommentDTO;
import org.neo4j.graphdb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Amir Shams on 6/28/2017.
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @ResponseBody
    public void addComment(@RequestBody CommentDTO dto)
    {
        String username = securityHelper.getCurrentUserUserName();

        commentService.addComment(username,dto.getItemId(),dto.getBody());
    }

    @Autowired
    private GraphDatabaseService db;

}
