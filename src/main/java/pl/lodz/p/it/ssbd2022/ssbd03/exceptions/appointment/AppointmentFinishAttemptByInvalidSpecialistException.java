package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd, gdy specjalista próbuje zakończyć wizytę inną niż własna
 */
@ApplicationException(rollback = true)
public class AppointmentFinishAttemptByInvalidSpecialistException extends AppBaseException {

    private static final String INVALID_SPECIALIST = "server.error.appBase.appointmentFinishAttemptByInvalidSpecialist";

    public AppointmentFinishAttemptByInvalidSpecialistException() {
        super(INVALID_SPECIALIST, Response.Status.UNAUTHORIZED);
    }
}
