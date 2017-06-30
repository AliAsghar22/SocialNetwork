package neo4j.ir.web;

import neo4j.ir.web.dto.MovieDTO;
import neo4j.ir.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Ali Asghar on 29/06/2017.
 */
@Controller
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    MovieService movieService;

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody MovieDTO dto) {
        movieService.add(dto);
        return ResponseEntity.ok("");
    }

    @GetMapping("/getByTitle")
    public ResponseEntity getByTitle(@RequestParam("title") String title){
        return ResponseEntity.ok(movieService.getByTitle(title));
    }

    @GetMapping("/all")
    public ResponseEntity getAll(){
        return ResponseEntity.ok(movieService.getAll());
    }
}
