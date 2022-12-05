package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.ResourceNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountAlreadyExistsException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountMOKFacade extends AbstractFacade<Account> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ssbd03mok")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private Tagger tagger;

    public AccountMOKFacade() {
        super(Account.class);
    }


    /**
     * Metoda usuwająca konto z bazy danych
     *
     * @param entity
     */
    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    //TODO na 90% jest to do wyrzucenia
    public void unsafeRemove(Account entity) {
        super.unsafeRemove(entity);
    }


    /**
     * Metoda edytująca konto w bazie danych. Uwzględnia wersję
     *
     * @param entity
     */
    @Override
    @RolesAllowed(Roles.AUTHENTICATED)
    public void edit(Account entity) {
        super.edit(entity);
    }

    /**
     * Metoda edytująca konto w bazie danych. NIE Uwzględnia wersji
     *
     * @param entity
     */
    @Override
    @RolesAllowed(Roles.ANONYMOUS)
    public void unsafeEdit(Account entity) {
        super.unsafeEdit(entity);
    }

    /**
     * Metoda zwiększająca wersję konta.
     * Wymaga podania encji w stanie zarządzalnym
     *
     * @param account
     */
    @RolesAllowed(Roles.AUTHENTICATED)
    public void forceVersionIncrement(Account account) {
        entityManager.lock(account, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
    }

    /**
     * Metoda wyszukująca konkretne konto względem wprowadzonego loginu
     *
     * @param login Login użytkownika, którego szukamy
     * @return Obiekt znalezionego konta
     * @throws InvalidParametersException, gdy podano niepoprawną wartość parametru
     * @throws DatabaseException,          gdy wystąpi błąd związany z bazą danych
     */
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    public Account findByLogin(String login) {
        try {
            TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByLogin", Account.class);
            typedQuery.setParameter("login", login);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw new ResourceNotFoundException();
        } catch (IllegalArgumentException iae) {
            throw new InvalidParametersException(iae.getCause());
        } catch (PersistenceException pe) {
            throw new DatabaseException(pe.getCause());
        }
    }

    /**
     * Zwraca listę stronicowanych użytkowników w bazie danych, w których imieniu i/lub nazwisku występuje dana fraza.
     *
     * @param pageNumber Numer strony (startuje od 1)
     * @param perPage    Ilość użytkowników, którzy mają zostać zwróceni
     * @param phrase     Fraza, która występuje w imieniu i/lub nazwisku szukanych użytkowników
     * @return Znalezieni użytkownicy wraz z ich całkowitą ilością (jako liczba)
     * @throws InvalidParametersException, gdy podano niepoprawną wartość parametru
     * @throws DatabaseException,          gdy wystąpi błąd związany z bazą danych
     */
    @RolesAllowed(Roles.ADMINISTRATOR)
    public PaginationData findInRangeWithPhrase(int pageNumber, int perPage, String phrase) {
        try {

            List<Account> data = entityManager
                    .createNamedQuery("Account.searchByPhrase", Account.class)
                    .setParameter("phrase", "%" + phrase + "%")
                    .setMaxResults(perPage)
                    .setFirstResult((pageNumber - 1) * perPage)
                    .getResultList();

            Long totalCount = entityManager
                    .createNamedQuery("Account.searchByPhrase.count", Long.class)
                    .setParameter("phrase", "%" + phrase + "%")
                    .getSingleResult();

            int totalPages = (int) Math.ceil((double) totalCount / perPage);

            return new PaginationData(totalCount.intValue(), totalPages, pageNumber, data);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException(e.getCause());
        } catch (PersistenceException e) {
            throw new DatabaseException(e.getCause());
        }
    }

    /**
     * Metoda dodaje nowe konto do bazy danych
     *
     * @param entity konto użytkownika
     * @throws AccountAlreadyExistsException gdy użytkownik o podanym loginie lub emailu już istnieje
     */

    @Override
    @RolesAllowed({Roles.ANONYMOUS, Roles.ADMINISTRATOR})
    public void create(Account entity) {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            if (e.getConstraintViolations().contains(Account.CONSTRAINT_LOGIN_UNIQUE)) {
                throw AccountAlreadyExistsException.loginExists();
            } else if (e.getConstraintViolations().contains(Account.CONSTRAINT_EMAIL_UNIQUE)) {
                throw AccountAlreadyExistsException.emailExists();
            }
            throw new DatabaseException(e);
        }
    }
}
