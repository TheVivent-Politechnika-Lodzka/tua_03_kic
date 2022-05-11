package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades;

import jakarta.ejb.Stateless;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;

@Interceptors(TrackerInterceptor.class)
@Stateless
public class ReviewFacade extends AbstractFacade<ImplantReview> {

    @PersistenceContext(unitName = "ssbd03mopPU")
    private EntityManager em;

    public ReviewFacade() {
        super(ImplantReview.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


}
