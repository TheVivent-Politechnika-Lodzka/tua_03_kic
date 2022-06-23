package pl.lodz.p.it.ssbd2022.ssbd03.mok.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.AccountConfirmationToken;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.ResetPasswordToken;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.TransactionException;
import pl.lodz.p.it.ssbd2022.ssbd03.global_services.EmailService;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccessLevelMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.ChangeOwnPasswordDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.ChangePasswordDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services.MOKServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;
import pl.lodz.p.it.ssbd2022.ssbd03.security.ReCaptchaService;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.InternationalizationProvider;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;

@RequestScoped
@DenyAll
@Path("/mok")
public class MOKEndpoint extends AbstractEndpoint implements MOKEndpointInterface {

    private static final long serialVersionUID = 1L;

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
    private InternationalizationProvider provider;
    @Inject
    private ReCaptchaService reCaptchaService;
    @Inject
    private Tagger tagger;

    /**
     * @param registerClientDto - dane konta
     * @return Response zawierający status HTTP
     * @throws TransactionException jeśli transakcja nie zostanie zatwierdzona
     */
    @Override
    public Response register(RegisterClientDto registerClientDto) {

        if (!Config.DEBUG)
            reCaptchaService.checkCaptchaValidation(registerClientDto.getCaptcha());

        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        Account account = accountMapper.createAccountfromRegisterClientDto(registerClientDto);
        AccountConfirmationToken token;
        token = repeat(mokServiceInterface, () -> mokServiceInterface.registerAccount((account)));
//        );
//        do {
//            token = mokServiceInterface.registerAccount(account);
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }

        StringBuilder title = new StringBuilder();
        StringBuilder content = new StringBuilder();

        title.append(provider.getMessage("account.register.email.title"));

        content.append(provider.getMessage("account.register.email.content.localAddress")
                .replace("{baseUrl}", Config.WEBSITE_URL)
                .replace("{token}", token.getToken())
        );

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
        repeat(mokServiceInterface, () -> mokServiceInterface.confirmRegistration(registerConfirmDto.getToken()));

//        do {
//            mokServiceInterface.confirmRegistration(registerConfirmDto.getToken());
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }

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

        registeredAccount = mokServiceInterface.createAccount(account);

