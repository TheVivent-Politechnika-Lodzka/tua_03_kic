package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

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
