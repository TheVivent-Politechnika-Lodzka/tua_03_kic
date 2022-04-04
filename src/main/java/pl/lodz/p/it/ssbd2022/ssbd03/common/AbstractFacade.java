package pl.lodz.p.it.ssbd2022.ssbd03.common;

import jakarta.persistence.EntityManager;

import java.util.List;

public abstract class AbstractFacade<T> {
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass){
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity){
        getEntityManager().persist(entity);
    }

    public void edit(T entity){
        getEntityManager().merge(entity);
    }

    public void remove (T entity){
        getEntityManager().remove(entity);
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
