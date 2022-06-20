package pl.lodz.p.it.ssbd2022.ssbd03.global_services;

import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.global_services.facades.AccountConfirmationFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.global_services.facades.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.global_services.facades.RefreshTokenFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.global_services.facades.ResetPasswordFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;

@Startup
@Singleton
@RunAs(Roles.ADMINISTRATOR)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Interceptors(TrackerInterceptor.class)
public class CleanerService {

    @Inject
    AccountConfirmationFacade accountConfirmationFacade;
    @Inject
    AccountFacade accountFacade;
    @Inject
    ResetPasswordFacade resetPasswordFacade;
    @Inject
    RefreshTokenFacade refreshTokenFacade;

    /**
     * Metoda wywoływana przez serwer aplikacji co minutę.
     * Usuwa niepotwierdzone konta z nieważnymi tokenami.
     */
    @Schedule(hour = "*", minute = "*/1", persistent = false)
    private void cleanUnconfirmedAccounts() {
        accountConfirmationFacade.findExpiredTokens().forEach((token) -> {
            accountConfirmationFacade.unsafeRemove(token);
            accountFacade.unsafeRemove(token.getAccount());
        });
    }


    /**
     * Metoda wywoływana przez serwer aplikacji co minutę.
     * Usuwa wygasłe tokeny resetowania haseł.
     */
    @Schedule(hour = "*", minute = "*/1", persistent = false)
    private void clearExpiredResetPasswordTokens() {
        resetPasswordFacade.findExpiredTokens()
                .forEach(token -> resetPasswordFacade.unsafeRemove(token));
    }

    /**
     * Metoda wywoływana przez serwer aplikacji co minutę.
     * Usuwa wygasłe tokeny odświeżające
     */
    @Schedule(hour = "*", minute = "*/1", persistent = false)
    private void clearExpiredRefreshTokens() {
        refreshTokenFacade.findExpiredTokens()
                .forEach(token -> refreshTokenFacade.unsafeRemove(token));
    }
}
