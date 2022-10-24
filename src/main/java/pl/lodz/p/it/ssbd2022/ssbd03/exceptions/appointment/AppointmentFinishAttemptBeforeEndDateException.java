package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd, gdy następuje próba oznaczenia wizyty jako zakończonej przed ustaloną datą końca wizyty
 */
@ApplicationException(rollback = true)
public class AppointmentFinishAttemptBeforeEndDateException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String APPOINTMENT_END_DATE_EXCEPTION = "server.error.appBase.appointmentFinishAttemptBeforeEndDateException";

    /**
     * Konstruktor o dostępie publicznym potrzebny do budowania wyjątku
     * z klasy AppointmentFinishAttemptBeforeEndDateException
     */
    public AppointmentFinishAttemptBeforeEndDateException() {
        super(APPOINTMENT_END_DATE_EXCEPTION, Response.Status.BAD_REQUEST);
    }
}