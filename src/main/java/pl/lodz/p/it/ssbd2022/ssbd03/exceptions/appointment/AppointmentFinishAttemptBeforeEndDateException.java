package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd, gdy następuje próba oznaczenia wizyty jako zakończonej przed ustaloną datą końca wizyty
 */
@ApplicationException(rollback = true)
public class AppointmentFinishAttemptBeforeEndDateException extends AppBaseException {

    private static final String APPOINTMENT_END_DATE_EXCEPTION = "server.error.appBase.appointmentFinishAttemptBeforeEndDateException";

    public AppointmentFinishAttemptBeforeEndDateException() {
        super(APPOINTMENT_END_DATE_EXCEPTION, Response.Status.BAD_REQUEST);
    }
}