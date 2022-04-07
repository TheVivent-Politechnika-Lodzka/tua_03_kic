package pl.lodz.p.it.ssbd2022.ssbd03.resources;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.dto.CredentialDto;
import pl.lodz.p.it.ssbd2022.ssbd03.security.JWTGenerator;

@Stateless
@Path("login")
public class TestLogin {

    @Inject
    IdentityStoreHandler identityStoreHandler;

    @Inject
    JWTGenerator jwtGenerator;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticate(CredentialDto credentialDto) {
        Credential credential = new UsernamePasswordCredential(credentialDto.getLogin(), new Password(credentialDto.getPassword()));
        CredentialValidationResult result = identityStoreHandler.validate(credential);
        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            return Response.accepted().entity(jwtGenerator.createJWT(result)).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response test(){
        return Response.ok("logowanie").build();
    }
}
