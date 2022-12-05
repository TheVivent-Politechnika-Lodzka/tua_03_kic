package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Wyjątek reprezentujący błąd, gdy specjalista próbuje zakończyć wizytę inną niż własna
 */
@ApplicationException(rollback = true)
public class AppointmentFinishAttemptByInvalidSpecialistException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String INVALID_SPECIALIST = "server.error.appBase.appointmentFinishAttemptByInvalidSpecialist";

    /**
     * Konstruktor o dostępie publicznym potrzebny do budowania wyjątku
     * z klasy AppointmentFinishAttemptByInvalidSpecialistException
     */
    public AppointmentFinishAttemptByInvalidSpecialistException() {
        super(INVALID_SPECIALIST, Response.Status.UNAUTHORIZED);
    }
}
