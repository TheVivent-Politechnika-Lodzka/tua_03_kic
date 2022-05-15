package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

@Interceptors(TrackerInterceptor.class)
@Stateless
public class ImplantFacade extends AbstractFacade<Implant> {

    @PersistenceContext(unitName = "ssbd03mopPU")
    private EntityManager em;

    @Inject @Getter
    private HashAlgorithm hashAlgorithm;

    public ImplantFacade() {
        super(Implant.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


}
