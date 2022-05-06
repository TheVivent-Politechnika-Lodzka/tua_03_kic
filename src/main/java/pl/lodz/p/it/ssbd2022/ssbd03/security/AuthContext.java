package pl.lodz.p.it.ssbd2022.ssbd03.security;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.model.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.services.MOKService;

@Stateless
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
            return mokService.findAccountByLogin(securityContext.getCallerPrincipal().getName());
        } catch (Exception e) {
            return null;
        }
    }

}
