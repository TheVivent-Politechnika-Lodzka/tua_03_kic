package pl.lodz.p.it.ssbd2022.ssbd03.mok.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.TransactionException;
import pl.lodz.p.it.ssbd2022.ssbd03.global_services.EmailService;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccessLevelMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services.MOKServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;
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
    @Inject
    private EmailService emailService;
    @Inject
    private HashAlgorithm hashAlgorithm;

    /**
     * @param registerClientDto - dane konta
     * @return Response zawierający status HTTP
     * @throws TransactionException jeśli transakcja nie zostanie zatwierdzona
     */
    @Override
    public Response register(RegisterClientDto registerClientDto) {
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        Account account = accountMapper.createAccountfromRegisterClientDto(registerClientDto);
        String token;
        do {
            token = mokServiceInterface.registerAccount(account);
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }
        emailService.sendEmail(
                account.getEmail(),
                "Active account - KIC",
                "Your link to active account: https://localhost:8181/active?token=" + token
                        +"\n \n or \n \n" +
                        "https://kic.agency:8403/active?token=" + token);

        return Response.ok().build();
    }

    /**
     * @param registerConfirmDto token
     * @return Response zawierający status HTTP
     * @throws TransactionException jeśli transakcja nie zostanie zatwierdzona
     */
    @Override
    public Response confirmRegistration(RegisterClientConfirmDto registerConfirmDto) {
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

    /**
     * @param createAccountDto dane konta do utworzenia
     * @return odpowiedź zawierająca status http
     * @throws TransactionException jeśli transakcja nie została zatwierdzona
     */
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
 //      todo I18n
        emailService.sendEmail(
                activatedAccount.getEmail(),
                "KIC - Twoje konto zostalo odblokowane",
                "Informujemy, \u017ce Twoje konto zosta\u0142o odblokowane. \n " +
                        "Mo\u017cesz teraz zalogowa\u0107 si\u0119 na swoje konto.");
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
//      todo I18n
        emailService.sendEmail(deactivatedAccount.getEmail(),
                "KIC - Twoje konto zostalo zablokowane",
                "Informujemy, \u017ce Twoje konto zosta\u0142o zablokowane\n " +
                        "Po wi\u0119cej inormacji skontaktuj si\u0119 z pomoc\u0105 " +
                        "techniczn\u0105: szuryssbd@gmail.com.");
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
    public Response getAllAccounts(int page, int limit, String phrase) {
        // TODO: zmniejszyć ilość danych o kontach na liście
        PaginationData paginationData;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            paginationData = mokServiceInterface.findAllAccounts(page, limit, phrase);
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
        ResetPasswordToken token;
        do {
            token = mokServiceInterface.resetPassword(login);
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }
        Account account = token.getAccount();
        //TODO I18n
        emailService.sendEmail(
                account.getEmail(),
                "Reset password",
                "Your link to reset password: \n"
                        + "localhost:8080/mok/reset-password-token \n"
                        + "Your data to reset password: \nlogin: " + login + "\ntoken:"
                        + hashAlgorithm.generate(token.getId().toString().toCharArray())
        );

        return Response.ok().build();
    }

    /**
     * @param registerConfirmDto token
     * @return odpowiedź zawierająca status http
     * @throws TransactionException jeśli transakcja nie została zatwierdzona
     */
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
