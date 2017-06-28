package neo4j.ir.Service;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by Amir Shams on 6/28/2017.
 */
@Controller
@RequestMapping("/graph")
public class GraphPrinter {

    @Autowired
    private GraphDatabaseService db;

    @GetMapping
    @ResponseBody
    public String getAllNodesAndRelationShips()
    {
        String result = "";

        Transaction tx = db.beginTx();

        for (Node temp : db.getAllNodes()) {
            result += (temp.toString() + "\r\n<br><br>\r\n");
            for(Relationship rel : temp.getRelationships())
                result += ("    " + rel.toString() + "\r\n<br><br>\r\n");

        }

        tx.success();
        tx.close();

        return result;
    }

    @GetMapping(value = "/node/{id}")
    @ResponseBody
    public String getNode(@PathVariable("id") long id)
    {
        String result = "";

        Transaction tx = db.beginTx();

        Node node = db.getNodeById(id);
        Map<String,Object> resultMap = node.getAllProperties();
        for(Map.Entry<String,Object> entry : resultMap.entrySet())
            result += (entry.getKey() + " : " + entry.getValue().toString() + "\r\n<br><br>\r\n");

        tx.success();
        tx.close();

        return result;
    }

    @GetMapping(value = "/relationship/{id}")
    @ResponseBody
    public String getEdge(@PathVariable("id") long id)
    {
        String result = "";

        Transaction tx = db.beginTx();

        Relationship relationship = db.getRelationshipById(id);
        Map<String,Object> resultMap = relationship.getAllProperties();
        for(Map.Entry<String,Object> entry : resultMap.entrySet())
            result += (entry.getKey() + " : " + entry.getValue().toString() + "\r\n<br><br>\r\n");

        tx.success();
        tx.close();

        return result;
    }

}
