package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.model.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.model.Review;

@Stateless
public class ReviewFacade extends AbstractFacade<Review> {

    @PersistenceContext(unitName = "ssbd03mopPU")
    private EntityManager em;

    public ReviewFacade() {
        super(Review.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


}
