package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.ImplantFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

@Stateful
@DenyAll
@Interceptors(TrackerInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MOPService extends AbstractService implements MOPServiceInterface, SessionSynchronization {

    @Inject
    ImplantFacade implantFacade;


    /**
     * Metoda zwracająca liste wszczepów
     * @param page numer strony
     * @param pageSize  ilość pozycji na stronie na stronie
     * @param phrase szukana fraza
     * @return lista wszczepów
     * @throws InvalidParametersException jeśli podano nieprawidłowe parametry
     */
    @Override
    @PermitAll
    public PaginationData findImplants(int page, int pageSize, String phrase) {
        if(page == 0 || pageSize == 0) {
            throw new InvalidParametersException();
        }
        return implantFacade.findInRangeWithPhrase(page, pageSize, phrase);
    }
}
