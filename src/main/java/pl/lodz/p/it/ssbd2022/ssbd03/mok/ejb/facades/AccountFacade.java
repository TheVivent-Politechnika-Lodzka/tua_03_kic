package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountAlreadyExistsException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

@Interceptors(TrackerInterceptor.class)
@Stateful
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    @Inject @Getter
    private HashAlgorithm hashAlgorithm;

    public AccountFacade() {
        super(Account.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Account findByLogin(String login){
        TypedQuery<Account> typedQuery = em.createNamedQuery("Account.findByLogin", Account.class);
        typedQuery.setParameter("login",login);
        return typedQuery.getSingleResult();
    }

    @Override
    public void create(Account entity) {
        try{
            super.create(entity);
        }
        catch(ConstraintViolationException e){
            if (e.getCause().getMessage().contains(Account.CONSTRAINT_LOGIN_UNIQUE)) {
                throw AccountAlreadyExistsException.loginExists();
            }
            else if(e.getCause().getMessage().contains(Account.CONSTRAINT_EMAIL_UNIQUE)){
                throw AccountAlreadyExistsException.emailExists();
            }
            //TODO: wymienić to w przyszłości na wyjątek związany z błędem bazy danych
            throw e;
        }
    }

}
