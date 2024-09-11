package ru.ivanov.ggnetwork.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.ivanov.ggnetwork.entities.User;
import ru.ivanov.ggnetwork.repositories.UserRepository;

import java.time.ZonedDateTime;
import java.util.Date;


@Component
public class JwtUtils {

    @Value("${app.token.duration.days}")
    private Integer tokenDurationDays;
    @Value("${app.token.secret}")
    private String tokenSecret;

    @Autowired
    private UserRepository userRepository;

    public String generateToken(User user) {
        Date experationDate = Date.from(ZonedDateTime.now()
                .plusDays(tokenDurationDays)
                .toInstant());
        return JWT.create()
                .withSubject("User details")
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(new Date())
                .withExpiresAt(experationDate)
                .withIssuer("spring-app-ggnetwork")
                .sign(Algorithm.HMAC256(tokenSecret));
    }

    public UserDetails validateTokenAndRetrieveUserdetails(String token) {
        var verifier = JWT.require(Algorithm.HMAC256(tokenSecret))
                .withSubject("User details")
                .withIssuer("spring-app-ggnetwork")
                .build();
        var jwt =  verifier.verify(token);
        var username = jwt.getClaim("username").asString();
        var userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty())
            throw new JWTVerificationException("user with username" + username +" not found");
        var user = userOptional.get();
        if (!user.getId().equals(jwt.getClaim("id").asInt()))
            throw new JWTVerificationException("id is not match");
        return new UserDetailsImpl(user);
    }

}
