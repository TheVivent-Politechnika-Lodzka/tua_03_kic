package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;


import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;


/**
 * Wyjątek reprezentujący błąd związany z próbą anulowania własnej wizyty
 * w przypadku gdy ktoś próbuje anulować nie swoją wizytę
 */
@ApplicationException(rollback = true)
public class AppointmentDoesNotBelongToYouException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String APPOINTMENT_DOES_NOT_BELONG_TO_YOU = "server.error.appBase.appointmentDoesNotBelongToYou";

    /**
     * konstruktor wyjątku
     */
    public AppointmentDoesNotBelongToYouException() {
        super(APPOINTMENT_DOES_NOT_BELONG_TO_YOU, Response.Status.FORBIDDEN);
    }

}
