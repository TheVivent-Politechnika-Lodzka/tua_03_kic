package pl.lodz.p.it.ssbd2022.ssbd03.security;

import io.jsonwebtoken.*;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

public class AuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    JWTGenerator jwtGenerator;


    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpMessageContext httpMessageContext) throws AuthenticationException {

        if (httpServletRequest.getRequestURL().toString().contains("/")) {
            return httpMessageContext.doNothing();
        }

        if (httpServletRequest.getRequestURL().toString().endsWith("/api/login") || httpServletRequest.getRequestURL().toString().endsWith("/api/ping") ) {
            return httpMessageContext.doNothing();
        }

        String headerAuth = httpServletRequest.getHeader("Authorization");

        if (headerAuth == null || !headerAuth.startsWith("Bearer")) {
            return httpMessageContext.responseUnauthorized();
        }

        String jwtToken = headerAuth.substring("Bearer ".length()).trim();

        try {
            Claims claims = jwtGenerator.decodeJWT(jwtToken);

            String login = claims.getSubject();
            String groups = claims.get("auth").toString();

            return httpMessageContext.notifyContainerAboutLogin(login, new HashSet<>(Arrays.asList(groups.split(","))));

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return httpMessageContext.responseUnauthorized();
        }
    }
}
