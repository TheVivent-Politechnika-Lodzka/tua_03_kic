package pl.lodz.p.it.ssbd2022.ssbd03.mok.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountEditDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.CredentialDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.services.MOKService;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;

import java.io.IOException;

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

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "ADMINISTRATOR", "SPECIALIST", "CLIENT" })
    @Path("/edit")
    public AccountDto editOwnAccount(AccountEditDto accountEditDto) {
        Account currentUser = authContext.getCurrentUser();
        return editAccount(currentUser, accountEditDto);
    }

    private AccountDto editAccount(Account account, AccountEditDto accountEditDto) {
        Account editedAccount = mokService.edit(account, accountEditDto.getFirstName(), accountEditDto.getSurname(), accountEditDto.getEmail(), accountEditDto.getPhoneNumber());
        return new AccountDto(editedAccount);
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

    @POST
    @Path("/jackson-test")
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
//    public Response jacksonTest(String accountDto){
//
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            AccountDto accountDto1 = mapper.readValue(accountDto, AccountDto.class);
//            System.out.println("############################");
//            System.out.println(accountDto1);
//            System.out.println("############################");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return Response.ok().build();
//    }
    public Response jacksonTest(AccountDto accountDto) {
        System.out.println("##############################");
        System.out.println(accountDto);
        System.out.println("##############################");
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
