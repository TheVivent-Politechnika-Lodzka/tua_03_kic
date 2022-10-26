package pl.lodz.p.it.ssbd2022.ssbd03.global_services.facades;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.RefreshToken;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

import java.time.Instant;
import java.util.List;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class RefreshTokenGLOBALFacade extends AbstractFacade<RefreshToken> {

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
    public RefreshTokenGLOBALFacade() {
        super(RefreshToken.class);
    }

    /**
     * Metoda usuwa token z bazy danych
     *
     * @param refreshToken - token służący do odświerzania
     */
    @Override
    @PermitAll
    public void unsafeRemove(RefreshToken refreshToken) {
        super.unsafeRemove(refreshToken);
    }

    /**
     * Metoda zwraca tokeny przed podaną datą
     *
     * @return List<RefreshToken> - Zwraca listę wygasłych tokenów
     */
    @PermitAll
    public List<RefreshToken> findExpiredTokens() {
        TypedQuery<RefreshToken> typedQuery = entityManager.createNamedQuery("RefreshToken.findExpired", RefreshToken.class);
        typedQuery.setParameter("now", Instant.now().getEpochSecond());
        return typedQuery.getResultList();
    }

}
