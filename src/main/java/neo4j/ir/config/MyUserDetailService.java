package neo4j.ir.config;

import neo4j.ir.nodes.User;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

/**
 * Created by Ali Asghar on 27/06/2017.
 */
public class MyUserDetailService implements UserDetailsService {
    private GraphDatabaseService db;

    @Autowired
    public MyUserDetailService(GraphDatabaseService graphDatabaseService) {
        this.db = graphDatabaseService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = User.loadByUserName(userName, db);
        if (user == null)
            return null;
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword()
                , Collections.singletonList((GrantedAuthority) () -> "ROLE_USER"));
    }
}
