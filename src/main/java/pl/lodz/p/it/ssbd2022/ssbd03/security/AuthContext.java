package pl.lodz.p.it.ssbd2022.ssbd03.security;

import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.services.MOKService;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AuthContext {

    @Inject
    private SecurityContext securityContext;

    @Inject
    private MOKService mokService;


    public Account getCurrentUser() {
        if (securityContext.getCallerPrincipal() == null) {
            return null;
        }
        try {
            return mokService.findByLogin(securityContext.getCallerPrincipal().getName());
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
