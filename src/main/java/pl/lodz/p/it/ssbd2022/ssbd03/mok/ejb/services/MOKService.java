package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services;

import io.jsonwebtoken.*;
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
import jakarta.servlet.http.HttpServletRequest;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.AccountConfirmationToken;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.RefreshToken;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.ResetPasswordToken;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.access_level.AccessLevelNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.access_level.AccessLevelViolationException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountPasswordIsTheSameException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountPasswordMatchException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountStatusException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.InvalidCredentialException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.InAppOptimisticLockException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.token.TokenDecodeInvalidException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.token.TokenExpiredException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.token.TokenInvalidException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.*;
import pl.lodz.p.it.ssbd2022.ssbd03.security.JWTGenerator;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.LoginResponseDto;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Taggable;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateful
@DenyAll
@Interceptors(TrackerInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MOKService extends AbstractService implements MOKServiceInterface, SessionSynchronization {

    protected static final Logger LOGGER = Logger.getGlobal();
    @Inject
    private IdentityStoreHandler identityStoreHandler;
    @Inject
    private JWTGenerator jwtGenerator;
    @Inject
    private AccountFacade accountFacade;
    @Inject
    private RefreshTokenFacade refreshTokenFacade;
    @Inject
    private HashAlgorithm hashAlgorithm;
    @Inject
    private AccountConfirmationFacade accountConfirmationFacade;
    @Inject
    private AccessLevelFacade accessLevelFacade;
    @Inject
    private ResetPasswordFacade resetPasswordFacade;
    @Inject
    private HttpServletRequest httpServletRequest;

    /**
     * Metoda uwierzytelnia użytkownika i zwraca token
     *
     * @param login    Login konta, które ma zostać uwierzytelnione
     * @param password Hasło konta, które ma zostać uwierzytelnione
     * @return token użytkownika uwierzytelnionego
     */
    @Override
    @RolesAllowed(Roles.ANONYMOUS)
    public String authenticate(String login, String password) {
        UsernamePasswordCredential credential = new UsernamePasswordCredential(login, new Password(password));
        CredentialValidationResult result = identityStoreHandler.validate(credential);
        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            LOGGER.log(Level.INFO, "User {0} has been authenticated from ip {1}", new Object[]{
                    login, httpServletRequest.getRemoteAddr()
            });
            return jwtGenerator.createJWT(result);
        }
        throw new InvalidCredentialException();

    }

//    @PermitAll
    @Override
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    public String createRefreshToken(String login) {
        RefreshToken refreshToken = new RefreshToken();
        Account account = accountFacade.findByLogin(login);
        refreshToken.setAccount(account);
        refreshToken.setToken(jwtGenerator.createRefreshJWT(login));
        refreshTokenFacade.create(refreshToken);
        return refreshToken.getToken();
    }


    /**
     * Metoda uwierzytelnia użytkownika i zwraca token
     *
     * @param refreshToken
     * @return JWTStruct z odświeżonym tokenem
     */
    @Override
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    public LoginResponseDto refreshToken(String refreshToken) {
        RefreshToken refreshTokenObject = refreshTokenFacade.findByToken(refreshToken);

        List<String> accessLevels = new ArrayList<>();
        refreshTokenObject.getAccount()
                .getAccessLevelCollection().forEach(
                        accessLevel -> accessLevels.add(accessLevel.getLevel()
                        ));
        String accessToken = jwtGenerator.createJWT(refreshTokenObject.getAccount().getLogin(), accessLevels);

        LoginResponseDto jwtStruct = new LoginResponseDto();
        jwtStruct.setAccessToken(accessToken);
        jwtStruct.setRefreshToken(refreshTokenObject.getToken());
        return jwtStruct;
    }

    /**
     * Metoda zwracająca konto o podanym loginie
     *
     * @param login login
     * @return konto o podanym loginie
     */
    @Override
    @RolesAllowed(Roles.AUTHENTICATED)
    public Account findAccountByLogin(String login) {
        return accountFacade.findByLogin(login);
    }

    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Account deactivateAccount(String login) {
        Account account = accountFacade.findByLogin(login);
        if (!account.isActive()) throw AccountStatusException.accountAlreadyInactive();
        account.setActive(false);
        accountFacade.edit(account);
        return account;
    }

    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Account activateAccount(String login) {
        Account account = accountFacade.findByLogin(login);
        if (account.isActive()) throw AccountStatusException.accountArleadyActive();
        account.setActive(true);
        accountFacade.edit(account);
        return account;
    }

    @Override
    @RolesAllowed(Roles.AUTHENTICATED)
    public Account editAccount(String login, Account account) {
        Account accountFromDb = accountFacade.findByLogin(login);
        accountFromDb.setFirstName(account.getFirstName());
        accountFromDb.setLastName(account.getLastName());
        boolean anyAccessLevelHasChanged = false;

        for (AccessLevel accessLevel : accountFromDb.getAccessLevelCollection()) {
            // ------------ DataAdministrator ------------
            if (accessLevel instanceof DataAdministrator dataAdministratorDB) {
                DataAdministrator dataAdministrator =
                        (DataAdministrator) findAccessLevelByName(account.getAccessLevelCollection(), accessLevel.getClass());
                if (dataAdministrator != null) {
                    dataAdministratorDB.setContactEmail(dataAdministrator.getContactEmail());
                    dataAdministratorDB.setPhoneNumber(dataAdministrator.getPhoneNumber());
                    anyAccessLevelHasChanged = true;
                }
            }
            // ------------ DataSpecialist ------------
            if (accessLevel instanceof DataSpecialist dataSpecialistDB) {
                DataSpecialist dataSpecialist =
                        (DataSpecialist) findAccessLevelByName(account.getAccessLevelCollection(), accessLevel.getClass());
                if (dataSpecialist != null) {
                    dataSpecialistDB.setContactEmail(dataSpecialist.getContactEmail());
                    dataSpecialistDB.setPhoneNumber(dataSpecialist.getPhoneNumber());
                    anyAccessLevelHasChanged = true;
                }
            }
            // ------------ DataClient ------------
            if (accessLevel instanceof DataClient dataClientDB) {
                DataClient dataClient =
                        (DataClient) findAccessLevelByName(account.getAccessLevelCollection(), accessLevel.getClass());
                if (dataClient != null) {
                    dataClientDB.setPesel(dataClient.getPesel());
                    dataClientDB.setPhoneNumber(dataClient.getPhoneNumber());
                    anyAccessLevelHasChanged = true;
                }
            }
        }

        accountFacade.edit(accountFromDb);
        if (anyAccessLevelHasChanged) {
            accountFacade.forceVersionIncrement(accountFromDb);
        }

        return accountFromDb;
    }

    // NIE ROZŁĄCZAJ MNIE OD FUNKCJI WYŻEJ (ಥ_ಥ)
    @RolesAllowed(Roles.AUTHENTICATED)
    private AccessLevel findAccessLevelByName(Collection<AccessLevel> list, Class<? extends AccessLevel> clazz) {
        for (AccessLevel accessLevel : list)
            if (accessLevel.getClass().equals(clazz))
                return accessLevel;
        return null;
    }

    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public PaginationData findAllAccounts(int page, int size, String phrase) {
        if (page == 0 || size == 0) {
            throw new InvalidParametersException();
        }
        return accountFacade.findInRangeWithPhrase(page, size, phrase);
    }

    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Account changeAccountPassword(String login, String newPassword) {
        Account account = accountFacade.findByLogin(login);

        if (hashAlgorithm.verify(newPassword.toCharArray(), account.getPassword())) {
            throw new AccountPasswordIsTheSameException();
        }

        account.setPassword(hashAlgorithm.generate(newPassword.toCharArray()));
        accountFacade.edit(account);
        return account;
    }

    @Override
    @RolesAllowed(Roles.AUTHENTICATED)
    public Account changeAccountPassword(String login, String oldPassword, String newPassword) {
        Account account = accountFacade.findByLogin(login);

        if (!hashAlgorithm.verify(oldPassword.toCharArray(), account.getPassword())) {
            throw new AccountPasswordMatchException();
        }
        if (hashAlgorithm.verify(newPassword.toCharArray(), account.getPassword())) {
            throw new AccountPasswordIsTheSameException();
        }

        account.setPassword(hashAlgorithm.generate(newPassword.toCharArray()));
        accountFacade.edit(account);
        return account;
    }


    /**
     * Metoda tworzy konto i zwraca token z bazy danych, pozwalający aktywować konto
     *
     * @param account - dane konta
     * @return token z bazy danych, pozwalający aktywować konto
     */
    @Override
    @RolesAllowed(Roles.ANONYMOUS)
    public AccountConfirmationToken registerAccount(Account account) {
        accountFacade.create(account);
        String token = jwtGenerator.createRegistrationJWT(account.getLogin());
        AccountConfirmationToken accountConfirmationToken = new AccountConfirmationToken();
        accountConfirmationToken.setToken(token);
        accountConfirmationToken.setAccount(account);
        accountConfirmationFacade.create(accountConfirmationToken);
        return accountConfirmationToken;
    }

    /**
     * Metoda wyszukuje użytkownika w bazie danych i potwierdza jego konto
     *
     * @param token - token konta, które ma zostać potwierdzone
     * @return potwierdzone konto
     * @throws TokenDecodeInvalidException - w momencie, gdy token został źle utworzony
     * @throws TokenExpiredException       - w momencie, gdy token wygasł
     */
    @Override
    @RolesAllowed(Roles.ANONYMOUS)
    public Account confirmRegistration(String token) {
        Claims claims;
        try {
            claims = jwtGenerator.decodeJWT(token);
        } catch (SignatureException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            throw new TokenDecodeInvalidException();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        }
        String login = claims.getSubject();
        AccountConfirmationToken accountConfirmationToken = accountConfirmationFacade.findToken(login);
        if (!accountConfirmationToken.getToken().equals(token)) {
            throw new TokenInvalidException();
        }

        Account account = accountFacade.findByLogin(login);
        account.setConfirmed(true);
        accountFacade.unsafeEdit(account);
        accountConfirmationFacade.unsafeRemove(accountConfirmationToken);
        return account;
    }

    /**
     * Metoda tworząca nowe konto i zwtracająca nowo utworzone konto z bazy danych
     *
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
        accountFacade.forceVersionIncrement(account);
        return account;
    }

    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Account removeAccessLevelFromAccount(String login, String accessLevelName) {
        Account account = accountFacade.findByLogin(login);

        AccessLevel access = account.getAccessLevelCollection().stream()
                .filter(level -> level.getLevel().equals(accessLevelName))
                .findFirst()
                .orElse(null);

        if (access == null)
            throw new AccessLevelNotFoundException();

        account.removeAccessLevel(access);
        accountFacade.edit(account);
        accountFacade.forceVersionIncrement(account);

        return account;
    }

    @Override
    @RolesAllowed(Roles.ANONYMOUS)
    public ResetPasswordToken resetPassword(String login) {
        Account account = accountFacade.findByLogin(login);
        String token = jwtGenerator.createResetPasswordJWT(login);
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setAccount(account);
        resetPasswordToken.setToken(token);
        resetPasswordFacade.create(resetPasswordToken);
        return resetPasswordToken;
    }

    @Override
    @RolesAllowed(Roles.ANONYMOUS)
    public Account confirmResetPassword(String password, String token) {
        Claims claims;
        try {
            claims = jwtGenerator.decodeJWT(token);
        } catch (SignatureException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            throw new TokenDecodeInvalidException();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        }
        String login = claims.getSubject();
        ResetPasswordToken resetPasswordToken = resetPasswordFacade.findToken(login);
        if (!resetPasswordToken.getToken().equals(token)) {
            throw new TokenInvalidException();
        }


        Account account = accountFacade.findByLogin(login);
        account.setPassword(hashAlgorithm.generate(password.toCharArray()));
        accountFacade.unsafeEdit(account);
        resetPasswordFacade.unsafeRemove(resetPasswordToken);

        return account;
    }

}
