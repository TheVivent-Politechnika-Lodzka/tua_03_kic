package pl.lodz.p.it.ssbd2022.ssbd03.mok.services;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.transaction.Transactional;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.InvalidCredentialException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.security.JWTGenerator;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class MOKService {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private IdentityStoreHandler indentityStoreHandler;

    @Inject
    private JWTGenerator jwtGenerator;

    public String authenticate(Credential credential) {
        CredentialValidationResult result = indentityStoreHandler.validate(credential);
        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            return jwtGenerator.createJWT(result);
        }
        throw new InvalidCredentialException();
    }

    public Account findByLogin(String login) {
        return accountFacade.findByLogin(login);
    }



    public void deactivate(String login) {
        Account account = accountFacade.findByLogin(login);
        account.setActive(false);
        accountFacade.edit(account);
    }

    public void activate(String login) {

        Account account = accountFacade.findByLogin(login);
        account.setActive(true);
        accountFacade.edit(account);


    }

    public Account edit(Account account, String firstName, String surname, String email, String phoneNumber) {
        account.setFirstName(firstName);
        account.setLastName(surname);
        accountFacade.edit(account);
        return account;
    }
}
