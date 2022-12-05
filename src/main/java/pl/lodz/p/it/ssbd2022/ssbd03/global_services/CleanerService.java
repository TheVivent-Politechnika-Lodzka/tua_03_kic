package pl.lodz.p.it.ssbd2022.ssbd03.global_services;

import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.global_services.facades.AccountConfirmationGLOBALFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.global_services.facades.AccountGLOBALFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.global_services.facades.RefreshTokenGLOBALFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.global_services.facades.ResetPasswordGLOBALFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;

import javax.annotation.security.RunAs;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Startup
@Singleton
@RunAs(Roles.ADMINISTRATOR)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Interceptors(TrackerInterceptor.class)
public class CleanerService {

    private static final long serialVersionUID = 1L;

    @Inject
    AccountConfirmationGLOBALFacade accountConfirmationFacade;
    @Inject
    AccountGLOBALFacade accountFacade;
    @Inject
    ResetPasswordGLOBALFacade resetPasswordFacade;
    @Inject
    RefreshTokenGLOBALFacade refreshTokenFacade;

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
