package neo4j.ir.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by Ali Asghar on 27/06/2017.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/register.html","/user/add")
                .permitAll()

                .and()

                .authorizeRequests()
                .antMatchers("/**")
                .access("hasRole('USER')")

                .and()

                .authorizeRequests()
                .antMatchers("/news/**")
                .access("hasRole('ADMIN')")

                .and()

                .formLogin()
                .defaultSuccessUrl("/")
                .permitAll()

                .and()

                .logout()
                .permitAll()

                .and()

                .csrf().disable();
    }
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("user").password("pass").roles("USER");
//    }
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return userDetailsService;
    }
}
