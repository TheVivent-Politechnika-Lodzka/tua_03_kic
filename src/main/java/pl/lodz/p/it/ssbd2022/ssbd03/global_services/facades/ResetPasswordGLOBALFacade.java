package pl.lodz.p.it.ssbd2022.ssbd03.global_services.facades;

import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.ResetPasswordToken;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.util.List;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@RunAs(Roles.ADMINISTRATOR)
public class ResetPasswordGLOBALFacade extends AbstractFacade<ResetPasswordToken> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ssbd03mok")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private Tagger tagger;

    /**
     * Konstruktor
     */
    public ResetPasswordGLOBALFacade() {
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
        typedQuery.setParameter("now", Instant.now().getEpochSecond());
        return typedQuery.getResultList();
    }
}
