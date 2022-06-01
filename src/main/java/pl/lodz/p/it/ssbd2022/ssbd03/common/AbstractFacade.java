package pl.lodz.p.it.ssbd2022.ssbd03.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.access_level.AccessLevelNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.InAppOptimisticLockException;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Taggable;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;

public abstract class AbstractFacade<T> {

    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    protected abstract Tagger getTagger();

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
     * @throws InAppOptimisticLockException
     */
    private void verifyTag(AbstractEntity entity) {
        getTagger().verifyTag();
        Taggable tag = getTagger().getTagFromHeader();
        if (!entity.getId().equals(tag.getId()) || !entity.getVersion().equals(tag.getVersion()))
            throw InAppOptimisticLockException.userHasOldEntity();
    }

    /**
     * Metoda EDYTUJĄCA podaną encję z bazy danych. Uwzględnia sprawdzenie wersji.
     * @param entity Obiekt encji
     * @param tagFromDto ETag przekazany z Dto do sprawdzenia
     * @throws InAppOptimisticLockException
     * @throws DatabaseException
     * @throws ConstraintViolationException
     */
    protected void edit(T entity) {
        if (entity instanceof AbstractEntity abstractEntity)
            verifyTag(abstractEntity);
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
            throw InAppOptimisticLockException.userHasOldEntity();
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
    protected void remove(T entity) {
        if (entity instanceof AbstractEntity abstractEntity)
            verifyTag(abstractEntity);
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
            throw InAppOptimisticLockException.userHasOldEntity();
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


    /**
     * Pobiera liczbę wszystkich encji danego typu.
     *
     * @return liczbę wszystkich encji danego typu
     * @throws DatabaseException, gdy wystąpi błąd związany z bazą danych
     */
    protected int count() {
        try {
            CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Long> cq = qb.createQuery(Long.class);
            cq.select(qb.count(cq.from(entityClass)));
            return getEntityManager().createQuery(cq).getSingleResult().intValue();
        } catch (PersistenceException e) {
            throw new DatabaseException(e.getCause());
        }

    }

}
