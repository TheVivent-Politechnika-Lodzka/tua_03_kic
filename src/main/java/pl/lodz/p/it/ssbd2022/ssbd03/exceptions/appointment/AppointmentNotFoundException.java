package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

public class AppointmentNotFoundException extends AppBaseException {
    private static final String APPOINTMENT_NOT_FOUND = "server.error.appBase.appointmentNotFound";

    public AppointmentNotFoundException() {
        super(APPOINTMENT_NOT_FOUND, Response.Status.NOT_FOUND);
    }
}
