package pl.lodz.p.it.ssbd2022.ssbd03.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaQuery;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;

public abstract class AbstractFacade<T> {

    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();
    protected abstract HashAlgorithm getHashAlgorithm();

    public void create(T entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
    }

    private void verifyTag(AbstractEntity entity, String tagFromDto){
        String entityTag = getHashAlgorithm().generateDtoTag(
                entity.getId(),
                entity.getVersion()
        );
        if (!entityTag.equals(tagFromDto))
            throw new InAppOptimisticLockException();
    }

    public void edit(T entity, String tagFromDto){
        if (entity instanceof AbstractEntity abstractEntity)
            verifyTag(abstractEntity, tagFromDto);
        unsafeEdit(entity);
    }

    public void unsafeEdit(T entity){
        try {
            getEntityManager().merge(entity);
            getEntityManager().flush();
        } catch (OptimisticLockException e){
            throw new InAppOptimisticLockException(e);
        }
    }

    public void remove(T entity, String tagFromDto){
        if (entity instanceof AbstractEntity abstractEntity)
            verifyTag(abstractEntity, tagFromDto);
        unsafeRemove(entity);
    }

    public void unsafeRemove(T entity){
        try {
        getEntityManager().remove(entity);
        getEntityManager().flush();
        } catch (OptimisticLockException e){
            throw new InAppOptimisticLockException(e);
        }

    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List findAll() {
        CriteriaQuery criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery();
        criteriaQuery.select(criteriaQuery.from(entityClass));
        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    /***
     *
     * Zwraca listę encji danego typu z bazy danych, które zostały wcześniej stronicowane.
     *
     * @param pageNumber Numer strony
     * @param perPage Ilość encji, które mają zostać zwrócone
     * @return Encje, wraz z ich całkowitą ilością (jako liczba)
     * @throws InvalidParametersException, gdy podano niepoprawną wartość parametru
     * @throws DatabaseException, gdy wystąpi błąd związany z bazą danych
     */
    public PaginationData findInRange(int pageNumber, int perPage) {
        try {
            CriteriaQuery criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(entityClass));

            pageNumber -= 1;

            List data = getEntityManager()
                    .createQuery(criteriaQuery)
                    .setMaxResults(perPage)
                    .setFirstResult(pageNumber * perPage)
                    .getResultList();

            int totalCount = count();
            return new PaginationData(totalCount, data);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException();
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
    public int count() {
        try {
            CriteriaQuery criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(entityClass));
            return getEntityManager().createQuery(criteriaQuery).getResultList().size();
        } catch (PersistenceException e) {
            throw new DatabaseException(e.getCause());
        }

    }

}
