package pl.lodz.p.it.ssbd2022.ssbd03.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.token.TokenExpiredException;

import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthenticationMechanism implements HttpAuthenticationMechanism {

    private static final long serialVersionUID = 1L;

    @Inject
    JWTGenerator jwtGenerator;


    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpMessageContext httpMessageContext) throws AuthenticationException {

//        return httpMessageContext.doNothing();

        // zapewnia, że tylko zapytania do backendu są autoryzowane
        // zapytania do frontendu (SPA) nie są autoryzowane
        if (httpServletRequest.getPathInfo() == null) {
            return httpMessageContext.doNothing();
        }

        // zezwolenie na logowanie, rejestrację oraz pingowanie
        // zezwolenie na zmianę hasła
//        if (httpServletRequest.getPathInfo().endsWith("login")
//                || httpServletRequest.getPathInfo().endsWith("register")
//                || httpServletRequest.getPathInfo().endsWith("register-confirm")
//                || httpServletRequest.getPathInfo().endsWith("ping")
//                || httpServletRequest.getPathInfo().contains("reset-password")
//        ) {
//            return httpMessageContext.doNothing();
//        }

        String headerAuth = httpServletRequest.getHeader("Authorization");

        if (headerAuth == null || !headerAuth.startsWith("Bearer")) {
            return httpMessageContext.notifyContainerAboutLogin("Unauthorized", getGroups(Roles.ANONYMOUS));
        }

        String jwtToken = headerAuth.substring("Bearer ".length()).trim();

        try {
            Claims claims = jwtGenerator.decodeJWT(jwtToken);

            String login = claims.getSubject();
            String groups = claims.get("auth").toString();
            groups += "," + Roles.AUTHENTICATED;

            return httpMessageContext.notifyContainerAboutLogin(login, getGroups(groups));

        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return httpMessageContext.responseUnauthorized();
        }
    }

    private static final Map<String, Set<String>> mapRoles = Map.of(
            Roles.CLIENT, Set.of(Roles.CLIENT, Roles.AUTHENTICATED),
            Roles.ADMINISTRATOR, Set.of(Roles.ADMINISTRATOR, Roles.AUTHENTICATED),
            Roles.SPECIALIST, Set.of(Roles.SPECIALIST, Roles.AUTHENTICATED),
            Roles.ANONYMOUS, Set.of(Roles.ANONYMOUS),
            Roles.AUTHENTICATED, Set.of(Roles.AUTHENTICATED)
    );

    private Set<String> getGroups(String groups) {
        return Arrays.stream(groups.split(","))
                .map(x -> mapRoles.get(x))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
