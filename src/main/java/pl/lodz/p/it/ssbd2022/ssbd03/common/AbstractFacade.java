package pl.lodz.p.it.ssbd2022.ssbd03.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.InAppOptimisticLockException;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;

public abstract class AbstractFacade<T> {

    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    protected abstract HashAlgorithm getHashAlgorithm();

    /**
     * Metoda tworzy nową encję w bazie danych.
     * @param entity
     * @throws ConstraintViolationException
     */
    protected void create(T entity) {
        try {
            getEntityManager().persist(entity);
            getEntityManager().flush();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException)
                throw (ConstraintViolationException) e.getCause();
            throw e;
        }

    }

    /**
     * Metoda weryfikuje czy podany tag jest poprawny.
     * @param entity
     * @param tagFromDto
     * @throws InAppOptimisticLockException
     */
    private void verifyTag(AbstractEntity entity, String tagFromDto) {
        String entityTag = getHashAlgorithm().generateETag(
                entity.getId(),
                entity.getVersion()
        );
        if (!entityTag.equals(tagFromDto))
            throw new InAppOptimisticLockException();
    }

    /**
     * Metoda usuwa podaną encję z bazy danych. Uwzględnia sprawdzenie wersji.
     * @param entity Obiekt encji
     * @param tagFromDto ETag przekazany z Dto do sprawdzenia
     * @throws InAppOptimisticLockException
     * @throws DatabaseException
     * @throws ConstraintViolationException
     */
    protected void edit(T entity, String tagFromDto) {
        if (entity instanceof AbstractEntity abstractEntity)
            verifyTag(abstractEntity, tagFromDto);
        unsafeEdit(entity);
    }

    /**
     * Metoda modyfikuje encję w bazie danych. NIE uwzględnia sprawdzenia wersji.
     * @param entity Obiekt encji
     * @throws InAppOptimisticLockException
     * @throws ConstraintViolationException
     */
    protected void unsafeEdit(T entity) {
        try {
            getEntityManager().merge(entity);
            getEntityManager().flush();
        } catch (OptimisticLockException e) {
            throw new InAppOptimisticLockException(e);
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException)
                throw (ConstraintViolationException) e.getCause();
            throw e;
        }
    }

    /**
     * Metoda usuwa podaną encję z bazy danych. Uwzlgędnia sprawdzenie wersji
     * @param entity
     * @param tagFromDto
     * @throws InAppOptimisticLockException
     * @throws DatabaseException
     * @throws ConstraintViolationException
     */
    protected void remove(T entity, String tagFromDto) {
        if (entity instanceof AbstractEntity abstractEntity)
            verifyTag(abstractEntity, tagFromDto);
        unsafeRemove(entity);
    }

    /**
     * Metoda usuwa podaną encję z bazy danych. NIE uwzlgędnia sprawdzenia wersji
     * @param entity
     * @throws InAppOptimisticLockException
     * @throws ConstraintViolationException
     */
    protected void unsafeRemove(T entity) {
        try {
            getEntityManager().remove(entity);
            getEntityManager().flush();
        } catch (OptimisticLockException e) {
            throw new InAppOptimisticLockException(e);
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException)
                throw (ConstraintViolationException) e.getCause();
            throw e;
        }

    }

    /**
     * Metoda zwraca obiekt encji o podanym id.
     * @param id
     * @return encja o podanym id
     */
    protected T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /***
     *
     * Zwraca listę encji danego typu z bazy danych, które zostały wcześniej stronicowane.
     *
     * @param pageNumber Numer strony (startuje od 1)
     * @param perPage Ilość encji, które mają zostać zwrócone
     * @return Encje, wraz z ich całkowitą ilością (jako liczba)
     * @throws InvalidParametersException, gdy podano niepoprawną wartość parametru
     * @throws DatabaseException, gdy wystąpi błąd związany z bazą danych
     */
    protected PaginationData findInRange(int pageNumber, int perPage) {
        try {
            CriteriaQuery criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(entityClass));

            pageNumber--;

            List data = getEntityManager()
                    .createQuery(criteriaQuery)
                    .setMaxResults(perPage)
                    .setFirstResult(pageNumber * perPage)
                    .getResultList();

            pageNumber++;
            int totalCount = count();
            int totalPages = (int) Math.ceil((double) totalCount /  perPage);

            return new PaginationData(totalCount, totalPages, pageNumber, data);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException(e.getCause());
        } catch (PersistenceException e) {
            throw new DatabaseException(e.getCause());
        }

    }

    /**
     * Pobiera liczbę wszystkich encji danego typu.
     *
     * @return liczbę wszystkich encji danego typu
     * @throws DatabaseException, gdy wystąpi błąd związany z bazą danych
     */
    protected int count() {
        try {
            CriteriaQuery criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(entityClass));
            return getEntityManager().createQuery(criteriaQuery).getResultList().size();
        } catch (PersistenceException e) {
            throw new DatabaseException(e.getCause());
        }

    }

}
