package pl.lodz.p.it.ssbd2022.ssbd03.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.InAppOptimisticLockException;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

import java.util.List;

public abstract class AbstractFacade<T> {
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass){
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();
    protected abstract HashAlgorithm getHashAlgorithm();

    public void create(T entity){
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

    public T find(Object id){
        return getEntityManager().find(entityClass, id);
    }

    public List findAll(){
        jakarta.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public int count(){
        jakarta.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        jakarta.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(rt); // nie wiem czy to jest dobrze
        jakarta.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
