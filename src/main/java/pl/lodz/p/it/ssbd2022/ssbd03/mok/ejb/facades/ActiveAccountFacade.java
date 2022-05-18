package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ConfirmationAccountToken;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ActiveAccountFacade extends AbstractFacade<ConfirmationAccountToken> {

    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    @Inject
    @Getter
    private HashAlgorithm hashAlgorithm;

    public ActiveAccountFacade() {
        super(ConfirmationAccountToken.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void create(ConfirmationAccountToken entity) {
        super.create(entity);
    }

    public ConfirmationAccountToken findToken(String login){
        TypedQuery<ConfirmationAccountToken> typedQuery = em.createNamedQuery("ConfirmationAccountToken.findByLogin", ConfirmationAccountToken.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getSingleResult();
    }
}
