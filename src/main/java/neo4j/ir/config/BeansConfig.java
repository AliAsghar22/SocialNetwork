package neo4j.ir.config;

import neo4j.ir.Application;
import neo4j.ir.Service.MyUserDetailService;
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
public class BeansConfig {

    @Bean("userDetailsService")
    public UserDetailsService getUserDetailsService(){
        return new MyUserDetailService();
    }

    @Bean
    public GraphDatabaseService graphDatabaseService(){
        GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
        GraphDatabaseService db = dbFactory.newEmbeddedDatabase(new File("neoDB"));
        Application.registerShutdownHook(db);
        return db;
    }

}
