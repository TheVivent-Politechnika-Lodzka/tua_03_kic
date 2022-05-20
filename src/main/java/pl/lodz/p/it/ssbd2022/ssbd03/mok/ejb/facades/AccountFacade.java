package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountAlreadyExistsException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "ssbd03mokPU")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private HashAlgorithm hashAlgorithm;

    public AccountFacade() {
        super(Account.class);
    }

    // TODO: Dodanie Javadoc
    public Account findByLogin(String login) {
        // TODO: dodać łapanie wyjątku kiedy nie znaleziono konta
        TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByLogin", Account.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getSingleResult();
    }

    // TODO: Dodanie Javadoc
    public PaginationData findInRangeWithPhrase(int pageNumber, int perPage, String phrase) {
            // TODO: dodać łapanie wyjątku kiedy nie znaleziono konta
            TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.searchByPhrase", Account.class);

            pageNumber -= 1;
            List<Account> data = typedQuery.setParameter("phrase", "%" + phrase + "%")
                    .setMaxResults(perPage)
                    .setFirstResult(pageNumber * perPage)
                    .getResultList();

            int totalCount = entityManager.createNamedQuery("Account.searchByPhrase", Account.class)
                    .setParameter("phrase", "%" + phrase + "%")
                    .getResultList().size();

            return new PaginationData(totalCount, data);
    }

    @Override
    public void create(Account entity) {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            if (e.getConstraintName().contains(Account.CONSTRAINT_LOGIN_UNIQUE)) {
                throw AccountAlreadyExistsException.loginExists();
            } else if (e.getConstraintName().contains(Account.CONSTRAINT_EMAIL_UNIQUE)) {
                throw AccountAlreadyExistsException.emailExists();
            }
            throw new DatabaseException(e);
        }
    }
}
