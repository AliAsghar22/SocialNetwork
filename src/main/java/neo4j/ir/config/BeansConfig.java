package neo4j.ir.config;

import neo4j.ir.Service.MyUserDetailService;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by Ali Asghar on 27/06/2017.
 */
@Configuration
public class BeansConfig {

    @Bean("userDetailsService")
    public UserDetailsService getUserDetailsService(){
        return new MyUserDetailService();
    }
//
//    @Bean
//    public IDBAccess idbAccess(){
//        Properties props = new Properties();
//
//        // properties for remote access and for embedded access
//        // (not needed for in memory access)
//        // have a look at the DBProperties interface
//        // the appropriate database access class will pick the properties it needs
//        props.setProperty(DBProperties.SERVER_ROOT_URI, "http://localhost:7474");
//        props.setProperty(DBProperties.DATABASE_DIR, "C:/NEO4J_DBS/01");
//
//        AuthToken authToken = AuthTokens.basic("neo4j", "neo");
//
//        /** connect to an in memory database (no properties are required) */
//        return DBAccessFactory.createDBAccess(DBType.REMOTE, props, authToken);
//    }

    @Bean
    public Driver driver(){
        return GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "neo" ) );
    }

}
