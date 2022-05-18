package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import jakarta.ejb.Stateful;
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
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;


@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ResetPasswordFacade extends AbstractFacade<ResetPasswordToken> {

    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    @Inject @Getter
    private HashAlgorithm hashAlgorithm;

    public ResetPasswordFacade() {
        super(ResetPasswordToken.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    // TODO: Dodanie Javadoc
    public ResetPasswordToken findResetPasswordToken(String login) {
        // TODO: Dodać obsługę wyjątku nie znalezionego tokenu
        TypedQuery<ResetPasswordToken> typedQuery = em.createNamedQuery("ResetPassword.findByLogin", ResetPasswordToken.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getSingleResult();
    }
}
