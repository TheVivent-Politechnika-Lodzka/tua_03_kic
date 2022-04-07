package pl.lodz.p.it.ssbd2022.ssbd03.security;


import io.jsonwebtoken.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;

import java.text.ParseException;
import java.util.Date;

@ApplicationScoped
public class JWTGenerator {

    // to by należało dać do jakiegoś configa
    private final long TIMEOUT = 30 * 60 * 1000;
    private final String SHAREDSECRET = "zjZi6JWZ99IT0Trx49MNitLpwPjQc81BOUZytttWprg=";

    public  String createJWT(CredentialValidationResult cred) {

        String token = Jwts.builder().
                setSubject(cred.getCallerPrincipal().getName())
                .claim("auth", String.join(",", cred.getCallerGroups()))
                .signWith(SignatureAlgorithm.HS256, SHAREDSECRET)
                .setExpiration(new Date(new Date().getTime() + TIMEOUT)).compact();
        return token;
    }

    public  Claims decodeJWT(String jwt) {
        //chwilowe rozwiazanie
        //Ta linia rzuci wyjątek jeśli token nie jest podpisany jak powinien.
        return Jwts.parser()
                .setSigningKey(SHAREDSECRET)
                .parseClaimsJws(jwt)
                .getBody();
    }
}

