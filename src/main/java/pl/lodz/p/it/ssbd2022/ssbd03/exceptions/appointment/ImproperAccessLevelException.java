package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;


/**
 * Wyjątek rzucany, gdy użytkownik nie posiada odpowiednich uprawnień, aby być przypisanym do wizyty
 */
@ApplicationException(rollback = true)
public class ImproperAccessLevelException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String ACCOUNT_NOT_SPECIALIST = "server.error.appBase.improperAccessLevel.accountNotSpecialist";
    private static final String ACCOUNT_NOT_CLIENT = "server.error.appBase.improperAccessLevel.accountNotClient";

    private ImproperAccessLevelException(String message) {
        super(message, Response.Status.BAD_REQUEST);
    }

    /**
     * Zwraca wyjątek z informacją, że konto nie jest specjalistą
     *
     * @return
     */
    public static ImproperAccessLevelException accountNotSpecialist() {
        return new ImproperAccessLevelException(ACCOUNT_NOT_SPECIALIST);
    }

    /**
     * Zwraca wyjątek z informacją, że konto nie jest klientem
     *
     * @return
     */
    public static ImproperAccessLevelException accountNotClient() {
        return new ImproperAccessLevelException(ACCOUNT_NOT_CLIENT);
    }

}
