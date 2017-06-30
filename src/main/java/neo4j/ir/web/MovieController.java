package neo4j.ir.web;

import neo4j.ir.Service.GenreService;
import neo4j.ir.Service.KeywordService;
import neo4j.ir.Service.MovieService;
import neo4j.ir.Service.PersonService;
import neo4j.ir.web.dto.MovieDTO;
import neo4j.ir.web.dto.MovieSearchDTO;
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

    @Autowired
    PersonService personService;

    @Autowired
    KeywordService keywordService;

    @Autowired
    GenreService genreService;

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody MovieDTO dto) {
        movieService.add(dto);
        return ResponseEntity.ok("added");
    }

    @GetMapping("/getByTitle")
    public ResponseEntity getByTitle(@RequestParam("title") String title){
        return ResponseEntity.ok(movieService.getByTitle(title));
    }

    @GetMapping("/all")
    public ResponseEntity getAll(){
        return ResponseEntity.ok(movieService.getAll());
    }

    @GetMapping("/top5")
    public ResponseEntity getTop5(){
        return ResponseEntity.ok(movieService.getTop5());
    }

    @GetMapping("/latest")
    public ResponseEntity getLatest(){
        return ResponseEntity.ok(movieService.getLatest());
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody MovieDTO dto){
        movieService.update(dto);
        return ResponseEntity.ok("updated");
    }

    @GetMapping("/detail")
    public ResponseEntity getDetail(@RequestParam("movieId") int movieId){
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setMovie(movieService.getById(movieId));
        movieDTO.setDirector(personService.getMovieDirector(movieId));
        movieDTO.setProducer(personService.getMovieProducer(movieId));
        movieDTO.setWriter(personService.getMovieWriter(movieId));
        movieDTO.setActors(personService.getMovieActors(movieId));
        movieDTO.setGenres(genreService.getMovieGenres(movieId));
        movieDTO.setKeywords(keywordService.getMovieKeywords(movieId));

        return ResponseEntity.ok(movieDTO);
    }

    @PostMapping("/search")
    public ResponseEntity search(@RequestBody MovieSearchDTO dto){
        return ResponseEntity.ok(movieService.search(dto));
    }
}