//        int TXCounter = Config.MAX_TX_RETRIES;
//        boolean commitedTX;
//        do {
//            registeredAccount = mokServiceInterface.createAccount(account);
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(registeredAccount);

        return Response.ok(acc).tag(tagger.tag(acc)).build();
    }

    @Override
    public Response activateAccount(String login) {
        tagger.verifyTag();

        Account activatedAccount;

        activatedAccount = repeat(mokServiceInterface, () -> mokServiceInterface.activateAccount(login));

//        int TXCounter = Config.MAX_TX_RETRIES;
//        boolean commitedTX;
//        do {
//            activatedAccount = mokServiceInterface.activateAccount(login);
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }


        emailService.sendEmail(
                activatedAccount.getEmail(),
                provider.getMessage("account.unblock.email.title"),
                provider.getMessage("account.unblock.email.content"));

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(activatedAccount);

        return Response.ok(acc).tag(tagger.tag(acc)).build();
    }

    @Override
    public Response deactivateAccount(String login) {
        tagger.verifyTag();

        Account deactivatedAccount;

        deactivatedAccount = repeat(mokServiceInterface, () -> mokServiceInterface.deactivateAccount(login));

//        int TXCounter = Config.MAX_TX_RETRIES;
//        boolean commitedTX;
//        do {
//            deactivatedAccount = mokServiceInterface.deactivateAccount(login);
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }
        emailService.sendEmail(deactivatedAccount.getEmail(),
                provider.getMessage("account.block.email.title"),
                provider.getMessage("account.block.email.content"));

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(deactivatedAccount);

        return Response.ok(acc).tag(tagger.tag(acc)).build();
    }

    @Override
    public Response addAccessLevel(String login, AccessLevelDto accessLevelDto) {
        tagger.verifyTag();

        AccessLevel accessLevel = accessLevelMapper.createAccessLevelFromDto(accessLevelDto);
        Account account;

        account = repeat(mokServiceInterface, () -> mokServiceInterface.addAccessLevelToAccount(login, accessLevel));

//        int TXCounter = Config.MAX_TX_RETRIES;
//        boolean commitedTX;
//        do {
//            account = mokServiceInterface.addAccessLevelToAccount(login, accessLevel);
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(account);

        return Response.ok(acc).tag(tagger.tag(acc)).build();
    }

    @Override
    public Response removeAccessLevel(String login, String accessLevelName) {
        tagger.verifyTag();

        Account newAccessLevelAccount;

        newAccessLevelAccount = repeat(mokServiceInterface, () -> mokServiceInterface.removeAccessLevelFromAccount(login, accessLevelName));

//        int TXCounter = Config.MAX_TX_RETRIES;
//        boolean commitedTX;
//        do {
//            newAccessLevelAccount = mokServiceInterface.removeAccessLevelFromAccount(login, accessLevelName);
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(newAccessLevelAccount);

        return Response.ok(acc).tag(tagger.tag(acc)).build();
    }

    @Override
    public Response changeOwnPassword(ChangeOwnPasswordDto changeOwnPasswordDto) {
        tagger.verifyTag(changeOwnPasswordDto);

        if (!Config.DEBUG)
            reCaptchaService.checkCaptchaValidation(changeOwnPasswordDto.getCaptcha());

        String login = authContext.getCurrentUserLogin();
        Account account;

        account = repeat(mokServiceInterface, () -> mokServiceInterface.changeAccountPassword(
                login,
                changeOwnPasswordDto.getOldPassword(),
                changeOwnPasswordDto.getNewPassword()
        ));

//        int TXCounter = Config.MAX_TX_RETRIES;
//        boolean commitedTX;
//        do {
//            account = mokServiceInterface.changeAccountPassword(
//                    login,
//                    changeOwnPasswordDto.getOldPassword(),
//                    changeOwnPasswordDto.getNewPassword()
//            );
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(account);

        return Response.ok(acc).tag(tagger.tag(acc)).build();
    }

    @Override
    public Response changePassword(String login, ChangePasswordDto changePasswordDto) {
        tagger.verifyTag(changePasswordDto);

        Account account;

        account = repeat(mokServiceInterface, () -> mokServiceInterface.changeAccountPassword(
                login,
                changePasswordDto.getNewPassword()
        ));
//        int TXCounter = Config.MAX_TX_RETRIES;
//        boolean commitedTX;
//        do {
//            account = mokServiceInterface.changeAccountPassword(
//                    login,
//                    changePasswordDto.getNewPassword()
//            );
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(account);

        return Response.ok(acc).tag(tagger.tag(acc)).build();
    }

    @Override
    public Response editOwnAccount(AccountWithAccessLevelsDto accountWithAccessLevelsDto) {
        tagger.verifyTag(accountWithAccessLevelsDto);

        if (!Config.DEBUG)
            reCaptchaService.checkCaptchaValidation(accountWithAccessLevelsDto.getCaptcha());

        String login = authContext.getCurrentUserLogin();
        Account update = accountMapper.createAccountFromDto(accountWithAccessLevelsDto);
        Account editedAccount;

        editedAccount = repeat(mokServiceInterface, () -> mokServiceInterface.editAccount(login, update));

//        int TXCounter = Config.MAX_TX_RETRIES;
//        boolean commitedTX;
//        do {
//            editedAccount = mokServiceInterface.editAccount(login, update);
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(editedAccount);

        return Response.ok(acc).tag(tagger.tag(acc)).build();
    }

    @Override
    public Response editAccount(String login, AccountWithAccessLevelsDto accountWithAccessLevelsDto) {
        tagger.verifyTag(accountWithAccessLevelsDto);

        Account update = accountMapper.createAccountFromDto(accountWithAccessLevelsDto);
        Account editedAccount;

        editedAccount = repeat(mokServiceInterface, () -> mokServiceInterface.editAccount(login, update));

//        int TXCounter = Config.MAX_TX_RETRIES;
//        boolean commitedTX;
//        do {
//            editedAccount = mokServiceInterface.editAccount(login, update);
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(editedAccount);

        return Response.ok(acc).tag(tagger.tag(acc)).build();
    }

    /**
     * @param loginCredentialsDto - dane logowania
     * @return Response zawierający status HTTP, accessToken oraz refreshToken
     * @throws TransactionException jeśli transakcja nie zostanie zatwierdzona
     */
    @Override
    public Response login(LoginCredentialsDto loginCredentialsDto) {
        String accessToken; // zawiera accessToken i refreshToken
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;

        accessToken = repeat(mokServiceInterface, () -> mokServiceInterface.authenticate(
                loginCredentialsDto.getLogin(),
                loginCredentialsDto.getPassword()
        ));

        // pobranie accessToken
//        do {
//            accessToken = mokServiceInterface.authenticate(
//                    loginCredentialsDto.getLogin(),
//                    loginCredentialsDto.getPassword()
//            );
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }

        TXCounter = Config.MAX_TX_RETRIES;
        // pobranie refreshToken
        String refreshToken;

        refreshToken = repeat(mokServiceInterface, () -> mokServiceInterface.createRefreshToken(loginCredentialsDto.getLogin()));

