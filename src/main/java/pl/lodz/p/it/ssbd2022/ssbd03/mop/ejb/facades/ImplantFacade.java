package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.model.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.model.Implant;

@Stateless
public class ImplantFacade extends AbstractFacade<Implant> {

    @PersistenceContext(unitName = "ssbd03mopPU")
    private EntityManager em;

    public ImplantFacade() {
        super(Implant.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


}
