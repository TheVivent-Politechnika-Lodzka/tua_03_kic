package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z próbą anulowania wizyty, która została już anulowana
 */
@ApplicationException(rollback = true)
public class AppointmentStatusException extends AppBaseException {

    private static final String APPOINTMENT_STATUS_ALREADY_CANCELLED = "server.error.appBase.appointmentAlreadyCancelled";

    public AppointmentStatusException() {
        super(APPOINTMENT_STATUS_ALREADY_CANCELLED, Response.Status.BAD_REQUEST);
    }

}
