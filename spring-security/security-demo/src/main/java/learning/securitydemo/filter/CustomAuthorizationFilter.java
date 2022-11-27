package learning.securitydemo.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    // all the logic we're going to put to filter the request coming in and determine if the user has access to the application or not
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // I want to do is to check to see if this is not the login path, because if this is the case, then I don't want to try to do anything here,
        // I just want to let it go through => if it is login path, I know the user wants to just login, I don't need to do anything
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/token/refresh")) {     // this is the login path
            filterChain.doFilter(request,response);
                // that's not going to do anything, it's just going to make this filter pass the request to the next filter and the filter chain
                // the user just try to log in
        } else {    // that's when I need to start checking to see if this has an authorization and then set the user as the logged in user and security context

            // first thing -> try to access the authorization header
                // looking for authorization header that should be the key for the token
            String authorizationHeader = request.getHeader(AUTHORIZATION);   // I'm looking for the authorization key -> 'AUTHORIZATION'
            // now I check if I have header in there (is not null) and if the header start with Bearer (Bearer and space):
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
               try {
                   String token = authorizationHeader.substring("Bearer ".length());   // we pass in how many letters we want to remove, we cut the bearer to get the token
                   // should keep the same secret that I use to sign the algorithm
                   Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                   // now we can verify the token:
                   JWTVerifier verifier = JWT.require(algorithm).build(); // I need to pass in the algorithm to that and then I need to call build
                   // we need the same secret that we use to encode the token and then pass the algorithm to verifier
                   // now I need to do coded (decoded?) token
                   DecodedJWT decodedJWT = verifier.verify(token);
                   // if token is valid, we can grab the name of the username:
                   String username = decodedJWT.getSubject();   // will give me the username that comes with the token
                   // now I need to get the roles:
                   String[] roles = decodedJWT.getClaim("roles").asArray(String.class);    // the key here is 'roles', this key is from token (?), that's why we pass in "roles"
                        // and we need to tell what kind of collection is this, how we want to collect it -> '.asArray(String.class)'

                   // => we got username, roles, we don't need password because at that point the user has been authenticated, and their JSON web token or their access token is valid

                   Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                   stream(roles).forEach(role ->{
                       authorities.add(new SimpleGrantedAuthority(role));
                   });
                   UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null,authorities);
                   // now we need to set this user and the security context holder
                   SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    // that's how we say to Spring security: this is the user, their username, roles and this is what they can do in the application
                   filterChain.doFilter(request,response);
               } catch (Exception exception){
                   log.info("Error logging in: {}", exception.getMessage());
                   response.setHeader("error", exception.getMessage());
                   response.setStatus(FORBIDDEN.value());
                   //response.sendError(FORBIDDEN.value());

                   Map<String, String> error = new HashMap<>();
                   error.put("error_message",exception.getMessage());
                   response.setContentType(APPLICATION_JSON_VALUE);
                   new ObjectMapper().writeValue(response.getOutputStream(),error);
               }
            } else {
                filterChain.doFilter(request,response);
            }
        }
    }
}
