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
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.AccountConfirmationToken;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

import java.time.Instant;
import java.util.List;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountConfirmationGLOBALFacade extends AbstractFacade<AccountConfirmationToken> {

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
    public AccountConfirmationGLOBALFacade() {
        super(AccountConfirmationToken.class);
    }

    /**
     * Metoda usuwa token potwierdzający konto
     *
     * @param entity - token
     */
    @Override
    @PermitAll
    public void unsafeRemove(AccountConfirmationToken entity) {
        super.unsafeRemove(entity);
    }

    /**
     * Metoda szuka wygasłe tokeny
     *
     * @return List<AccountConfirmationToken> - Lista wygasłych tokenów
     */
    @PermitAll
    public List<AccountConfirmationToken> findExpiredTokens() {
        TypedQuery<AccountConfirmationToken> typedQuery = entityManager
                .createNamedQuery("AccountConfirmationToken.findExpired", AccountConfirmationToken.class);
        typedQuery.setParameter("now", Instant.now());
        return typedQuery.getResultList();
    }

}
