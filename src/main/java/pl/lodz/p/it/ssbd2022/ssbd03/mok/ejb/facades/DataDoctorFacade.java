package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.model.DataDoctor;

@Stateless
public class DataDoctorFacade extends AbstractFacade<DataDoctor> {

    @PersistenceContext(unitName = "ssbd03mokPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DataDoctorFacade(){
        super(DataDoctor.class);
    }
}
