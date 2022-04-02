package pl.lodz.p.it.ssbd2022.ssbd03.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2022.ssbd03.model.AccessLevel;

public class AccessLevelFacade extends AbstractFacade{

    @PersistenceContext(unitName = "ssbd03adminPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccessLevelFacade(){
        super(AccessLevel.class);
    }

}
