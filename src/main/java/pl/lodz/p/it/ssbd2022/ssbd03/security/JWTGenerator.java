package pl.lodz.p.it.ssbd2022.ssbd03.security;


import io.jsonwebtoken.*;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Stateless
public class JWTGenerator {

    private static final long serialVersionUID = 1L;

    public String createJWT(CredentialValidationResult cred) {
        String token = Jwts.builder().
                setSubject(cred.getCallerPrincipal().getName())
                .claim("auth", String.join(",", cred.getCallerGroups()))
                .signWith(SignatureAlgorithm.HS256, Config.JWT_SECRET)
                .setExpiration(new Date(new Date().getTime() + Config.JWT_EXPIRATION_SECONDS * 1000)).compact();
        return token;
    }

    public  String createJWT(String login, List<String> accessLevels) {
        String token = Jwts.builder().
                setSubject(login)
                .claim("auth", String.join(",", accessLevels))
                .signWith(SignatureAlgorithm.HS256, Config.JWT_SECRET)
                .setExpiration(new Date(new Date().getTime() + Config.JWT_EXPIRATION_SECONDS * 1000)).compact();
        return token;
    }

    public String createRegistrationJWT(String login) {
        return Jwts.builder()
                .setSubject(login)
                .signWith(SignatureAlgorithm.HS256, Config.JWT_SECRET)
                .setExpiration(Date.from(Instant.now().plusSeconds(Config.REGISTER_TOKEN_EXPIRATION_SECONDS))).compact();
    }

    public String createResetPasswordJWT(String login) {
        return Jwts.builder()
                .setSubject(login)
                .signWith(SignatureAlgorithm.HS256, Config.JWT_SECRET)
                .setExpiration(Date.from(Instant.now().plusSeconds(Config.RESET_PASSWORD_TOKEN_EXPIRATION_SECONDS))).compact();
    }

    public String createRefreshJWT(String login) {
        return Jwts.builder()
                .setSubject(login)
                .signWith(SignatureAlgorithm.HS256, Config.JWT_SECRET)
                .setExpiration(Date.from(Instant.now().plusSeconds(Config.REFRESH_TOKEN_EXPIRATION_SECONDS))).compact();
    }

    public  Claims decodeJWT(String jwt) {
        //chwilowe rozwiazanie
        //Ta linia rzuci wyjątek jeśli token nie jest podpisany jak powinien.
        return Jwts.parser()
                .setSigningKey(Config.JWT_SECRET)
                .parseClaimsJws(jwt)
                .getBody();
    }
}

