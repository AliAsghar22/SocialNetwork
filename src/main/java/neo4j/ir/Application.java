package neo4j.ir;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

//		Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "neo" ) );
//		Session session = driver.session();
//
//		session.run( "CREATE (a:Person {name: {name}, title: {title}})",
//				parameters( "name", "Arthur", "title", "King" ) );
//
//		StatementResult result = session.run( "MATCH (a:Person) WHERE a.name = {name} " +
//						"RETURN a.name AS name, a.title AS title",
//				parameters( "name", "Arthur" ) );
//		while ( result.hasNext() )
//		{
//			Record record = result.next();
//			System.out.println( record.get( "title" ).asString() + " " + record.get( "name" ).asString() );
//		}
//		session.close();
//		driver.close();
	}



	public static void registerShutdownHook( final GraphDatabaseService graphDb )
	{
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		Runtime.getRuntime().addShutdownHook( new Thread()
		{
			@Override
			public void run()
			{
				graphDb.shutdown();
			}
		} );
	}

}
