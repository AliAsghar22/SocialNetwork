package neo4j.ir.Service;

import neo4j.ir.nodes.RelationshipTypes;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Amir Shams on 6/28/2017.
 */
@Service
public class CommentService {

    @Autowired
    private GraphDatabaseService db;
    @Autowired
    private UserService userService;
    @Autowired
    private FilmService filmService;

    public void addComment(String username, String item, String comment)
    {
        Transaction tx = db.beginTx();
        Node user = userService.getNode(username);
        Node film = filmService.getNode(item);
        Relationship rel = user.createRelationshipTo(film,RelationshipTypes.COMMENTED);
        rel.setProperty("body",comment);
        tx.success();
        tx.close();
    }
}
