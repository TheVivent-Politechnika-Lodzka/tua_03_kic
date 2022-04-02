package pl.lodz.p.it.ssbd2022.ssbd03.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2022.ssbd03.model.DataPersonal;

public class DataPersonalFacade extends AbstractFacade{

    @PersistenceContext(unitName = "ssbd03adminPU")
    private EntityManager em;

    public DataPersonalFacade() {
        super(DataPersonal.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;

    }
}
