package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ConfirmationAccountToken;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

import java.time.Instant;
import java.util.List;

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

    /**
     * Metoda tworzy token potwierdzający konto
     * @param entity
     */
    @Override
    @PermitAll
    public void create(ConfirmationAccountToken entity) {
        super.create(entity);
    }

    /**
     * Metoda usuwa token potwierdzający konto
     * @param entity
     */
    @Override
    @PermitAll
    public void unsafeRemove(ConfirmationAccountToken entity) {
        super.unsafeRemove(entity);
    }

    /**
     * Metoda szuka token użytkownika o podanym loginie
     * @param login - login konta
     * @return ConfirmationAccountToken - obiekt zawierający token użytkownika o podanym loginie
     * @throws DatabaseException, gdy wystąpi błąd związany z bazą danych
     */
    @PermitAll
    public ConfirmationAccountToken findToken(String login){
        try {
            TypedQuery<ConfirmationAccountToken> typedQuery = em.createNamedQuery("ConfirmationAccountToken.findByLogin", ConfirmationAccountToken.class);
            typedQuery.setParameter("login", login);
            return typedQuery.getSingleResult();
        } catch (PersistenceException pe) {
            throw new DatabaseException(pe.getCause());
        }
    }


    /**
     * Metoda szuka wygasłe tokeny
     *
     * @return Lista wygasłych tokenów
     */
    @PermitAll
    public List<ConfirmationAccountToken> findExpiredTokens(){
        TypedQuery<ConfirmationAccountToken> typedQuery = em.createNamedQuery("ConfirmationAccountToken.findExpired", ConfirmationAccountToken.class);
        typedQuery.setParameter("now", Instant.now());
        return typedQuery.getResultList();
    }

}
