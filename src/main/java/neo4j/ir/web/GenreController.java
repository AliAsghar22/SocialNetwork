package neo4j.ir.web;

import neo4j.ir.Service.GenreService;
import neo4j.ir.nodes.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Ali Asghar on 29/06/2017.
 */
@Controller
@RequestMapping("/genre")
public class GenreController {
    @Autowired GenreService genreService;

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody Genre g){
        genreService.add(g);
        return ResponseEntity.ok("");
    }

    @GetMapping("/getByName")
    public ResponseEntity getByName(String name){
        return ResponseEntity.ok(genreService.getByName(name));
    }

    @GetMapping("/all")
    public ResponseEntity getAll(){
        return ResponseEntity.ok(genreService.getAll());
    }
}
