package pl.lodz.p.it.ssbd2022.ssbd03.mok.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccessLevelMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services.MOKServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.ArrayList;
import java.util.List;

@RequestScoped
@DenyAll
@Path("/mok")
public class MOKEndpoint implements MOKEndpointInterface {

//    @Inject
//    private MOKService mokService;

    @Inject
    MOKServiceInterface mokServiceInterface;
    @Inject
    private AuthContext authContext;
    @Inject
    private AccountMapper accountMapper;
    @Inject
    private AccessLevelMapper accessLevelMapper;

    @Override
    public Response register(RegisterClientDto registerClientDto) {
        Account account = accountMapper.createAccountfromCreateClientAccountDto(registerClientDto);
        mokServiceInterface.registerAccount(account);
        return Response.ok().build();
    }

    @Override
    public Response registerConfirm(RegisterClientConfirmDto registerConfirmDto) {
        mokServiceInterface.confirmRegistration(registerConfirmDto.getToken());
        return Response.ok().build();
    }

    @Override
    public Response createAccount(CreateAccountDto createAccountDto) {
        Account account = accountMapper.createAccountfromCreateAccountDto(createAccountDto);
        Account registeredAccount = mokServiceInterface.createAccount(account);
        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(registeredAccount)
        ).build();
    }

    @Override
    public Response activateAccount(String login, ETagDto eTagDto) {
        Account activatedAccount = mokServiceInterface.activateAccount(login, eTagDto.getETag());
        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(activatedAccount)
        ).build();
    }

    @Override
    public Response deactivateAccount(String login, ETagDto eTagDto) {
        Account deactivatedAccount = mokServiceInterface.deactivateAccount(login, eTagDto.getETag());
        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(deactivatedAccount)
        ).build();
    }

    @Override
    public Response addAccessLevel(String login, AccessLevelDto accessLevelDto) {
        AccessLevel accessLevel = accessLevelMapper.createAccessLevelFromDto(accessLevelDto);
        Account account = mokServiceInterface.addAccessLevelToAccount(login, accessLevel);

        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)
        ).build();
    }

    @Override
    public Response removeAccessLevel(String login, String accessLevelName, String eTag) {
        Account newAccessLevelAccount = mokServiceInterface.removeAccessLevelFromAccount(login, accessLevelName, eTag);
        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(newAccessLevelAccount)
        ).build();
    }

    @Override
    public Response changeOwnPassword(ChangeOwnPasswordDto changeOwnPasswordDto) {
        String login = authContext.getCurrentUserLogin();
        Account account = mokServiceInterface.changeAccountPassword(
                login, changeOwnPasswordDto.getOldPassword(), changeOwnPasswordDto.getNewPassword(),
                changeOwnPasswordDto.getETag());

        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)
        ).build();
    }

    @Override
    public Response changePassword(String login, ChangePasswordDto changePasswordDto) {
        Account account = mokServiceInterface.changeAccountPassword(
                login, changePasswordDto.getNewPassword(), changePasswordDto.getETag());

        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)
        ).build();
    }

    @Override
    public Response editOwnAccount(AccountWithAccessLevelsDto accountWithAccessLevelsDto) {
        String login = authContext.getCurrentUserLogin();
        Account update = accountMapper.createAccountFromDto(accountWithAccessLevelsDto);
        Account editedAccount = mokServiceInterface.editAccount(login, update, accountWithAccessLevelsDto.getETag());

        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(editedAccount)
        ).build();
    }

    @Override
    public Response editAccount(String login, AccountWithAccessLevelsDto accountWithAccessLevelsDto) {
        mokService.edit(login, accountWithAccessLevelsDto);
        return Response.ok().build();
    }

    @Override
    public Response login(LoginCredentialsDto loginCredentialsDto) {
        String token = mokServiceInterface.authenticate(
                loginCredentialsDto.getLogin(), loginCredentialsDto.getPassword());
        return Response.ok(token).build();
    }

    @Override
    public Response getAllAccounts(int page, int limit) {
        PaginationData paginationData = mokServiceInterface.findAllAccounts(page, limit);
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
        mokServiceInterface.resetPassword(login);
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
        Account account = mokServiceInterface.findAccountByLogin(login);
        return Response.ok(accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)).build();
    }
}
