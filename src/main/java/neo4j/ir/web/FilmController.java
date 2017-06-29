package neo4j.ir.web;

import neo4j.ir.Service.FilmService;
import neo4j.ir.nodes.Film;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by Ali Asghar on 29/06/2017.
 */
@Controller
@RequestMapping("/film")
public class FilmController {
    @Autowired
    FilmService filmService;
    @Autowired
    GraphDatabaseService db;

    @PostMapping("/edit")
    public ResponseEntity editFilm(@RequestBody Film film) {
        Transaction tx = db.beginTx();
        try{
            filmService.update(film);
            tx.success();
            tx.close();
            return ResponseEntity.ok("film edited successfully");
        }catch (Exception e){
            tx.success();
            tx.close();
            return ResponseEntity.badRequest().body("couldn't edit film");
        }

    }

    @PostMapping("/add")
    public ResponseEntity addFilm(@RequestBody Film film) {
        Transaction tx = db.beginTx();
        try{
            filmService.add(film);
            tx.success();
            tx.close();
            return ResponseEntity.ok("film added successfully");
        }catch (Exception e){
            tx.success();
            tx.close();
            return ResponseEntity.badRequest().body("couldn't add film");
        }
    }

    @GetMapping("/list")
    public ResponseEntity getOfUsers(){
        Transaction tx = db.beginTx();
        List<Film> users = filmService.getAllFilms();
        tx.success();
        tx.close();
        return ResponseEntity.ok(users);

    }

}
