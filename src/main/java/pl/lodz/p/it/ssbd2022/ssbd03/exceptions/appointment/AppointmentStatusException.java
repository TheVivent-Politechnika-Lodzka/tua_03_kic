package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

@ApplicationException(rollback = true)
public class AppointmentStatusException extends AppBaseException {

    private static final String APPOINTMENT_STATUS_ALREADY_CANCELLED = "server.error.appBase.appointmentAlreadyCancelled";

    private AppointmentStatusException(String message) {
        super(message, Response.Status.BAD_REQUEST);
    }

    public static AppointmentStatusException appointmentAlreadyCancelled() {
        return new AppointmentStatusException(APPOINTMENT_STATUS_ALREADY_CANCELLED);
    }


}
