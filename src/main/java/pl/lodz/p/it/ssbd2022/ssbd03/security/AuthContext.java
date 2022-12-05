package pl.lodz.p.it.ssbd2022.ssbd03.security;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services.MOKService;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services.MOKServiceInterface;

@Stateful
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AuthContext {

    private static final long serialVersionUID = 1L;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private MOKServiceInterface mokService;

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public Account getCurrentUser() {
        if (securityContext.getCallerPrincipal() == null) {
            return null;
        }
        try {
            return mokService.findAccountByLogin(securityContext.getCallerPrincipal().getName());
        } catch (Exception e) {
            return null;
        }
    }

    public String getCurrentUserLogin() {
        if (securityContext.getCallerPrincipal() == null) {
            return null;
        }
        return securityContext.getCallerPrincipal().getName();
    }

}
