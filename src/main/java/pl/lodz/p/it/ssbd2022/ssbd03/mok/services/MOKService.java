package pl.lodz.p.it.ssbd2022.ssbd03.mok.services;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ClientErrorException;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountEditDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.model.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.security.JWTGenerator;

@Stateless
@Transactional(Transactional.TxType.REQUIRED)
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
        throw new ClientErrorException("Invalid username or password", 401);
    }

    public Account findByLogin(String login) {
        return accountFacade.findByLogin(login);
    }



    public void deactivate(String login) {
        Account account = accountFacade.findByLogin(login);
        if (account == null) {
            throw new ClientErrorException("Account with login " + login + " does not exist", 404);
        }
        account.setActive(false);
        accountFacade.edit(account);
    }

    public void activate(String login) {
        Account account = accountFacade.findByLogin(login);
        if (account == null) {
            throw new ClientErrorException("Account with login " + login + " does not exist", 404);
        }
        account.setActive(true);
        accountFacade.edit(account);
    }

    public Account edit(Account account, Account editData) {
        account.setFirstName(editData.getFirstName());
        account.setSurname(editData.getSurname());
        accountFacade.edit(account);
        return account;
    }
}
