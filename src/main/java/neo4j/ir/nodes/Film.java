package neo4j.ir.nodes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

/**
 * Created by Ali Asghar on 27/06/2017.
 */
public class Film {
    @JsonProperty
    private String name;

    @JsonProperty
    private String type;

    @JsonIgnore
    private Node node;


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

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Node save(GraphDatabaseService db){
        try(Transaction tx = db.beginTx()){
            Node n = loadByName(this.getName(), db).getNode();
            if (n == null) {
                n = db.createNode();
            }
            n.setProperty("name", getName());
            n.setProperty("type", Types.FILM);
            n.addLabel(Types.FILM);
            tx.success();
            tx.close();
            return n;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Film loadByName(String name, GraphDatabaseService db){
        try (Transaction tx = db.beginTx()){
            Node me = db.findNode(Types.FILM, "name", name);
            Film f = new Film();
            f.setNode(me);
            f.setName((String)me.getProperty("name"));
            tx.success();
            return f;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
