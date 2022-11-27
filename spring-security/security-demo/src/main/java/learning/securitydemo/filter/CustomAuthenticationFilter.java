package learning.securitydemo.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("Username is: {}",username);
        log.info("Password is: {}",password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        return authenticationManager.authenticate(authenticationToken);
    }

    // if authentication is successful then Spring gonna call this 'successfulAuthentication' method
    // we need to give the user access and refresh token after the user have successfully logged in
    // we need to have some way to generate the token, sign the token, and then send the token over to the user
    // we can use external library to do that for us ->Auth0 java json token
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // we need to get user's information (user which is successfully log in)
        // here is the User coming from Spring security
        User user = (User) authentication.getPrincipal(); // getPrincipal() returning the object
        // this is going to be the algorithm that I'm going to use to sign the JSON web token and the refresh token
        // the access token and the refresh token that I'm going to give to the user
        // this 'secret' should be encrypted
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());     //HS256("secret".getBytes());
        // this secret would be something I would saved somewhere secure and I would probably encrypt it
        // now we need to create token -> so the first token we need is the access token
        String access_token = com.auth0.jwt.JWT.create()     //create()
                .withSubject(user.getUsername())      // the subject can be really any string that you want (like user ID or username or anything unique)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))              // expiration -> for example ten minute
                .withIssuer(request.getRequestURL().toString())        // issuer -> for example the of the company or the author of this token
                        // this is gonna be the URL of our application
                // and then I can pass in all the claims so all the roles for that specific user
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    // that's gonna be all the claims of the user -> so whatever permissions or authorities or roles we put in for that specific user we're gonna pass those into the token as the rules of that specific user

                // then we can just sign this token with the algorithm
                .sign(algorithm);

        // so this is access token, remember, we also have to pass in the refresh token

        String refresh_token = com.auth0.jwt.JWT.create()     //create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))              // expiration -> here I can put longer time, for example a week or a month
                .withIssuer(request.getRequestURL().toString())        // issuer -> for example the of the company or the author of this token
                .sign(algorithm);

        // we can use the response to send those to the user in the front end
            // (1) response.setHeader("access_token",access_token);
            // (2) response.setHeader("refresh_token",refresh_token);
        // whenever user logs in successfully, we can check the headers in the response, they should have the access and refresh token

        // but instead of setting headers here, I want to actually send something in the response body
            // -> I'm gonna comment that 2lines (line 1, line 2)

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token",access_token);
        tokens.put("refresh_token",refresh_token);
        // I need to set the response type
            // I want this to be JSON -> 'APPLICATION_JSON_VALUE'
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),tokens); // -> that's going to return everything in the body and a nice JSON format
    }
}
