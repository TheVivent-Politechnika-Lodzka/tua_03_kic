package pl.lodz.p.it.ssbd2022.ssbd03.mok.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.AccountConfirmationToken;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.ResetPasswordToken;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.TransactionException;
import pl.lodz.p.it.ssbd2022.ssbd03.global_services.EmailService;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccessLevelMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services.MOKServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;
import pl.lodz.p.it.ssbd2022.ssbd03.security.ReCaptchaService;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.InternationalizationProvider;
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
    @Inject
    private InternationalizationProvider provider;
    @Inject
    private ReCaptchaService reCaptchaService;

    /**
     * @param registerClientDto - dane konta
     * @return Response zawierający status HTTP
     * @throws TransactionException jeśli transakcja nie zostanie zatwierdzona
     */
    @Override
    public Response register(RegisterClientDto registerClientDto) {

        reCaptchaService.checkCaptchaValidation(registerClientDto.getCaptcha());

        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        Account account = accountMapper.createAccountfromRegisterClientDto(registerClientDto);
        AccountConfirmationToken token;
        do {
            token = mokServiceInterface.registerAccount(account);
            commitedTX = mokServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        StringBuilder title = new StringBuilder();
        StringBuilder content = new StringBuilder();

        title.append(provider.getMessage("account.register.email.title"));

        content.append(provider.getMessage("account.register.email.content.localAddress"))
                .append(" https://localhost:8181/active?token=").append(token.getToken()).append(" ")
                .append(provider.getMessage("account.register.email.content.remoteAddress"))
                .append(" https://kic.agency:8403/active?token=").append(token.getToken()).append(" ");

        emailService.sendEmail(
                token.getAccount().getEmail(),
                title.toString(),
                content.toString());

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
        emailService.sendEmail(
                activatedAccount.getEmail(),
                provider.getMessage("account.unblock.email.title"),
                provider.getMessage("account.unblock.email.content"));
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
        emailService.sendEmail(deactivatedAccount.getEmail(),
                provider.getMessage("account.block.email.title"),
                provider.getMessage("account.block.email.content"));
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

        reCaptchaService.checkCaptchaValidation(changeOwnPasswordDto.getCaptcha());

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

        reCaptchaService.checkCaptchaValidation(accountWithAccessLevelsDto.getCaptcha());

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

    /**
     * @param loginCredentialsDto - dane logowania
     * @return Response zawierający status HTTP
     * @throws TransactionException jeśli transakcja nie zostanie zatwierdzona
     */
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
        StringBuilder message = new StringBuilder();

        message.append(provider.getMessage("account.resetPassword.email.content.link"))
                .append(provider.getMessage("account.resetPassword.email.content.login"))
                .append(" ").append(login)
                .append(provider.getMessage("account.resetPassword.email.content.token"))
                .append(" ").append(token.getToken());

        emailService.sendEmail(
                account.getEmail(),
                provider.getMessage("account.resetPassword.email.title"),
                message.toString()
        );

        return Response.ok().build();
    }

    /**
     * @param resetPasswordDto token
     * @return odpowiedź zawierająca status http
     * @throws TransactionException jeśli transakcja nie została zatwierdzona
     */
    @Override
    public Response resetPasswordToken(ResetPasswordTokenDto resetPasswordDto) {
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            mokServiceInterface.confirmResetPassword(
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
