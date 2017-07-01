package neo4j.ir.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Ali Asghar on 30/06/2017.
 */
@Controller
@RequestMapping("/upload")
public class UploadController {

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "/multimedia";

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public ResponseEntity singleFileUpload(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.ok("file is empty");
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            return ResponseEntity.ok("uploaded successfully");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("uploaded successfully");
    }

}
