package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z próbą anulowania własnej wizyty
 * w przypadku gdy ktoś próbuje anulować wizytę później niż dzień przed nią
 */
@ApplicationException(rollback = true)
public class AppointmentCannotBeCancelledAnymoreException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String APPOINTMENT_CANNOT_BE_CANCELLED_ANYMORE = "server.error.appBase.appointmentCannotBeCancelledAnymore";

    /**
     * konstruktor wyjątku
     */
    public AppointmentCannotBeCancelledAnymoreException() {
        super(APPOINTMENT_CANNOT_BE_CANCELLED_ANYMORE, Response.Status.FORBIDDEN);
    }

}
