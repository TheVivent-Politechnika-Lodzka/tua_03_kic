package pl.lodz.p.it.ssbd2022.ssbd03.mok.services;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.ServletException;
import jakarta.ws.rs.ClientErrorException;
import pl.lodz.p.it.ssbd2022.ssbd03.security.JWTGenerator;

@Stateless
public class AuthService {

    @Inject
    private IdentityStoreHandler indentityStoreHandler;

    @Inject
    private JWTGenerator jwtGenerator;

    public String authenticate(Credential credential) {
        CredentialValidationResult result = indentityStoreHandler.validate(credential);
        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            return jwtGenerator.createJWT(result);
        }
        throw new ClientErrorException("Invalid username or password", 401);
    }

}
