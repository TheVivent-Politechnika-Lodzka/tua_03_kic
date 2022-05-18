package pl.lodz.p.it.ssbd2022.ssbd03.mok.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccessLevelMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services.MOKService;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services.MOKServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services.TestService;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.ArrayList;
import java.util.List;

@RequestScoped
@DenyAll
@Path("/mok")
public class MOKEndpoint implements MOKEndpointInterface{

    @Inject
    private MOKService mokService;

    @Inject
    private AuthContext authContext;

    @Inject
    private AccountMapper accountMapper;

    @Inject
    private AccessLevelMapper accessLevelMapper;

    @Inject
    MOKServiceInterface mokServiceInterface;

    @Override
    public Response register(RegisterClientDto registerClientDto) {
        Account account = accountMapper.createAccountfromCreateClientAccountDto(registerClientDto);
        mokService.registerClientAccount(account);
        return Response.ok().build();
    }

    @Override
    public Response registerConfirm(RegisterClientConfirmDto registerConfirmDto) {
        mokService.confirm(registerConfirmDto.getToken());
        return Response.ok().build();
    }

    @Override
    public Response createAccount(CreateAccountDto createAccountDto) {
        Account account = accountMapper.createAccountfromCreateAccountDto(createAccountDto);
        mokService.createAccount(account);
        return Response.ok().build();
    }

    @Override
    public Response activateAccount(String login, ETagDto eTagDto) {
        mokService.activate(login, eTagDto.getETag());
        return Response.ok().build();
    }

    @Override
    public Response deactivateAccount(String login, ETagDto eTagDto) {
        mokService.deactivate(login, eTagDto.getETag());
        return Response.ok().build();
    }

    @Override
    public Response addAccessLevel(String login, AccessLevelDto accessLevelDto) {
        AccessLevel accessLevel = accessLevelMapper.createAccessLevelFromDto(accessLevelDto);
        Account account = mokService.addAccessLevel(login, accessLevel);
        return Response.ok(accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)).build();
    }

    @Override
    public Response removeAccessLevel(String login, String accessLevel, String eTag) {
        Account newAccessLevelAccount = mokService.removeAccessLevel(login, accessLevel, eTag);
        return Response.ok(accountMapper.createAccountWithAccessLevelsDtoFromAccount(newAccessLevelAccount)).build();
    }

    @Override
    public Response changeOwnPassword(ChangeOwnPasswordDto changeOwnPasswordDto) {
        String principal = authContext.getCurrentUserLogin();
        mokService.changeOwnPassword(principal, changeOwnPasswordDto.getNewPassword(), changeOwnPasswordDto.getOldPassword());
        return Response.ok().build();
    }

    @Override
    public Response changePassword(String login, ChangePasswordDto changePasswordDto) {
        mokService.changeAccountPassword(login, changePasswordDto.getNewPassword());
        return Response.ok().build();
    }

    @Override
    public Response editOwnAccount(AccountWithAccessLevelsDto accountWithAccessLevelsDto) {
        String currentUser = authContext.getCurrentUserLogin();
        Account editedAccount = mokService.edit(currentUser, accountWithAccessLevelsDto);
        return Response.ok(accountMapper.createAccountWithAccessLevelsDtoFromAccount(editedAccount)).build();
    }

    @Override
    public Response editAccount(String login, AccountWithAccessLevelsDto accountWithAccessLevelsDto) {
        mokService.edit(login, accountWithAccessLevelsDto);
        return Response.ok().build();
    }

    @Override
    public Response login(LoginCredentialsDto loginCredentialsDto) {
        Credential credential = new UsernamePasswordCredential(loginCredentialsDto.getLogin(), new Password(loginCredentialsDto.getPassword()));
        String token = mokService.authenticate(credential);
        return Response.ok(token).build();
    }

    @Override
    public Response getAllAccounts(int page, int limit) {
        PaginationData paginationData = mokService.findInRange(page, limit);
        List<Account> accounts = paginationData.getData();
        List<AccountWithAccessLevelsDto> accountsDTO = new ArrayList<>();
        for (Account account : accounts) {
            accountsDTO.add(accountMapper.createAccountWithAccessLevelsDtoFromAccount(account));
        }
        paginationData.setData(accountsDTO);
        return Response.ok().entity(paginationData).build();
    }

    @Override
    public Response resetPassword(String login) {
        mokService.reset(login);
        return Response.ok().build();
    }

    @Override
    public Response resetPasswordToken(ResetPasswordTokenDto resetPasswordDto) {
        mokService.resetPassword(resetPasswordDto);
        return Response.ok().build();
    }

    @Override
    public Response getOwnAccount() {
        String user = authContext.getCurrentUserLogin();
        Account account = mokService.findByLogin(user);
        return Response.ok().entity(accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)).build();
    }

    @Override
    public Response getAccount(String login) {
        Account account = mokService.findByLogin(login);
        return Response.ok(accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)).build();
    }
}
