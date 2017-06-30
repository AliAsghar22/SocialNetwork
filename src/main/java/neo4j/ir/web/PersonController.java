package neo4j.ir.web;

import neo4j.ir.Service.PersonService;
import neo4j.ir.nodes.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by Ali Asghar on 29/06/2017.
 */
@Controller
@RequestMapping("/person")
public class PersonController {
    @Autowired PersonService personService;

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody Person p){
        personService.add(p);
        return ResponseEntity.ok().body("");
    }


    @GetMapping("/getByName")
    public ResponseEntity getByName(@RequestParam("name") String name){
        return  ResponseEntity.ok().body(personService.getByName(name));
    }

    @GetMapping("/all")
    public ResponseEntity getAll(){
        return ResponseEntity.ok(personService.getAll());
    }

}
