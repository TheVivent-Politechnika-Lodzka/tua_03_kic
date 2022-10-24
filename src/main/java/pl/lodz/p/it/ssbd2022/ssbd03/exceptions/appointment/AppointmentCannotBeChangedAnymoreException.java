package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z próbą edycji wizyty
 * w przypadku gdy ktoś próbuje anulować wizytę później niż dzień przed nią
 */
@ApplicationException(rollback = true)
public class AppointmentCannotBeChangedAnymoreException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String APPOINTMENT_CANNOT_BE_CHANGED_ANYMORE = "server.error.appBase.appointmentCannotBeChangedAnymore";

    /**
     * konstruktor wyjątku
     */
    public AppointmentCannotBeChangedAnymoreException() {
        super(APPOINTMENT_CANNOT_BE_CHANGED_ANYMORE, Response.Status.FORBIDDEN);
    }

}
