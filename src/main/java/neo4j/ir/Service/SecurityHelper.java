package neo4j.ir.Service;

import neo4j.ir.nodes.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SecurityHelper {

    @Autowired
    UserService userService;

    public User getCurrentUser(){
        String email = ((org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return  userService.getUser(email);
    }

    public String getCurrentUserUserName(){
        return getCurrentUser().getUserName();
    }

    public boolean hasAuthority(String authority){
        Collection authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (Object ga :
                authorities) {
            String auth = ((GrantedAuthority) ga).getAuthority();
            if(auth.equalsIgnoreCase(authority))
                return true;
        }
        return false;
    }

    public boolean hasRole(String role){
        return hasAuthority("ROLE_" + role);
    }
}
