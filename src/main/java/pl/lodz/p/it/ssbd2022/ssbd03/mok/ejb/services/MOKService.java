package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services;

import io.jsonwebtoken.Claims;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractManager;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.global_services.EmailService;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ConfirmationAccountToken;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.TokenInvalidException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.access_level.AccessLevelNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.access_level.AccessLevelViolationException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountPasswordIsTheSameException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountPasswordMatchException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.TokenExpierdException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.AccessLevelFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.ActiveAccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.ResetPasswordFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.security.JWTGenerator;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Stateful
@DenyAll
@Interceptors(TrackerInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MOKService extends AbstractManager implements MOKServiceInterface, SessionSynchronization {

    @Inject
    private IdentityStoreHandler identityStoreHandler;
    @Inject
    private JWTGenerator jwtGenerator;
    @Inject
    private AccountFacade accountFacade;
    @Inject
    private HashAlgorithm hashAlgorithm;
    @Inject
    private EmailService emailConfig;
    @Inject
    private ActiveAccountFacade activeAccountFacade;
    @Inject
    private AccessLevelFacade accessLevelFacade;
    @Inject
    private ResetPasswordFacade resetPasswordFacade;


    @Override
    @PermitAll
    public String authenticate(String login, String password) {
        UsernamePasswordCredential credential = new UsernamePasswordCredential(login, new Password(password));
        CredentialValidationResult result = identityStoreHandler.validate(credential);
        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            return jwtGenerator.createJWT(result);
        }
        // TODO: zmienić na wyjątek dziedziczący po AppBaseException
        throw new ClientErrorException("Invalid username or password", 401);
    }

    /**
     * Metoda zwracająca konto o podanym loginie
     * @param login login
     * @return konto o podanym loginie
     */
    @Override
    @PermitAll
    public Account findAccountByLogin(String login) {
        return accountFacade.findByLogin(login);
    }

    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Account deactivateAccount(String login, String etag) {
        Account account = accountFacade.findByLogin(login);
        account.setActive(false);
        accountFacade.edit(account, etag);
        return account;
    }

    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Account activateAccount(String login, String etag) {
        Account account = accountFacade.findByLogin(login);
        account.setActive(true);
        accountFacade.edit(account, etag);
        return account;
    }

    @Override
    @PermitAll
    public Account editAccount(String login, Account account, String etag) {
        Account accountFromDb = accountFacade.findByLogin(login);
        accountFromDb.setFirstName(account.getFirstName());
        accountFromDb.setLastName(account.getLastName());
        accountFromDb.setLanguage(account.getLanguage());

        for (AccessLevel accessLevel : accountFromDb.getAccessLevelCollection()) {
            // ------------ DataAdministrator ------------
            if (accessLevel instanceof DataAdministrator dataAdministratorDB) {
                DataAdministrator dataAdministrator =
                        (DataAdministrator) findAccessLevelByName(account.getAccessLevelCollection(), accessLevel.getClass());
                if (dataAdministrator != null) {
                    dataAdministratorDB.setContactEmail(dataAdministrator.getContactEmail());
                    dataAdministratorDB.setPhoneNumber(dataAdministrator.getPhoneNumber());
                }
            }
            // ------------ DataSpecialist ------------
            if (accessLevel instanceof DataSpecialist dataSpecialistDB) {
                DataSpecialist dataSpecialist =
                        (DataSpecialist) findAccessLevelByName(account.getAccessLevelCollection(), accessLevel.getClass());
                if (dataSpecialist != null) {
                    dataSpecialistDB.setContactEmail(dataSpecialist.getContactEmail());
                    dataSpecialistDB.setPhoneNumber(dataSpecialist.getPhoneNumber());
                }
            }
            // ------------ DataClient ------------
            if (accessLevel instanceof DataClient dataClientDB) {
                DataClient dataClient =
                        (DataClient) findAccessLevelByName(account.getAccessLevelCollection(), accessLevel.getClass());
                if (dataClient != null) {
                    dataClientDB.setPesel(dataClient.getPesel());
                    dataClientDB.setPhoneNumber(dataClient.getPhoneNumber());
                }
            }
        }

        accountFacade.edit(accountFromDb, etag);

        return accountFromDb;
    }
    // NIE ROZŁĄCZAJ MNIE OD FUNKCJI WYŻEJ (ಥ_ಥ)
    private AccessLevel findAccessLevelByName(Collection<AccessLevel> list, Class<? extends AccessLevel> clazz) {
        for (AccessLevel accessLevel : list)
            if (accessLevel.getClass().equals(clazz))
                return accessLevel;
        return null;
    }

    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public PaginationData findAllAccounts(int page, int size, String phrase) {
        return accountFacade.findInRangeWithPhrase(page, size, phrase);
    }

    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Account changeAccountPassword(String login, String newPassword, String etag) {
        Account account = accountFacade.findByLogin(login);

        if (hashAlgorithm.verify(newPassword.toCharArray(), account.getPassword())) {
            throw new AccountPasswordIsTheSameException();
        }

        account.setPassword(hashAlgorithm.generate(newPassword.toCharArray()));
        accountFacade.edit(account, etag);
        return account;
    }

    @Override
    @PermitAll
    public Account changeAccountPassword(String login, String oldPassword, String newPassword, String etag) {
        Account account = accountFacade.findByLogin(login);

        if (!hashAlgorithm.verify(oldPassword.toCharArray(), account.getPassword())) {
            throw new AccountPasswordMatchException();
        }
        if (hashAlgorithm.verify(newPassword.toCharArray(), account.getPassword())) {
            throw new AccountPasswordIsTheSameException();
        }

        account.setPassword(hashAlgorithm.generate(newPassword.toCharArray()));
        accountFacade.edit(account, etag);
        return account;
    }

    @Override
    @PermitAll
    public Account registerAccount(Account account) {
        accountFacade.create(account);
        Instant date = Instant.now().plusSeconds(Config.REGISTER_TOKEN_EXPIRATION_SECONDS);
        String token = jwtGenerator.createJWTForEmail(account.getLogin(), date);
        ConfirmationAccountToken activeAccountToken = new ConfirmationAccountToken(account, token, date);
        activeAccountFacade.create(activeAccountToken);
        // TODO: przenieść wysyłanie maila do endpointu (z upewnienieniem się, że transakcja się powiedzie)
        emailConfig.sendEmail(
                account.getEmail(),
                "Active account - KIC",
                "Your link to active account: https://localhost:8181/active?token=" + token
                        +"\n \n or \n \n" +
                        "https://kic.agency:8403/active?token=" + token);
        return account;
    }

    @Override
    @PermitAll
    public Account confirmRegistration(String token) {
        // TODO: obsługa wyjątków z .decodeJwt()
        Claims claims = jwtGenerator.decodeJWT(token);
        ConfirmationAccountToken activeAccountToken = activeAccountFacade.findToken(claims.getSubject());
        if (activeAccountToken.getExpDate().isBefore(Instant.now())) throw new TokenExpierdException();
        Account account = accountFacade.findByLogin(claims.getSubject());
        account.setConfirmed(true);
        accountFacade.unsafeEdit(account);
        return account;
    }

    /**
     * Metoda tworząca nowe konto i zwtracająca nowo utworzone konto z bazy danych
     * @param account konto do utworzenia
     * @return utworzone konto wyszukane z bazy danych po loginie
     */
    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Account createAccount(Account account) {
        accountFacade.create(account);
        return accountFacade.findByLogin(account.getLogin());
    }

    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Account addAccessLevelToAccount(String login, AccessLevel accessLevel) {
        Account account = accountFacade.findByLogin(login);
        account.getAccessLevelCollection().forEach(al -> {
            if (accessLevel instanceof DataSpecialist
                    && al instanceof DataClient) {
                throw AccessLevelViolationException.clientCantBeSpecialist();
            }
            if (accessLevel instanceof DataClient
                    && al instanceof DataSpecialist) {
                throw AccessLevelViolationException.specialistCantBeClient();
            }
        });
        account.addAccessLevel(accessLevel);
        accessLevelFacade.create(accessLevel);
        return account;
    }

    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Account removeAccessLevelFromAccount(String login, String accessLevelName, String accessLevelEtag) {
        Account account = accountFacade.findByLogin(login);

        AccessLevel access = account.getAccessLevelCollection().stream()
                .filter(level -> level.getLevel().equals(accessLevelName))
                .findFirst()
                .orElse(null);

        if (access == null)
            throw new AccessLevelNotFoundException();

        account.removeAccessLevel(access);

        accessLevelFacade.remove(access, accessLevelEtag);

        return account;
    }

    @Override
    @PermitAll
    public Account resetPassword(String login) {
        Account account = accountFacade.findByLogin(login);
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setAccount(account);
        resetPasswordFacade.create(resetPasswordToken);
        // TODO: przenieść wysyłanie maila do endpointu (z upewnienieniem się, że transakcja się powiedzie)
        emailConfig.sendEmail(
                account.getEmail(),
                "Reset password",
                "Your link to reset password: \n"
                        + "localhost:8080/mok/resetPassword/"
                        + login + "/"
                        + hashAlgorithm.generate(resetPasswordToken.getId().toString().toCharArray())
        );
        return account;
    }

    @Override
    @PermitAll
    public Account confirmResetPassword(String login, String password, String token) {

        ResetPasswordToken resetPasswordToken =
                resetPasswordFacade.findResetPasswordToken(login);

        if (!hashAlgorithm.verify(resetPasswordToken.getId().toString().toCharArray(), token))
            throw new TokenInvalidException();


        Account account = accountFacade.findByLogin(login);
        account.setPassword(hashAlgorithm.generate(password.toCharArray()));
        accountFacade.unsafeEdit(account);
        resetPasswordFacade.unsafeRemove(resetPasswordToken);

        return account;
    }

}
