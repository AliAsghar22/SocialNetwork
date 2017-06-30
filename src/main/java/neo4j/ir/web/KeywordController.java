package neo4j.ir.web;

import neo4j.ir.Service.KeywordService;
import neo4j.ir.nodes.Keyword;
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
@RequestMapping("/keyword")
public class KeywordController {
    @Autowired
    KeywordService keywordService;

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody Keyword g){
        keywordService.add(g);
        return ResponseEntity.ok("");
    }

    @GetMapping("/getByName")
    public ResponseEntity getByName(String name){
        return ResponseEntity.ok(keywordService.getByName(name));
    }

    @GetMapping("/all")
    public ResponseEntity getAll(){
        return ResponseEntity.ok(keywordService.getAll());
    }

}
