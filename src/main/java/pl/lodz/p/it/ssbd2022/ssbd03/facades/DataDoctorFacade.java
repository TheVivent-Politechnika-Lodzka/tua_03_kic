package pl.lodz.p.it.ssbd2022.ssbd03.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2022.ssbd03.model.DataDoctor;

public class DataDoctorFacade extends AbstractFacade{

    @PersistenceContext(unitName = "ssbd03adminPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DataDoctorFacade(){
        super(DataDoctor.class);
    }
}
