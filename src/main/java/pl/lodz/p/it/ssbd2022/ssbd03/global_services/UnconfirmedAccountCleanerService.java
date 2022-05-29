package pl.lodz.p.it.ssbd2022.ssbd03.global_services;

import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.AccountConfirmationFacade;

/**
 * Klasa odpowiedzialna za usuwanie niepotwierdzonych kont
 */
@Startup
@Singleton
@RunAs(Roles.ADMINISTRATOR)
public class UnconfirmedAccountCleanerService {

    @Inject
    private AccountConfirmationFacade activeAccountFacade;

    @Inject
    private AccountFacade accountFacade;


    /**
     * Metoda wywoływana przez serwer aplikacji co minutę.
     * Usuwa niepotwierdzone konta z nieważnymi tokenami.
     */
    @Schedule(hour = "*", minute = "*/1", persistent = false)
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    private void searchForTokens() {
        System.out.println("UnconfirmedAccountCleanerService.searchForTokens()");
        activeAccountFacade.findExpiredTokens().forEach((token) -> {
            activeAccountFacade.unsafeRemove(token);
            accountFacade.unsafeRemove(token.getAccount());
        });
    }

}
