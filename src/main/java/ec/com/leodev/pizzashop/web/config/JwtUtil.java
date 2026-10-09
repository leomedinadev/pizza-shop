package ec.com.leodev.pizzashop.web.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private final Algorithm algorithm;

    // El secreto llega por configuración (variable de entorno JWT_SECRET), no va en el código
    public JwtUtil(@Value("${pizza-shop.jwt.secret}") String secretKey) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("Falta el secreto JWT (variable de entorno JWT_SECRET).");
        }
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    public String create(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuer("platzi-piazza")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + + TimeUnit.DAYS.toMillis(15)))
                .sign(this.algorithm);
    }

    public boolean isValid(String jwt) {
        try {
            JWT.require(this.algorithm).build().verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUserName(String jwt) {
        return JWT.require(this.algorithm).build().verify(jwt).getSubject();
    }
}
