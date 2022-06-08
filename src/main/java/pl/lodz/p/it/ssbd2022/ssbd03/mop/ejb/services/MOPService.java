package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.ImplantFacade;

@Stateful
@DenyAll
@Interceptors(TrackerInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MOPService extends AbstractService implements MOPServiceInterface, SessionSynchronization {

    @Inject
    private ImplantFacade implantFacade;

    /**
     * Metoda tworząca nowy wszczep
     * @param implant - nowy wszczep
     * @return Implant
     */
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Override
    public Implant createImplant(Implant implant) {
        implantFacade.create(implant);
        return implantFacade.findByUUID(implant.getId());
    }


}
