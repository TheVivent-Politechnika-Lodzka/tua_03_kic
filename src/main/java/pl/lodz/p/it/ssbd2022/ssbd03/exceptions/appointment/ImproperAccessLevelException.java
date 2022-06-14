package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;


/**
 * Wyjątek rzucany, gdy użytkownik nie posiada odpowiednich uprawnień, aby być przypisanym do wizyty
 */
@ApplicationException(rollback = true)
public class ImproperAccessLevelException extends AppBaseException {

    private static final String ACCOUNT_NOT_SPECIALIST = "server.error.appBase.improperAccessLevel.accountNotSpecialist";
    private static final String ACCOUNT_NOT_CLIENT = "server.error.appBase.improperAccessLevel.accountNotClient";

    private ImproperAccessLevelException(String message) {
        super(message, Response.Status.BAD_REQUEST);
    }

    public static ImproperAccessLevelException accountNotSpecialist() {
        return new ImproperAccessLevelException(ACCOUNT_NOT_SPECIALIST);
    }

    public static ImproperAccessLevelException accountNotClient() {
        return new ImproperAccessLevelException(ACCOUNT_NOT_CLIENT);
    }

}
