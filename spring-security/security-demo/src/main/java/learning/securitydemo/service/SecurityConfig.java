package learning.securitydemo.service;


import learning.securitydemo.filter.CustomAuthenticationFilter;
import learning.securitydemo.filter.CustomAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // tell Spring how we want to manage the users and the security and the application
    // 'WebSecurityConfigurerAdapter' is the main security class
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // we don't have any configuration or any override for these two beans -> so somehow in our
    // application, we need to create two beans in our app until Spring how we want to upload
    // the user and then create a Bean for the password encoder
        // we can create our own password encoder, but we can just use the one from Spring, which is
        // pretty secure
    // create Bean in 'SecurityDemoApplication'

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    /*@Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }*/


    // there are many ways how I can tell Spring how to look for the users
    // first one is in memory -> I can select in memory ('inMemoryAuthentication' and then I pass in a username and a password
    // so that Spring can use to check for users whenever users are trying to log into the application
        //  auth.inMemoryAuthentication()
    // I can also use 'auth.jdbcAuthentication' -> so I can create a service class and then passing all
    // the queries and everything, and then use JDBC to make my own request and then override the JDBC user detail manager configure
        // but we have JPA so we don't have to do it
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // configure Authentication
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        // auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // configure Authorization
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // declares which Page(URL) will have What access type
        /* (3) */ http.authorizeRequests().antMatchers("/api/login/**","/api/token/refresh/**").permitAll();        // whatever path that we don't want to secure and after that, we can say something like that
        /* (1) */ http.authorizeRequests().antMatchers(GET,"/api/user/**").hasAnyAuthority("ROLE_USER");    // everything that comes after that then they need to have user_role
        /* (2) */ http.authorizeRequests().antMatchers(POST,"/api/user/save/**").hasAnyAuthority("ROLE_ADMIN");    // they need to have role_admin if they need to access the route
                // for example for saving user I need to have admin permission (whatever comes after that), they can send post request
        // this is not good:
            // we allow everyone to be able to access this application at this point
                    //http.authorizeRequests().anyRequest().permitAll();
            // we want to everyone to be authenticated:
        // Any other URLs which are not configured in above antMatchers generally declared aunthenticated() in real time
        http.authorizeRequests().anyRequest().authenticated();
        // now if we want to allow certain path we can do something like this (3) but we need to do before these two lines ((1),(2))
        // => the order and this matters!

        // we need to add filter
        // we gonna have an authentication filter -> we can check the user whenever they're trying log in
            // and we need to tell this configuration about this filter
        //http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http.addFilter(customAuthenticationFilter); // authentication filter
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);  // authorization filter

        /*
        I’ve already used the WebSecurityConfigurerAdapter class in many cases to set up the endpoint authorization rules and the authentication method in Spring apps.
        In the configure(HttpSecurity http) method, we specify the authentication method as HTTP Basic and the authorization rules.
        examples:
        specifying that any request needs authentication:
            http.authorizeRequests().anyRequest().authenticated();

        only users with the authority "read" can send requests:
            http.authorizeRequests().anyRequest().hasAuthority("read");

         */
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /* (?)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
}
