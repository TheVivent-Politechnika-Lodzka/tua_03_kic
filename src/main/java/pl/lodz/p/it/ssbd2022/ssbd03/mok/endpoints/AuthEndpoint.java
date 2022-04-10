package pl.lodz.p.it.ssbd2022.ssbd03.mok.endpoints;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.CredentialDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.services.AuthService;

@Stateless
@Path("login")
public class AuthEndpoint {

    @Inject
    private AuthService authService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticate(CredentialDto credentialDto) {
        Credential credential = new UsernamePasswordCredential(credentialDto.getLogin(), new Password(credentialDto.getPassword()));
        String token = authService.authenticate(credential);
        return Response.ok(token).build();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response test(){
        return Response.ok("logowanie").build();
    }
}
