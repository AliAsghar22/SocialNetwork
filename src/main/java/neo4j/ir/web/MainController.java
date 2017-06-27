package neo4j.ir.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Ali Asghar on 28/06/2017.
 */
@Controller
public class MainController {

    @GetMapping("/")
    public String index(){
        return "index";
    }
}
