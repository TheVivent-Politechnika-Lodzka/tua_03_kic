package pl.lodz.p.it.ssbd2022.ssbd03.global_services;

import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.ResetPasswordFacade;

import java.time.Instant;

/*
 * Klasa odpowiedzialna za usuwanie wygasłych tokenów
 */
@Startup
@Singleton
@RunAs(Roles.ADMINISTRATOR)
public class ResetPasswordSchedulerService {

    @Inject
    ResetPasswordFacade resetPasswordFacade;

    /**
     * Metoda wywoływana przez serwer aplikacji co minutę.
     * Usuwa wygasłe tokeny resetowania haseł.
     */
    @Schedule(hour = "*", minute = "*/1", persistent = false)
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void searchForTokens() {
        System.out.println("ReseetPasswordSchedulerService: searchForTokens");
        resetPasswordFacade.findExpiredTokens()
                .forEach(token -> resetPasswordFacade.unsafeRemove(token));
    }

}