//        do {
//            refreshToken = mokServiceInterface.createRefreshToken(loginCredentialsDto.getLogin());
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }

        LoginResponseDto jwtStruct = new LoginResponseDto();
        jwtStruct.setAccessToken(accessToken);
        jwtStruct.setRefreshToken(refreshToken);

        return Response.ok(jwtStruct).build();
    }

    /**
     * Endpoint umożliwiający odświeżanie tokenu
     *
     * @param refreshTokenDto
     * @return
     */
    @Override
    public Response refreshToken(RefreshTokenDto refreshTokenDto) {
        LoginResponseDto tokens; // zawiera accessToken i refreshToken

        tokens = repeat(mokServiceInterface, () -> mokServiceInterface.refreshToken(refreshTokenDto.getRefreshToken()));

//        int TXCounter = Config.MAX_TX_RETRIES;
//        boolean commitedTX;
//        do {
//            tokens = mokServiceInterface.refreshToken(refreshTokenDto.getRefreshToken());
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }
        return Response.ok(tokens).build();
    }

    @Override
    public Response getAllAccounts(int page, int limit, String phrase) {
        // TODO: zmniejszyć ilość danych o kontach na liście
        PaginationData paginationData;

        paginationData = repeat(mokServiceInterface, () -> mokServiceInterface.findAllAccounts(page, limit, phrase));

//        int TXCounter = Config.MAX_TX_RETRIES;
//        boolean commitedTX;
//        do {
//            paginationData = mokServiceInterface.findAllAccounts(page, limit, phrase);
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }

        List<Account> accounts = paginationData.getData();
        List<AccountWithAccessLevelsDto> accountsDTO = accountMapper.createListOfAccountWithAccessLevelDTO(accounts);
        paginationData.setData(accountsDTO);
        return Response.ok().entity(paginationData).build();
    }

    @Override
    public Response resetPassword(String login) {
        ResetPasswordToken token;

        token = repeat(mokServiceInterface, () -> mokServiceInterface.resetPassword(login));

//        int TXCounter = Config.MAX_TX_RETRIES;
//        boolean commitedTX;
//        do {
//            token = mokServiceInterface.resetPassword(login);
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }
        Account account = token.getAccount();
        StringBuilder message = new StringBuilder();

        message.append(
                        provider.getMessage("account.resetPassword.email.content.link")
                                .replace("{token}", token.getToken())
                                .replace("{baseUrl}", Config.WEBSITE_URL)
                )
                .append(provider.getMessage("account.resetPassword.email.content.login"))
                .append(" ").append(login);

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

        repeat(mokServiceInterface, () -> mokServiceInterface.confirmResetPassword(
                resetPasswordDto.getPassword(),
                resetPasswordDto.getToken()
        ));

//        int TXCounter = Config.MAX_TX_RETRIES;
//        boolean commitedTX;
//        do {
//            mokServiceInterface.confirmResetPassword(
//                    resetPasswordDto.getPassword(),
//                    resetPasswordDto.getToken()
//            );
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);

//        if (!commitedTX) {
//            throw new TransactionException();
//        }

        return Response.ok().build();
    }

    @Override
    public Response getOwnAccount() {
        String user = authContext.getCurrentUserLogin();
        Account account;

        account = repeat(mokServiceInterface, () -> mokServiceInterface.findAccountByLogin(user));

//        int TXCounter = Config.MAX_TX_RETRIES;
//        boolean commitedTX;
//        do {
//            account = mokServiceInterface.findAccountByLogin(user);
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(account);

        return Response.ok().entity(acc).tag(tagger.tag(acc)).build();
    }

    @Override
    public Response getAccount(String login) {
        Account account;

        account = repeat(mokServiceInterface, () -> mokServiceInterface.findAccountByLogin(login));

//        int TXCounter = Config.MAX_TX_RETRIES;
//        boolean commitedTX;
//        do {
//            account = mokServiceInterface.findAccountByLogin(login);
//            commitedTX = mokServiceInterface.isLastTransactionCommited();
//        } while (!commitedTX && --TXCounter > 0);
//
//        if (!commitedTX) {
//            throw new TransactionException();
//        }

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(account);

        return Response.ok(acc).tag(tagger.tag(acc)).build();
    }


}
