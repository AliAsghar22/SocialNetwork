package neo4j.ir;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static GraphDatabaseService db;
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}
}
