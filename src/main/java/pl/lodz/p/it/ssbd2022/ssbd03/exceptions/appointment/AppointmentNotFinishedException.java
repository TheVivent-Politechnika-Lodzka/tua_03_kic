package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z dodaniem recenzji w momencie, gdy wizyta nie została zakończona
 * Oznacza to, że implant nie został jeszcze wmontowany
 */
public class AppointmentNotFinishedException extends AppBaseException {

    private static final String APPOINTMENT_NOT_FINISHED = "server.error.appBase.appointmentNotFinished";

    public AppointmentNotFinishedException() {
        super(APPOINTMENT_NOT_FINISHED, Response.Status.FORBIDDEN);
    }
}
