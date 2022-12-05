package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.AccountConfirmationToken;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.ResourceNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountConfirmationMOKFacade extends AbstractFacade<AccountConfirmationToken> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ssbd03mok")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private Tagger tagger;

    public AccountConfirmationMOKFacade() {
        super(AccountConfirmationToken.class);
    }

    /**
     * Metoda tworzy token potwierdzający konto
     *
     * @param entity
     */
    @Override
    @RolesAllowed(Roles.ANONYMOUS)
    public void create(AccountConfirmationToken entity) {
        super.create(entity);
    }

    /**
     * Metoda usuwa token potwierdzający konto
     *
     * @param entity
     */
    @Override
    @RolesAllowed(Roles.ANONYMOUS)
    public void unsafeRemove(AccountConfirmationToken entity) {
        super.unsafeRemove(entity);
    }

    /**
     * Metoda szuka token użytkownika o podanym loginie
     *
     * @param login - login konta
     * @return ConfirmationAccountToken - obiekt zawierający token użytkownika o podanym loginie
     * @throws DatabaseException, gdy wystąpi błąd związany z bazą danych
     */
    @RolesAllowed(Roles.ANONYMOUS)
    public AccountConfirmationToken findToken(String login) {
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
