package neo4j.ir.Service;

import neo4j.ir.nodes.Film;
import neo4j.ir.nodes.Types;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amir Shams on 6/28/2017.
 */
@Service
public class FilmService {

    @Autowired
    GraphDatabaseService db;

    public Node add(Film film) throws Exception {
        if (getNode(film.getName()) != null) {
            throw new Exception("This user already exits");
        }
        Node node = db.createNode();
        convertToNode(node, film);
        return node;
    }

    public Node update(Film film) throws Exception {
        Node oldNode = getNode(film.getName());
        if (oldNode == null)
            throw new Exception("This user doesn't exits");
        convertToNode(oldNode, film);
        return oldNode;
    }

    //
    public List<Film> getAllFilms() {
        ResourceIterator<Node> nodes = db.findNodes(Types.FILM);
        List<Film> films = new ArrayList<>();
        while (nodes.hasNext()) {
            Node n = nodes.next();
            films.add(convertToFilm(n));
        }

        return films;
    }

    public Node getNode(String name) {
        return db.findNode(Types.FILM, "name", name);
    }

    public Film convertToFilm(Node n){
        Film f = new Film();
        f.setName((String)n.getProperty("name"));
        f.setType(Types.FILM.name());
        return f;
    }

    public void convertToNode(Node n, Film f){
        n.setProperty("name", f.getName());
        n.setProperty("type", Types.FILM.name());
        n.addLabel(Types.FILM);
    }

    public Film getFilm(String name){
        Node n = getNode(name);
        if(n != null){
            return convertToFilm(n);
        }
        return null;
    }
}
