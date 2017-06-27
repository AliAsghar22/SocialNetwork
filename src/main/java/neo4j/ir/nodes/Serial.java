package neo4j.ir.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

/**
 * Created by Ali Asghar on 27/06/2017.
 */
public class Serial {

    @JsonProperty
    private String name;

    @JsonProperty
    private String type;

    @JsonIgnore
    private Node node;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean save(GraphDatabaseService db) {
        try (Transaction tx = db.beginTx()) {
            Node n = loadByName(this.getName(), db).getNode();
            if (n == null) {
                n = db.createNode();
            }
            n.setProperty("name", getName());
            n.setProperty("type", Types.SERIAL);
            n.addLabel(Types.SERIAL);
            tx.success();
            tx.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Serial loadByName(String name, GraphDatabaseService db) {
        try (Transaction tx = db.beginTx()) {
            Node me = db.findNode(Types.SERIAL, "name", name);
            Serial s = new Serial();
            s.setName((String) me.getProperty("name"));
            s.setNode(me);
            tx.success();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
