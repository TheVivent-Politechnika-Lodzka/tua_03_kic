package pl.lodz.p.it.ssbd2022.ssbd03.common;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.InAppOptimisticLockException;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Taggable;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

public abstract class AbstractFacade<T> {

    private static final long serialVersionUID = 1L;

    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    protected abstract Tagger getTagger();

    /**
     * Metoda tworzy nową encję w bazie danych.
     *
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
     *
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
     *
     * @param entity Obiekt encji
     * @throws InAppOptimisticLockException
     * @throws DatabaseException
     * @throws ConstraintViolationException
     */
    protected void edit(T entity) {
        if (entity instanceof AbstractEntity)
            verifyTag((AbstractEntity) entity);
        unsafeEdit(entity);
    }

    /**
     * Metoda modyfikuje encję w bazie danych. NIE uwzględnia sprawdzenia wersji.
     *
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
     *
     * @param entity
     * @throws InAppOptimisticLockException
     * @throws DatabaseException
     * @throws ConstraintViolationException
     */
    protected void remove(T entity) {
        if (entity instanceof AbstractEntity)
            verifyTag((AbstractEntity)entity);
        unsafeRemove(entity);
    }

    /**
     * Metoda usuwa podaną encję z bazy danych. NIE uwzlgędnia sprawdzenia wersji
     *
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
     *
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
//    protected int count(String namedQuery, Map<String, String> params, Class clazz) {
//        try {
//            CriteriaBuilder qb = getEntityManager().getCriteriaBuilder();
//            CriteriaQuery<Long> cq = qb.createQuery(Long.class);
//            cq.select(qb.count(cq.from(entityClass)));
//            return getEntityManager().createQuery(cq).getSingleResult().intValue();
//        } catch (PersistenceException e) {
//            throw new DatabaseException(e.getCause());
//        }
//    }

}
