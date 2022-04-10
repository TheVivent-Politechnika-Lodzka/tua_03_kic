package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.model.DataAdministrator;

@Stateless
public class DataAdministratorFacade extends AbstractFacade<DataAdministrator> {
    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;


    public DataAdministratorFacade() {
        super(DataAdministrator.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
