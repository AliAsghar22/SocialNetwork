package neo4j.ir.config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.File;

/**
 * Created by Ali Asghar on 27/06/2017.
 */
@Configuration
public class Beans {

    @Bean
    public GraphDatabaseService graphDatabaseService(){
        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        return dbFactory.newEmbeddedDatabase(new File("E:\\neoDB"));
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new MyUserDetailService(graphDatabaseService());
    }
}
