package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.access_level.AccessLevelExistsException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccessLevelFacade extends AbstractFacade<AccessLevel> {

    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    @Inject @Getter
    private HashAlgorithm hashAlgorithm;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccessLevelFacade(){
        super(AccessLevel.class);
    }

    @Override
    public void create(AccessLevel entity) {
        try {
            super.create(entity);
        }
        catch (PersistenceException e){
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t != null) {
                if (t.getMessage().contains(AccessLevel.CONSTRAINT_ACCESS_LEVEL_FOR_ACCOUNT_UNIQUE)) {
                    throw new AccessLevelExistsException();
                }
            }
            throw e;
        }
    }

}
