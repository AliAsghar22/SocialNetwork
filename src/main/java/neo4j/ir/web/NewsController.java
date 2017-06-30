package neo4j.ir.web;

import neo4j.ir.Service.NewsService;
import neo4j.ir.nodes.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Ali Asghar on 30/06/2017.
 */
@Controller
@RequestMapping("/news")
public class NewsController {
    @Autowired
    NewsService newsService;

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody News g){
        newsService.add(g);
        return ResponseEntity.ok("");
    }

    @GetMapping("/all")
    public ResponseEntity getAll(){
        return ResponseEntity.ok(newsService.getAll());
    }
}
