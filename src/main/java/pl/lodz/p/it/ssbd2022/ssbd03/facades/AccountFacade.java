package pl.lodz.p.it.ssbd2022.ssbd03.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pl.lodz.p.it.ssbd2022.ssbd03.model.Account;

import java.util.List;

@Stateless
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "ssbd03adminPU")
    private EntityManager em;

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

}
