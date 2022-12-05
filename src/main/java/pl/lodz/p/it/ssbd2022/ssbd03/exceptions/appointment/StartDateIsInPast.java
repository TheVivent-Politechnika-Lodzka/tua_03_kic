package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Wyjątek rzucany, gdy podana data rozpoczęcia wizyty jest w przeszłości
 */
@ApplicationException(rollback = true)
public class StartDateIsInPast extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String START_DATE_IS_IN_PAST = "server.error.appBase.startDateIsInPast";

    /**
     * konstruktor wyjątku
     */
    public StartDateIsInPast() {
        super(START_DATE_IS_IN_PAST, Response.Status.BAD_REQUEST);
    }

}
