package pl.lodz.p.it.ssbd2022.ssbd03.mok.cdi.endpoints;

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
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.CreateAccountDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.LoginCredentialsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.LoginResponseDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.RefreshTokenDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.RegisterClientConfirmDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.RegisterClientDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.ResetPasswordTokenDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services.MOKServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;
import pl.lodz.p.it.ssbd2022.ssbd03.security.ReCaptchaService;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.InternationalizationProvider;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import javax.annotation.security.DenyAll;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@DenyAll
@Path("/mok")
@TransactionAttribute(TransactionAttributeType.NEVER)
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

        Account account = accountMapper.createAccountfromRegisterClientDto(registerClientDto);
        AccountConfirmationToken token = repeat(mokServiceInterface, () -> mokServiceInterface.registerAccount((account)));

        StringBuilder content = new StringBuilder();

        String title = provider.getMessage("account.register.email.title");

        content.append(provider.getMessage("account.register.email.content.localAddress")
                .replace("{baseUrl}", Config.WEBSITE_URL)
                .replace("{token}", token.getToken())
        );

        emailService.sendEmail(
                token.getAccount().getEmail(),
                title,
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

        repeat(mokServiceInterface, () -> mokServiceInterface.confirmRegistration(registerConfirmDto.getToken()));

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
        Account registeredAccount = repeat(mokServiceInterface, () -> mokServiceInterface.createAccount(account));

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(registeredAccount);

        return Response.ok(acc).tag(tagger.tag(acc)).build();
    }

    @Override
    public Response activateAccount(String login) {
        tagger.verifyTag();

        Account activatedAccount = repeat(mokServiceInterface, () -> mokServiceInterface.activateAccount(login));

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

        Account deactivatedAccount = repeat(mokServiceInterface, () -> mokServiceInterface.deactivateAccount(login));

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
        Account account = repeat(mokServiceInterface, () -> mokServiceInterface.addAccessLevelToAccount(login, accessLevel));

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(account);

        return Response.ok(acc).tag(tagger.tag(acc)).build();
    }

    @Override
    public Response removeAccessLevel(String login, String accessLevelName) {
        tagger.verifyTag();

        Account newAccessLevelAccount = repeat(mokServiceInterface, () -> mokServiceInterface.removeAccessLevelFromAccount(login, accessLevelName));

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(newAccessLevelAccount);

        return Response.ok(acc).tag(tagger.tag(acc)).build();
    }

    @Override
    public Response changeOwnPassword(ChangeOwnPasswordDto changeOwnPasswordDto) {
        tagger.verifyTag(changeOwnPasswordDto);

        if (!Config.DEBUG)
            reCaptchaService.checkCaptchaValidation(changeOwnPasswordDto.getCaptcha());

        String login = authContext.getCurrentUserLogin();
        Account account = repeat(mokServiceInterface, () -> mokServiceInterface.changeAccountPassword(
                login,
                changeOwnPasswordDto.getOldPassword(),
                changeOwnPasswordDto.getNewPassword()
        ));

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(account);

        return Response.ok(acc).tag(tagger.tag(acc)).build();
    }

    @Override
    public Response changePassword(String login, ChangePasswordDto changePasswordDto) {
        tagger.verifyTag(changePasswordDto);

        Account account = repeat(mokServiceInterface, () -> mokServiceInterface.changeAccountPassword(
                login,
                changePasswordDto.getNewPassword()
        ));

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
        Account editedAccount = repeat(mokServiceInterface, () -> mokServiceInterface.editAccount(login, update));

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(editedAccount);

        return Response.ok(acc).tag(tagger.tag(acc)).build();
    }

    @Override
    public Response editAccount(String login, AccountWithAccessLevelsDto accountWithAccessLevelsDto) {
        tagger.verifyTag(accountWithAccessLevelsDto);

        Account update = accountMapper.createAccountFromDto(accountWithAccessLevelsDto);
        Account editedAccount = repeat(mokServiceInterface, () -> mokServiceInterface.editAccount(login, update));

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(editedAccount);

        return Response.ok().entity(acc).tag(tagger.tag(acc)).build();
    }

    /**
     * @param loginCredentialsDto - dane logowania
     * @return Response zawierający status HTTP, accessToken oraz refreshToken
     * @throws TransactionException jeśli transakcja nie zostanie zatwierdzona
     */
    @Override
    public Response login(LoginCredentialsDto loginCredentialsDto) {
        String accessToken = repeat(mokServiceInterface, () -> mokServiceInterface.authenticate(
                loginCredentialsDto.getLogin(),
                loginCredentialsDto.getPassword()
        ));

        // pobranie refreshToken
        String refreshToken = repeat(mokServiceInterface, () -> mokServiceInterface.createRefreshToken(loginCredentialsDto.getLogin()));

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
        LoginResponseDto tokens = repeat(mokServiceInterface, () -> mokServiceInterface.refreshToken(refreshTokenDto.getRefreshToken()));

        return Response.ok(tokens).build();
    }

    @Override
    public Response getAllAccounts(int page, int limit, String phrase) {
        PaginationData paginationData = repeat(mokServiceInterface, () -> mokServiceInterface.findAllAccounts(page, limit, phrase));

        List<Account> accounts = paginationData.getData();
        List<AccountWithAccessLevelsDto> accountsDTO = accountMapper.createListOfAccountWithAccessLevelDTO(accounts);
        paginationData.setData(accountsDTO);
        return Response.ok().entity(paginationData).build();
    }

    @Override
    public Response resetPassword(String login) {
        ResetPasswordToken token = repeat(mokServiceInterface, () -> mokServiceInterface.resetPassword(login));

        Account account = token.getAccount();

        String message = provider.getMessage("account.resetPassword.email.content.link")
                .replace("{token}", token.getToken())
                .replace("{baseUrl}", Config.WEBSITE_URL) +
                provider.getMessage("account.resetPassword.email.content.login") +
                " " + login;

        emailService.sendEmail(
                account.getEmail(),
                provider.getMessage("account.resetPassword.email.title"),
                message
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

        return Response.ok().build();
    }

    @Override
    public Response getOwnAccount() {
        String user = authContext.getCurrentUserLogin();
        Account account = repeat(mokServiceInterface, () -> mokServiceInterface.findAccountByLogin(user));

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(account);

        return Response.ok().entity(acc).tag(tagger.tag(acc)).build();
    }

    @Override
    public Response getAccount(String login) {
        Account account = repeat(mokServiceInterface, () -> mokServiceInterface.findAccountByLogin(login));

        AccountWithAccessLevelsDto acc = accountMapper.createAccountWithAccessLevelsDtoFromAccount(account);

        return Response.ok().entity(acc).tag(tagger.tag(acc)).build();
    }


}
