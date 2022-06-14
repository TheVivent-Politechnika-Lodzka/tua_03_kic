package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek rzucany, gdy podana data rozpoczęcia wizyty jest w przeszłości
 */
@ApplicationException(rollback = true)
public class StartDateIsInPast extends AppBaseException {

    private static final String START_DATE_IS_IN_PAST = "server.error.appBase.startDateIsInPast";

    public StartDateIsInPast() {
        super(START_DATE_IS_IN_PAST, Response.Status.BAD_REQUEST);
    }

}
