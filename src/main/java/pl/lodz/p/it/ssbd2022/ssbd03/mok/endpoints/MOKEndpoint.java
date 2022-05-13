package pl.lodz.p.it.ssbd2022.ssbd03.mok.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.EmailConfig;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithTokenDTO;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.CredentialDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.services.MOKService;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;

import java.util.UUID;

@Stateless
@Path("mok")
@DenyAll
@TransactionAttribute(TransactionAttributeType.NEVER)
public class MOKEndpoint {

    @Inject
    private MOKService mokService;

    @Inject
    private AuthContext authContext;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/login")
    public Response authenticate(CredentialDto credentialDto) {
        Credential credential = new UsernamePasswordCredential(credentialDto.getLogin(), new Password(credentialDto.getPassword()));
        String token = mokService.authenticate(credential);
        return Response.ok(token).build();
    }

    @GET
    @PermitAll
    @Path("/reset/{login}")
    public Response reset(@PathParam("login") String login) {
        mokService.reset(login);
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/resetPassword")
    public Response resetPassword(AccountWithTokenDTO accountWithTokenDTO) {
        mokService.resetPassword(accountWithTokenDTO);
        return Response.ok().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "ADMINISTRATOR", "SPECIALIST", "CLIENT" })
    @Path("/edit")
    public AccountWithAccessLevelsDto editOwnAccount(AccountWithAccessLevelsDto accountEditDto) {
        Account currentUser = authContext.getCurrentUser();
        return editAccount(currentUser.getLogin(), accountEditDto);
    }

    private AccountWithAccessLevelsDto editAccount(String login, AccountWithAccessLevelsDto accountEditDto) {
        Account editedAccount = mokService.edit(login, accountEditDto);
        return new AccountWithAccessLevelsDto(editedAccount);
    }

    @GET
    @Path("/deactivate/{login}")
    @RolesAllowed("ADMINISTRATOR")
    public Response deactivate(@PathParam("login") String login) {
        mokService.deactivate(login);
        return Response.ok().build();
    }

    @GET
    @Path("/activate/{login}")
    @RolesAllowed("ADMINISTRATOR")
    public Response activate(@PathParam("login") String login) {
        mokService.activate(login);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path(("/ping"))
    public Response test(){
        return Response.ok("pong").build();
    }

}
