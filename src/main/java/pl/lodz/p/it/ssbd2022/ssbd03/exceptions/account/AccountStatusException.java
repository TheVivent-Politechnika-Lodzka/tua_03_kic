package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z aktywacja/deaktywacją konta
 */
@ApplicationException(rollback = true)
public class AccountStatusException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String ACCOUNT_STATUS_ALREADY_ACTIVE = "server.error.appBase.accountAlreadyActive";
    private static final String ACCOUNT_STATUS_ALREADY_INACTIVE = "server.error.appBase.accountAlreadyInactive";

    /**
     * Metoda będąca konstruktorem odpowiadającym za tworzenie wyjątków dotyczących błędów
     * w ramach blokady/odblokowania konta
     *
     * @param message Wiadomość zawarta w wyjątku
     */
    private AccountStatusException(String message) {
        super(message, Response.Status.BAD_REQUEST);
    }

    /**
     * Metoda zwajacąca wyjątek w przypadku próby aktywacji konta gdy jest już aktywne
     *
     * @return Wyjątek typu AccountStatusExteption, response status 401
     */
    public static AccountStatusException accountArleadyActive() {
        return new AccountStatusException(ACCOUNT_STATUS_ALREADY_ACTIVE);
    }

    /**
     * Metoda zwajacąca wyjątek w przypadku próby deaktywacji konta gdy jest już niekatywne
     *
     * @return Wyjątek typu AccountStatusExteption, response status 401
     */
    public static AccountStatusException accountAlreadyInactive() {
        return new AccountStatusException(ACCOUNT_STATUS_ALREADY_INACTIVE);
    }


}
