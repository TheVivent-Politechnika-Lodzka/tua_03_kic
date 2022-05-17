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
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.EmailConfig;
import pl.lodz.p.it.ssbd2022.ssbd03.common.TaggedDto;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ActiveAccountToken;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccessLevelMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.services.MOKService;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.ArrayList;
import java.util.List;

@Stateless
@Path("mok")
@DenyAll
@TransactionAttribute(TransactionAttributeType.NEVER)
public class MOKEndpoint {

    @Inject
    private MOKService mokService;

    @Inject
    private AuthContext authContext;

    @Inject
    private AccountMapper accountMapper;

    @Inject
    private AccessLevelMapper accessLevelMapper;

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMINISTRATOR"})
    @Path("/access-level/{login}")
    public Response addAccessLevel(@PathParam("login") String login, @Valid AccessLevelDto accessLevelDto) {
        AccessLevel accessLevel = accessLevelMapper.createAccessLevelFromDto(accessLevelDto);
        Account account = mokService.addAccessLevel(login, accessLevel);
        return Response.ok(accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)).build();
    }

    //stwórz nowe konto
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMINISTRATOR")
    @Path("/account")
    public Response createAccount(CreateAccountDto accountDto) {
        Account account = accountMapper.createAccountfromCreateAccountDto(accountDto);
        mokService.createAccount(account);
        return Response.ok().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/register")
    public Response registerClientAccount(RegisterClientAccountDto accountDto) {
        Account account = accountMapper.createAccountfromCreateClientAccountDto(accountDto);
        mokService.registerClientAccount(account);
        return Response.ok().build();
    }

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
    @Path("/{login}")
    @RolesAllowed("ADMINISTRATOR")
    public Response getAccountDetailsByLogin(@PathParam("login") String login) {
        Account account = mokService.findByLogin(login);
        return Response.ok(accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)).build();
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
    public Response resetPassword(ResetPasswordDTO accountWithTokenDTO) {
        mokService.resetPassword(accountWithTokenDTO);
        return Response.ok().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMINISTRATOR", "SPECIALIST", "CLIENT"})
    @Path("/edit")
    public AccountWithAccessLevelsDto editOwnAccount(AccountWithAccessLevelsDto accountEditDto) {
        Account currentUser = authContext.getCurrentUser();
        return editAccount(currentUser.getLogin(), accountEditDto);
    }

    private AccountWithAccessLevelsDto editAccount(String login, AccountWithAccessLevelsDto accountEditDto) {
        Account editedAccount = mokService.edit(login, accountEditDto);
        return accountMapper.createAccountWithAccessLevelsDtoFromAccount(editedAccount);
    }

    @GET
    @Path("/deactivate/{login}")
    @RolesAllowed("ADMINISTRATOR")
    public Response deactivate(@PathParam("login") String login, TaggedDto taggedDto) {
        mokService.deactivate(login, taggedDto.getTag());
        return Response.ok().build();
    }

    @GET
    @Path("/activate/{login}")
    @RolesAllowed("ADMINISTRATOR")
    public Response activate(@PathParam("login") String login, TaggedDto taggedDto) {
        mokService.activate(login, taggedDto.getTag());
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/activeAccount")
    @PermitAll
    public Response activeAccountByToken(TokenDto tokenDto) {
        mokService.confirm(tokenDto.getToken());
        return Response.ok().build();
    }

    @GET
    @Path(("/account"))
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response accountDetails(){
        String user = authContext.getCurrentUserLogin();
        Account account = mokService.findByLogin(user);
        return Response.ok().entity(accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)).build();
    }

    @GET
    @Path("/account/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response findByLogin(@PathParam("login") String login) {
        Account account = mokService.findByLogin(login);
        return Response.ok().entity(accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)).build();
    }

    @GET
    @Path("/accounts")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMINISTRATOR")
    public Response findInRange(@QueryParam("page") int page, @QueryParam("limit") int limit) {

        PaginationData paginationData = mokService.findInRange(page, limit);
        List<Account> accounts = paginationData.getData();
        List<AccountWithAccessLevelsDto> accountsDTO = new ArrayList<>();
        for (Account account : accounts) {
            accountsDTO.add(accountMapper.createAccountWithAccessLevelsDtoFromAccount(account));
        }
        paginationData.setData(accountsDTO);
        return Response.ok().entity(paginationData).build();

    }

    @PATCH
    @Path("/password")
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response changeOwnPassword(@Valid ChangeOwnPasswordDto changeOwnPasswordDto) {
        String principal = authContext.getCurrentUserLogin();

        mokService.changeOwnPassword(principal, changeOwnPasswordDto.getNewPassword(), changeOwnPasswordDto.getOldPassword());
        return Response.ok().build();
    }

    @PATCH
    @Path("/password/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMINISTRATOR")
    public Response changeAccountPassword(@PathParam(value = "login") String login, @Valid ChangeAccountPasswordDto changeAccountPasswordDto) {
        mokService.changeAccountPassword(login, changeAccountPasswordDto.getNewPassword());
        return Response.ok().build();
    }

    @PUT
    @Path("/edit/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMINISTRATOR")
    public Response editOtherAccountData(@PathParam("login") String login, AccountWithAccessLevelsDto accountEditDto) {
        mokService.edit(login, accountEditDto);
        return Response.ok().build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path(("/ping"))
    public Response test() {
        return Response.ok("pong").build();
    }
}
