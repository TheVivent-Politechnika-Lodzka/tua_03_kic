package pl.lodz.p.it.ssbd2022.ssbd03.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2022.ssbd03.model.DataClient;

@Stateless
public class DataClientFacade extends AbstractFacade<DataClient>{

    @PersistenceContext(unitName = "ssbd03adminPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DataClientFacade(){
        super(DataClient.class);
    }
}
