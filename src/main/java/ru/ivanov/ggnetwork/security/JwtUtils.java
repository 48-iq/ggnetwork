package ru.ivanov.ggnetwork.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.ivanov.ggnetwork.entities.User;

import java.time.ZonedDateTime;
import java.util.Date;


@Component
public class JwtUtils {

    @Value("${app.token.duration.days}")
    private Integer tokenDurationDays;
    @Value("${app.token.secret}")
    private String tokenSecret;

    public String generateToken(User user) {
        Date experationDate = Date.from(ZonedDateTime.now()
                .plusDays(tokenDurationDays)
                .toInstant());
        return JWT.create()
                .withSubject("User details")
                .withClaim("username", user.getUsername())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(new Date())
                .withExpiresAt(experationDate)
                .withIssuer("spring-app-ggnetwork")
                .sign(Algorithm.HMAC256(tokenSecret));
    }

    public String validateTokenAndRetrieveUsername(String token) {
        var verifier = JWT.require(Algorithm.HMAC256(tokenSecret))
                .withSubject("User details")
                .withIssuer("spring-app-ggnetwork")
                .build();
        return verifier.verify(token)
                .getClaim("username").asString();
    }

}
