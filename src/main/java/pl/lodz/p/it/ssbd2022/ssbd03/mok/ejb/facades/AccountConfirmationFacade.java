package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.*;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.AccountConfirmationToken;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.ResourceNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

import java.time.Instant;
import java.util.List;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountConfirmationFacade extends AbstractFacade<AccountConfirmationToken> {

    @PersistenceContext(unitName = "ssbd03mokPU")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private Tagger tagger;

    public AccountConfirmationFacade() {
        super(AccountConfirmationToken.class);
    }

    /**
     * Metoda tworzy token potwierdzający konto
     * @param entity
     */
    @Override
    @PermitAll
    public void create(AccountConfirmationToken entity) {
        super.create(entity);
    }

    /**
     * Metoda usuwa token potwierdzający konto
     * @param entity
     */
    @Override
    @PermitAll
    public void unsafeRemove(AccountConfirmationToken entity) {
        super.unsafeRemove(entity);
    }

    /**
     * Metoda szuka token użytkownika o podanym loginie
     * @param login - login konta
     * @return ConfirmationAccountToken - obiekt zawierający token użytkownika o podanym loginie
     * @throws DatabaseException, gdy wystąpi błąd związany z bazą danych
     */
    @PermitAll
    public AccountConfirmationToken findToken(String login){
        try {
            TypedQuery<AccountConfirmationToken> typedQuery = entityManager
                    .createNamedQuery("AccountConfirmationToken.findByLogin", AccountConfirmationToken.class);
            typedQuery.setParameter("login", login);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw new ResourceNotFoundException();
        } catch (PersistenceException pe) {
            throw new DatabaseException(pe.getCause());
        }
    }


}
