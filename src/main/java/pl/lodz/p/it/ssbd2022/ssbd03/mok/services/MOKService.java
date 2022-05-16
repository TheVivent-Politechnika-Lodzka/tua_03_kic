package pl.lodz.p.it.ssbd2022.ssbd03.mok.services;

import io.jsonwebtoken.Claims;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.ws.rs.ClientErrorException;
import pl.lodz.p.it.ssbd2022.ssbd03.common.EmailConfig;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ActiveAccountToken;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountPasswordIsTheSameException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountPasswordMatchException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.TokenExpierdException;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.ResetPasswordDTO;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataAdministratorDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataClientDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataSpecialistDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.ActiveAccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.ResetPasswordFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.security.JWTGenerator;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class MOKService {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private ResetPasswordFacade resetPasswordFacade;

    @Inject
    private ActiveAccountFacade activeAccountFacade;

    @Inject
    private IdentityStoreHandler indentityStoreHandler;

    @Inject
    private JWTGenerator jwtGenerator;

    @Inject
    private EmailConfig emailConfig;

    @Inject
    private HashAlgorithm hashAlgorithm;

    public String authenticate(Credential credential) {
        CredentialValidationResult result = indentityStoreHandler.validate(credential);
        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            return jwtGenerator.createJWT(result);
        }
        throw new ClientErrorException("Invalid username or password", 401);
    }

    public Account findByLogin(String login) {
        return accountFacade.findByLogin(login);
    }

    public void deactivate(String login) {
        Account account = accountFacade.findByLogin(login);
        account.setActive(false);
        accountFacade.unsafeEdit(account);
    }

    public void activate(String login) {
        Account account = accountFacade.findByLogin(login);
        account.setActive(true);
        accountFacade.unsafeEdit(account);
    }

    public Account edit(String login, AccountWithAccessLevelsDto accountDto) {
        Account account = this.findByLogin(login);
        account.setFirstName(accountDto.getFirstName());
        account.setLastName(accountDto.getLastName());
        // tu update access levels
        for (AccessLevelDto accessLevelDto : accountDto.getAccessLevels()) {
            for (AccessLevel accessLevel : account.getAccessLevelCollection()) {
                updateAccessLevelData(accessLevel, accessLevelDto);
            }
        }

        accountFacade.edit(account, accountDto.getTag());
        return account;
    }

    public PaginationData findInRange(int page, int limit) {
        PaginationData paginationData = accountFacade.findInRange(page, limit);
        return paginationData;
    }

    // TODO: ZNALEŹĆ LEPSZY SPOSÓB
    private void updateAccessLevelData(AccessLevel accessLevel, AccessLevelDto accessLevelDto) {
        try {
            DataClient dataClient =  (DataClient) accessLevel;
            DataClientDto dataClientDto = (DataClientDto) accessLevelDto;
            dataClient.setPesel(dataClientDto.getPesel());
            dataClient.setPhoneNumber(dataClientDto.getPhoneNumber());
            return;
        }
        catch (ClassCastException e) {

        }
        try {
            DataAdministrator dataAdministrator =  (DataAdministrator) accessLevel;
            DataAdministratorDto dataAdministratorDto = (DataAdministratorDto) accessLevelDto;
            dataAdministrator.setContactEmail(dataAdministratorDto.getContactEmail());
            dataAdministrator.setPhoneNumber(dataAdministratorDto.getPhoneNumber());
            return;
        }
        catch (ClassCastException e) {

        }
        try {
            DataSpecialist dataSpecialist =  (DataSpecialist) accessLevel;
            DataSpecialistDto dataSpecialistDto = (DataSpecialistDto) accessLevelDto;
            dataSpecialist.setContactEmail(dataSpecialistDto.getContactEmail());
            dataSpecialist.setPhoneNumber(dataSpecialistDto.getPhoneNumber());
            return;
        }
        catch (ClassCastException e) {

        }

    }
    public void changeOwnPassword(String login, String newPassword, String oldPassword) {
        Account account = accountFacade.findByLogin(login);
        if (account == null) {
            throw new AccountNotFoundException();
        }

        if (oldPassword == null || !hashAlgorithm.verify(oldPassword.toCharArray(), account.getPassword())) {
            //przemyslec budowanie wyjatkow
            throw new AccountPasswordMatchException();
        }
        if(hashAlgorithm.verify(newPassword.toCharArray(),account.getPassword())){
            //przemyslec budowanie wyjatkow
            throw new AccountPasswordIsTheSameException();

        }
        account.setPassword(hashAlgorithm.generate(newPassword.toCharArray()));
        accountFacade.unsafeEdit(account);
    }

    public void reset(String login) {
        Account account = accountFacade.findByLogin(login);
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setAccount(account);
        resetPasswordFacade.create(resetPasswordToken);
        emailConfig.sendEmail(
                account.getEmail(),
                "Reset password",
                "Your link to reset password: \n"
                        + "localhost:8080/mok/resetPassword/"
                        + login + "/"
                        + hashAlgorithm.generate(resetPasswordToken.getId().toString().toCharArray())
        );
    }

    public void resetPassword(ResetPasswordDTO accountWithTokenDTO) {
        ResetPasswordToken resetPasswordToken = resetPasswordFacade.findResetPasswordToken(accountWithTokenDTO.getLogin());
        if(hashAlgorithm.verify(resetPasswordToken.getId().toString().toCharArray(), accountWithTokenDTO.getToken())) {
            Account account = accountFacade.findByLogin(accountWithTokenDTO.getLogin());
            account.setPassword(hashAlgorithm.generate(accountWithTokenDTO.getPassword().toCharArray()));
            accountFacade.unsafeEdit(account);
            resetPasswordFacade.unsafeRemove(resetPasswordToken);
        }
    }

    public void createAccount(Account account) {
        accountFacade.create(account);
    }

    public void registerClientAccount(Account account){
        accountFacade.create(account);
        String token = jwtGenerator.createJWTForEmail(account.getLogin());
        Date date = new Date(new Date().getTime() + (60 * 60 * 1000));
        ActiveAccountToken activeAccountToken = new ActiveAccountToken(account,token,date);
        activeAccountFacade.create(activeAccountToken);
        emailConfig.sendEmail(
                account.getEmail(),
                "Active account - KIC",
                "Your link to active account: https://localhost:8181/active \n"
                        + "Token: " + token);
    }

    public void confirm(String token) {
        Claims claims = jwtGenerator.decodeJWT(token);
        if(claims.getExpiration().before(new Date())) throw new TokenExpierdException();
        Account account = accountFacade.findByLogin(claims.getSubject());
        account.setConfirmed(true);
        accountFacade.unsafeEdit(account);
    }


}
