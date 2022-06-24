package pl.lodz.p.it.ssbd2022.ssbd03.global_services.facades;

import jakarta.annotation.security.PermitAll;
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
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.AccountConfirmationToken;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

import java.time.Instant;
import java.util.List;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountConfirmationFacade extends AbstractFacade<AccountConfirmationToken> {

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
    public AccountConfirmationFacade() {
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
