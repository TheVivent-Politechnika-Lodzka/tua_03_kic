package pl.lodz.p.it.ssbd2022.ssbd03.mok.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.TransactionException;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccessLevelMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services.MOKServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;

@RequestScoped
@DenyAll
@Path("/mok")
public class MOKEndpoint implements MOKEndpointInterface {

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
        // TODO: przenieść wysyłanie maila do endpointu
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        Account account = accountMapper.createAccountfromCreateClientAccountDto(registerClientDto);
        do {
            mokServiceInterface.registerAccount(account);
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok().build();
    }

    @Override
    public Response registerConfirm(RegisterClientConfirmDto registerConfirmDto) {
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            mokServiceInterface.confirmRegistration(registerConfirmDto.getToken());
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok().build();
    }

    @Override
    public Response createAccount(CreateAccountDto createAccountDto) {
        Account account = accountMapper.createAccountfromCreateAccountDto(createAccountDto);
        Account registeredAccount;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            registeredAccount = mokServiceInterface.createAccount(account);
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(registeredAccount)
        ).build();
    }

    @Override
    public Response activateAccount(String login, ETagDto eTagDto) {
        Account activatedAccount;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            activatedAccount = mokServiceInterface.activateAccount(login, eTagDto.getETag());
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(activatedAccount)
        ).build();
    }

    @Override
    public Response deactivateAccount(String login, ETagDto eTagDto) {
        Account deactivatedAccount;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            deactivatedAccount = mokServiceInterface.deactivateAccount(login, eTagDto.getETag());
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(deactivatedAccount)
        ).build();
    }

    @Override
    public Response addAccessLevel(String login, AccessLevelDto accessLevelDto) {
        AccessLevel accessLevel = accessLevelMapper.createAccessLevelFromDto(accessLevelDto);
        Account account;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            account = mokServiceInterface.addAccessLevelToAccount(login, accessLevel);
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)
        ).build();
    }

    @Override
    public Response removeAccessLevel(String login, String accessLevelName, String eTag) {
        Account newAccessLevelAccount;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            newAccessLevelAccount = mokServiceInterface.removeAccessLevelFromAccount(login, accessLevelName, eTag);
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(newAccessLevelAccount)
        ).build();
    }

    @Override
    public Response changeOwnPassword(ChangeOwnPasswordDto changeOwnPasswordDto) {
        String login = authContext.getCurrentUserLogin();
        Account account;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            account = mokServiceInterface.changeAccountPassword(
                    login,
                    changeOwnPasswordDto.getOldPassword(),
                    changeOwnPasswordDto.getNewPassword(),
                    changeOwnPasswordDto.getETag()
            );
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)
        ).build();
    }

    @Override
    public Response changePassword(String login, ChangePasswordDto changePasswordDto) {
        Account account;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            account = mokServiceInterface.changeAccountPassword(
                    login,
                    changePasswordDto.getNewPassword(),
                    changePasswordDto.getETag()
            );
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }
        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)
        ).build();
    }

    @Override
    public Response editOwnAccount(AccountWithAccessLevelsDto accountWithAccessLevelsDto) {
        String login = authContext.getCurrentUserLogin();
        Account update = accountMapper.createAccountFromDto(accountWithAccessLevelsDto);
        Account editedAccount;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            editedAccount = mokServiceInterface.editAccount(login, update, accountWithAccessLevelsDto.getETag());
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(editedAccount)
        ).build();
    }

    @Override
    public Response editAccount(String login, AccountWithAccessLevelsDto accountWithAccessLevelsDto) {
        Account update = accountMapper.createAccountFromDto(accountWithAccessLevelsDto);
        Account editedAccount;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            editedAccount = mokServiceInterface.editAccount(login, update, accountWithAccessLevelsDto.getETag());
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(editedAccount)
        ).build();
    }

    @Override
    public Response login(LoginCredentialsDto loginCredentialsDto) {
        String token;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            token = mokServiceInterface.authenticate(
                    loginCredentialsDto.getLogin(),
                    loginCredentialsDto.getPassword()
            );
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok(token).build();
    }

    @Override
    public Response getAllAccounts(int page, int limit) {
        // TODO: zmniejszyć ilość danych o kontach na liście
        PaginationData paginationData;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            paginationData = mokServiceInterface.findAllAccounts(page, limit);
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        List<Account> accounts = paginationData.getData();
        List<AccountWithAccessLevelsDto> accountsDTO = accountMapper.createListOfAccountWithAccessLevelDTO(accounts);
        paginationData.setData(accountsDTO);
        return Response.ok().entity(paginationData).build();
    }

    @Override
    public Response resetPassword(String login) {
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            mokServiceInterface.resetPassword(login);
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok().build();
    }

    @Override
    public Response resetPasswordToken(ResetPasswordTokenDto resetPasswordDto) {
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            mokServiceInterface.confirmResetPassword(
                    resetPasswordDto.getLogin(),
                    resetPasswordDto.getPassword(),
                    resetPasswordDto.getToken()
            );
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok().build();
    }

    @Override
    public Response getOwnAccount() {
        String user = authContext.getCurrentUserLogin();
        Account account;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            account = mokServiceInterface.findAccountByLogin(user);
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok().entity(accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)).build();
    }

    @Override
    public Response getAccount(String login) {
        Account account;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            account = mokServiceInterface.findAccountByLogin(login);
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok(accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)).build();
    }
}
