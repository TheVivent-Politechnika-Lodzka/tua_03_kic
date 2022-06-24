package pl.lodz.p.it.ssbd2022.ssbd03.global_services.facades;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RunAs;
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
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.ResetPasswordToken;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

import java.time.Instant;
import java.util.List;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RunAs(Roles.ADMINISTRATOR)
public class ResetPasswordFacade extends AbstractFacade<ResetPasswordToken> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ssbd03mokPU")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private Tagger tagger;

    /**
     * Konstruktor
     */
    public ResetPasswordFacade() {
        super(ResetPasswordToken.class);
    }

    /**
     * Metoda usuwa token z bazy danych
     *
     * @param resetPasswordToken - token
     */
    @Override
    @PermitAll
    public void unsafeRemove(ResetPasswordToken resetPasswordToken) {
        super.unsafeRemove(resetPasswordToken);
    }

    /**
     * Metoda zwraca tokeny przed podaną datą
     *
     * @return List<ResetPasswordToken> - lista wygasych tokenów
     */
    @PermitAll
    public List<ResetPasswordToken> findExpiredTokens() {
        TypedQuery<ResetPasswordToken> typedQuery = entityManager
                .createNamedQuery("ResetPassword.findBeforeDate", ResetPasswordToken.class);
        typedQuery.setParameter("now", Instant.now());
        return typedQuery.getResultList();
    }
}
