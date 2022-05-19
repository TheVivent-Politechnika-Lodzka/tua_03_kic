package pl.lodz.p.it.ssbd2022.ssbd03.mok.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.ejb.AccessLocalException;
import jakarta.ejb.EJBAccessException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.TransactionException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.InAppOptimisticLockException;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccessLevelMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services.MOKServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        boolean commitedTX = false;
        Account account = accountMapper.createAccountfromCreateClientAccountDto(registerClientDto);
        do {
            try {
                mokServiceInterface.registerAccount(account);
                commitedTX = mokServiceInterface.isLastTransactionCommited();
                if(!commitedTX) {
                    //Informacja o odwołaniu tranzakcji -> logger
                }
            } catch (InAppOptimisticLockException iao) {
                //Informacja o zaistnieniu blokady -> logger
                commitedTX = true;
                if (TXCounter < 2) {
                    throw iao;
                }
            } catch (WebApplicationException wa) {
                throw wa;
            } catch (EJBAccessException | AccessLocalException ejbae) {
                throw TransactionException.forbiddenAccess();
            }
            //Ew. łapanie innych, nieprzewidzianych wyzej wyjatkow
            TXCounter--;
        } while (commitedTX && TXCounter > 0);

        if (commitedTX) {
            throw TransactionException.transactionRollback();
        }

        return Response.ok().build();
    }

    @Override
    public Response registerConfirm(RegisterClientConfirmDto registerConfirmDto) {
        // TODO: dodać powtarzanie transakcji
        mokServiceInterface.confirmRegistration(registerConfirmDto.getToken());
        return Response.ok().build();
    }

    @Override
    public Response createAccount(CreateAccountDto createAccountDto) {
        // TODO: dodać powtarzanie transakcji
        Account account = accountMapper.createAccountfromCreateAccountDto(createAccountDto);
        Account registeredAccount = mokServiceInterface.createAccount(account);
        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(registeredAccount)
        ).build();
    }

    @Override
    public Response activateAccount(String login, ETagDto eTagDto) {
        // TODO: dodać powtarzanie transakcji
        Account activatedAccount = mokServiceInterface.activateAccount(login, eTagDto.getETag());
        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(activatedAccount)
        ).build();
    }

    @Override
    public Response deactivateAccount(String login, ETagDto eTagDto) {
        // TODO: dodać powtarzanie transakcji
        Account deactivatedAccount = mokServiceInterface.deactivateAccount(login, eTagDto.getETag());
        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(deactivatedAccount)
        ).build();
    }

    @Override
    public Response addAccessLevel(String login, AccessLevelDto accessLevelDto) {
        // TODO: dodać powtarzanie transakcji
        AccessLevel accessLevel = accessLevelMapper.createAccessLevelFromDto(accessLevelDto);
        Account account = mokServiceInterface.addAccessLevelToAccount(login, accessLevel);

        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)
        ).build();
    }

    @Override
    public Response removeAccessLevel(String login, String accessLevelName, String eTag) {
        // TODO: dodać powtarzanie transakcji
        Account newAccessLevelAccount = mokServiceInterface.removeAccessLevelFromAccount(login, accessLevelName, eTag);
        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(newAccessLevelAccount)
        ).build();
    }

    @Override
    public Response changeOwnPassword(ChangeOwnPasswordDto changeOwnPasswordDto) {
        // TODO: dodać powtarzanie transakcji
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
        // TODO: dodać powtarzanie transakcji
        Account account = mokServiceInterface.changeAccountPassword(
                login, changePasswordDto.getNewPassword(), changePasswordDto.getETag());

        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)
        ).build();
    }

    @Override
    public Response editOwnAccount(AccountWithAccessLevelsDto accountWithAccessLevelsDto) {
        // TODO: dodać powtarzanie transakcji
        String login = authContext.getCurrentUserLogin();
        Account update = accountMapper.createAccountFromDto(accountWithAccessLevelsDto);
        Account editedAccount = mokServiceInterface.editAccount(login, update, accountWithAccessLevelsDto.getETag());

        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(editedAccount)
        ).build();
    }

    @Override
    public Response editAccount(String login, AccountWithAccessLevelsDto accountWithAccessLevelsDto) {
        // TODO: dodać powtarzanie transakcji
        Account update = accountMapper.createAccountFromDto(accountWithAccessLevelsDto);
        Account editedAccount = mokServiceInterface.editAccount(login, update, accountWithAccessLevelsDto.getETag());
        return Response.ok(
                accountMapper.createAccountWithAccessLevelsDtoFromAccount(editedAccount)
        ).build();
    }

    @Override
    public Response login(LoginCredentialsDto loginCredentialsDto) {
        // TODO: dodać powtarzanie transakcji
        String token = mokServiceInterface.authenticate(
                loginCredentialsDto.getLogin(), loginCredentialsDto.getPassword());
        return Response.ok(token).build();
    }

    @Override
    public Response getAllAccounts(int page, int limit) {
        // TODO: dodać powtarzanie transakcji
        // TODO: zmniejszyć ilość danych o kontach na liście
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
        // TODO: dodać powtarzanie transakcji
        mokServiceInterface.resetPassword(login);
        return Response.ok().build();
    }

    @Override
    public Response resetPasswordToken(ResetPasswordTokenDto resetPasswordDto) {
        // TODO: dodać powtarzanie transakcji
        mokServiceInterface.confirmResetPassword(
                resetPasswordDto.getLogin(), resetPasswordDto.getPassword(), resetPasswordDto.getToken()
        );
        return Response.ok().build();
    }

    @Override
    public Response getOwnAccount() {
        // TODO: dodać powtarzanie transakcji
        String user = authContext.getCurrentUserLogin();
        Account account = mokServiceInterface.findAccountByLogin(user);
        return Response.ok().entity(accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)).build();
    }

    @Override
    public Response getAccount(String login) {
        // TODO: dodać powtarzanie transakcji
        Account account = mokServiceInterface.findAccountByLogin(login);
        return Response.ok(accountMapper.createAccountWithAccessLevelsDtoFromAccount(account)).build();
    }
}
