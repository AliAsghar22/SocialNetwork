package neo4j.ir.web;


import neo4j.ir.Service.GenreService;
import neo4j.ir.Service.KeywordService;
import neo4j.ir.Service.MovieService;
import neo4j.ir.Service.PersonService;
import neo4j.ir.Service.*;
import neo4j.ir.nodes.Movie;
import neo4j.ir.web.dto.MovieDTO;
import neo4j.ir.web.dto.MovieSearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Autowired
    SecurityHelper securityHelper;

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

    @GetMapping(value = "/{movieId}")
    public String getDetail(@PathVariable("movieId") String movieId,Map<String,Object> model){

        String userName = securityHelper.getCurrentUserUserName();

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setMovie(movieService.getByTitle(movieId));

        String comment = movieService.getComment(userName,movieDTO.getMovie().getId());

        Double score = movieService.getScore(userName,movieDTO.getMovie().getId());

        model.put("commented",comment!=null);
        model.put("yourComment",comment);
        model.put("scored",score!=null);
        model.put("yourScore",score);
        model.put("movie",movieDTO.getMovie());

        return "MovieDetail";
    }

    @PostMapping("/search")
    public ResponseEntity search(@RequestBody MovieSearchDTO dto){
        return ResponseEntity.ok(movieService.search(dto));
    }

    @GetMapping("/suggestByGenre")
    public ResponseEntity suggestByGenre(){
        return ResponseEntity.ok(movieService.suggestByGenre(securityHelper.getCurrentUserUserName()));
    }

    @GetMapping("/suggestByOtherPeople")
    public ResponseEntity suggestByOtherPeople(){
        return ResponseEntity.ok(movieService.suggestByOtherPeople(securityHelper.getCurrentUserUserName()));
    }

    @GetMapping("/list")
    public String list(Map<String,Object> model,
     @RequestParam(value="title", required=false) String title,
     @RequestParam(value="year", required = false) String year,
     @RequestParam(value="actor", required=false) String actor,
     @RequestParam(value="genre", required = false) String genre,
     @RequestParam(value="director", required=false) String direcor,
     @RequestParam(value="writer", required = false) String writer)
    {

        MovieSearchDTO dto = new MovieSearchDTO();
        dto.setTitle(title);
        dto.setWriterName(writer);
        dto.setDirectorName(direcor);
        dto.setProductionYear(year);
        List<String> actors = new ArrayList<>();
        List<String> genres = new ArrayList<>();
        actors.add(actor);
        genres.add(genre);
        dto.setActorNames(actors);
        dto.setGenres(genres);

        model.put("searchResult",movieService.search(dto));
        model.put("top5",movieService.getTop5());
        model.put("recent",movieService.getLatest());

        return "Movies";
    }

}
